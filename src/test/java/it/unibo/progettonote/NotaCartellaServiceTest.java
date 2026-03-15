package it.unibo.progettonote;
import it.unibo.progettonote.client.Nota;
import it.unibo.progettonote.client.*;
import it.unibo.progettonote.server.*;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class NotaCartellaServiceTest {

    @Before
    public void setup() {
        DatabaseCore.enableTestMode();
        DatabaseCartelle.getCartelleRepo().clear();
        DatabaseNote.getNoteRepo().clear();
        DatabaseCore.commit();
    }

    @Test
    public void spostaNotaInCartellaOk() {
        Cartella c = new Cartella("Lavoro", "mario");
        DatabaseCartelle.getCartelleRepo().put(c.getId(), c);

        Nota n = new Nota("Titolo", "Contenuto", "mario");
        n.setId("id_nota");
        DatabaseNote.getNoteRepo().put("id_nota", n);
        DatabaseCore.commit();

        NotaCartellaService service = new NotaCartellaService();
        service.spostaNotaInCartella("id_nota", c.getId(), "mario");

        assertEquals(c.getId(), DatabaseNote.getNoteRepo().get("id_nota").getIdCartella());
    }

    @Test(expected = IllegalArgumentException.class)
    public void spostaNotaInCartellaCartellaNonMia() {
        Cartella c = new Cartella("Segreta", "luigi");
        DatabaseCartelle.getCartelleRepo().put(c.getId(), c);

        Nota n = new Nota("Titolo", "Contenuto", "mario");
        n.setId("id_nota");
        DatabaseNote.getNoteRepo().put("id_nota", n);
        DatabaseCore.commit();

        NotaCartellaService service = new NotaCartellaService();
        service.spostaNotaInCartella("id_nota", c.getId(), "mario");
    }

    @Test
    public void listaNotePerCartellaOk() {
        Cartella c = new Cartella("Lavoro", "mario");
        DatabaseCartelle.getCartelleRepo().put(c.getId(), c);

        Nota n1 = new Nota("A", "X", "mario");
        n1.setId("id_1"); n1.setIdCartella(c.getId());

        Nota n2 = new Nota("B", "Y", "mario");
        n2.setId("id_2"); n2.setIdCartella(null);

        DatabaseNote.getNoteRepo().put(n1.getId(), n1);
        DatabaseNote.getNoteRepo().put(n2.getId(), n2);
        DatabaseCore.commit();

        NotaCartellaService service = new NotaCartellaService();
        List<Nota> res = service.listaNotePerCartella(c.getId(), "mario");

        assertEquals(1, res.size());
        assertEquals("A", res.get(0).getTitolo());
    }
}