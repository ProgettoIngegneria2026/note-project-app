package it.unibo.progettonote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;


public class Nota implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String titolo;
    private String contenuto;
    private Date dataCreazione;
    private Date dataUltimaModifica;
    private String proprietario;
    private String idCartella; // può essere null
    private List<VersioneNota> versioni = new ArrayList<>();
    private Map<String, String> permessi = new HashMap<>();
    private List<String> collaboratori = new ArrayList<>();



    public Nota() {}

    public Nota(String titolo, String contenuto, String proprietario) {
        this.id = UUID.randomUUID().toString();
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.proprietario = proprietario;
        this.dataCreazione = new Date();
        this.dataUltimaModifica = new Date();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getContenuto() { return contenuto; }
    public void setContenuto(String contenuto) { this.contenuto = contenuto; }

    public Date getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(Date date) { this.dataCreazione = date; }

    public Date getDataUltimaModifica() { return dataUltimaModifica; }
    public void setDataUltimaModifica(Date date) { this.dataUltimaModifica = date; }

    public String getProprietario() { return proprietario; }
    public void setProprietario(String proprietario) { this.proprietario = proprietario; }

    public String getIdCartella() { return idCartella; }
    public void setIdCartella(String idCartella) { this.idCartella = idCartella; }

    public List<VersioneNota> getVersioni() { return versioni; }
    public void setVersioni(List<VersioneNota> versioni) { this.versioni = versioni; }
    public void assegnaPermesso(String utente, String tipoPermesso) {
    permessi.put(utente, tipoPermesso);
}

public String getPermesso(String utente) {
    return permessi.get(utente);
}

public boolean puoLeggere(String utente) {
    return utente.equals(proprietario) || permessi.containsKey(utente);
}

public boolean puoScrivere(String utente) {
    return utente.equals(proprietario) || "scrittura".equals(permessi.get(utente));
}

public Map<String, String> getPermessi() {
    return permessi;
}

public void setPermessi(Map<String, String> permessi) {
    this.permessi = permessi;
}
public void aggiungiCollaboratore(String email) {
    if (!collaboratori.contains(email)) {
        collaboratori.add(email);
    }
}

public void rimuoviCollaboratore(String email) {
    collaboratori.remove(email);
}

public List<String> getCollaboratori() {
    return collaboratori;
}


}