package it.unibo.progettonote;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FolderServiceTest {

    @Before
    public void setup() {
        DatabaseCartelle.enableTestMode();
    }

    @Test
    public void creaCartellaOk() {
        FolderService service = new FolderService();
        Cartella c = service.creaCartella("Lavoro", "mario");

        assertNotNull(c.getId());
        assertEquals("Lavoro", c.getNome());
        assertEquals("mario", c.getProprietario());
    }

    @Test(expected = IllegalArgumentException.class)
    public void creaCartellaNomeVuoto() {
        FolderService service = new FolderService();
        service.creaCartella("   ", "mario");
    }
}