package it.unibo.progettonote.server;

import java.util.*;
import it.unibo.progettonote.client.VersioneNota;
import it.unibo.progettonote.client.Nota;

public class NotaService {
    private static Map<String, Nota> RAM_REPO = new HashMap<>();

    public Map<String, Nota> getRepo() {
        Map<String, Nota> repo = DatabaseNote.getNoteRepo();
        return repo != null ? repo : RAM_REPO;
    }

    public void aggiungiVersione(Nota nota, String testoDaSalvare) {
        List<VersioneNota> storico = nota.getVersioni();
        if (storico == null) storico = new ArrayList<>();
        
        int num = storico.size() + 1;
        storico.add(new VersioneNota(num, testoDaSalvare, nota.getProprietario(), new Date()));
        nota.setVersioni(storico);
    }

    public boolean creaNuovaNota(String titolo, String contenuto, String proprietario, String idCartella) {
        if (contenuto != null && contenuto.length() > 280) return false;
        try {
            Nota nuovaNota = new Nota(titolo, contenuto, proprietario);
            nuovaNota.setId(titolo);
            nuovaNota.setIdCartella(idCartella);
            
            // Versione 1 generata alla creazione
            aggiungiVersione(nuovaNota, contenuto);

            getRepo().put(titolo, nuovaNota); 
            try { DatabaseCore.commit(); } catch(Exception e){}
            return true;
        } catch (Exception e) { return false; }
    }

    public String updateNota(String idNota, String nuovoTitolo, String nuovoContenuto, String utente, String idCartella, Date dataLettura) {
        Nota nota = getRepo().get(idNota);
        if (nota == null || (nuovoContenuto != null && nuovoContenuto.length() > 280)) return "ERRORE";

        boolean canEdit = utente.equals(nota.getProprietario()) || "WRITE".equals(nota.getPermessi().get(utente));
        if (!canEdit) return "ERRORE";

        if (nota.getDataModifica() != null && dataLettura != null) {
            if ((nota.getDataModifica().getTime() / 1000) > (dataLettura.getTime() / 1000)) {
                return "CONFLITTO";
            }
        }

        // Aggiunge una versione ad ogni modifica del testo
        if (!nota.getContenuto().equals(nuovoContenuto)) {
            aggiungiVersione(nota, nuovoContenuto);
        }

        nota.setTitolo(nuovoTitolo);
        nota.setContenuto(nuovoContenuto);
        nota.setId(nuovoTitolo);
        nota.setIdCartella(idCartella);
        nota.setDataModifica(new Date());

        if (!idNota.equals(nuovoTitolo)) {
            getRepo().remove(idNota);
        }
        getRepo().put(nuovoTitolo, nota);
        try { DatabaseCore.commit(); } catch(Exception e){}
        
        return "OK";
    }

    public boolean ripristinaVersione(String idNota, int num, String utente) {
        Nota nota = getRepo().get(idNota);
        if (nota == null || nota.getVersioni() == null) return false;
        boolean canEdit = utente.equals(nota.getProprietario()) || "WRITE".equals(nota.getPermessi().get(utente));
        if (!canEdit) return false;

        for (VersioneNota v : nota.getVersioni()) {
            if (v.getNumeroVersione() == num) {
                // SOVRASCRIVE SOLO IL TESTO, NIENTE GENERAZIONE DI NUOVE VERSIONI
                nota.setContenuto(v.getContenuto());
                nota.setDataModifica(new Date());
                try { DatabaseCore.commit(); } catch(Exception e){}
                return true;
            }
        }
        return false;
    }

    public boolean eliminaNota(String idNota, String proprietario) {
        Nota nota = getRepo().get(idNota);
        if (nota != null && nota.getProprietario().equals(proprietario)) {
            getRepo().remove(idNota);
            try { DatabaseCore.commit(); } catch(Exception e){}
            return true;
        }
        return false;
    }
    
    public boolean duplicaNota(String idNota, String utente) {
        Nota originale = getRepo().get(idNota);
        if (originale == null) return false;
        return creaNuovaNota(originale.getTitolo() + " (Copia)", originale.getContenuto(), utente, originale.getIdCartella());
    }

    public boolean condividiNota(String idNota, String proprietario, String emailDestinatario, String permesso) {
        Nota nota = getRepo().get(idNota);
        if (nota != null && nota.getProprietario().equals(proprietario)) {
            nota.getPermessi().put(emailDestinatario, permesso);
            try { DatabaseCore.commit(); } catch(Exception e){}
            return true;
        }
        return false;
    }
}