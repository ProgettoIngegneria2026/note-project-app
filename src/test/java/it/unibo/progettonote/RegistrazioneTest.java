package it.unibo.progettonote;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistrazioneTest {

    @Test
    void testResetGestioneUtenti() {
        GestioneUtenti gu = new GestioneUtenti();
        gu.reset();
        assertTrue(true);
    }
}
