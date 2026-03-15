package it.unibo.progettonote;
import it.unibo.progettonote.client.Nota;
import it.unibo.progettonote.server.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RegistrazioneTest {

    @Before
    public void setup() {
        DatabaseCore.enableTestMode(); 
        DatabaseUtenti.getUtentiRepo().clear();
        DatabaseCore.commit();
    }

    @Test
    public void testRegistrazioneSuccesso() {
        GestioneUtenti gu = new GestioneUtenti();
        boolean ok = gu.registraNuovoUtente("mario_rossi", "mario@email.it", "password123");
        assertTrue("La registrazione dovrebbe avere successo", ok);
    }

    @Test
    public void testRegistrazioneEmailDuplicata() {
        GestioneUtenti gu = new GestioneUtenti();
        gu.registraNuovoUtente("user1", "stessa@email.it", "pass1");
        
        // Tento di registrare un altro utente con la stessa email
        boolean ok2 = gu.registraNuovoUtente("user2", "stessa@email.it", "pass2");
        assertFalse("La registrazione dovrebbe fallire per email duplicata", ok2);
    }
}