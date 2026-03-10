package it.unibo.progettonote;

import java.util.*;

public class GestioneCartelleService {
    private final List<Cartella> cartelle = new ArrayList<>();

    public void creaCartella(String nome, String owner) {
        cartelle.add(new Cartella(nome, owner));
    }

    public void rinominaCartella(String vecchioNome, String nuovoNome, String owner) {
        for (Cartella c : cartelle) {
            if (c.getNome().equals(vecchioNome) && c.getOwner().equals(owner)) {
                c.setNome(nuovoNome);
            }
        }
    }

    public List<Cartella> listaCartelle(String owner) {
        List<Cartella> result = new ArrayList<>();
        for (Cartella c : cartelle) {
            if (c.getOwner().equals(owner)) result.add(c);
        }
        return result;
    }
}
