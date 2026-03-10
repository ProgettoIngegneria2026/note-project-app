package it.unibo.progettonote;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseNote {

    private static Map<String, Nota> noteRepo = new HashMap<>();

    public static Map<String, Nota> getNoteRepo() {
        return noteRepo;
    }

    public void aggiungiNota(Nota nota) {
        if (nota.getId() == null) {
            nota.setId(String.valueOf(noteRepo.size() + 1));
        }
        noteRepo.put(nota.getId(), nota);
    }

    public static void close() {
        noteRepo.clear();
    }

    public List<Nota> findByOwner(String owner) {
        return noteRepo.values().stream()
                .filter(n -> n.getOwner().equals(owner))
                .collect(Collectors.toList());
    }

    public Nota findByIdAndOwner(String id, String owner) {
        Nota n = noteRepo.get(id);
        if (n != null && n.getOwner().equals(owner)) return n;
        return null;
    }
}
