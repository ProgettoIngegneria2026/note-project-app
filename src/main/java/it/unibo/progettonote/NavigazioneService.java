package it.unibo.progettonote;

import java.util.*;

public class NavigazioneService {
    private final NotaService notaService;

    public NavigazioneService(NotaService notaService) {
        this.notaService = notaService;
    }

    public Nota notaPiuRecente(List<Nota> note) {
        return note.isEmpty() ? null : note.get(note.size() - 1);
    }
}
