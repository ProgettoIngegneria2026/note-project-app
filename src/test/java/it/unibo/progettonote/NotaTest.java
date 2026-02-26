package it.unibo.progettonote;

import org.junit.Test;
import static org.junit.Assert.*;

public class NotaTest {

    @Test
    public void testCreazioneNotaValida() {
        // Creazione di una nota con contenuto entro i limiti
        Nota nota = new Nota("Titolo", "Contenuto breve", "User1");
        
        // Verifica che il contenuto sia salvato correttamente e rispetti il limite
        assertNotNull(nota.getContenuto());
        assertTrue("Il contenuto deve essere <= 280 caratteri", nota.getContenuto().length() <= 280);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreazioneNotaTroppoLunga() {
        // Genera una stringa di 281 caratteri
        String testoLungo = "a".repeat(281);
        
        // La chiamata deve sollevare IllegalArgumentException
        ValidatoreNote.valida(testoLungo); 
    }
}