package it.unibo.progettonote;

import java.util.HashMap;
import java.util.Map;

public class NotaService {
    private Map<String, Nota> noteRepo = new HashMap<>();

    public void salvaNota(Nota nota, String user) throws IllegalAccessException {
        if (!nota.isModificabile(user)) {
            throw new IllegalAccessException("Utente non autorizzato a modificare questa nota");
        }
        noteRepo.put(nota.getId(), nota);
    }

    public Nota getNota(String id) {
        return noteRepo.get(id);
    }

    public void aggiungiNota(Nota nota) {
        noteRepo.put(nota.getId(), nota);
    }
}
