package it.unibo.progettonote;

import java.util.concurrent.ConcurrentNavigableMap;

public class GestioneUtenti {

    public boolean registraNuovoUtente(String username, String email, String password) {
        if (username == null || email == null || password == null) return false;

        ConcurrentNavigableMap<String, Utente> repo = DatabaseUtenti.getUtentiRepo();
        if (repo.containsKey(email)) {
            System.out.println("DEBUG: Registrazione fallita. Email " + email + " già presente.");
            return false;
        }

        Utente nuovo = new Utente(username, email, password);
        repo.put(email, nuovo);
        DatabaseUtenti.getDB().commit(); // <--- FONDAMENTALE per salvare su disco!
        System.out.println("DEBUG: Utente " + email + " salvato con successo.");
        return true;
    }

    public boolean login(String email, String password) {
        Utente utente = DatabaseUtenti.getUtentiRepo().get(email);

        if (utente == null) {
            System.out.println("DEBUG: Login fallito. Email " + email + " non trovata nel DB.");
            return false;
        }

        if (utente.getPassword().equals(password)) {
            SessioneUtente.login(utente);
            System.out.println("DEBUG: Login successo per " + email);
            return true;
        } else {
            System.out.println("DEBUG: Password errata per " + email);
            return false;
        }
    }
}
