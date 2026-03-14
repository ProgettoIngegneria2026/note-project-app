package it.unibo.progettonote.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GestioneUtentiServiceAsync {
    void loginServer(String email, String password, AsyncCallback<Boolean> callback);
    void registraServer(String username, String email, String password, AsyncCallback<Boolean> callback);
}