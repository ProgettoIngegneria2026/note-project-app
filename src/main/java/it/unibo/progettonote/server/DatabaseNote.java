package it.unibo.progettonote.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unibo.progettonote.client.Nota;

public class DatabaseNote {
    private static Map<String, Nota> noteRepo = new HashMap<>();

    public static Map<String, it.unibo.progettonote.client.Nota> getNoteRepo() {
        return noteRepo;
    }

    public static List<Nota> findAccessibili(String user) {
        List<Nota> res = new ArrayList<>();
        for (Nota n : noteRepo.values()) {
            boolean isOwner = n.getProprietario() != null && n.getProprietario().equals(user);
            boolean isCollab = n.getCollaboratori() != null && n.getCollaboratori().contains(user);
            if (isOwner || isCollab) {
                res.add(n);
            }
        }
        return res;
    }

    public static Nota findById(String id) {
        return noteRepo.get(id);
    }

    public static void close() {
        noteRepo.clear();
    }
}