package it.unibo.progettonote.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;

public class DashboardNoteWeb {
    private String emailUtente;
    private String notaCorrente = null; 
    private String idNotaCorrente = null; 
    private String cartellaCorrenteDellaNota = null;
    private ArrayList<String> mieCartelle = new ArrayList<>(); // Salva le cartelle per i menu a tendina
    
    private final VerticalPanel listaNote = new VerticalPanel();
    private final VerticalPanel listaCartelle = new VerticalPanel(); 
    private final TextArea areaLettura = new TextArea();
    private final Label infoNota = new Label();
    private final Label titoloListaNote = new Label("Le tue note:");
    
    private final GestioneNoteServiceAsync noteService = GWT.create(GestioneNoteService.class);
    private final HorizontalPanel pannelloAzioni = new HorizontalPanel();
    
    private Button btnModifica, btnElimina, btnVersioni, btnDuplica, btnCondividi;

    public DashboardNoteWeb(String emailUtente) {
        this.emailUtente = emailUtente;
    }

    public void mostra() {
        StyleInjector.inject(
            "body { font-family: 'Segoe UI', Tahoma, sans-serif; background: #f4f7f6; margin:0; padding:20px; color:#333; } " +
            ".gwt-Button { background: #007bff; color: white; border: none; padding: 8px 12px; border-radius: 4px; cursor: pointer; margin: 2px; font-weight:bold; } " +
            ".gwt-Button:hover { background: #0056b3; } " +
            ".menu-btn { width: 100%; margin-bottom: 8px; } " +
            ".gwt-TextBox, .gwt-TextArea, .gwt-ListBox { width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; margin-bottom: 10px; box-sizing: border-box; } " +
            ".gwt-DialogBox { background: white; padding: 20px; border-radius: 8px; border: 1px solid #ccc; box-shadow: 0 4px 15px rgba(0,0,0,0.2); } " +
            ".folder-link { color: #d97706; text-decoration: none; display: block; padding: 6px; cursor: pointer; border-radius: 4px; font-weight: bold; } " +
            ".folder-link:hover { background: #fef3c7; } " +
            ".note-link { color: #007bff; text-decoration: none; display: block; padding: 6px; cursor: pointer; border-bottom: 1px solid #eee; } " +
            ".note-link:hover { background: #e9ecef; border-radius: 4px; }"
        );

        RootPanel.get().clear();
        HorizontalPanel mainLayout = new HorizontalPanel();
        mainLayout.setWidth("100%");
        mainLayout.setSpacing(15);

        // --- MENU SINISTRO ---
        VerticalPanel menuPanel = new VerticalPanel();
        menuPanel.setWidth("280px");
        
        Button btnNuova = new Button("+ Nuova Nota"); btnNuova.addStyleName("menu-btn");
        btnNuova.addClickHandler(e -> mostraDialogoCreazione());
        
        Button btnNuovaCartella = new Button("+ Nuova Cartella"); btnNuovaCartella.addStyleName("menu-btn");
        btnNuovaCartella.addClickHandler(e -> mostraDialogoCartella());
        
        Button btnEsci = new Button("Esci"); btnEsci.addStyleName("menu-btn");
        btnEsci.getElement().getStyle().setProperty("background", "#dc3545");
        btnEsci.addClickHandler(e -> InterfacciaRegistrazioneWeb.mostra());
        
        HorizontalPanel barraRicerca = new HorizontalPanel();
        barraRicerca.setWidth("100%");
        TextBox boxRicerca = new TextBox(); boxRicerca.getElement().setPropertyString("placeholder", "Cerca parola...");
        Button btnCerca = new Button("🔍");
        btnCerca.addClickHandler(e -> caricaNote(boxRicerca.getText()));
        barraRicerca.add(boxRicerca); barraRicerca.add(btnCerca);

        menuPanel.add(btnNuova);
        menuPanel.add(btnNuovaCartella);
        menuPanel.add(barraRicerca);
        menuPanel.add(new HTML("<hr>"));
        
        // Sezione Cartelle
        Label lblCartelle = new Label("CARTELLE");
        lblCartelle.getElement().getStyle().setProperty("fontWeight", "bold");
        menuPanel.add(lblCartelle);
        
        Anchor linkTutte = new Anchor("📁 Tutte le note");
        linkTutte.setStyleName("folder-link");
        linkTutte.addClickHandler(e -> caricaNote(null));
        menuPanel.add(linkTutte);
        
        menuPanel.add(listaCartelle);
        menuPanel.add(new HTML("<hr>"));
        
        // Sezione Note
        titoloListaNote.getElement().getStyle().setProperty("fontWeight", "bold");
        menuPanel.add(titoloListaNote);
        
        ScrollPanel scrollNote = new ScrollPanel(listaNote);
        scrollNote.setHeight("300px");
        menuPanel.add(scrollNote);
        menuPanel.add(new HTML("<hr>"));
        menuPanel.add(btnEsci);

        // --- AREA LETTURA DESTRA ---
        VerticalPanel contentArea = new VerticalPanel();
        contentArea.setWidth("100%");
        
        infoNota.getElement().getStyle().setProperty("color", "#666");
        infoNota.getElement().getStyle().setProperty("fontSize", "13px");
        infoNota.getElement().getStyle().setProperty("marginBottom", "10px");
        
        areaLettura.setSize("100%", "400px");
        areaLettura.setReadOnly(true);
        areaLettura.getElement().getStyle().setProperty("fontSize", "16px");
        
        btnModifica = new Button("✏️ Modifica"); btnModifica.addClickHandler(e -> mostraDialogoModifica());
        btnElimina = new Button("🗑️ Elimina"); btnElimina.getElement().getStyle().setProperty("background", "#dc3545");
        btnElimina.addClickHandler(e -> eliminaNotaCorrente());
        btnVersioni = new Button("🕒 Versioni"); btnVersioni.addClickHandler(e -> mostraStorico());
        btnDuplica = new Button("📄 Duplica"); btnDuplica.addClickHandler(e -> duplicaNotaCorrente());
        btnCondividi = new Button("🤝 Condividi"); btnCondividi.addClickHandler(e -> mostraDialogoCondivisione());

        pannelloAzioni.add(btnModifica);
        pannelloAzioni.add(btnCondividi);
        pannelloAzioni.add(btnDuplica);
        pannelloAzioni.add(btnVersioni);
        pannelloAzioni.add(btnElimina);
        pannelloAzioni.setVisible(false);

        contentArea.add(new HTML("<h2 style='margin-top:0;'>Lettura Nota</h2>"));
        contentArea.add(pannelloAzioni);
        contentArea.add(infoNota);
        contentArea.add(areaLettura);

        mainLayout.add(menuPanel);
        mainLayout.add(contentArea);
        RootPanel.get().add(mainLayout);

        caricaCartelle();
        caricaNote(null);
    }

