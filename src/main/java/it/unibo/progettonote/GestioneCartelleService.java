package it.unibo.progettonote;

import java.util.List;

public class GestioneCartelleService {

    private final DatabaseCartelle dbCartelle;

    public GestioneCartelleService(DatabaseCartelle dbCartelle) {
        this.dbCartelle = dbCartelle;
    }

    public List<Cartella> getCartelle(String userId) {
        return dbCartelle.getCartelleByOwner(userId);
    }

    public void eliminaCartellaSeVuota(String cartellaId, String userId) {
        Cartella c = dbCartelle.getCartella(cartellaId);
        if (c != null && c.getNote().isEmpty() && c.getOwner().equals(userId)) {
            dbCartelle.removeCartella(cartellaId);
        }
    }
}
