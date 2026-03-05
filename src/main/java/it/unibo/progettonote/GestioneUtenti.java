package it.unibo.progettonote;

import java.util.concurrent.ConcurrentNavigableMap;

public class GestioneUtenti {

    public boolean registraNuovoUtente(String username, String email, String password) {
        if (username == null || email == null || password == null) return false;

        // Recuperiamo il repo
        ConcurrentNavigableMap<String, Utente> repo = DatabaseUtenti.getUtentiRepo();

        if (repo.containsKey(email)) {
            System.out.println("DEBUG: Registrazione fallita. Email " + email + " già presente.");
            return false;
        }

        Utente nuovo = new Utente(username, email, password);
        repo.put(email, nuovo);

        // CORREZIONE: Usiamo il commit centralizzato
        DatabaseCore.commit();

        System.out.println("DEBUG: Utente " + email + " salvato con successo.");
        return true;
    }

    public boolean login(String email, String password) {
        // Recuperiamo l'utente
        Utente utente = DatabaseUtenti.getUtentiRepo().get(email);

        if (utente == null) {
            System.out.println("DEBUG: Login fallito. Email " + email + " non trovata nel DB.");
            return false;
        }

        if (utente.getPassword().equals(password)) {
            // Salviamo l'utente nella sessione per l'UC6 e UC7
            SessioneUtente.login(utente);
            System.out.println("DEBUG: Login successo per " + email);
            return true;
        } else {
            System.out.println("DEBUG: Password errata per " + email);
            return false;
        }
    }
}
