package it.unibo.progettonote;

import java.util.*;

public class NotaCartellaService {
    private final NotaService notaService;
    private final Map<String, List<Nota>> cartelle = new HashMap<>();

    public NotaCartellaService(NotaService notaService) {
        this.notaService = notaService;
    }

    public void aggiungiNota(Nota n) {
        cartelle.computeIfAbsent(n.getOwner(), k -> new ArrayList<>()).add(n);
    }

    public void spostaNotaInCartella(String notaId, String vecchiaCartella, String nuovaCartella) {
        // Dummy: sposta nota tra cartelle
    }
}
