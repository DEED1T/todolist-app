package m2sdl.prjdevops.controller;

import lombok.Setter;
import m2sdl.prjdevops.domain.Tache;
import m2sdl.prjdevops.service.TacheService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Setter
@RestController
public class TacheController {

    private final TacheService tacheService;

    public TacheController(TacheService tacheService) {
        this.tacheService = tacheService;
    }

    @GetMapping(path = "api/todos", produces = {"application/json; charset=UTF-8"})
    public List<Tache> findAllTaches() {
        return tacheService.findAllTaches().stream().filter(Objects::nonNull).toList();
    }

    @GetMapping(path = "api/todo", produces = {"application/json; charset=UTF-8"})
    public Tache findTache(@RequestParam(name = "id") long idTache) {
        if (idTache < 0) throw new IllegalArgumentException("idTache is negative.");

        return tacheService.findTacheById(idTache);
    }

    @PostMapping(path = "api/addTodo", produces = {"application/json; charset=UTF-8"})
    public ResponseEntity<Tache> addTache(@RequestParam(name = "tache") Tache tache) {
        return new ResponseEntity<>(tacheService.saveTache(tache), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/api/deleteTodo")
    public void deleteTache(@RequestParam(name = "idTache") long idTache) {
        tacheService.deleteTache(idTache);
    }

    @PatchMapping(path = "/api/updateTodo", produces = {"application/json; charset=UTF-8"})
    public ResponseEntity<Tache> updateTache(@RequestParam(name = "idTache") long idTache,
                                             @RequestParam(name = "titre") String updatedTitre,
                                             @RequestParam(name = "texte") String updatedTexte)
    {
        if (updatedTitre == null || updatedTexte == null || updatedTitre.isBlank() || updatedTexte.isBlank())
            //isBlank vérifie si le String n'est pas vide et ne contient pas que des espaces.
            throw new IllegalArgumentException("Titre or texte is empty or only contains whitespaces");

        Tache tacheToUpdate = tacheService.findTacheById(idTache);
        if (tacheToUpdate != null) {
            tacheToUpdate.setTitre(updatedTitre);
            tacheToUpdate.setTexte(updatedTexte);

            return new ResponseEntity<>(tacheService.saveTache(tacheToUpdate), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}
