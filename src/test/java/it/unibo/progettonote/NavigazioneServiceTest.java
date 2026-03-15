package it.unibo.progettonote;
import it.unibo.progettonote.client.Nota;
import it.unibo.progettonote.client.*;
import it.unibo.progettonote.server.*;

import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;

public class NavigazioneServiceTest {

    @Before
    public void setup() {
        DatabaseCore.enableTestMode();
        DatabaseNote.getNoteRepo().clear();
        DatabaseCore.commit();
    }

    @Test
    public void listaNoteSoloUtenteOrdinatePerData() {
        String mario = "mario";
        
        Nota n1 = new Nota("A", "c1", mario);
        n1.setId("id_A"); n1.setDataUltimaModifica(new Date(1000));

        Nota n2 = new Nota("B", "c2", mario);
        n2.setId("id_B"); n2.setDataUltimaModifica(new Date(3000));

        Nota n3 = new Nota("C", "c3", "luigi"); // Nota di un altro
        n3.setId("id_C"); n3.setDataUltimaModifica(new Date(5000));

        DatabaseNote.getNoteRepo().put(n1.getId(), n1);
        DatabaseNote.getNoteRepo().put(n2.getId(), n2);
        DatabaseNote.getNoteRepo().put(n3.getId(), n3);
        DatabaseCore.commit();

        NavigazioneService service = new NavigazioneService();
        List<Nota> res = service.listaNoteUtenteOrdinate(mario);

        assertEquals(2, res.size());
        assertEquals("B", res.get(0).getTitolo()); // B è più recente di A
        assertEquals("A", res.get(1).getTitolo());
    }

    @Test
    public void dettaglioNotaOk() {
        Nota n = new Nota("Titolo", "Contenuto", "mario");
        n.setId("id_test");
        DatabaseNote.getNoteRepo().put("id_test", n);
        DatabaseCore.commit();

        NavigazioneService service = new NavigazioneService();
        Nota det = service.dettaglioNota("id_test", "mario");
        assertEquals("Titolo", det.getTitolo());
    }

    @Test(expected = IllegalArgumentException.class)
    public void dettaglioNotaNonMia() {
        Nota n = new Nota("Titolo", "Contenuto", "luigi");
        n.setId("id_vietato");
        DatabaseNote.getNoteRepo().put("id_vietato", n);
        DatabaseCore.commit();

        NavigazioneService service = new NavigazioneService();
        service.dettaglioNota("id_vietato", "mario"); // Lancia eccezione
    }
}