package it.unibo.progettonote;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class NotaCartellaServiceTest {

    @Before
    public void setup() {
        // DB in-memory fresh
        DatabaseCore.enableTestMode();

        // reset riferimenti a mappe (così non usi mappe vecchie)
        DatabaseCartelle.close();
        DatabaseNote.close();

        // ora prendi mappe nuove e puliscile
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
        String notaId = UUID.randomUUID().toString();
        n.setId(notaId);
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
        String notaId = UUID.randomUUID().toString();
        n.setId(notaId);
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
        n1.setId(UUID.randomUUID().toString());
        n1.setIdCartella(c.getId());

        Nota n2 = new Nota("B", "Y", user);
        n2.setId(UUID.randomUUID().toString());
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