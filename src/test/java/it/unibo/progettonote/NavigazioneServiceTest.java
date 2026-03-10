package it.unibo.progettonote;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class NavigazioneServiceTest {

    @Test
    void testNotaPiuRecente() {
        NotaService notaService = new NotaService();
        NavigazioneService nav = new NavigazioneService(notaService);

        Nota n1 = notaService.creaNuovaNota("1", "Titolo1", "Contenuto1", "user1");
        Nota n2 = notaService.creaNuovaNota("2", "Titolo2", "Contenuto2", "user1");

        List<Nota> note = Arrays.asList(n1, n2);
        assertEquals(n2, nav.notaPiuRecente(note));
    }
}
