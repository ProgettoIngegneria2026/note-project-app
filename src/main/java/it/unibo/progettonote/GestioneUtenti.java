package it.unibo.progettonote;

public class GestioneUtenti {

    private DatabaseUtenti dbUtenti;

    public GestioneUtenti() {
        dbUtenti = new DatabaseUtenti();
    }

    // Registra nuovo utente
    public void registra(String username, String password, String email) {
        dbUtenti.getUtentiRepo().put(username, password + "|" + email);
    }

    public DatabaseUtenti getDbUtenti() {
        return dbUtenti;
    }
}
