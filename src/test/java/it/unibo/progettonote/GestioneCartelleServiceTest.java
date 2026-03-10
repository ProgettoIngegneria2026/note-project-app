package it.unibo.progettonote;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class GestioneCartelleServiceTest {

    private GestioneCartelleService service;
    private final String UTENTE_TEST = "mario@test.it";

    @Before
    public void setup() {
        // Reset database
        DatabaseCartelle db = new DatabaseCartelle();
        db.getCartelleRepo().clear();
        DatabaseNote.getNoteRepo().clear();

        service = new GestioneCartelleService();
    }

    @Test
    public void testCreaCartellaSuccesso() {
        Cartella c = service.creaCartella("Università", UTENTE_TEST);

        assertNotNull(c.getId());
        assertEquals("Università", c.getNome());
        assertEquals(UTENTE_TEST, c.getProprietario());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreaCartellaNomeVuoto() {
        service.creaCartella("   ", UTENTE_TEST);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreaCartellaProprietarioNull() {
        service.creaCartella("Lavoro", null);
    }

    @Test
    public void testListaCartellePerUtente() {
        service.creaCartella("Cartella 1", UTENTE_TEST);
        service.creaCartella("Cartella 2", UTENTE_TEST);
        service.creaCartella("Altro Utente", "luigi@test.it");

        List<Cartella> lista = service.listaCartelle(UTENTE_TEST);
        assertEquals(2, lista.size());
    }

    @Test
    public void testRinominaCartella() {
        Cartella c = service.creaCartella("VecchioNome", UTENTE_TEST);
        service.rinominaCartella(c.getId(), UTENTE_TEST, "NuovoNome");

        assertEquals("NuovoNome", c.getNome());
    }
}
