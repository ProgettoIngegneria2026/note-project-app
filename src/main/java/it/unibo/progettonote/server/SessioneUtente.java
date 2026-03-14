package it.unibo.progettonote.server;

import it.unibo.progettonote.client.Utente;

public class SessioneUtente {
    private static Utente utenteLoggato;

    public static void login(Utente utente) {
        utenteLoggato = utente;
    }

    public static void logout() {
        utenteLoggato = null;
    }

    public static Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    public static boolean isLoggato() {
        return utenteLoggato != null;
    }
}