    private void caricaCartelle() {
        noteService.elencoCartelle(emailUtente, new AsyncCallback<ArrayList<String>>() {
            public void onFailure(Throwable c) {}
            public void onSuccess(ArrayList<String> cartelle) {
                mieCartelle = cartelle; // Salva per i menu a tendina
                listaCartelle.clear();
                for (String c : cartelle) {
                    Anchor link = new Anchor("📂 " + c);
                    link.setStyleName("folder-link");
                    link.addClickHandler(e -> caricaNotePerCartella(c));
                    listaCartelle.add(link);
                }
            }
        });
    }

    private void caricaNote(String query) {
        titoloListaNote.setText(query != null && !query.isEmpty() ? "Risultati ricerca:" : "Tutte le note:");
        AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>() {
            public void onFailure(Throwable c) { Window.alert("Errore caricamento note"); }
            public void onSuccess(ArrayList<String> titoli) { renderizzaListaNote(titoli); }
        };

        if (query == null || query.trim().isEmpty()) { noteService.elencoNote(emailUtente, callback); } 
        else { noteService.ricercaNote(emailUtente, query, callback); }
    }

    private void caricaNotePerCartella(String nomeCartella) {
        titoloListaNote.setText("Note in: " + nomeCartella);
        noteService.elencoNotePerCartella(emailUtente, nomeCartella, new AsyncCallback<ArrayList<String>>() {
            public void onFailure(Throwable c) {}
            public void onSuccess(ArrayList<String> titoli) { renderizzaListaNote(titoli); }
        });
    }

    private void renderizzaListaNote(ArrayList<String> titoli) {
        listaNote.clear();
        if(titoli.isEmpty()) { listaNote.add(new HTML("<i>Nessuna nota trovata.</i>")); return; }
        for (String t : titoli) {
            Anchor link = new Anchor("📄 " + t);
            link.setStyleName("note-link");
            link.addClickHandler(e -> apriNota(t));
            listaNote.add(link);
        }
    }

