package it.unibo.progettonote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseCartelle {

    private Map<String, Cartella> cartelleRepo = new HashMap<>();

    public List<Cartella> findByOwner(String owner) {
        List<Cartella> result = new ArrayList<>();
        for (Cartella c : cartelleRepo.values()) {
            if (c.getProprietario().equals(owner)) {
                result.add(c);
            }
        }
        return result;
    }

    public boolean existsByNameAndOwner(String nome, String owner) {
        return cartelleRepo.values().stream()
                .anyMatch(c -> c.getNome().equals(nome) && c.getProprietario().equals(owner));
    }

    public Cartella findByIdAndOwner(String id, String owner) {
        Cartella c = cartelleRepo.get(id);
        if (c != null && c.getProprietario().equals(owner)) {
            return c;
        }
        return null;
    }

    public void aggiungiCartella(Cartella c) {
        cartelleRepo.put(c.getId(), c);
    }

    public void rimuoviCartella(Cartella c) {
        cartelleRepo.remove(c.getId());
    }

    public Map<String, Cartella> getCartelleRepo() {
        return cartelleRepo;
    }
}
