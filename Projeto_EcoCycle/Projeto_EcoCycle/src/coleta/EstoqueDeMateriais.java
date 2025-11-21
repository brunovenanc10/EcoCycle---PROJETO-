package coleta;

import java.util.EnumMap;
import java.util.Map;


public class EstoqueDeMateriais {
    private final Map<Material, Double> estoque = new EnumMap<>(Material.class);

    public EstoqueDeMateriais() {
        for (Material m : Material.values()) estoque.put(m, 0.0);
    }

    public void adicionar(Material m, double kg) {
        estoque.put(m, estoque.get(m) + kg);
    }

    public void remover(Material m, double kg) {
        estoque.put(m, Math.max(0.0, estoque.get(m) - kg));
    }

    public double get(Material m) {
        return estoque.get(m);
    }

    public double totalKg() {
        return estoque.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        estoque.forEach((m, kg) -> sb.append(String.format("%s: %.2fkg, ", m, kg)));
        return sb.toString();
    }
}