    private void apriNota(String titolo) {
        notaCorrente = titolo; idNotaCorrente = titolo; 
        noteService.leggiNota(titolo, emailUtente, new AsyncCallback<Nota>() {
            public void onFailure(Throwable c) { Window.alert("Errore"); }
            public void onSuccess(Nota n) { 
                if (n == null) return;
                cartellaCorrenteDellaNota = n.getIdCartella();
                areaLettura.setText(n.getContenuto()); 
                String cart = (n.getIdCartella() != null) ? n.getIdCartella() : "Nessuna";
                infoNota.setText("Cartella: " + cart + " | Creata: " + n.getDataCreazione() + " | Autore: " + n.getProprietario());
                
                boolean isOwner = emailUtente.equals(n.getProprietario());
                boolean canWrite = isOwner || "WRITE".equals(n.getPermessi().get(emailUtente));

                btnModifica.setVisible(canWrite); btnVersioni.setVisible(canWrite);
                btnElimina.setVisible(isOwner); btnCondividi.setVisible(isOwner);
                pannelloAzioni.setVisible(true); 
            }
        });
    }

    private void eliminaNotaCorrente() {
        if (Window.confirm("Eliminare definitivamente: " + notaCorrente + "?")) {
            noteService.eliminaNota(idNotaCorrente, emailUtente, new AsyncCallback<Boolean>() {
                public void onFailure(Throwable c) {}
                public void onSuccess(Boolean r) {
                    if (r) { areaLettura.setText(""); infoNota.setText(""); pannelloAzioni.setVisible(false); caricaNote(null); }
                }
            });
        }
    }

    private void duplicaNotaCorrente() {
        noteService.duplicaNota(idNotaCorrente, emailUtente, new AsyncCallback<Boolean>() {
            public void onFailure(Throwable c) { Window.alert("Errore duplicazione"); }
            public void onSuccess(Boolean r) { caricaNote(null); }
        });
    }

    private void limitTextArea(TextArea ta) {
        ta.addKeyUpHandler(e -> { if (ta.getText().length() > 280) ta.setText(ta.getText().substring(0, 280)); });
    }

    private ListBox creaMenuCartelle(String seleziona) {
        ListBox box = new ListBox();
        box.addItem("-- Nessuna Cartella --", "");
        for (int i = 0; i < mieCartelle.size(); i++) {
            box.addItem(mieCartelle.get(i), mieCartelle.get(i));
            if (mieCartelle.get(i).equals(seleziona)) box.setSelectedIndex(i + 1);
        }
        return box;
    }

    private void mostraDialogoCreazione() {
        DialogBox db = new DialogBox(true); db.setText("Nuova Nota (Max 280 car.)");
        VerticalPanel vp = new VerticalPanel(); vp.setWidth("350px");
        
        TextBox titolo = new TextBox();
        TextArea contenuto = new TextArea(); contenuto.setSize("100%", "150px"); limitTextArea(contenuto);
        ListBox boxCartelle = creaMenuCartelle("");
        
        Button salva = new Button("Salva");
        salva.addClickHandler(e -> {
            String idCart = boxCartelle.getSelectedValue().isEmpty() ? null : boxCartelle.getSelectedValue();
            noteService.salvaNota(emailUtente, titolo.getText(), contenuto.getText(), idCart, new AsyncCallback<Boolean>() {
                public void onFailure(Throwable c) { Window.alert("Errore Server."); }
                public void onSuccess(Boolean r) { if(r) { db.hide(); caricaNote(null); } else Window.alert("Titolo o Testo vuoti/troppo lunghi."); }
            });
        });
        
        vp.add(new HTML("<b>Titolo:</b>")); vp.add(titolo);
        vp.add(new HTML("<b>Cartella:</b>")); vp.add(boxCartelle);
        vp.add(new HTML("<b>Contenuto:</b>")); vp.add(contenuto);
        vp.add(salva); db.setWidget(vp); db.center();
    }

