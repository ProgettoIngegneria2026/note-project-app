package it.unibo.progettonote.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("utenti")
public interface GestioneUtentiService extends RemoteService {
    boolean loginServer(String email, String password);
    boolean registraServer(String username, String email, String password);
}