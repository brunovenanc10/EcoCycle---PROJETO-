import core.*;
import usuarios.*;
import coleta.*;

import java.time.LocalDate;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Repositorio repo = new Repositorio();
        ServicosEco service = new ServicosEco(repo);


        Usuario admin = service.cadastrarUsuario(new Administrador("Admin Local", "admin@eco.org"));
        Usuario coopA = service.cadastrarUsuario(new Cooperativa("Cooperativa Verde", "contato@verde.org"));
        Usuario cid1 = service.cadastrarUsuario(new Cidadao("Bruno Venancio", "bruno@gmail.com"));
        Usuario cid2 = service.cadastrarUsuario(new Cidadao("Harlen Henrick", "harlen@gmail.com"));

        System.out.println("Usuários cadastrados:");
        repo.usuarios.values().forEach(System.out::println);


        PontoDeColeta p1 = service.cadastrarPonto(admin.getId(), "Ponto Praça Central", "Praça Central, 100");
        PontoDeColeta p2 = service.cadastrarPonto(coopA.getId(), "Ponto Mercado Verde", "Rua Verde, 50");

        service.registrarMateriaisNoPonto(p1.getId(), Map.of(Material.PLASTICO, 6.0, Material.PAPEL, 3.0, Material.VIDRO, 1.5, Material.METAL, 0.8));
        service.registrarMateriaisNoPonto(p2.getId(), Map.of(Material.PLASTICO, 5.0, Material.PAPEL, 2.0, Material.VIDRO, 0.5, Material.METAL, 1.0));

        System.out.println("\nPontos e inventários após registro:");
        service.listarPontos().forEach(System.out::println);

        try {
            service.solicitarColeta(cid1.getId(), p2.getId());
        } catch (Exception e) {
            System.out.println("\nTentativa de solicitar coleta em ponto não cheio: " + e.getMessage());
        }

        service.registrarMateriaisNoPonto(p1.getId(), Map.of(Material.PLASTICO, 5.0));
        System.out.println("\nP1 inventário agora: " + p1.getInventario());

        var req1 = service.solicitarColeta(cid1.getId(), p1.getId());
        System.out.println("\nSolicitação criada: " + req1);

        var reg1 = service.registrarColeta(coopA.getId(), req1.getId(), Map.of(Material.PLASTICO, 10.0, Material.PAPEL, 5.0, Material.VIDRO, 2.0, Material.METAL, 2.0), LocalDate.now());
        System.out.println("\nColeta registrada: " + reg1);
        System.out.println("P1 inventário após coleta: " + p1.getInventario());

        service.registrarMateriaisNoPonto(p2.getId(), Map.of(Material.PLASTICO, 6.0));
        var req2 = service.solicitarColeta(cid2.getId(), p2.getId());
        var reg2 = service.registrarColeta(coopA.getId(), req2.getId(), Map.of(Material.PLASTICO, 6.0, Material.PAPEL, 2.0), LocalDate.now().minusDays(2));
        System.out.println("\nSegunda coleta: " + reg2);

        var inicio = LocalDate.now().minusDays(7);
        var fim = LocalDate.now();
        var totals = service.relatorioTotalPorPeriodo(inicio, fim);
        double co2 = service.impactoCO2evitado(inicio, fim);
        System.out.println("\nRelatório de " + inicio + " a " + fim);
        totals.forEach((m, kg) -> System.out.printf("  %s -> %.2f kg%n", m, kg));
        System.out.printf("Impacto CO2 evitado (estimado): %.2f kgCO2%n", co2);

        System.out.println("\nRanking de cidadãos (por pontos):");
        var ranking = service.rankingAnualCidadaos(LocalDate.now().getYear());
        int pos = 1;
        for (Usuario u : ranking) {
            System.out.printf("%d) %s - %d pts%n", pos++, u.getNome(), u.getPontosRecompensa());
        }

        System.out.println("\nFim da simulação");
    }
}

