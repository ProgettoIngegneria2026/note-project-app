package it.unibo.progettonote.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;

public interface GestioneNoteServiceAsync {
    
    void salvaNota(String email, String titolo, String contenuto, String idCartella, AsyncCallback<Boolean> callback);
    void elencoNote(String email, AsyncCallback<ArrayList<String>> callback);
    void elencoNotePerCartella(String email, String idCartella, AsyncCallback<ArrayList<String>> callback);
    void leggiNota(String idNota, String email, AsyncCallback<Nota> callback);
    
    void modificaNota(String idNota, String nuovoTitolo, String nuovoContenuto, String email, String idCartella, AsyncCallback<Boolean> callback);
    void eliminaNota(String idNota, String email, AsyncCallback<Boolean> callback);
    void ricercaNote(String email, String parolaChiave, AsyncCallback<ArrayList<String>> callback);
    void duplicaNota(String idNota, String email, AsyncCallback<Boolean> callback);
    
    void getStoricoVersioni(String idNota, AsyncCallback<ArrayList<String>> callback);
    void ripristinaVersione(String idNota, int numeroVersione, String email, AsyncCallback<Boolean> callback);
    
    void creaCartella(String email, String nomeCartella, AsyncCallback<Boolean> callback);
    void elencoCartelle(String email, AsyncCallback<ArrayList<String>> callback);
    
    void condividiNota(String idNota, String emailProprietario, String emailDestinatario, String permesso, AsyncCallback<Boolean> callback);
}