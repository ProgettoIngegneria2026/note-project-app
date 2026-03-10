package it.unibo.progettonote;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;

public class NotaService {

    public boolean creaNuovaNota(String titolo, String contenuto, String proprietario) {

        try {

            ValidatoreNote.valida(contenuto);

            Nota nuovaNota = new Nota(titolo, contenuto, proprietario);

            String idUnivoco = UUID.randomUUID().toString();
            nuovaNota.setId(idUnivoco);

            ConcurrentNavigableMap<String, Nota> repo = DatabaseNote.getNoteRepo();
            repo.put(idUnivoco, nuovaNota);

            DatabaseCore.commit();

            return true;

        } catch (IllegalArgumentException e) {

            System.out.println("Errore validazione: " + e.getMessage());
            return false;

        }
    }

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

    public boolean updateNota(String idNota, String nuovoTitolo, String nuovoContenuto, String proprietario) {

        try {

            ValidatoreNote.valida(nuovoContenuto);

            ConcurrentNavigableMap<String, Nota> repo = DatabaseNote.getNoteRepo();

            Nota nota = repo.get(idNota);

            if (nota == null) {
                throw new IllegalArgumentException("Nota non trovata");
            }

            if (!nota.puoModificare(proprietario)) {
                throw new IllegalArgumentException("Operazione non autorizzata");
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

    public boolean aggiungiCollaboratore(String idNota, String proprietario, String collaboratore) {

        ConcurrentNavigableMap<String, Nota> repo = DatabaseNote.getNoteRepo();

        Nota nota = repo.get(idNota);

        if (nota == null) return false;

        if (!nota.getProprietario().equals(proprietario)) {
            return false;
        }

        nota.aggiungiCollaboratore(collaboratore);

        repo.put(idNota, nota);

        DatabaseCore.commit();

        return true;
    }

    public boolean rimuoviCollaboratore(String idNota, String proprietario, String collaboratore) {

        ConcurrentNavigableMap<String, Nota> repo = DatabaseNote.getNoteRepo();

        Nota nota = repo.get(idNota);

        if (nota == null) return false;

        if (!nota.getProprietario().equals(proprietario)) {
            return false;
        }

        nota.rimuoviCollaboratore(collaboratore);

        repo.put(idNota, nota);

        DatabaseCore.commit();

        return true;
    }
}
