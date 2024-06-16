package m2sdl.prjdevops;

import jakarta.transaction.Transactional;
import m2sdl.prjdevops.domain.Tache;
import m2sdl.prjdevops.service.TacheService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WebControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TacheService tacheService;

    @Test
    void givenAMockMVCAndNoExistingTaches_whenHTTPGetOnTodosTaches_thenReturnSuccessAndHeaderToAddTaches() throws Exception {
        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("todos"))
                .andExpect(content().string(containsString("<h2 class=\"text-center mt-4\"> Aucune tâche ! Commencez par <a href=\"/newTodo\">ajouter une tâche.</a></h2>")));
    }

    @Nested
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Transactional
    class otherTests {
        private Tache tache1, tache2, tache3, tache4;

        @BeforeEach
        void setUp() {
            this.tache1 = new Tache("Cours", "Acheter des stylos", "Joséphine");
            this.tache2 = new Tache("Administratif", "Faire la déclaration d'impots", "Isabelle");
            this.tache3 = new Tache("Sport", "Changer de vélo", "Hamdi");
            this.tache4 = new Tache("Bricolage", "Réparer le pied de la table", "Tristan");

            this.tache1 = tacheService.saveTache(this.tache1);
            this.tache2 = tacheService.saveTache(this.tache2);
            this.tache3 = tacheService.saveTache(this.tache3);
            this.tache4 = tacheService.saveTache(this.tache4);
        }

        @Test
        void givenAMockMVCAndSomeExistingTaches_whenHTTPGetOnTodosTaches_thenReturnAllTachesAndSuccess() throws Exception {
            mockMvc.perform(get("/todos"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("todos"))
                    .andExpect(content().string(containsString("<h2 class=\"text-center mt-4\">Mes tâches à faire</h2>")));
        }

        @Test
        void givenAValidTacheId_whenHTTPGetTacheWithId_thenReturnSuccessAndTacheView() throws Exception {
            mockMvc.perform(get("/todo/" + this.tache1.getId()))
                    .andExpect(status().isOk())
                    .andExpect(view().name("todo"))
                    .andExpect(content().string(containsString(tache1.getTitre())));
        }

        @Test
        void givenAnInvalidTacheId_whenHTTPGetTacheWithId_thenReturnError() throws Exception {
            mockMvc.perform(get("/todo/" + Integer.MAX_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("error"))
                    .andDo(print());
        }

        @Test
        void givenANewTache_whenHTTPPostNewTache_thenReturnSuccessAndTodosView() throws Exception {
            mockMvc.perform(post("/addtodo")
                            .param("titre", this.tache2.getTitre())
                            .param("texte", this.tache2.getTexte())
                            .param("utilisateur", this.tache2.getUtilisateur()))
                    .andExpect(status().isFound())
                    .andExpect(view().name("redirect:/todos"))
                    .andDo(print());
        }

        @Test
        void givenAnInvalidTache_whenHTTPPostNewTache_thenReturnError() throws Exception {
            mockMvc.perform(post("/addtodo")
                            .param("titre", "")
                            .param("texte", this.tache2.getTexte()))
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andDo(print());
        }

        @Test
        void givenAValidTache_whenHTTPPostEditTache_thenReturnSuccess() throws Exception {
            Long id = tache3.getId();

            mockMvc.perform(get("/todo/edit/" + id))
                    .andExpect(status().isOk())
                    .andExpect(view().name("edit-todo"))
                    .andExpect(content().string(Matchers.containsString(tache3.getTitre())))
                    .andExpect(model().attribute("tacheToUpdate", hasProperty("id", is(tache3.getId()))))
                    .andDo(print());
        }

        @Test
        void givenAnInValidTache_whenHTTPPostEditTache_thenReturnSuccess() throws Exception {
            mockMvc.perform(get("/todo/edit/" + Integer.MAX_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andDo(print());
        }

        @Test
        void givenATache_whenHTTPDelete_thenReturnSuccess() throws Exception {
            long countach = tacheService.countTaches();
            assertTrue(countach > 0);
            mockMvc.perform(get("/todo/delete/" + tache4.getId()))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/todos"))
                    .andDo(print());

            assertEquals(countach - 1, tacheService.findAllTaches().size());
        }

        @Test
        void givenAnInvalidTache_whenHTTPDelete_thenReturnSuccess() throws Exception {
            mockMvc.perform(get("/todo/delete/" + Integer.MAX_VALUE))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/todos"))
                    .andDo(print());
        }

    }
}
