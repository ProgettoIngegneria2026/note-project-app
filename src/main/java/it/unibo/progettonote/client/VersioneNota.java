package it.unibo.progettonote.client;

import java.io.Serializable;
import java.util.Date;

public class VersioneNota implements Serializable {
    public VersioneNota() {} 
    private static final long serialVersionUID = 1L;
    
    private int numeroVersione;
    private String contenuto;
    private String autore;
    private Date data;

    // Costruttore richiesto da NotaService (int, String, String, Date)
    public VersioneNota(int numeroVersione, String contenuto, String autore, Date data) {
        this.numeroVersione = numeroVersione;
        this.contenuto = contenuto;
        this.autore = autore;
        this.data = data;
    }

    // Getter
    public int getNumeroVersione() { return numeroVersione; }
    public String getContenuto() { return contenuto; }
    public String getAutore() { return autore; }
    public Date getData() { return data; }
}