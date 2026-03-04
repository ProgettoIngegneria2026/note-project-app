package it.unibo.progettonote;

import org.mapdb.Serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;

public class DatabaseNote {

    private static ConcurrentNavigableMap<String, Nota> noteRepo;

    public static ConcurrentNavigableMap<String, Nota> getNoteRepo() {
        if (noteRepo == null) {
            noteRepo = DatabaseCore.getDB()
                    .treeMap("notes", Serializer.STRING, Serializer.JAVA)
                    .createOrOpen();
        }
        return noteRepo;
    }

    public static void close() {
        noteRepo = null;
    }

    // #22: query per owner
    public static List<Nota> findByOwner(String owner) {
        List<Nota> res = new ArrayList<>();
        for (Nota n : getNoteRepo().values()) {
            if (owner.equals(n.getProprietario())) {
                res.add(n);
            }
        }
        return res;
    }

    public static Nota findByIdAndOwner(String id, String owner) {
        Nota n = getNoteRepo().get(id);
        if (n == null) return null;
        return owner.equals(n.getProprietario()) ? n : null;
    }
}