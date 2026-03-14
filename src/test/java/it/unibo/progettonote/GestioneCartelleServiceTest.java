package it.unibo.progettonote;

import org.junit.Before;
import org.junit.Test;

import it.unibo.progettonote.client.Cartella;
import it.unibo.progettonote.server.DatabaseCartelle;
import it.unibo.progettonote.server.DatabaseCore;
import it.unibo.progettonote.server.DatabaseNote;
import it.unibo.progettonote.server.GestioneCartelleService;

import java.util.List;
import static org.junit.Assert.*;

public class GestioneCartelleServiceTest {

    private GestioneCartelleService service; // Usiamo il nuovo servizio della collega
    private final String UTENTE_TEST = "mario@test.it";

    @Before
    public void setup() {
        // Reset centralizzato
        DatabaseCore.enableTestMode();
        DatabaseCartelle.close();
        DatabaseNote.close();

        // Pulizia esplicita
        DatabaseCartelle.getCartelleRepo().clear();
        DatabaseNote.getNoteRepo().clear();
        DatabaseCore.commit();

        // Inizializziamo il nuovo servizio
        service = new GestioneCartelleService();
    }

    @Test
    public void testCreaCartellaSuccesso() {
        String nome = "Università";
        Cartella c = service.creaCartella(nome, UTENTE_TEST);

        assertNotNull(c.getId());
        assertEquals(nome, c.getNome());
        assertEquals(UTENTE_TEST, c.getProprietario());

        // Verifichiamo con il repo fisico
        assertTrue(DatabaseCartelle.getCartelleRepo().containsKey(c.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreaCartellaNomeVuoto() {
        service.creaCartella("   ", UTENTE_TEST); // Gestione spaziatura inclusa
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreaCartellaProprietarioNull() {
        service.creaCartella("Lavoro", null);
    }

    @Test
    public void testListaCartellePerUtente() {
        // Creazione dati
        service.creaCartella("Cartella 1", UTENTE_TEST);
        service.creaCartella("Cartella 2", UTENTE_TEST);
        service.creaCartella("Altro Utente", "luigi@test.it");

        // Cambiato nome metodo: listaCartellePerUtente -> listaCartelle
        List<Cartella> lista = service.listaCartelle(UTENTE_TEST);

        assertEquals(2, lista.size());
        // Verifichiamo anche l'ordinamento
        assertEquals("Cartella 1", lista.get(0).getNome());
        assertEquals("Cartella 2", lista.get(1).getNome());
    }

    @Test
    public void testListaVuotaSeNessunaCartella() {
        List<Cartella> lista = service.listaCartelle("utente_nuovo");
        assertTrue(lista.isEmpty());
    }
}