package usuarios;

import java.util.concurrent.atomic.AtomicInteger;


public abstract class Usuario {
    private static final AtomicInteger COUNTER = new AtomicInteger(1);
    protected final int id;
    protected String nome;
    protected String contato;
    protected int pontosRecompensa = 0;

    public Usuario(String nome, String contato) {
        this.id = COUNTER.getAndIncrement();
        this.nome = nome;
        this.contato = contato;
    }

    public int getId() {
        return id; }
    public String getNome() {
        return nome; }
    public String getContato() {
        return contato; }
    public int getPontosRecompensa() {
        return pontosRecompensa; }
    public void adicionarPontos(int pts) {
        pontosRecompensa += pts; }

    public abstract String getTipo();

    @Override
    public String toString() {
        return String.format("%s[id=%d] %s - %s pts:%d", getTipo(), id, nome, contato, pontosRecompensa);
    }
}
