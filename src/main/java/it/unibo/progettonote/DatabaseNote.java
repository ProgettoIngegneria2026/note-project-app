package it.unibo.progettonote;

import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;

public class DatabaseNote {

    private static ConcurrentNavigableMap<String, Nota> noteRepo;

    public static ConcurrentNavigableMap<String, Nota> getNoteRepo() {
        if (noteRepo == null) {
            noteRepo = DatabaseCore.getDB()
                    .treeMap("notes", org.mapdb.Serializer.STRING, org.mapdb.Serializer.JAVA)
                    .createOrOpen();
        }
        return noteRepo;
    }

    public static void close() {
        noteRepo = null;
    }

    public static List<Nota> findByOwner(String proprietario) {
        List<Nota> res = new ArrayList<>();
        for (Nota n : getNoteRepo().values()) {
            if (proprietario.equals(n.getProprietario())) {
                res.add(n);
            }
        }
        return res;
    }

    public static Nota findByIdAndOwner(String id, String proprietario) {
        Nota n = getNoteRepo().get(id);
        if (n == null) return null;
        return proprietario.equals(n.getProprietario()) ? n : null;
    }
}
