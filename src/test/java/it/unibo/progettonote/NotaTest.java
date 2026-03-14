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
    private String proprietario1 = "utente1@example.com";
    private String proprietario2 = "utente2@example.com";
    

    @Before
    public void setUp() {
        // Abilita la modalità di test con database in memoria
        DatabaseCore.enableTestMode();
        // Pulisce i repository per garantire l'isolamento del test
        DatabaseNote.close();
        DatabaseNote.close();
        DatabaseCore.enableTestMode();
        DatabaseCore.commit();

        notaService = new NotaService();
    }

    @After
    public void tearDown() {
        DatabaseNote.close();
        DatabaseCore.enableTestMode();
        DatabaseCore.commit();
    }

    @Test
    public void testCreaNuovaNota() {
        boolean creata = notaService.creaNuovaNota("Titolo1", "Contenuto valido", proprietario1);
        assertTrue(creata);

        List<Nota> note = DatabaseNote.findAccessibili(proprietario1);
        assertEquals(1, note.size());
        assertEquals("Titolo1", note.get(0).getTitolo());
    }

    @Test
    public void testUpdateNotaPermesso() {
        notaService.creaNuovaNota("Titolo1", "Contenuto iniziale", proprietario1);
        Nota nota = DatabaseNote.findAccessibili(proprietario1).get(0);

        // Aggiornamento dal proprietario corretto
        boolean aggiornata = notaService.updateNota(nota.getId(), "Titolo Modificato", "Nuovo contenuto", proprietario1);
        assertTrue(aggiornata);

        Nota notaAggiornata = DatabaseNote.findById(nota.getId());
        assertEquals("Titolo Modificato", notaAggiornata.getTitolo());

        // Aggiornamento da utente non proprietario
        boolean aggiornamentoFallito = notaService.updateNota(nota.getId(), "Altro Titolo", "Contenuto", proprietario2);
        assertFalse(aggiornamentoFallito);
    }

    @Test
    public void testFindAccessibili() {
        notaService.creaNuovaNota("Nota1", "Contenuto1", proprietario1);
        notaService.creaNuovaNota("Nota2", "Contenuto2", proprietario2);

        List<Nota> noteUtente1 = DatabaseNote.findAccessibili(proprietario1);
        List<Nota> noteUtente2 = DatabaseNote.findAccessibili(proprietario2);

        assertEquals(1, noteUtente1.size());
        assertEquals("Nota1", noteUtente1.get(0).getTitolo());

        assertEquals(1, noteUtente2.size());
        assertEquals("Nota2", noteUtente2.get(0).getTitolo());
    }

    @Test
    public void testDettaglioNotaAccesso() {
        notaService.creaNuovaNota("Nota1", "Contenuto1", proprietario1);
        Nota nota = DatabaseNote.findAccessibili(proprietario1).get(0);

        // Accesso proprietario
        Nota trovata = DatabaseNote.findById(nota.getId());
        assertTrue(trovata.puoAccedere(proprietario1));

        // Accesso utente non proprietario
        assertFalse(trovata.puoAccedere(proprietario2));
    }

    @Test
    public void testAggiornamentoSoloProprietario() {
        notaService.creaNuovaNota("Nota1", "Contenuto iniziale", proprietario1);
        Nota nota = DatabaseNote.findAccessibili(proprietario1).get(0);

        // Tentativo update da utente non autorizzato
        boolean aggiornamentoFallito = notaService.updateNota(nota.getId(), "Titolo Modificato", "Nuovo contenuto", proprietario2);
        assertFalse(aggiornamentoFallito);

        // Verifica che il titolo non sia cambiato
        Nota notaDopo = DatabaseNote.findById(nota.getId());
        assertEquals("Nota1", notaDopo.getTitolo());
    }
    @Test
    public void testRipristinaVersione() {
        DatabaseCore.enableTestMode();
        DatabaseNote.close();
        DatabaseCore.enableTestMode();
        
        NotaService service = new NotaService();
        service.creaNuovaNota("Titolo", "Testo Originale", "user1");
        
        Nota nota = DatabaseNote.findAccessibili("user1").get(0);
        String idNota = nota.getId();
        
        // Creo due nuove versioni
        service.aggiungiVersione(nota, "Modifica 1"); // Diventa Versione 1
        service.aggiungiVersione(nota, "Modifica 2"); // Diventa Versione 2
        DatabaseNote.getNoteRepo().put(idNota, nota);
        
        // Ripristino la Versione 1
        boolean ripristinato = service.ripristinaVersione(idNota, 1, "user1");
        
        assertTrue(ripristinato);
        assertEquals("Modifica 1", DatabaseNote.findById(idNota).getContenuto());
    }
}
