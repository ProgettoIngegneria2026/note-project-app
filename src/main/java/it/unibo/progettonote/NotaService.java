package it.unibo.progettonote;

import java.util.*;

public class NotaService {
    private final List<Nota> note = new ArrayList<>();

    public Nota creaNuovaNota(String id, String titolo, String contenuto, String owner) {
        Nota n = new Nota(id, titolo, contenuto, owner);
        note.add(n);
        return n;
    }

    public void aggiungiCollaboratore(String notaId, String collaboratore) {
        // Implementazione dummy
    }

    public void rimuoviCollaboratore(String notaId, String collaboratore) {
        // Implementazione dummy
    }

    public void updateNota(String id, String nuovoTitolo, String nuovoContenuto, String owner) {
        for (Nota n : note) {
            if (n.getId().equals(id) && n.getOwner().equals(owner)) {
                n.setTitolo(nuovoTitolo);
                n.setContenuto(nuovoContenuto);
            }
        }
    }

    public List<Nota> findByOwner(String owner) {
        List<Nota> result = new ArrayList<>();
        for (Nota n : note) {
            if (n.getOwner().equals(owner)) result.add(n);
        }
        return result;
    }

    public Nota findByIdAndOwner(String id, String owner) {
        for (Nota n : note) {
            if (n.getId().equals(id) && n.getOwner().equals(owner)) return n;
        }
        return null;
    }
}
