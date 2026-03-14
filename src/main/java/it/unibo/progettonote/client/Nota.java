package it.unibo.progettonote.client; // <-- CAMBIA QUESTO IN BASE AL TUO PACKAGE ORIGINALE

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Nota implements Serializable {
    private String id;
    private String titolo;
    private String contenuto;
    private String proprietario;
    
    // Nuovi campi per Requisiti
    private Date dataCreazione;
    private Date dataModifica;
    private List<VersioneNota> versioni;
    private Map<String, String> permessi; // email -> "READ" o "WRITE"

    // Campi ripristinati per compatibilità con i tuoi test
    private String idCartella;
    private List<String> collaboratori;

    public Nota() { 
        // RISOLVE IL PROBLEMA "ID nota non valido"
        this.id = UUID.randomUUID().toString(); 
        
        this.permessi = new HashMap<>(); 
        this.collaboratori = new ArrayList<>();
        this.versioni = new ArrayList<>(); // Evita NullPointerException
        this.dataCreazione = new Date();
        this.dataModifica = new Date();
    }

    public Nota(String titolo, String contenuto, String proprietario) {
        this();
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.proprietario = proprietario;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }
    public String getContenuto() { return contenuto; }
    public void setContenuto(String contenuto) { this.contenuto = contenuto; }
    public String getProprietario() { return proprietario; }
    public void setProprietario(String proprietario) { this.proprietario = proprietario; }
    
    public Date getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(Date dataCreazione) { this.dataCreazione = dataCreazione; }
    public Date getDataModifica() { return dataModifica; }
    public void setDataModifica(Date dataModifica) { this.dataModifica = dataModifica; }
    
    public List<VersioneNota> getVersioni() { return versioni; }
    public void setVersioni(List<VersioneNota> versioni) { this.versioni = versioni; }
    public Map<String, String> getPermessi() { return permessi; }
    public void setPermessi(Map<String, String> permessi) { this.permessi = permessi; }

    // --- METODI DI COMPATIBILITÀ ---
    public String getIdCartella() { return idCartella; }
    public void setIdCartella(String idCartella) { this.idCartella = idCartella; }
    
    public List<String> getCollaboratori() { return collaboratori; }
    public void setCollaboratori(List<String> collaboratori) { this.collaboratori = collaboratori; }

    public Date getDataUltimaModifica() { return dataModifica; }
    public void setDataUltimaModifica(Date dataModifica) { this.dataModifica = dataModifica; }

    public boolean puoAccedere(String emailUtente) {
        if (emailUtente == null) return false;
        if (emailUtente.equals(proprietario)) return true;
        if (permessi != null && permessi.containsKey(emailUtente)) return true;
        if (collaboratori != null && collaboratori.contains(emailUtente)) return true;
        return false;
    }
}