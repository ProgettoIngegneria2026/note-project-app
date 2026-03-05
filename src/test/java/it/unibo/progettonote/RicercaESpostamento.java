package it.unibo.progettonote;

import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;

public class RicercaESpostamento {

    private RicercaService ricercaService;
    private NotaCartellaService notaCartellaService;
    private FolderService folderService; // Usiamo il service per la creazione
    private final String EMAIL = "email@email.it";

    @Before
    public void setup() {
        // Reset centralizzato tramite Core
        DatabaseCore.enableTestMode();

        // Pulizia esplicita di tutti i repository
        DatabaseNote.close();
        DatabaseCartelle.close();
        DatabaseUtenti.close();

        DatabaseNote.getNoteRepo().clear();
        DatabaseCartelle.getCartelleRepo().clear();
        DatabaseCore.commit();

        ricercaService = new RicercaService();
        notaCartellaService = new NotaCartellaService();
        folderService = new FolderService();
    }

    @Test
    public void testFlussoCompletoSpostamentoERicerca() {
        // 1. Preparazione: Creiamo una cartella usando il FolderService
        Cartella c1 = folderService.creaCartella("Esami", EMAIL);

        // Verifichiamo se esiste davvero
        assertTrue("La cartella dovrebbe esistere nel DB",
                DatabaseCartelle.existsByNameAndOwner("Esami", EMAIL));

        // 2. Creazione Note
        Nota n1 = new Nota("Studiare SWENG", "Pattern e Git", EMAIL);
        Nota n2 = new Nota("Spesa", "Pane e Latte", EMAIL);
        DatabaseNote.getNoteRepo().put(n1.getId(), n1);
        DatabaseNote.getNoteRepo().put(n2.getId(), n2);
        DatabaseCore.commit();

        // 3. TEST UC6: Spostamento
        notaCartellaService.spostaNotaInCartella(n1.getId(), c1.getId(), EMAIL);

        // Verifica dello spostamento usando il metodo helper del repository
        Nota notaSpostata = DatabaseNote.getNoteRepo().get(n1.getId());
        assertEquals("L'ID cartella della nota deve corrispondere", c1.getId(), notaSpostata.getIdCartella());

        // 4. TEST UC7: Ricerca per Testo
        List<Nota> risultatiTesto = ricercaService.cercaPerParolaChiave("SWENG", EMAIL);
        assertEquals("Dovrebbe trovare esattamente 1 nota", 1, risultatiTesto.size());
        assertTrue(risultatiTesto.get(0).getTitolo().contains("SWENG"));

        // 5. TEST UC7: Filtro Date
        // Creiamo un range che include il momento attuale
        Date inizio = new Date(System.currentTimeMillis() - 5000); // 5 secondi fa
        Date fine = new Date(System.currentTimeMillis() + 5000);   // tra 5 secondi

        List<Nota> risultatiData = ricercaService.cercaPerData(inizio, fine, EMAIL);
        assertEquals("Entrambe le note sono state create ora, quindi deve trovarne 2", 2, risultatiData.size());
    }
}