package it.unibo.progettonote.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unibo.progettonote.client.Cartella;

public class DatabaseCartelle {
    private static Map<String, Cartella> cartelleRepo = new HashMap<>();

    public static Map<String, Cartella> getCartelleRepo() {
        return cartelleRepo;
    }

    public static List<Cartella> findByOwner(String owner) {
        List<Cartella> res = new ArrayList<>();
        for (Cartella c : cartelleRepo.values()) {
            if (owner.equals(c.getProprietario())) {
                res.add(c);
            }
        }
        return res;
    }

    public static Cartella findByIdAndOwner(String id, String owner) {
        Cartella c = cartelleRepo.get(id);
        if (c == null) return null;
        return owner.equals(c.getProprietario()) ? c : null;
    }

    public static boolean existsByNameAndOwner(String nome, String owner) {
        for (Cartella c : cartelleRepo.values()) {
            if (owner.equals(c.getProprietario()) && nome.equalsIgnoreCase(c.getNome())) {
                return true;
            }
        }
        return false;
    }

    public static void close() {
        cartelleRepo.clear();
    }
}