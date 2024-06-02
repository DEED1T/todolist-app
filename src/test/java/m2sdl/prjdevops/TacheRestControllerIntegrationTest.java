package m2sdl.prjdevops;

import m2sdl.prjdevops.domain.Tache;
import m2sdl.prjdevops.service.TacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class TacheRestControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);

    @Autowired
    private TacheService tacheService;

    private Tache tache1;
    private Tache tache2;
    private Tache tache3;
    private Tache tache4;

    @BeforeEach
    public void setUp() {
        this.tache1 = new Tache("Cours", "Acheter des stylos");
        this.tache2 = new Tache("Administratif", "Faire la déclaration d'impots");
        this.tache3 = new Tache("Sport", "Changer de vélo");
        this.tache4 = new Tache("Bricolage", "Réparer le pied de la table");

        this.tache1 = tacheService.saveTache(this.tache1);
        this.tache2 = tacheService.saveTache(this.tache2);
        this.tache3 = tacheService.saveTache(this.tache3);
        this.tache4 = tacheService.saveTache(this.tache4);
    }

    @Test
    void givenTaches_whenGetAllTaches_thenReturnJsonArray() throws Exception {
        mvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))

                .andExpect(jsonPath("$[0].id", is(tache1.getId().intValue())))
                .andExpect(jsonPath("$[1].id", is(tache2.getId().intValue())))
                .andExpect(jsonPath("$[2].id", is(tache3.getId().intValue())))
                .andExpect(jsonPath("$[3].id", is(tache4.getId().intValue())))

                .andExpect(jsonPath("$[0].titre", is(tache1.getTitre())))
                .andExpect(jsonPath("$[1].titre", is(tache2.getTitre())))
                .andExpect(jsonPath("$[2].titre", is(tache3.getTitre())))
                .andExpect(jsonPath("$[3].titre", is(tache4.getTitre())))

                .andExpect(jsonPath("$[0].texte", is(tache1.getTexte())))
                .andExpect(jsonPath("$[1].texte", is(tache2.getTexte())))
                .andExpect(jsonPath("$[2].texte", is(tache3.getTexte())))
                .andExpect(jsonPath("$[3].texte", is(tache4.getTexte())));
    }

    @Test
    void givenATache_whenFindTacheById_thenReturnTacheJsonSuccess() throws Exception {
        mvc.perform(get("/api/todo?id=" + 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))

                .andExpect(jsonPath("$.id", is(tache1.getId().intValue())))
                .andExpect(jsonPath("$.titre", is(tache1.getTitre())))
                .andExpect(jsonPath("$.texte", is(tache1.getTexte())));
    }

    @Test
    void givenAnNonExistantId_whenFindTacheById_thenReturnTacheJsonNotFound() throws Exception {
        mvc.perform(get("/api/todo/id=" + 123456789L))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenANewTache_whenAddTache_thenReturnTacheJsonSuccess() throws Exception {
        mvc.perform(post("/api/addTodo?titre=" + tache2.getTitre() + "&texte=" + tache2.getTexte()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))

                .andExpect(jsonPath("$.titre", is(tache2.getTitre())))
                .andExpect(jsonPath("$.texte", is(tache2.getTexte())));
    }

    @Test
    void givenAnExistingTache_whenUpdateTacheText_thenReturnTacheJsonSuccess() throws Exception {
        String newTacheTexte = "Renouveler abonnement à la salle";

        mvc.perform(patch("/api/updateTodo?id=" + tache3.getId() + "&texte=" + newTacheTexte))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))

                .andExpect(jsonPath("$.id", is(tache3.getId().intValue())))
                .andExpect(jsonPath("$.titre", is(tache3.getTitre())))
                .andExpect(jsonPath("$.texte", is(newTacheTexte)));
    }

    @Test
    void givenAnExistingTache_whenUpdateTacheTitre_thenReturnTacheJsonSuccess() throws Exception {
        String newTacheTitre = "Corvée";

        mvc.perform(patch("/api/updateTodo?id=" + tache4.getId() + "&titre=" + newTacheTitre))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))

                .andExpect(jsonPath("$.id", is(tache4.getId().intValue())))
                .andExpect(jsonPath("$.titre", is(newTacheTitre)))
                .andExpect(jsonPath("$.texte", is(tache4.getTexte())));
    }

    @Test
    void givenAnExistingTache_whenUpdateWithoutText_thenReturnError() throws Exception {
        mvc.perform(patch("/api/updateTodo?id=" + tache3.getId()))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void givenAnExistingTache_whenDeleteTache_thenReturnTacheJsonSuccess() throws Exception {
        long countach = tacheService.countTaches();

        mvc.perform(delete("/api/deleteTodo?id=" + tache4.getId()))
        .andExpect(status().isOk());

        assertEquals(countach - 1, tacheService.countTaches());
    }

}
