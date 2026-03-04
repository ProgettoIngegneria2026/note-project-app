package it.unibo.progettonote;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class RegistrazioneTest {

    @Before
    public void setup() {
        DatabaseUtenti.close();        // stacca eventuale mappa vecchia
        DatabaseCore.enableTestMode(); // DB in memoria
        DatabaseUtenti.getUtentiRepo().clear();
        DatabaseCore.commit();
    }

    @Test
    public void testRegistrazioneSuccesso() {
        GestioneUtenti gu = new GestioneUtenti();

        String username = "user_" + UUID.randomUUID();
        String email = "user_" + UUID.randomUUID() + "@email.it";

        boolean ok = gu.registraNuovoUtente(username, email, "password123");
        assertTrue("La registrazione dovrebbe avere successo", ok);
    }

    @Test
    public void testRegistrazioneEmailDuplicata() {
        GestioneUtenti gu = new GestioneUtenti();

        String email = "dup_" + UUID.randomUUID() + "@email.it";

        String username1 = "user_" + UUID.randomUUID();
        assertTrue(gu.registraNuovoUtente(username1, email, "password123"));

        String username2 = "user_" + UUID.randomUUID();
        boolean ok2 = gu.registraNuovoUtente(username2, email, "password123");

        assertFalse("La registrazione dovrebbe fallire per email duplicata", ok2);
    }
}