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

    public static List<Nota> findAccessibili(String user) {

        List<Nota> res = new ArrayList<>();

        for (Nota n : getNoteRepo().values()) {

            if (n.getProprietario().equals(user) || n.getCollaboratori().contains(user)) {
                res.add(n);
            }

        }

        return res;
    }

    public static Nota findById(String id) {
        return getNoteRepo().get(id);
    }
}
