package it.unibo.progettonote;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;

public class FolderService {

    private final ConcurrentNavigableMap<String, Cartella> repo;

    public FolderService() {
        this.repo = DatabaseCartelle.getCartelleRepo();
    }

    public Cartella creaCartella(String nome, String proprietario) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome cartella vuoto");
        }

        if (proprietario == null || proprietario.trim().isEmpty()) {
            throw new IllegalArgumentException("Proprietario non valido");
        }

        Cartella cartella = new Cartella(nome.trim(), proprietario.trim());
        repo.put(cartella.getId(), cartella);
        return cartella;
    }

    public List<Cartella> listaCartellePerUtente(String proprietario) {
        List<Cartella> risultato = new ArrayList<>();
        for (Cartella c : repo.values()) {
            if (c.getProprietario().equals(proprietario)) {
                risultato.add(c);
            }
        }
        return risultato;
    }
}