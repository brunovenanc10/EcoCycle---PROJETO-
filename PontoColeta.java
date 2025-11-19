package coleta;

import java.util.ArrayList;
import java.util.List;


public class PontoColeta {
    private static int COUNTER = 1;
    private final int id;
    private String nome;
    private String endereco;
    private MaterialInventory inventario = new MaterialInventory();
    private boolean ativo = true;
    private final List<Integer> solicitacoes = new ArrayList<>();
    public static final double LIMITE_CHEIO = 10.0;

    public PontoColeta(String nome, String endereco) {
        this.id = COUNTER++;
        this.nome = nome;
        this.endereco = endereco;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public MaterialInventory getInventario() { return inventario; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public void registrarSolicitacao(int reqId) { solicitacoes.add(reqId); }

    public boolean estaCheio() { return inventario.totalKg() >= LIMITE_CHEIO; }

    @Override
    public String toString() {
        return String.format("Ponto[id=%d] %s - %s | total: %.2fkg", id, nome, endereco, inventario.totalKg());
    }
}