import it.unibo.progettonote.Nota;


import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class NotaServiceTest {

    private NotaService notaService;

    @Before
    public void setUp() {
        notaService = new NotaService();
        DatabaseNote.reset(); // puliamo la repo prima di ogni test
    }

    @Test
    public void testCreaNuovaNota() {
        boolean creata = notaService.creaNuovaNota("Titolo Test", "Contenuto Test", "proprietario@email.it");
        assertTrue(creata);

        Nota nota = DatabaseNote.getNoteRepo().firstEntry().getValue();
        assertEquals("Titolo Test", nota.getTitolo());
        assertEquals("Contenuto Test", nota.getContenuto());
        assertEquals("proprietario@email.it", nota.getProprietario());
        assertNotNull(nota.getId());
        assertNotNull(nota.getDataCreazione());
    }

    @Test

public void testAggiungiERimuoviCollaboratore() {
    // Creazione nota
    notaService.creaNuovaNota("Titolo1", "Contenuto1", "proprietario@email.it");
    String notaId = DatabaseNote.getNoteRepo().firstKey();

    // Aggiungi collaboratore
    boolean aggiunto = notaService.aggiungiCollaboratore(notaId, "collab@email.it");
    assertTrue(aggiunto);

    Nota nota = DatabaseNote.getNoteRepo().get(notaId);
    assertTrue(nota.getCollaboratori().contains("collab@email.it"));

    // Rimuovi collaboratore
    boolean rimosso = notaService.rimuoviCollaboratore(notaId, "collab@email.it");
    assertTrue(rimosso);

    // Rileggi la nota dal database per verificare lo stato aggiornato
    nota = DatabaseNote.getNoteRepo().get(notaId);
    assertFalse(nota.getCollaboratori().contains("collab@email.it"));
}


    @Test
    public void testGetNoteDiUnCollaboratore() {
        // Creazione nota e aggiunta collaboratore
        notaService.creaNuovaNota("Titolo2", "Contenuto2", "proprietario@email.it");
        String notaId = DatabaseNote.getNoteRepo().firstKey();
        notaService.aggiungiCollaboratore(notaId, "collab2@email.it");

        // Recupero note condivise
        List<Nota> noteCollaboratore = notaService.getNoteCondiviseConUtente("collab2@email.it");
        assertEquals(1, noteCollaboratore.size());
        assertEquals(notaId, noteCollaboratore.get(0).getId());
    }
}
