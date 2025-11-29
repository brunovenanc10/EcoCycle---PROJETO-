package core;

import java.util.*;
import usuarios.*;
import coleta.*;


public class Repositorio {
    public final Map<Integer, Usuario> usuarios = new HashMap<>();
    public final Map<Integer, PontoDeColeta> pontos = new HashMap<>();
    public final Map<Integer, SolicitarColeta> solicitacoes = new HashMap<>();
    public final Map<Integer, RegistroDeColetas> registros = new HashMap<>();
}


