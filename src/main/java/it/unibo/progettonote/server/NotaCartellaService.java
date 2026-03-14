
package it.unibo.progettonote.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.unibo.progettonote.client.Cartella;
import it.unibo.progettonote.client.Nota;

public class NotaCartellaService {

    private final Map<String, Nota> noteRepo;
    private final Map<String, Cartella> cartelleRepo;
    private final GestioneCartelleService gestioneCartelleService;

    public NotaCartellaService() {
        this.noteRepo = DatabaseNote.getNoteRepo();
        this.cartelleRepo = DatabaseCartelle.getCartelleRepo();
        this.gestioneCartelleService = new GestioneCartelleService();
    }

    /**
     * Sposta una nota in una cartella specifica o nella root (null).
     * Gestisce i controlli di proprietà e l'atomicità del salvataggio.
     */
    public void spostaNotaInCartella(String notaId, String cartellaId, String proprietario) {
        if (notaId == null || notaId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID nota non valido");
        }
        if (proprietario == null || proprietario.trim().isEmpty()) {
            throw new IllegalArgumentException("Proprietario non valido");
        }

        Nota nota = noteRepo.get(notaId);
        if (nota == null) throw new IllegalArgumentException("Nota inesistente");

        // Controllo sicurezza: la nota deve appartenere a chi la sposta
        if (!proprietario.equals(nota.getProprietario())) {
            throw new IllegalArgumentException("Non hai i permessi per spostare questa nota");
        }

        // Caso SPOSTAMENTO IN ROOT (nessuna cartella)
        if (cartellaId == null || "ROOT".equalsIgnoreCase(cartellaId) || cartellaId.trim().isEmpty()) {
            nota.setIdCartella(null);
        } else {
            // Caso SPOSTAMENTO IN CARTELLA SPECIFICA
            Cartella cartella = cartelleRepo.get(cartellaId);
            if (cartella == null) throw new IllegalArgumentException("Cartella di destinazione inesistente");

            if (!proprietario.equals(cartella.getProprietario())) {
                throw new IllegalArgumentException("La cartella di destinazione non ti appartiene");
            }
            nota.setIdCartella(cartellaId);
        }

        // Salvataggio e Commit centralizzato
        noteRepo.put(notaId, nota);
        DatabaseCore.commit();
    }

    /**
     * Delega la lista delle cartelle al service di gestione cartelle.
     */

    public List<Cartella> listaCartelleUtente(String proprietario) {
        return gestioneCartelleService.listaCartelle(proprietario);
    }

    /**
     * Recupera tutte le note appartenenti a una specifica cartella per un utente.
     */
    public List<Nota> listaNotePerCartella(String cartellaId, String proprietario) {
        if (proprietario == null) throw new IllegalArgumentException("Proprietario mancante");

        List<Nota> risultato = new ArrayList<>();
        for (Nota n : noteRepo.values()) {
            boolean ownerMatch = proprietario.equals(n.getProprietario());

            // Gestione logica: se cartellaId è null, cerchiamo le note in Root
            boolean folderMatch = (cartellaId == null) ? (n.getIdCartella() == null) : cartellaId.equals(n.getIdCartella());

            if (ownerMatch && folderMatch) {
                risultato.add(n);
            }
        }
        return risultato;
    }
}