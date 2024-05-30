package m2sdl.prjdevops;

import jakarta.persistence.EntityNotFoundException;
import m2sdl.prjdevops.domain.Tache;
import m2sdl.prjdevops.service.TacheService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TacheServiceIntegrationTest {

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

    @AfterAll
    public static void after() {

    }

    @Test
    void givenANullTache_whenSaveTache_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> tacheService.saveTache(null));
    }

    @Test
    void givenAnExistingTache_whenGetTache_thenTacheIsReturned() {
        Tache fetchedTache = tacheService.findTacheById(0L);
        assertNotNull(fetchedTache);
    }

    @Test
    void givenANegativeId_whenGetTache_thenThrowException() {
        assertThrows(EntityNotFoundException.class, () -> tacheService.findTacheById(-1L));
    }

    @Test
    void given4Taches_whenGetAllTache_thenCardinalIs4() {
        List<Tache> taches = tacheService.findAllTaches();

        assertEquals(4, taches.size(), "Wrong number of Taches (Must be 4)");
    }

    @Test
    void givenATache_whenSaveTache_ThenTacheIsUnchanged() {
        Tache fetchedTache = tacheService.findTacheById(1L);

        assertEquals(fetchedTache.getTitre(), tache1.getTitre());
        assertEquals(fetchedTache.getTexte(), tache1.getTexte());
    }

    @Test
    void givenATache_whenTacheIsUpdated_ThenIdRemainsTheSame() {
        Long idBeforeUpdate = tache3.getId();
        Tache fetchedTache3 = tacheService.findTacheById(idBeforeUpdate);

        fetchedTache3.setDate(LocalDateTime.now());

        fetchedTache3 = tacheService.saveTache(fetchedTache3);

        assertEquals(idBeforeUpdate, fetchedTache3.getId());
    }

    @Test
    void givenATache_whenTacheIsUpdated_thenTachesCountRemainsTheSame() {
        long count = tacheService.countTaches();

        Tache fetchedTache4 = tacheService.findTacheById(tache4.getId());
        fetchedTache4.setDate(LocalDateTime.now());

        assertEquals(count, tacheService.countTaches(), "Wrong number of Taches (Must be 4)");
    }

    @Test
    void givenANewTache_whenNewTacheIsSaved_thenTachesCountIsUpdated() {
        long count = tacheService.countTaches();

        tacheService.saveTache(new Tache("Travail", "Répondre aux mails"));

        assertEquals(count + 1, tacheService.countTaches(), "Wrong number of Taches (Must be 5)");
    }

    @Test
    void givenANewTache_whenTacheIsSaved_thenTacheHasNonNullDate() {
        Tache newTache = new Tache("Santé", "Re-remplir le kit de 1er secours");

        newTache = tacheService.saveTache(newTache);

        assertNotNull(newTache.getDate());
    }

    @Test
    void givenANewTache_whenTacheIsUpdated_thenDateTacheIsUnchanged() {
        Tache newTache = new Tache("Maison", "Ranger le grenier");

        newTache = tacheService.saveTache(newTache);
        LocalDateTime dateNewTache = newTache.getDate();
        newTache.setTexte("Vider le grenier");

        newTache = tacheService.saveTache(newTache);

        assertEquals(dateNewTache, newTache.getDate(), "Date tache has been updated.");
    }

    @Test
    void givenATache_whenTacheIsDeleted_thenTacheIsDeletedFromDB() {
        tacheService.deleteTache(tache2.getId());

        assertNull(tacheService.findTacheById(tache2.getId()));
    }

}
