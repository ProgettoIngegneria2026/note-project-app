package it.unibo.progettonote;

import org.junit.Test;

import static org.junit.Assert.*;

public class NotaTest {

    @Test
    public void testCreazioneNotaValida() {
        Nota nota = new Nota("Titolo", "Contenuto breve", "User1");

        assertNotNull(nota.getContenuto());
        assertTrue("Il contenuto deve essere <= 280 caratteri", nota.getContenuto().length() <= 280);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreazioneNotaTroppoLunga() {
        String testoLungo = new String(new char[281]).replace('\0', 'a');
        ValidatoreNote.valida(testoLungo);
    }

    @Test
    public void testUpdateNotaValida() {
        DatabaseNote.close();
        DatabaseCore.enableTestMode();

        try {
            NotaService service = new NotaService();

            boolean creata = service.creaNuovaNota("Titolo vecchio", "Contenuto vecchio", "User1");
            assertTrue(creata);

            Nota nota = DatabaseNote.findByOwner("User1").get(0);

            boolean aggiornata = service.updateNota(
                    nota.getId(),
                    "Titolo nuovo",
                    "Contenuto nuovo",
                    "User1"
            );

            assertTrue(aggiornata);

            Nota notaAggiornata = DatabaseNote.findByIdAndOwner(nota.getId(), "User1");
            assertEquals("Titolo nuovo", notaAggiornata.getTitolo());
            assertEquals("Contenuto nuovo", notaAggiornata.getContenuto());
        } finally {
            DatabaseNote.close();
            DatabaseCore.disableTestMode();
        }
    }

    @Test
    public void testUpdateNotaNonValida() {
        DatabaseNote.close();
        DatabaseCore.enableTestMode();

        try {
            NotaService service = new NotaService();

            boolean creata = service.creaNuovaNota("Titolo", "Contenuto breve", "User1");
            assertTrue(creata);

            Nota nota = DatabaseNote.findByOwner("User1").get(0);

            String testoLungo = new String(new char[281]).replace('\0', 'a');

            boolean aggiornata = service.updateNota(
                    nota.getId(),
                    "Titolo nuovo",
                    testoLungo,
                    "User1"
            );

            assertFalse(aggiornata);
        } finally {
            DatabaseNote.close();
            DatabaseCore.disableTestMode();
        }
    }

    @Test
    public void testUpdateNotaAggiornaDataUltimaModifica() throws InterruptedException {
        DatabaseNote.close();
        DatabaseCore.enableTestMode();

        try {
            NotaService service = new NotaService();

            boolean creata = service.creaNuovaNota("Titolo", "Contenuto", "User1");
            assertTrue(creata);

            Nota nota = DatabaseNote.findByOwner("User1").get(0);
            java.util.Date dataPrima = nota.getDataUltimaModifica();

            Thread.sleep(10);

            boolean aggiornata = service.updateNota(
                    nota.getId(),
                    "Titolo aggiornato",
                    "Contenuto aggiornato",
                    "User1"
            );

            assertTrue(aggiornata);

            Nota notaAggiornata = DatabaseNote.findByIdAndOwner(nota.getId(), "User1");
            assertTrue(notaAggiornata.getDataUltimaModifica().after(dataPrima));
        } finally {
            DatabaseNote.close();
            DatabaseCore.disableTestMode();
        }
    }
}