package it.unibo.progettonote;

import org.junit.Before;
import org.junit.Test;

import it.unibo.progettonote.client.Cartella;
import it.unibo.progettonote.client.Nota;
import it.unibo.progettonote.server.DatabaseCartelle;
import it.unibo.progettonote.server.DatabaseCore;
import it.unibo.progettonote.server.DatabaseNote;
import it.unibo.progettonote.server.NotaCartellaService;

import java.util.List;
import static org.junit.Assert.*;

public class NotaCartellaServiceTest {

    @Before
    public void setup() {
        DatabaseCore.enableTestMode();
        DatabaseCartelle.close();
        DatabaseNote.close();
        DatabaseCartelle.getCartelleRepo().clear();
        DatabaseNote.getNoteRepo().clear();
        DatabaseCore.commit();
    }

    @Test
    public void spostaNotaInCartellaOk() {
        String user = "mario";

        Cartella c = new Cartella("Lavoro", user);
        DatabaseCartelle.getCartelleRepo().put(c.getId(), c);

        Nota n = new Nota("Titolo", "Contenuto", user);
        String notaId = n.getId();
        DatabaseNote.getNoteRepo().put(notaId, n);
        DatabaseCore.commit();

        NotaCartellaService service = new NotaCartellaService();
        service.spostaNotaInCartella(notaId, c.getId(), user);

        Nota aggiornata = DatabaseNote.getNoteRepo().get(notaId);
        assertEquals(c.getId(), aggiornata.getIdCartella());
    }

    @Test(expected = IllegalArgumentException.class)
    public void spostaNotaInCartellaCartellaNonMia() {
        String user = "mario";

        Cartella c = new Cartella("Segreta", "luigi");
        DatabaseCartelle.getCartelleRepo().put(c.getId(), c);

        Nota n = new Nota("Titolo", "Contenuto", user);
        String notaId = n.getId();
        DatabaseNote.getNoteRepo().put(notaId, n);
        DatabaseCore.commit();

        NotaCartellaService service = new NotaCartellaService();
        service.spostaNotaInCartella(notaId, c.getId(), user);
    }

    @Test
    public void listaNotePerCartellaOk() {
        String user = "mario";

        Cartella c = new Cartella("Lavoro", user);
        DatabaseCartelle.getCartelleRepo().put(c.getId(), c);

        Nota n1 = new Nota("A", "X", user);
        n1.setIdCartella(c.getId());

        Nota n2 = new Nota("B", "Y", user);
        n2.setIdCartella(null);

        DatabaseNote.getNoteRepo().put(n1.getId(), n1);
        DatabaseNote.getNoteRepo().put(n2.getId(), n2);
        DatabaseCore.commit();

        NotaCartellaService service = new NotaCartellaService();
        List<Nota> res = service.listaNotePerCartella(c.getId(), user);

        assertEquals(1, res.size());
        assertEquals("A", res.get(0).getTitolo());
    }
}