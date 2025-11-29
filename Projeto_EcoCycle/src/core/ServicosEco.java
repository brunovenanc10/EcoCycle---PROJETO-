package core;

import usuarios.*;
import coleta.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class ServicosEco {
    private final Repositorio repo;

    private static final Map<Material, Double> CO2_PER_KG;
    static {
        Map<Material, Double> m = new EnumMap<>(Material.class);
        m.put(Material.PLASTICO, 1.5);
        m.put(Material.PAPEL, 0.9);
        m.put(Material.VIDRO, 0.3);
        m.put(Material.METAL, 2.0);
        CO2_PER_KG = Collections.unmodifiableMap(m);
    }

    public ServicosEco(Repositorio repo) { this.repo = repo; }


    public Usuario cadastrarUsuario(Usuario u) {
        repo.usuarios.put(u.getId(), u);
        return u;
    }

    public PontoDeColeta cadastrarPonto(int requesterUserId, String nome, String endereco) {
        Usuario req = repo.usuarios.get(requesterUserId);
        if (!(req instanceof Cooperativa || req instanceof Administrador)) {
            throw new RuntimeException("Apenas administrador ou cooperativa podem cadastrar pontos.");
        }
        PontoDeColeta p = new PontoDeColeta(nome, endereco);
        repo.pontos.put(p.getId(), p);
        return p;
    }

    public List<PontoDeColeta> listarPontos() { return new ArrayList<>(repo.pontos.values()); }

    public void registrarMateriaisNoPonto(int pontoId, Map<Material, Double> adicionadosKg) {
        PontoDeColeta p = repo.pontos.get(pontoId);
        if (p == null) throw new IllegalArgumentException("Ponto não encontrado");
        adicionadosKg.forEach((m, kg) -> p.getInventario().adicionar(m, kg));
    }


    public SolicitarColeta solicitarColeta(int cidadaoId, int pontoId) {
        Usuario u = repo.usuarios.get(cidadaoId);
        if (!(u instanceof Cidadao)) throw new RuntimeException("Somente o cidadão pode solicitar coleta.");
        PontoDeColeta p = repo.pontos.get(pontoId);
        if (p == null) throw new IllegalArgumentException("Ponto não encontrado");
        if (!p.estaCheio()) throw new IllegalStateException("Ponto ainda não está cheio.");
        SolicitarColeta req = new SolicitarColeta(pontoId, cidadaoId);
        repo.solicitacoes.put(req.getId(), req);
        p.registrarSolicitacao(req.getId());
        return req;
    }

    public List<SolicitarColeta> listarRequestsAbertos() {
        return repo.solicitacoes.values().stream().filter(r -> !r.isAtendida()).collect(Collectors.toList());
    }


    public RegistroDeColetas registrarColeta(int cooperativaUserId, int requestId, Map<Material, Double> volumes, LocalDate data) {
        Usuario coop = repo.usuarios.get(cooperativaUserId);
        if (!(coop instanceof Cooperativa)) throw new RuntimeException("Somente a cooperativa pode registrar coleta.");

        SolicitarColeta req = repo.solicitacoes.get(requestId);
        if (req == null) throw new IllegalArgumentException("Solicitação não encontrada");
        if (req.isAtendida()) throw new IllegalStateException("Solicitação já atendida");

        PontoDeColeta ponto = repo.pontos.get(req.getPontoId());
        RegistroDeColetas reg = new RegistroDeColetas(ponto.getId(), cooperativaUserId, data);


        for (Map.Entry<Material, Double> e : volumes.entrySet()) {
            Material m = e.getKey();
            double pedido = e.getValue();
            double disponivel = ponto.getInventario().get(m);
            double recolhido = Math.min(disponivel, pedido);
            reg.setRecolhido(m, recolhido);
            ponto.getInventario().remover(m, recolhido);
        }

        req.marcarAtendida();
        repo.registros.put(reg.getId(), reg);


        Usuario solicitante = repo.usuarios.get(req.getCriadoPorUsuarioId());
        if (solicitante != null) {
            int pontos = (int)Math.round(reg.totalKg());
            solicitante.adicionarPontos(pontos);
        }

        return reg;
    }


    public Map<Material, Double> relatorioTotalPorPeriodo(LocalDate inicio, LocalDate fim) {
        Map<Material, Double> totals = new EnumMap<>(Material.class);
        for (Material m : Material.values()) totals.put(m, 0.0);
        for (RegistroDeColetas r : repo.registros.values()) {
            if (!r.getData().isBefore(inicio) && !r.getData().isAfter(fim)) {
                r.getDetalhes().forEach((m, kg) -> totals.put(m, totals.get(m) + kg));
            }
        }
        return totals;
    }

    public double impactoCO2evitado(LocalDate inicio, LocalDate fim) {
        Map<Material, Double> totals = relatorioTotalPorPeriodo(inicio, fim);
        double co2 = 0.0;
        for (Material m : Material.values()) {
            co2 += totals.get(m) * CO2_PER_KG.get(m);
        }
        return co2;
    }


    public List<Usuario> rankingAnualCidadaos(int year) {
        return repo.usuarios.values().stream()
                .filter(u -> u instanceof Cidadao)
                .sorted(Comparator.comparingInt(Usuario::getPontosRecompensa).reversed())
                .collect(Collectors.toList());
    }


    public List<RegistroDeColetas> listarRegistros() { return new ArrayList<>(repo.registros.values()); }
}
