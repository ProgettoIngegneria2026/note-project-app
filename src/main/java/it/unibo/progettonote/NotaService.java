package it.unibo.progettonote;

import java.util.Date;
import java.util.List;
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

            // 3. Salvataggio tramite il nuovo DatabaseNote centralizzato
            ConcurrentNavigableMap<String, Nota> repo = DatabaseNote.getNoteRepo();
            
            repo.put(idUnivoco, nuovaNota);
            DatabaseCore.commit(); // <-- Uso del nuovo commit di DatabaseCore
            
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Errore validazione: " + e.getMessage());
            return false;
        }
    }

    // Metodo per aggiungere una versione
    public void aggiungiVersione(Nota nota, String nuovoContenuto) {
        int numeroVersione = nota.getVersioni().size() + 1;
        VersioneNota v = new VersioneNota(numeroVersione, nuovoContenuto, nota.getProprietario(), new Date());
        nota.getVersioni().add(v);
        nota.setContenuto(nuovoContenuto);
        nota.setDataUltimaModifica(new Date());
    }
    public List<Nota> getNoteCondiviseConUtente(String utente) {
    ConcurrentNavigableMap<String, Nota> repo = DatabaseNote.getNoteRepo();

    return repo.values().stream()
        .filter(n -> n.getCollaboratori() != null && n.getCollaboratori().contains(utente))
        .toList();
}


}