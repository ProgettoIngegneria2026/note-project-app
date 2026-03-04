package it.unibo.progettonote;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class NavigazioneServiceTest {

    @Before
    public void setup() {
        DatabaseCore.enableTestMode();
        DatabaseNote.close();
        DatabaseNote.getNoteRepo().clear();
        DatabaseCore.commit();
    }

    @Test
    public void listaNoteSoloUtenteOrdinatePerData() {
        String mario = "mario";
        String luigi = "luigi";

        Nota n1 = new Nota("A", "c1", mario);
        n1.setId(UUID.randomUUID().toString());
        n1.setDataUltimaModifica(new Date(1000));

        Nota n2 = new Nota("B", "c2", mario);
        n2.setId(UUID.randomUUID().toString());
        n2.setDataUltimaModifica(new Date(3000));

        Nota n3 = new Nota("C", "c3", luigi);
        n3.setId(UUID.randomUUID().toString());
        n3.setDataUltimaModifica(new Date(5000));

        DatabaseNote.getNoteRepo().put(n1.getId(), n1);
        DatabaseNote.getNoteRepo().put(n2.getId(), n2);
        DatabaseNote.getNoteRepo().put(n3.getId(), n3);
        DatabaseCore.commit();

        NavigazioneService service = new NavigazioneService();
        List<Nota> res = service.listaNoteUtenteOrdinate(mario);

        assertEquals(2, res.size());
        assertEquals("B", res.get(0).getTitolo());
        assertEquals("A", res.get(1).getTitolo());
    }

    @Test
    public void dettaglioNotaOk() {
        String mario = "mario";

        Nota n = new Nota("Titolo", "Contenuto", mario);
        String id = UUID.randomUUID().toString();
        n.setId(id);

        DatabaseNote.getNoteRepo().put(id, n);
        DatabaseCore.commit();

        NavigazioneService service = new NavigazioneService();
        Nota det = service.dettaglioNota(id, mario);

        assertEquals("Titolo", det.getTitolo());
        assertEquals("Contenuto", det.getContenuto());
    }

    @Test(expected = IllegalArgumentException.class)
    public void dettaglioNotaNonMia() {
        String mario = "mario";
        String luigi = "luigi";

        Nota n = new Nota("Titolo", "Contenuto", luigi);
        String id = UUID.randomUUID().toString();
        n.setId(id);

        DatabaseNote.getNoteRepo().put(id, n);
        DatabaseCore.commit();

        NavigazioneService service = new NavigazioneService();
        service.dettaglioNota(id, mario);
    }
}