package it.unibo.progettonote.server;

import java.util.Comparator;
import java.util.List;

import it.unibo.progettonote.client.Cartella;

public class GestioneCartelleService {

    public List<Cartella> listaCartelle(String proprietario) {
        validaOwner(proprietario);
        List<Cartella> res = DatabaseCartelle.findByOwner(proprietario);
        res.sort(Comparator.comparing(Cartella::getNome, String.CASE_INSENSITIVE_ORDER));
        return res;
    }

    public Cartella creaCartella(String nome, String proprietario) {
        validaOwner(proprietario);
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome cartella non valido");
        }
        String clean = nome.trim();
        if (DatabaseCartelle.existsByNameAndOwner(clean, proprietario)) {
            throw new IllegalArgumentException("Esiste già una cartella con questo nome");
        }
        Cartella c = new Cartella(clean, proprietario);
        DatabaseCartelle.getCartelleRepo().put(c.getId(), c);
        DatabaseCore.commit();
        return c;
    }

    public void rinominaCartella(String cartellaId, String nuovoNome, String proprietario) {
        validaOwner(proprietario);
        if (cartellaId == null || cartellaId.trim().isEmpty()) {
            throw new IllegalArgumentException("Id cartella non valido");
        }
        if (nuovoNome == null || nuovoNome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome cartella non valido");
        }
        Cartella c = DatabaseCartelle.findByIdAndOwner(cartellaId, proprietario);
        if (c == null) throw new IllegalArgumentException("Cartella inesistente o non tua");

        String clean = nuovoNome.trim();
        if (!clean.equalsIgnoreCase(c.getNome()) && DatabaseCartelle.existsByNameAndOwner(clean, proprietario)) {
            throw new IllegalArgumentException("Esiste già una cartella con questo nome");
        }

        c.setNome(clean);
        DatabaseCartelle.getCartelleRepo().put(c.getId(), c);
        DatabaseCore.commit();
    }

    public void eliminaCartellaSeVuota(String cartellaId, String proprietario) {
        // Qui per UC2 completa dovresti verificare che NON ci siano Note con idCartella == cartellaId.
        // Se nel vostro modello Nota non c’è idCartella, per ora facciamo delete semplice (poi lo estendete).
        validaOwner(proprietario);
        Cartella c = DatabaseCartelle.findByIdAndOwner(cartellaId, proprietario);
        if (c == null) throw new IllegalArgumentException("Cartella inesistente o non tua");

        DatabaseCartelle.getCartelleRepo().remove(cartellaId);
        DatabaseCore.commit();
    }

    private void validaOwner(String proprietario) {
        if (proprietario == null || proprietario.trim().isEmpty()) {
            throw new IllegalArgumentException("Proprietario non valido");
        }
    }
}