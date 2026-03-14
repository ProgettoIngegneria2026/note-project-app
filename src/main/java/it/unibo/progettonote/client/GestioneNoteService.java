package it.unibo.progettonote.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;

@RemoteServiceRelativePath("gestioneNote")
public interface GestioneNoteService extends RemoteService {
    
    boolean salvaNota(String email, String titolo, String contenuto, String idCartella);
    ArrayList<String> elencoNote(String email);
    ArrayList<String> elencoNotePerCartella(String email, String idCartella);
    Nota leggiNota(String idNota, String email); 
    
    boolean modificaNota(String idNota, String nuovoTitolo, String nuovoContenuto, String email, String idCartella);
    boolean eliminaNota(String idNota, String email);
    ArrayList<String> ricercaNote(String email, String parolaChiave);
    boolean duplicaNota(String idNota, String email);
    
    ArrayList<String> getStoricoVersioni(String idNota);
    boolean ripristinaVersione(String idNota, int numeroVersione, String email);
    
    boolean creaCartella(String email, String nomeCartella);
    ArrayList<String> elencoCartelle(String email);
    
    boolean condividiNota(String idNota, String emailProprietario, String emailDestinatario, String permesso);
}