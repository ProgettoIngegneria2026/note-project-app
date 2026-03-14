package it.unibo.progettonote;

import org.junit.Before;
import org.junit.Test;

import it.unibo.progettonote.server.DatabaseCore;
import it.unibo.progettonote.server.DatabaseUtenti;
import it.unibo.progettonote.server.GestioneUtenti;

import static org.junit.Assert.*;

public class RegistrazioneTest {

    @Before
    public void setup() {
        DatabaseUtenti.close();
        DatabaseCore.enableTestMode();
        DatabaseUtenti.getUtentiRepo().clear();
        DatabaseCore.commit();
    }

    @Test
    public void testRegistrazioneSuccesso() {
        GestioneUtenti gu = new GestioneUtenti();
        String unique = String.valueOf(System.currentTimeMillis() + Math.random());
        String username = "user_" + unique;
        String email = "user_" + unique + "@email.it";

        boolean ok = gu.registraNuovoUtente(username, email, "password123");
        assertTrue("La registrazione dovrebbe avere successo", ok);
    }

    @Test
    public void testRegistrazioneEmailDuplicata() {
        GestioneUtenti gu = new GestioneUtenti();
        String unique = String.valueOf(System.currentTimeMillis() + Math.random());
        String email = "dup_" + unique + "@email.it";

        assertTrue(gu.registraNuovoUtente("user1_" + unique, email, "password123"));
        boolean ok2 = gu.registraNuovoUtente("user2_" + unique, email, "password123");

        assertFalse("La registrazione dovrebbe fallire per email duplicata", ok2);
    }
}