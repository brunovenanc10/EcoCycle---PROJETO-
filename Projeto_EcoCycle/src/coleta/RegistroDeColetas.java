package coleta;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;


public class RegistroDeColetas {
    private static int COUNTER = 1;
    private final int id;
    private final int pontoId;
    private final int cooperativaId;
    private final LocalDate data;
    private final Map<Material, Double> recolhido = new EnumMap<>(Material.class);

    public RegistroDeColetas(int pontoId, int cooperativaId, LocalDate data) {
        this.id = COUNTER++;
        this.pontoId = pontoId;
        this.cooperativaId = cooperativaId;
        this.data = data;
        for (Material m : Material.values()) recolhido.put(m, 0.0);
    }

    public int getId() {
        return id; }
    public int getPontoId() {
        return pontoId; }
    public int getCooperativaId() { 
        return cooperativaId; }
    public LocalDate getData() {
        return data; }
    public void setRecolhido(Material m, double kg) {
        recolhido.put(m, kg); }
    public double totalKg() {
        return recolhido.values().stream().mapToDouble(Double::doubleValue).sum(); }
    public Map<Material, Double> getDetalhes() {
        return recolhido; }

    @Override
    public String toString() {
        return String.format("Coleta[id=%d] ponto=%d coop=%d data=%s total=%.2fkg", id, pontoId, cooperativaId, data, totalKg());
    }
}

