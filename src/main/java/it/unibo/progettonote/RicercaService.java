package it.unibo.progettonote;

public class RicercaService {

    private NotaService notaService;

    public RicercaService(NotaService notaService) {
        this.notaService = notaService;
    }

    public void cerca(String id) {
        Nota n = notaService.getNoteRepo().get(id);
        if(n != null)
            System.out.println("Trovata nota: " + n.getTitolo());
        else
            System.out.println("Nota non trovata");
    }
}
