package m2sdl.prjdevops.controller;

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
import java.util.Objects;

@Setter
@RestController
public class TacheController {

    private final TacheService tacheService;

    private static final Logger LOGGER = LogManager.getLogger();

    public TacheController(TacheService tacheService) {
        this.tacheService = tacheService;
    }

    @GetMapping(path = "api/todos", produces = {"application/json; charset=UTF-8"})
    public List<Tache> findAllTaches() {
        return tacheService.findAllTaches().stream().filter(Objects::nonNull).toList();
    }

    @GetMapping(path = "api/todo", produces = {"application/json; charset=UTF-8"})
    public Tache findTache(@RequestParam(name = "id") long id) {
        if (id < 0) throw new IllegalArgumentException("idTache is negative.");

        return tacheService.findTacheById(id);
    }

    @PostMapping(path = "api/addTodo", produces = {"application/json; charset=UTF-8"})
    public ResponseEntity<Tache> addTache(@RequestParam(name = "titre") String titre, @RequestParam(name = "texte") String texte) {
        return new ResponseEntity<>(tacheService.saveTache(new Tache(titre, texte)), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/api/deleteTodo")
    public void deleteTache(@RequestParam(name = "id") long idTache) {
        tacheService.deleteTache(idTache);
    }

    @PatchMapping(path = "/api/updateTodo", produces = {"application/json; charset=UTF-8"})
    public ResponseEntity<Tache> updateTache(@RequestParam(name = "id") long idTache,
                                             @RequestParam(name = "titre", required = false) String updatedTitre,
                                             @RequestParam(name = "texte", required = false) String updatedTexte)
    {
        if (updatedTitre == null && updatedTexte == null) {
            LOGGER.warn("New todo title and text are null, exiting");
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "New todo title and text are null");
        }

        Tache tacheToUpdate = tacheService.findTacheById(idTache);
        if (tacheToUpdate != null) {
            tacheToUpdate.setTitre(updatedTitre == null ? tacheToUpdate.getTitre() : updatedTitre);
            tacheToUpdate.setTexte(updatedTexte == null ? tacheToUpdate.getTexte() : updatedTexte);

            return new ResponseEntity<>(tacheService.saveTache(tacheToUpdate), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}