    private void mostraDialogoModifica() {
        DialogBox db = new DialogBox(true); db.setText("Modifica Nota");
        VerticalPanel vp = new VerticalPanel(); vp.setWidth("350px");
        
        TextBox titolo = new TextBox(); titolo.setText(notaCorrente);
        TextArea contenuto = new TextArea(); contenuto.setText(areaLettura.getText()); contenuto.setSize("100%", "150px"); limitTextArea(contenuto);
        ListBox boxCartelle = creaMenuCartelle(cartellaCorrenteDellaNota);
        
        Button salva = new Button("Aggiorna");
        salva.addClickHandler(e -> {
            String idCart = boxCartelle.getSelectedValue().isEmpty() ? null : boxCartelle.getSelectedValue();
            noteService.modificaNota(idNotaCorrente, titolo.getText(), contenuto.getText(), emailUtente, idCart, new AsyncCallback<Boolean>() {
                public void onFailure(Throwable c) { Window.alert("Errore Server."); }
                public void onSuccess(Boolean r) { if(r) { db.hide(); apriNota(titolo.getText()); caricaNote(null); } }
            });
        });
        
        vp.add(new HTML("<b>Nuovo Titolo:</b>")); vp.add(titolo);
        vp.add(new HTML("<b>Cartella:</b>")); vp.add(boxCartelle);
        vp.add(new HTML("<b>Contenuto:</b>")); vp.add(contenuto);
        vp.add(salva); db.setWidget(vp); db.center();
    }

    private void mostraDialogoCondivisione() {
        DialogBox db = new DialogBox(true); db.setText("Condividi: " + notaCorrente);
        VerticalPanel vp = new VerticalPanel();
        TextBox emailDest = new TextBox();
        ListBox livello = new ListBox(); livello.addItem("Sola Lettura", "READ"); livello.addItem("Scrittura", "WRITE");
        Button salva = new Button("Conferma");
        salva.addClickHandler(e -> {
            noteService.condividiNota(idNotaCorrente, emailUtente, emailDest.getText(), livello.getSelectedValue(), new AsyncCallback<Boolean>() {
                public void onFailure(Throwable c) {}
                public void onSuccess(Boolean r) { if(r) { db.hide(); Window.alert("Condivisa!"); } else Window.alert("Errore."); }
            });
        });
        vp.add(new HTML("<b>Email Utente:</b>")); vp.add(emailDest);
        vp.add(new HTML("<b>Permesso:</b>")); vp.add(livello);
        vp.add(salva); db.setWidget(vp); db.center();
    }

    private void mostraDialogoCartella() {
        DialogBox db = new DialogBox(true); db.setText("Nuova Cartella");
        VerticalPanel vp = new VerticalPanel();
        TextBox nomeCartella = new TextBox();
        Button salva = new Button("Crea");
        salva.addClickHandler(e -> {
            noteService.creaCartella(emailUtente, nomeCartella.getText(), new AsyncCallback<Boolean>() {
                public void onFailure(Throwable c) {}
                public void onSuccess(Boolean r) { if(r) { db.hide(); caricaCartelle(); } }
            });
        });
        vp.add(new HTML("<b>Nome Cartella:</b>")); vp.add(nomeCartella);
        vp.add(salva); db.setWidget(vp); db.center();
    }

    private void mostraStorico() {
        noteService.getStoricoVersioni(idNotaCorrente, new AsyncCallback<ArrayList<String>>() {
            public void onFailure(Throwable c) { }
            public void onSuccess(ArrayList<String> versioni) {
                DialogBox db = new DialogBox(true); db.setText("Versioni");
                VerticalPanel vp = new VerticalPanel();
                if(versioni.isEmpty()) { vp.add(new HTML("Nessuna versione precedente.")); } 
                else {
                    for(int i = 0; i < versioni.size(); i++) {
                        String v = versioni.get(i); int numVer = i + 1;
                        HorizontalPanel riga = new HorizontalPanel(); riga.add(new Label(v));
                        Button btnRipristina = new Button("Ripristina");
                        btnRipristina.addClickHandler(e -> {
                            noteService.ripristinaVersione(idNotaCorrente, numVer, emailUtente, new AsyncCallback<Boolean>() {
                                public void onFailure(Throwable ex) {}
                                public void onSuccess(Boolean res) { db.hide(); apriNota(notaCorrente); }
                            });
                        });
                        riga.add(btnRipristina); vp.add(riga);
                    }
                }
                Button btnChiudi = new Button("Chiudi"); btnChiudi.addClickHandler(e -> db.hide());
                vp.add(btnChiudi); db.setWidget(vp); db.center();
            }
        });
    }
}