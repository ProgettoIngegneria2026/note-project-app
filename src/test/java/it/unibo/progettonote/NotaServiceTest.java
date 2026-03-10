package it.unibo.progettonote;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotaServiceTest {

    @Test
    void testCreaNuovaNota() {
        NotaService service = new NotaService();
        Nota n = service.creaNuovaNota("1", "Titolo", "Contenuto", "user1");
        assertNotNull(n);
        assertEquals("Titolo", n.getTitolo());
    }

    @Test
    void testUpdateNota() {
        NotaService service = new NotaService();
        Nota n = service.creaNuovaNota("1", "Titolo", "Contenuto", "user1");
        service.updateNota("1", "NuovoTitolo", "NuovoContenuto", "user1");
        assertEquals("NuovoTitolo", n.getTitolo());
        assertEquals("NuovoContenuto", n.getContenuto());
    }

    @Test
    void testFindByOwner() {
        NotaService service = new NotaService();
        service.creaNuovaNota("1", "Titolo", "Contenuto", "user1");
        assertEquals(1, service.findByOwner("user1").size());
    }

    @Test
    void testFindByIdAndOwner() {
        NotaService service = new NotaService();
        service.creaNuovaNota("1", "Titolo", "Contenuto", "user1");
        Nota n = service.findByIdAndOwner("1", "user1");
        assertNotNull(n);
    }
}
