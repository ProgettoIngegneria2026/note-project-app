package it.unibo.progettonote;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class RegistrazioneTest {

    @Before
    public void setup() {
        DatabaseUtenti.close();        // <— IMPORTANTISSIMO: stacca la mappa vecchia
        DatabaseCore.enableTestMode(); // passa a DB in memoria (nuovo DB)
        DatabaseUtenti.getUtentiRepo().clear();
        DatabaseCore.commit();
}

    @Test
    public void testRegistrazioneSuccesso() {
        GestioneUtenti service = new GestioneUtenti();
        boolean risultato = service.registraNuovoUtente("mario88", "mario@email.it", "pass1234");
        assertTrue("La registrazione dovrebbe avere successo", risultato);
    }

    @Test
    public void testEmailGiaEsistente() {
        GestioneUtenti service = new GestioneUtenti();
        service.registraNuovoUtente("utente1", "test@test.it", "password");

        boolean risultato = service.registraNuovoUtente("utente2", "test@test.it", "altrapass");
        assertFalse("Non dovrebbe permettere email duplicate", risultato);
    }
}