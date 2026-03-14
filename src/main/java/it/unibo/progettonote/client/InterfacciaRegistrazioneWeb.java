package it.unibo.progettonote.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class InterfacciaRegistrazioneWeb {

    // Crea il collegamento col server
    private static final GestioneUtentiServiceAsync utentiService = GWT.create(GestioneUtentiService.class);

    public static void mostra() {
        RootPanel loadingPanel = RootPanel.get("loading");
        if (loadingPanel != null) loadingPanel.getElement().removeFromParent();
        RootPanel.get().clear();

        TabPanel tabPanel = new TabPanel();
        tabPanel.setWidth("350px");

        // --- PANNELLO LOGIN ---
        VerticalPanel loginPanel = new VerticalPanel();
        loginPanel.setSpacing(15);
        TextBox logEmail = new TextBox();
        PasswordTextBox logPass = new PasswordTextBox();
        Button btnLogin = new Button("Accedi");
        logEmail.setWidth("200px"); logPass.setWidth("200px");

        loginPanel.add(new HTML("<b>Email:</b>")); loginPanel.add(logEmail);
        loginPanel.add(new HTML("<b>Password:</b>")); loginPanel.add(logPass);
        loginPanel.add(btnLogin);

        // --- PANNELLO REGISTRAZIONE ---
        VerticalPanel regPanel = new VerticalPanel();
        regPanel.setSpacing(15);
        TextBox regUser = new TextBox();
        TextBox regEmail = new TextBox();
        PasswordTextBox regPass = new PasswordTextBox();
        Button btnReg = new Button("Registrati");
        regUser.setWidth("200px"); regEmail.setWidth("200px"); regPass.setWidth("200px");

        regPanel.add(new HTML("<b>Username:</b>")); regPanel.add(regUser);
        regPanel.add(new HTML("<b>Email:</b>")); regPanel.add(regEmail);
        regPanel.add(new HTML("<b>Password:</b>")); regPanel.add(regPass);
        regPanel.add(btnReg);

        tabPanel.add(loginPanel, "Login");
        tabPanel.add(regPanel, "Registrazione");
        tabPanel.selectTab(0);

        VerticalPanel container = new VerticalPanel();
        container.setWidth("100%"); container.setHeight("100vh");
        container.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        container.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        
        DecoratorPanel formBox = new DecoratorPanel(); formBox.setWidget(tabPanel);
        container.add(new HTML("<h1 style='color: #333;'>Progetto Note</h1>"));
        container.add(formBox);

        // --- EVENTO LOGIN REALE ---
        btnLogin.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                utentiService.loginServer(logEmail.getText().trim(), logPass.getText(), new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Errore di connessione al server: " + caught.getMessage());
                    }
                    @Override
                    public void onSuccess(Boolean successo) {
                        if (successo) {
                            Window.alert("Login effettuato! Benvenuto.");
                            new DashboardNoteWeb(logEmail.getText().trim()).mostra();
                        } else {
                            Window.alert("Credenziali errate o utente inesistente.");
                        }
                    }
                });
            }
        });

        // --- EVENTO REGISTRAZIONE REALE ---
        btnReg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                utentiService.registraServer(regUser.getText().trim(), regEmail.getText().trim(), regPass.getText(), new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Errore di connessione al server.");
                    }
                    @Override
                    public void onSuccess(Boolean successo) {
                        if (successo) {
                            Window.alert("Registrazione completata! Ora puoi fare il login.");
                            tabPanel.selectTab(0); // Torna alla scheda login
                        } else {
                            Window.alert("Errore: Email già usata o dati non validi.");
                        }
                    }
                });
            }
        });

        RootPanel.get().add(container);
    }
}