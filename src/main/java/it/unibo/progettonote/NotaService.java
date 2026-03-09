package it.unibo.progettonote;

import java.util.ArrayList;
import java.util.List;

public class NotaService {

    // Crea una nuova nota
    public boolean creaNuovaNota(String titolo, String contenuto, String proprietario) {
        if (titolo == null || contenuto == null || contenuto.length() > 280) return false;

        Nota n = new Nota();
        n.setId(generateId());
        n.setTitolo(titolo);
        n.setContenuto(contenuto);
        n.setProprietario(proprietario);
        n.setCollaboratori(new ArrayList<>());
        n.setDataUltimaModifica(new java.util.Date());

        DatabaseNote.getNoteRepo().put(n.getId(), n);
        return true;
    }

    // Aggiorna una nota esistente
    public boolean updateNota(String idNota, String titolo, String contenuto, String proprietario) {
        Nota nota = DatabaseNote.getNoteRepo().get(idNota);
        if (nota == null) return false;
        if (!nota.getProprietario().equals(proprietario)) return false;
        if (titolo == null || contenuto == null || contenuto.length() > 280) return false;

        nota.setTitolo(titolo);
        nota.setContenuto(contenuto);
        nota.setDataUltimaModifica(new java.util.Date());

        DatabaseNote.getNoteRepo().put(idNota, nota);
        return true;
    }

    // Aggiunge collaboratori a una nota
    public boolean aggiungiCollaboratori(String idNota, List<String> collaboratori, String proprietario) {
        Nota nota = DatabaseNote.getNoteRepo().get(idNota);
        if (nota == null) return false;
        if (!nota.getProprietario().equals(proprietario)) return false;

        if (nota.getCollaboratori() == null) {
            nota.setCollaboratori(new ArrayList<>());
        }

        for (String c : collaboratori) {
            if (!nota.getCollaboratori().contains(c)) {
                nota.getCollaboratori().add(c);
            }
        }

        DatabaseNote.getNoteRepo().put(idNota, nota);
        return true;
    }

    // Rimuove collaboratori da una nota
    public boolean rimuoviCollaboratori(String idNota, List<String> collaboratori, String proprietario) {
        Nota nota = DatabaseNote.getNoteRepo().get(idNota);
        if (nota == null) return false;
        if (!nota.getProprietario().equals(proprietario)) return false;

        if (nota.getCollaboratori() != null) {
            nota.getCollaboratori().removeAll(collaboratori);
        }

        DatabaseNote.getNoteRepo().put(idNota, nota);
        return true;
    }

    // Genera un ID unico per la nota
    private String generateId() {
        return "N" + System.currentTimeMillis();
    }
}
