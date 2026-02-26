package it.unibo.progettonote;
public class NotaTest {
    @Test
    public void testCreazioneNotaValida() {
        Nota nota = new Nota("Titolo", "Contenuto breve", "User1");
        assertTrue(nota.getContenuto().length() <= 280); [cite: 8]
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreazioneNotaTroppoLunga() {
        String testoLungo = "a".repeat(281);
        // La logica deve lanciare un'eccezione se si supera il limite 
        ValidatoreNote.valida(testoLungo); 
    }
}