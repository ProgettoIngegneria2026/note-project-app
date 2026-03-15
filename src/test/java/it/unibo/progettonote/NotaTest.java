package it.unibo.progettonote;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.progettonote.client.Nota;
import it.unibo.progettonote.server.DatabaseCore;
import it.unibo.progettonote.server.DatabaseNote;
import it.unibo.progettonote.server.NotaService;

import java.util.List;

import static org.junit.Assert.*;

public class NotaTest {

    private NotaService notaService;
    private final String proprietario1 = "utente1@example.com";
    private final String proprietario2 = "utente2@example.com";

    @Before
    public void setUp() {
        DatabaseCore.enableTestMode();
        DatabaseNote.getNoteRepo().clear();
        DatabaseCore.commit();
        notaService = new NotaService();
    }

    @After
    public void tearDown() {
        DatabaseNote.getNoteRepo().clear();
        DatabaseCore.commit();
    }

    @Test
    public void testCreaNuovaNota() {
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

        String stato = notaService.updateNota(nota.getId(), "Titolo Modificato", "Nuovo contenuto", proprietario1, null, nota.getDataModifica());
        assertEquals("OK", stato);

        Nota notaAggiornata = DatabaseNote.findById("Titolo Modificato");
        assertEquals("Titolo Modificato", notaAggiornata.getTitolo());

        String statoFallito = notaService.updateNota("Titolo Modificato", "Altro Titolo", "Contenuto", proprietario2, null, notaAggiornata.getDataModifica());
        assertEquals("ERRORE", statoFallito);
    }

    @Test
    public void testFindAccessibili() {
        notaService.creaNuovaNota("Nota1", "Contenuto1", proprietario1, null);
        notaService.creaNuovaNota("Nota2", "Contenuto2", proprietario2, null);

        List<Nota> noteUtente1 = DatabaseNote.findAccessibili(proprietario1);
        List<Nota> noteUtente2 = DatabaseNote.findAccessibili(proprietario2);

        assertEquals(1, noteUtente1.size());
        assertEquals("Nota1", noteUtente1.get(0).getTitolo());

        assertEquals(1, noteUtente2.size());
        assertEquals("Nota2", noteUtente2.get(0).getTitolo());
    }

    @Test
    public void testDettaglioNotaAccesso() {
        notaService.creaNuovaNota("Nota1", "Contenuto1", proprietario1, null);
        Nota nota = DatabaseNote.findAccessibili(proprietario1).get(0);

        Nota trovata = DatabaseNote.findById(nota.getId());
        assertTrue(trovata.puoAccedere(proprietario1));
        assertFalse(trovata.puoAccedere(proprietario2));
    }

    @Test
    public void testAggiornamentoSoloProprietario() {
        notaService.creaNuovaNota("Nota1", "Contenuto iniziale", proprietario1, null);
        Nota nota = DatabaseNote.findAccessibili(proprietario1).get(0);

        String statoFallito = notaService.updateNota(nota.getId(), "Titolo Modificato", "Nuovo contenuto", proprietario2, null, nota.getDataModifica());
        assertEquals("ERRORE", statoFallito);

        Nota notaDopo = DatabaseNote.findById(nota.getId());
        assertEquals("Nota1", notaDopo.getTitolo());
    }

 @Test
    public void testRipristinaVersione() {
        notaService.creaNuovaNota("Titolo", "Testo Originale", proprietario1, null);
        Nota nota = DatabaseNote.findAccessibili(proprietario1).get(0);
        String idNota = nota.getId();
        
        // La creazione genera in automatico la V1 ("Testo Originale").
        // Aggiungiamo altre due versioni per il test:
        notaService.aggiungiVersione(nota, "Modifica 1"); // Diventa V2
        notaService.aggiungiVersione(nota, "Modifica 2"); // Diventa V3
        
        // Simuliamo che il testo attuale della nota sia l'ultimo inserito
        nota.setContenuto("Modifica 2");
        DatabaseNote.getNoteRepo().put(idNota, nota);
        DatabaseCore.commit();
        
        // Vogliamo tornare a "Modifica 1", che ora corrisponde alla Versione 2
        boolean ripristinato = notaService.ripristinaVersione(idNota, 2, proprietario1);
        
        assertTrue(ripristinato);
        assertEquals("Modifica 1", DatabaseNote.findById(idNota).getContenuto());
    }
}