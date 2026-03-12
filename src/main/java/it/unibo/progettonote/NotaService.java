package it.unibo.progettonote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
            return false;
        }
    }

    public void aggiungiVersione(Nota nota, String testoDaSalvare) {
        List<VersioneNota> storico = nota.getVersioni();
        if (storico == null) {
            storico = new ArrayList<>();
        } else {
            storico = new ArrayList<>(storico); 
        }

        // BLOCCO ANTI-CLONI INFINITI:
        // Controlla se il testo da salvare è ESATTAMENTE uguale a una versione già in memoria.
        // Gestisce anche i testi vuoti (null e "") in sicurezza.
        boolean giaPresente = storico.stream().anyMatch(v -> {
            String cont = v.getContenuto() == null ? "" : v.getContenuto();
            String daSalvare = testoDaSalvare == null ? "" : testoDaSalvare;
            return cont.equals(daSalvare);
        });

        if (giaPresente) {
            return; // Esce immediatamente senza salvare duplicati
        }

        int numeroVersione = storico.size() + 1;
        VersioneNota v = new VersioneNota(numeroVersione, testoDaSalvare, nota.getProprietario(), new Date());
        storico.add(v);
        
        nota.setVersioni(storico);
    }

    public boolean updateNota(String idNota, String nuovoTitolo, String nuovoContenuto, String proprietario) {
        try {
            ValidatoreNote.valida(nuovoContenuto);
            ConcurrentNavigableMap<String, Nota> repo = DatabaseNote.getNoteRepo();
            Nota nota = repo.get(idNota);

            if (nota == null || !nota.puoModificare(proprietario)) return false;

            // Prova a salvare nello storico SOLO se il contenuto è cambiato
            if (!nota.getContenuto().equals(nuovoContenuto)) {
                aggiungiVersione(nota, nota.getContenuto());
            }

            nota.setTitolo(nuovoTitolo);
            nota.setContenuto(nuovoContenuto);
            nota.setDataUltimaModifica(new Date());

            repo.put(idNota, nota);
            DatabaseCore.commit();
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean ripristinaVersione(String idNota, int numeroVersione, String utente) {
        ConcurrentNavigableMap<String, Nota> repo = DatabaseNote.getNoteRepo();
        Nota nota = repo.get(idNota);

        if (nota == null || !nota.puoModificare(utente) || nota.getVersioni() == null) return false;

        VersioneNota versioneDaRipristinare = nota.getVersioni().stream()
                .filter(v -> v.getNumeroVersione() == numeroVersione)
                .findFirst()
                .orElse(null);

        if (versioneDaRipristinare == null) return false;

        // Salva il testo attuale nello storico PRIMA di sovrascriverlo
        // (Tranquillo, se è già presente nello storico ci penserà "aggiungiVersione" a bloccarlo!)
        aggiungiVersione(nota, nota.getContenuto());

        // Applica il contenuto della versione vecchia
        nota.setContenuto(versioneDaRipristinare.getContenuto());
        nota.setDataUltimaModifica(new Date());
        
        repo.put(idNota, nota);
        DatabaseCore.commit();
        return true;
    }

    public boolean aggiungiCollaboratore(String idNota, String proprietario, String collaboratore) {
        ConcurrentNavigableMap<String, Nota> repo = DatabaseNote.getNoteRepo();
        Nota nota = repo.get(idNota);
        if (nota == null || !nota.getProprietario().equals(proprietario)) return false;
        nota.aggiungiCollaboratore(collaboratore);
        repo.put(idNota, nota);
        DatabaseCore.commit();
        return true;
    }
    public boolean eliminaNota(String idNota, String proprietario) {
        java.util.concurrent.ConcurrentNavigableMap<String, Nota> repo = DatabaseNote.getNoteRepo();
        Nota nota = repo.get(idNota);

        // Solo il proprietario può eliminare la nota
        if (nota == null || !nota.getProprietario().equals(proprietario)) return false;

        repo.remove(idNota);
        DatabaseCore.commit();
        return true;
    }

    public boolean rimuoviCollaboratore(String idNota, String proprietario, String collaboratore) {
        ConcurrentNavigableMap<String, Nota> repo = DatabaseNote.getNoteRepo();
        Nota nota = repo.get(idNota);
        if (nota == null || !nota.getProprietario().equals(proprietario)) return false;
        nota.rimuoviCollaboratore(collaboratore);
        repo.put(idNota, nota);
        DatabaseCore.commit();
        return true;
    }
}