package it.unibo.progettonote;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotaTest {

    @Test
    void testCreaNota() {
        NotaService service = new NotaService();
        Nota n = service.creaNuovaNota("1", "Titolo", "Contenuto", "user1");
        assertNotNull(n);
    }

    @Test
    void testUpdateNota() {
        NotaService service = new NotaService();
        Nota n = service.creaNuovaNota("1", "Titolo", "Contenuto", "user1");
        service.updateNota("1", "Titolo2", "Contenuto2", "user1");
        assertEquals("Titolo2", n.getTitolo());
    }
}
