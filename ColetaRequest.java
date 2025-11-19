package coleta;

import java.time.LocalDate;


public class ColetaRequest {
    private static int COUNTER = 1;
    private final int id;
    private final int pontoId;
    private final int criadoPorUsuarioId;
    private final LocalDate data;
    private boolean atendida = false;

    public ColetaRequest(int pontoId, int criadoPorUsuarioId) {
        this.id = COUNTER++;
        this.pontoId = pontoId;
        this.criadoPorUsuarioId = criadoPorUsuarioId;
        this.data = LocalDate.now();
    }

    public int getId() { return id; }
    public int getPontoId() { return pontoId; }
    public int getCriadoPorUsuarioId() { return criadoPorUsuarioId; }
    public LocalDate getData() { return data; }
    public boolean isAtendida() { return atendida; }
    public void marcarAtendida() { this.atendida = true; }

    @Override
    public String toString() {
        return String.format("Req[id=%d] ponto=%d por=%d atendida=%s", id, pontoId, criadoPorUsuarioId, atendida);
    }
}
