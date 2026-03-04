package it.unibo.progettonote;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class FolderServiceTest {

    private FolderService service;
    private final String UTENTE_TEST = "mario@test.it";

    @Before
    public void setup() {

        DatabaseCartelle.close();
        DatabaseNote.close();

        // 1. Attiviamo la modalità test centralizzata (In-Memory)
        DatabaseCore.enableTestMode();

        // 2. Puliamo i repository per isolare i test
        DatabaseCartelle.getCartelleRepo().clear();
        DatabaseNote.getNoteRepo().clear();

        // 3. Rendiamo effettive le pulizie
        DatabaseCore.commit();

        service = new FolderService();
    }

    @Test
    public void testCreaCartellaSuccesso() {
        String nome = "Università";
        Cartella c = service.creaCartella(nome, UTENTE_TEST);

        assertNotNull(c.getId());
        assertEquals(nome, c.getNome());
        assertEquals(UTENTE_TEST, c.getProprietario());

        // Verifichiamo che sia effettivamente nel DB
        assertTrue(DatabaseCartelle.getCartelleRepo().containsKey(c.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreaCartellaNomeVuoto() {
        service.creaCartella("", UTENTE_TEST);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreaCartellaProprietarioNull() {
        service.creaCartella("Lavoro", null);
    }

    @Test
    public void testListaCartellePerUtente() {
        // Creiamo cartelle per due utenti diversi
        service.creaCartella("Cartella 1", UTENTE_TEST);
        service.creaCartella("Cartella 2", UTENTE_TEST);
        service.creaCartella("Altro Utente", "luigi@test.it");

        List<Cartella> lista = service.listaCartellePerUtente(UTENTE_TEST);

        // Deve vedere solo le 2 cartelle di Mario
        assertEquals(2, lista.size());
        for (Cartella c : lista) {
            assertEquals(UTENTE_TEST, c.getProprietario());
        }
    }

    @Test
    public void testListaVuotaSeNessunaCartella() {
        List<Cartella> lista = service.listaCartellePerUtente("utente_nuovo");
        assertTrue(lista.isEmpty());
    }
}