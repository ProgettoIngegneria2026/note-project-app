package it.unibo.progettonote;

import java.util.List;

public class GestioneUtenti {

    private final DatabaseUtenti dbUtenti;

    public GestioneUtenti(DatabaseUtenti dbUtenti) {
        this.dbUtenti = dbUtenti;
    }

    public List<Utente> listaUtenti() {
        return dbUtenti.getUtentiRepo();
    }
}
