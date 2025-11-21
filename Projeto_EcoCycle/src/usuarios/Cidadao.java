package usuarios;

public class Cidadao extends Usuario {
    public Cidadao(String nome, String contato) { super(nome, contato); }
    @Override public String getTipo() { return "CIDADÃO"; }
}


