package it.unibo.progettonote;

import java.util.HashMap;
import java.util.Map;

public class DatabaseNote {
    private Map<String, Nota> noteRepo = new HashMap<>();

    public Map<String, Nota> getNoteRepo() {
        return noteRepo;
    }

    public void aggiungiNota(Nota nota) {
        noteRepo.put(nota.getId(), nota);
    }
}
