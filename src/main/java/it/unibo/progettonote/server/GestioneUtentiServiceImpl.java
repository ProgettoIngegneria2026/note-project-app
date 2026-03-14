package it.unibo.progettonote.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import it.unibo.progettonote.client.GestioneUtentiService;

public class GestioneUtentiServiceImpl extends RemoteServiceServlet implements GestioneUtentiService {
    
    // Inizializza la tua classe originale
    private GestioneUtenti logicaOriginale = new GestioneUtenti();

    @Override
    public boolean loginServer(String email, String password) {
        // Chiama il tuo metodo originale senza cambiare una riga
        return logicaOriginale.login(email, password);
    }

    @Override
    public boolean registraServer(String username, String email, String password) {
        return logicaOriginale.registraNuovoUtente(username, email, password);
    }
}