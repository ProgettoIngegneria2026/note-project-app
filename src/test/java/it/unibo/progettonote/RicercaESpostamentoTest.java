package it.unibo.progettonote;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RicercaESpostamentoTest {

    @Test
    void testAggiungiESpostaNota() {
        NotaService notaService = new NotaService();
        NotaCartellaService ncs = new NotaCartellaService(notaService);

        Nota n = notaService.creaNuovaNota("1", "Titolo", "Contenuto", "user1");
        ncs.aggiungiNota(n);
        ncs.spostaNotaInCartella("1", "vecchia", "nuova");

        assertNotNull(ncs);
    }
}
