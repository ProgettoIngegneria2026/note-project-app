package it.unibo.progettonote;

import java.util.List;

public class GestioneCartelleService {

    public List<Cartella> listaCartelle(String proprietario) {
        return DatabaseCartelle.findByOwner(proprietario);
    }

    public Cartella creaCartella(String nome, String proprietario) {
        if (nome == null || proprietario == null) throw new IllegalArgumentException("Dati non validi");
        Cartella c = new Cartella(nome, proprietario);
        DatabaseCartelle.getCartelleRepo().put(c.getId(), c);
        DatabaseCore.commit();
        return c;
    }

    public void rinominaCartella(String cartellaId, String nuovoNome, String proprietario) {
        Cartella c = DatabaseCartelle.findByIdAndOwner(cartellaId, proprietario);
        if (c != null && nuovoNome != null) {
            c.setNome(nuovoNome);
            DatabaseCartelle.getCartelleRepo().put(cartellaId, c);
            DatabaseCore.commit();
        }
    }

    public void eliminaCartellaSeVuota(String cartellaId, String proprietario) {
        Cartella c = DatabaseCartelle.findByIdAndOwner(cartellaId, proprietario);
        if (c != null) {
            DatabaseCartelle.getCartelleRepo().remove(cartellaId);
            DatabaseCore.commit();
        }
    }
}