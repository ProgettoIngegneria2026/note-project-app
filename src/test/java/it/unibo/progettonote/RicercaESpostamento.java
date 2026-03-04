package it.unibo.progettonote;

import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;

public class RicercaESpostamento {

    private RicercaService ricercaService;
    private NotaCartellaService notaCartellaService;
    private final String EMAIL = "email@email.it";

    @Before
    public void setup() {
        // Reset totale e modalità in-memory
        DatabaseCore.enableTestMode();
        DatabaseNote.close();
        DatabaseCartelle.close();

        DatabaseNote.getNoteRepo().clear();
        DatabaseCartelle.getCartelleRepo().clear();
        DatabaseCore.commit();

        ricercaService = new RicercaService();
        notaCartellaService = new NotaCartellaService();
    }

    @Test
    public void testFlussoCompletoSpostamentoERicerca() {
        // 1. Preparazione: Creiamo 1 cartella e 2 note
        Cartella c1 = new Cartella("Esami", EMAIL);
        DatabaseCartelle.getCartelleRepo().put(c1.getId(), c1);

        Nota n1 = new Nota("Studiare SWENG", "Pattern e Git", EMAIL);
        Nota n2 = new Nota("Spesa", "Pane e Latte", EMAIL);
        DatabaseNote.getNoteRepo().put(n1.getId(), n1);
        DatabaseNote.getNoteRepo().put(n2.getId(), n2);
        DatabaseCore.commit();

        // 2. TEST UC6: Spostiamo la nota SWENG nella cartella Esami
        notaCartellaService.spostaNotaInCartella(n1.getId(), c1.getId(), EMAIL);

        Nota notaSpostata = DatabaseNote.getNoteRepo().get(n1.getId());
        assertEquals("La nota dovrebbe essere nella cartella Esami", c1.getId(), notaSpostata.getIdCartella());

        // 3. TEST UC7: Cerchiamo la parola "SWENG"
        List<Nota> risultatiTesto = ricercaService.cercaPerParolaChiave("SWENG", EMAIL);
        assertEquals("Dovrebbe trovare 1 nota", 1, risultatiTesto.size());
        assertEquals("Studiare SWENG", risultatiTesto.get(0).getTitolo());

        // 4. TEST UC7: Cerchiamo con una query che non esiste
        List<Nota> vuoto = ricercaService.cercaPerParolaChiave("Pizza", EMAIL);
        assertTrue("Non dovrebbe trovare nulla", vuoto.isEmpty());

        // 5. TEST UC7: Filtro Date (Oggi)
        Date inizio = new Date(System.currentTimeMillis() - 100000); // un po' prima di adesso
        Date fine = new Date(System.currentTimeMillis() + 100000);   // un po' dopo
        List<Nota> risultatiData = ricercaService.cercaPerData(inizio, fine, EMAIL);
        assertEquals("Entrambe le note sono state create oggi", 2, risultatiData.size());
    }
}