package it.unibo.progettonote;

import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.stream.Collectors;

public class DatabaseCartelle {

    private static ConcurrentNavigableMap<String, Cartella> cartelleRepo;

    public static ConcurrentNavigableMap<String, Cartella> getCartelleRepo() {
        if (cartelleRepo == null) {
            cartelleRepo = DatabaseCore.getDB()
                    .treeMap("cartelle", org.mapdb.Serializer.STRING, org.mapdb.Serializer.JAVA)
                    .createOrOpen();
        }
        return cartelleRepo;
    }

    public static void close() {
        cartelleRepo = null;
    }

    public static List<Cartella> findByOwner(String proprietario) {
        return getCartelleRepo().values().stream()
                .filter(c -> c.getProprietario().equals(proprietario))
                .collect(Collectors.toList());
    }

    public static Cartella findByIdAndOwner(String id, String proprietario) {
        Cartella c = getCartelleRepo().get(id);
        if (c != null && c.getProprietario().equals(proprietario)) {
            return c;
        }
        return null;
    }

    public static boolean existsByNameAndOwner(String nome, String proprietario) {
        return getCartelleRepo().values().stream()
                .anyMatch(c -> c.getNome().equals(nome) && c.getProprietario().equals(proprietario));
    }
}
