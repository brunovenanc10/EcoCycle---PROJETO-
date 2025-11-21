package usuarios;

public class Cooperativa extends Usuario {
    public Cooperativa(String nome, String contato) {
        super(nome, contato);
    }
    @Override public String getTipo() {
        return "COOPERATIVA";
    }
}

