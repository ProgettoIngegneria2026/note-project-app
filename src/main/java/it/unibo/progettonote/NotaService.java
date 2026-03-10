package it.unibo.progettonote;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.stream.Collectors;

public class NotaService {

    // Metodo per creare una nuova nota (UC4)
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
            return false;
        }
    }

    // Controllo permessi scrittura (task #53)
    private boolean utenteHaPermessoScrittura(String utente, Nota nota) {
        if (nota.getPermessi() == null) return true;
        String permesso = nota.getPermessi().get(utente);
        return permesso != null && permesso.equalsIgnoreCase("WRITE");
    }

    // Aggiunge una versione controllando i permessi
    public boolean aggiungiVersione(Nota nota, String nuovoContenuto, String utente) {

        if (!utenteHaPermessoScrittura(utente, nota)) {
            System.out.println("Utente non autorizzato a modificare questa nota.");
            return false;
        }

        int numeroVersione = nota.getVersioni().size() + 1;

        VersioneNota v = new VersioneNota(
                numeroVersione,
                nuovoContenuto,
                utente,
                new Date()
        );

        nota.getVersioni().add(v);
        nota.setContenuto(nuovoContenuto);
        nota.setDataUltimaModifica(new Date());

        return true;
    }

    // Recupera le note condivise con un utente (task #51)
    public List<Nota> getNoteCondiviseConUtente(String utente) {

        ConcurrentNavigableMap<String, Nota> repo = DatabaseNote.getNoteRepo();

        return repo.values()
                .stream()
                .filter(n -> n.getCollaboratori() != null &&
                             n.getCollaboratori().contains(utente))
                .collect(Collectors.toList());
    }
    // Metodo per aggiornare una nota esistente (UC5)
public boolean updateNota(String idNota, String nuovoTitolo, String nuovoContenuto, String proprietario) {

    ConcurrentNavigableMap<String, Nota> repo = DatabaseNote.getNoteRepo();
    Nota nota = repo.get(idNota);

    if (nota == null) {
        return false;
    }

    if (!nota.getProprietario().equals(proprietario)) {
        return false;
    }

    try {
        ValidatoreNote.valida(nuovoContenuto);

        nota.setTitolo(nuovoTitolo);
        nota.setContenuto(nuovoContenuto);
        nota.setDataUltimaModifica(new Date());

        DatabaseCore.commit();
        return true;

    } catch (IllegalArgumentException e) {
        return false;
    }
}
}



