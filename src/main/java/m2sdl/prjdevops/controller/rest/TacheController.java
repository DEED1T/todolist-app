package m2sdl.prjdevops.controller.rest;

import lombok.Setter;
import m2sdl.prjdevops.domain.Tache;
import m2sdl.prjdevops.service.TacheService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Setter
@RestController("apiTodos")
public class TacheController {

    private final TacheService tacheService;

    private static final Logger LOGGER = LogManager.getLogger();

    public TacheController(TacheService tacheService) {
        this.tacheService = tacheService;
    }

    @GetMapping(path = "api/todos", produces = {"application/json; charset=UTF-8"})
    public List<Tache> findAllTaches() {
        return tacheService.findAllTaches().stream().toList();
    }

    @GetMapping(path = "api/todo", produces = {"application/json; charset=UTF-8"})
    public Tache findTache(@RequestParam(name = "id") long id) {
        if (id < 0) throw new IllegalArgumentException("idTache is negative.");

        return tacheService.findTacheById(id);
    }

    @PostMapping(path = "api/addTodo", produces = {"application/json; charset=UTF-8"})
    public ResponseEntity<Tache> addTache(@RequestParam(name = "titre") String titre,
                                          @RequestParam(name = "texte") String texte,
                                          @RequestParam(name = "utilisateur") String utilisateur) {

        return new ResponseEntity<>(tacheService.saveTache(new Tache(titre, texte, utilisateur)), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/api/deleteTodo")
    public void deleteTache(@RequestParam(name = "id") long idTache) {
        tacheService.deleteTache(idTache);
    }

    @PatchMapping(path = "/api/updateTodo", produces = {"application/json; charset=UTF-8"})
    public ResponseEntity<Tache> updateTache(@RequestParam(name = "id") long idTache,
                                             @RequestParam(name = "titre", required = false) String updatedTitre,
                                             @RequestParam(name = "texte", required = false) String updatedTexte,
                                             @RequestParam(name = "utilisateur", required = false) String updatedUtilisateur,
                                             @RequestParam(name = "done", required = false) boolean isDone)
    {
        if (updatedTitre == null && updatedTexte == null && updatedUtilisateur == null) {
            LOGGER.warn("New todo title, text and user are null, exiting");
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "New todo title, text and user are null");
        }

        Tache tacheToUpdate = tacheService.findTacheById(idTache);
        if (tacheToUpdate != null) {
            tacheToUpdate.setTitre(updatedTitre == null ? tacheToUpdate.getTitre() : updatedTitre);
            tacheToUpdate.setTexte(updatedTexte == null ? tacheToUpdate.getTexte() : updatedTexte);
            tacheToUpdate.setUtilisateur(updatedUtilisateur == null ? tacheToUpdate.getUtilisateur() : updatedUtilisateur);
            tacheToUpdate.setIsDone(isDone);

            return new ResponseEntity<>(tacheService.saveTache(tacheToUpdate), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}
