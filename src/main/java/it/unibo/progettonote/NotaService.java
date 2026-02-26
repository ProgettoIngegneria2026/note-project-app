package it.unibo.progettonote;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
public class NotaService {
    
    public void creaNuovaNota(String titolo, String contenuto, String proprietario, String idCartella) {
        // 1. Validazione 
        if (contenuto == null || contenuto.length() > 280) {
            throw new IllegalArgumentException("Contenuto mancante o superiore a 280 caratteri.");
        }

        // 2. Istanziazione Nota [cite: 9]
        Nota nuovaNota = new Nota();
        nuovaNota.setTitolo(titolo);
        nuovaNota.setContenuto(contenuto);
        nuovaNota.setProprietario(proprietario);
        nuovaNota.setIdCartella(idCartella);
        
        Date oraAttuale = new Date();
        nuovaNota.setDataCreazione(oraAttuale);
        nuovaNota.setDataUltimaModifica(oraAttuale);

        // 3. Generazione Versione iniziale [cite: 12, 13]
        VersioneNota v1 = new VersioneNota(1, titolo, contenuto, oraAttuale);
        nuovaNota.getVersioni().add(v1);

        // 4. Persistenza [cite: 36, 69]
        String idUnivoco = UUID.randomUUID().toString();
        DatabaseManager.getNoteRepo().put(idUnivoco, nuovaNota);
        DatabaseManager.getDB().commit(); // Salva fisicamente su disco 
    }
}