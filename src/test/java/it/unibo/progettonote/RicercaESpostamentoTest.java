package it.unibo.progettonote;
import it.unibo.progettonote.client.*;
import it.unibo.progettonote.server.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;

public class RicercaESpostamentoTest {

    private RicercaService ricercaService;
    private NotaCartellaService notaCartellaService;
    private GestioneCartelleService gestioneCartelleService;
    private final String EMAIL = "email@email.it";

    @Before
    public void setup() {
        DatabaseCore.enableTestMode();
        DatabaseNote.getNoteRepo().clear();
        DatabaseCartelle.getCartelleRepo().clear();
        DatabaseCore.commit();

        ricercaService = new RicercaService();
        notaCartellaService = new NotaCartellaService();
        gestioneCartelleService = new GestioneCartelleService();
    }

    @Test
    public void testFlussoCompletoSpostamentoERicerca() {
        Cartella c1 = gestioneCartelleService.creaCartella("Esami", EMAIL);
        assertTrue(DatabaseCartelle.existsByNameAndOwner("Esami", EMAIL));

        Nota n1 = new Nota("Studiare SWENG", "Pattern e Git", EMAIL);
        n1.setId("n1_id"); // Assegnazione esplicita ID pulita
        Nota n2 = new Nota("Spesa", "Pane e Latte", EMAIL);
        n2.setId("n2_id");

        DatabaseNote.getNoteRepo().put(n1.getId(), n1);
        DatabaseNote.getNoteRepo().put(n2.getId(), n2);
        DatabaseCore.commit();

        notaCartellaService.spostaNotaInCartella(n1.getId(), c1.getId(), EMAIL);
        assertEquals(c1.getId(), DatabaseNote.getNoteRepo().get(n1.getId()).getIdCartella());

        List<Nota> risultatiTesto = ricercaService.cercaPerParolaChiave("SWENG", EMAIL);
        assertEquals(1, risultatiTesto.size());

        Date inizio = new Date(System.currentTimeMillis() - 5000); 
        Date fine = new Date(System.currentTimeMillis() + 5000);   
        List<Nota> risultatiData = ricercaService.cercaPerData(inizio, fine, EMAIL);
        assertEquals(2, risultatiData.size());
    }
}