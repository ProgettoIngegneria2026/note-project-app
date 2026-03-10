package it.unibo.progettonote;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotaCartellaServiceTest {

    @Test
    void testAggiungiENaviga() {
        NotaService notaService = new NotaService();
        NotaCartellaService service = new NotaCartellaService(notaService);

        Nota nota = notaService.creaNuovaNota("1", "Titolo", "Contenuto", "user1");
        service.aggiungiNota(nota);

        assertNotNull(service);
    }

    @Test
    void testSpostaNota() {
        NotaService notaService = new NotaService();
        NotaCartellaService service = new NotaCartellaService(notaService);

        Nota nota = notaService.creaNuovaNota("1", "Titolo", "Contenuto", "user1");
        service.aggiungiNota(nota);

        service.spostaNotaInCartella("1", "vecchia", "nuova");
        assertNotNull(service);
    }
}
