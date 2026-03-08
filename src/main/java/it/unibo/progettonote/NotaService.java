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

            // 3. Salvataggio tramite DatabaseNote
            ConcurrentNavigableMap<String, Nota> repo = DatabaseNote.getNoteRepo();
            repo.put(idUnivoco, nuovaNota);
            DatabaseCore.commit();

            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Errore validazione: " + e.getMessage());
            return false;
        }
    }

    // Metodo per aggiungere una versione
    public void aggiungiVersione(Nota nota, String nuovoContenuto) {
        int numeroVersione = nota.getVersioni().size() + 1;
        VersioneNota v = new VersioneNota(
                numeroVersione,
                nuovoContenuto,
                nota.getProprietario(),
                new Date()
        );

        nota.getVersioni().add(v);
        nota.setContenuto(nuovoContenuto);
        nota.setDataUltimaModifica(new Date());
    }

    // Metodo per aggiornare una nota esistente (UC5)
    public boolean updateNota(String idNota, String nuovoTitolo, String nuovoContenuto, String proprietario) {
        try {
            ValidatoreNote.valida(nuovoContenuto);

            ConcurrentNavigableMap<String, Nota> repo = DatabaseNote.getNoteRepo();
            Nota nota = repo.get(idNota);

            if (nota == null) {
                throw new IllegalArgumentException("Nota non trovata.");
            }

            if (!nota.getProprietario().equals(proprietario)) {
                throw new IllegalArgumentException("Operazione non autorizzata.");
            }

            nota.setTitolo(nuovoTitolo);
            nota.setContenuto(nuovoContenuto);
            nota.setDataUltimaModifica(new Date());

            repo.put(idNota, nota);
            DatabaseCore.commit();

            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Errore aggiornamento: " + e.getMessage());
            return false;
        }
    }
}