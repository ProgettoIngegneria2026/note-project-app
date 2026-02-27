package it.unibo.progettonote;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;

public class NotaService {

    // Metodo per creare una nuova nota (UC4)
    public boolean creaNuovaNota(String titolo, String contenuto, String proprietario) {
        try {
            // 1. Validazione (280 caratteri)
            ValidatoreNote.valida(contenuto);

            // 2. Creazione oggetto Nota
            Nota nuovaNota = new Nota(titolo, contenuto, proprietario);
            String idUnivoco = UUID.randomUUID().toString();
            nuovaNota.setId(idUnivoco);

            // 3. Salvataggio su Database
            ConcurrentNavigableMap<String, Nota> repo = DatabaseUtenti.getUtentiRepo().getDB()
                .treeMap("notes", org.mapdb.Serializer.STRING, org.mapdb.Serializer.JAVA)
                .createOrOpen();
            
            repo.put(idUnivoco, nuovaNota);
            DatabaseUtenti.getDB().commit();
            
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Errore validazione: " + e.getMessage());
            return false;
        }
    }

    // Metodo per aggiungere una versione (richiesto dai tuoi errori precedenti)
    public void aggiungiVersione(Nota nota, String nuovoContenuto) {
        int numeroVersione = nota.getVersioni().size() + 1;
        VersioneNota v = new VersioneNota(numeroVersione, nuovoContenuto, nota.getProprietario(), new Date());
        nota.getVersioni().add(v);
        nota.setContenuto(nuovoContenuto);
        nota.setDataUltimaModifica(new Date());
    }
}