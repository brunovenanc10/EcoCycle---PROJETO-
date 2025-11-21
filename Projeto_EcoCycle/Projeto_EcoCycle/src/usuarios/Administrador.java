package usuarios;

public class Administrador extends Usuario {
    public Administrador(String nome, String contato) {
        super(nome, contato);
    }
    @Override public String getTipo() {
        return "ADMINISTRADOR"; }
}
