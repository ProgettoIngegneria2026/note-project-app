package it.unibo.progettonote.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import it.unibo.progettonote.client.GestioneNoteService;
import it.unibo.progettonote.client.Nota;
import java.util.*;

public class GestioneNoteServiceImpl extends RemoteServiceServlet implements GestioneNoteService {
    
    private final NotaService mioNotaService = new NotaService();
    private static Map<String, it.unibo.progettonote.client.Cartella> CARTELLE_RAM = new HashMap<>();

    private Map<String, it.unibo.progettonote.client.Cartella> getCartelleRepo() {
        Map<String, it.unibo.progettonote.client.Cartella> repo = DatabaseCartelle.getCartelleRepo();
        return repo != null ? repo : CARTELLE_RAM;
    }

    @Override
    public boolean salvaNota(String email, String titolo, String contenuto, String idCartella) {
        return mioNotaService.creaNuovaNota(titolo, contenuto, email, idCartella);
    }

    @Override
    public ArrayList<String> elencoNote(String email) {
        ArrayList<String> titoli = new ArrayList<>();
        for (Nota n : mioNotaService.getRepo().values()) {
            if (email.equals(n.getProprietario()) || n.getPermessi().containsKey(email)) {
                titoli.add(n.getTitolo());
            }
        }
        return titoli;
    }

    @Override
    public ArrayList<String> elencoNotePerCartella(String email, String idCartella) {
        ArrayList<String> titoli = new ArrayList<>();
        for (Nota n : mioNotaService.getRepo().values()) {
            if ((email.equals(n.getProprietario()) || n.getPermessi().containsKey(email)) && idCartella.equals(n.getIdCartella())) {
                titoli.add(n.getTitolo());
            }
        }
        return titoli;
    }

    @Override
    public Nota leggiNota(String idNota, String email) {
        return mioNotaService.getRepo().get(idNota);
    }

    @Override
    public boolean modificaNota(String idNota, String nuovoTitolo, String nuovoContenuto, String email, String idCartella) {
        return mioNotaService.updateNota(idNota, nuovoTitolo, nuovoContenuto, email, idCartella, null).equals("OK");
    }

    @Override
    public boolean eliminaNota(String idNota, String email) {
        return mioNotaService.eliminaNota(idNota, email);
    }

    @Override
    public ArrayList<String> ricercaNote(String email, String parolaChiave) {
        ArrayList<String> risultati = new ArrayList<>();
        String target = parolaChiave.toLowerCase();
        for (Nota n : mioNotaService.getRepo().values()) {
            if ((email.equals(n.getProprietario()) || n.getPermessi().containsKey(email)) && 
               (n.getTitolo().toLowerCase().contains(target) || n.getContenuto().toLowerCase().contains(target))) {
                risultati.add(n.getTitolo());
            }
        }
        return risultati;
    }

    @Override
    public boolean duplicaNota(String idNota, String email) {
        return mioNotaService.duplicaNota(idNota, email);
    }

    @Override
    public ArrayList<String> getStoricoVersioni(String idNota) {
        ArrayList<String> versioni = new ArrayList<>();
        Nota nota = mioNotaService.getRepo().get(idNota);
        if (nota != null && nota.getVersioni() != null) {
            for (it.unibo.progettonote.client.VersioneNota v : nota.getVersioni()) {
                versioni.add("V" + v.getNumeroVersione() + ": " + v.getContenuto());
            }
        }
        return versioni;
    }

    @Override
    public boolean ripristinaVersione(String idNota, int numeroVersione, String email) {
        return mioNotaService.ripristinaVersione(idNota, numeroVersione, email);
    }

    @Override
    public boolean creaCartella(String email, String nomeCartella) {
        it.unibo.progettonote.client.Cartella c = new it.unibo.progettonote.client.Cartella(nomeCartella, email);
        String id = nomeCartella + "_" + email;
        getCartelleRepo().put(id, c);
        try { DatabaseCore.commit(); } catch(Exception e){}
        return true;
    }

    @Override
    public ArrayList<String> elencoCartelle(String email) {
        ArrayList<String> cartelle = new ArrayList<>();
        for (it.unibo.progettonote.client.Cartella c : getCartelleRepo().values()) {
            if (email.equals(c.getProprietario())) cartelle.add(c.getNome());
        }
        return cartelle;
    }

    @Override
    public boolean condividiNota(String idNota, String emailProprietario, String emailDestinatario, String permesso) {
        return mioNotaService.condividiNota(idNota, emailProprietario, emailDestinatario, permesso);
    }
}