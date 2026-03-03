package it.unibo.progettonote;

import org.mapdb.Serializer;

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
        noteRepo = null; // NON chiudere DatabaseCore qui
    }
}