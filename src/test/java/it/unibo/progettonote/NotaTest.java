package it.unibo.progettonote;
import it.unibo.progettonote.client.Nota;
import it.unibo.progettonote.client.*;
import it.unibo.progettonote.server.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class NotaTest {

    private NotaService notaService;
    private final String proprietario1 = "utente1@example.com";
    private final String proprietario2 = "utente2@example.com";

    @Before
    public void setUp() {
        DatabaseCore.enableTestMode();
        DatabaseNote.close();
        DatabaseNote.getNoteRepo().clear();
        DatabaseCore.commit();
        notaService = new NotaService();
    }

    @After
    public void tearDown() {
        DatabaseNote.close();
    }

    @Test
    public void testCreaNuovaNota() {
        // Aggiornato con il 4° parametro (idCartella = null)
        boolean creata = notaService.creaNuovaNota("Titolo1", "Contenuto valido", proprietario1, null);
        assertTrue(creata);

        List<Nota> note = DatabaseNote.findAccessibili(proprietario1);
        assertEquals(1, note.size());
        assertEquals("Titolo1", note.get(0).getTitolo());
    }

    @Test
    public void testUpdateNotaPermesso() {
        notaService.creaNuovaNota("Titolo1", "Contenuto iniziale", proprietario1, null);
        Nota nota = DatabaseNote.findAccessibili(proprietario1).get(0);

        // Aggiornamento dal proprietario corretto (aggiunti idCartella e dataLettura)
        String stato = notaService.updateNota(nota.getId(), "Titolo Modificato", "Nuovo contenuto", proprietario1, null, nota.getDataModifica());
        assertEquals("OK", stato);

        Nota notaAggiornata = DatabaseNote.findById("Titolo Modificato");
        assertEquals("Titolo Modificato", notaAggiornata.getTitolo());

        // Aggiornamento da utente non proprietario
        String statoFallito = notaService.updateNota("Titolo Modificato", "Altro Titolo", "Contenuto", proprietario2, null, notaAggiornata.getDataModifica());
        assertEquals("ERRORE", statoFallito);
    }

    @Test
    public void testRipristinaVersione() {
        notaService.creaNuovaNota("Titolo", "Testo Originale", proprietario1, null);
        Nota nota = DatabaseNote.findAccessibili(proprietario1).get(0);
        
        // Creo versioni
        notaService.aggiungiVersione(nota, "Modifica 1");
        notaService.aggiungiVersione(nota, "Modifica 2");
        DatabaseNote.getNoteRepo().put(nota.getId(), nota);
        DatabaseCore.commit();
        
        // Ripristino la Versione 1
        boolean ripristinato = notaService.ripristinaVersione(nota.getId(), 1, proprietario1);
        assertTrue(ripristinato);
        assertEquals("Modifica 1", DatabaseNote.findById(nota.getId()).getContenuto());
    }
}