package m2sdl.prjdevops.controller.web;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import m2sdl.prjdevops.domain.Tache;
import m2sdl.prjdevops.service.TacheService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller("webController")
public class TacheWebController {
    private final TacheService tacheService;

    private static final Logger LOGGER = LogManager.getLogger();

    public TacheWebController(TacheService tacheService) {
        this.tacheService = tacheService;
    }

    @GetMapping(path = "/todo/{id}")
    public String getTodo(@PathVariable int id, Model model) {
        if (id < 0) throw new IllegalArgumentException("idTache is negative.");

        try {
            Tache fetchedTache = this.tacheService.findTacheById(id);
            model.addAttribute("tache", fetchedTache);
            return "redirect:/todo/" + id;
        } catch (EntityNotFoundException e) {
            return "error";
        }
    }

    @GetMapping(path = "/todos")
    public String getAllTodos(Model model) {
        model.addAttribute("taches", this.tacheService.findAllTaches());
        return "todos";
    }

    @PostMapping(path = "/addtodo")
    public String addtodo(@Valid Tache tache,
                          BindingResult bindingResult,
                          Model model) {

        if (bindingResult.hasErrors()) {
            LOGGER.error(bindingResult.getAllErrors());
            return "error";
        }

        this.tacheService.saveTache(tache);
        return "redirect:/todos";
    }

    @PatchMapping(path = "/todo/edit/{id}")
    public String updateTodo(@PathVariable int id,
                             @RequestParam(required = false) String titreTache,
                             @RequestParam(required = false) String texteTache,
                             Model model) {
        if(titreTache == null && texteTache == null) {
            LOGGER.warn("Nothing to update.");
            return "redirect:/todos";
        }

        try {
            Tache tacheToUpdate = this.tacheService.findTacheById(id);

            tacheToUpdate.setTitre(titreTache == null ? tacheToUpdate.getTitre() : titreTache);
            tacheToUpdate.setTexte(texteTache == null ? tacheToUpdate.getTexte() : texteTache);

            tacheToUpdate = this.tacheService.saveTache(tacheToUpdate);
            model.addAttribute("tache", tacheToUpdate);
            return "redirect:/todo/" + tacheToUpdate.getId();
        } catch (EntityNotFoundException e) {
            LOGGER.error(e.getMessage());
            return "error";
        }
    }

    @DeleteMapping(path = "/todo/delete/{id}")
    public String deleteTodo(@PathVariable int id) {
        if(id < 0) throw new IllegalArgumentException("id is negative.");

        this.tacheService.deleteTache(id);

        return "redirect:/todos";
    }

    @GetMapping(path = "/newTodo")
    public String showNewTodoForm(Model model) {
        Tache tacheToAdd = new Tache();
        model.addAttribute("tache", tacheToAdd);
        return "add-todo";
    }
}
