package m2sdl.prjdevops;

import m2sdl.prjdevops.domain.Tache;
import m2sdl.prjdevops.service.TacheService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TacheServiceIntegrationTest {

    @Autowired
    private TacheService tacheService;

    private Tache tache1;
    private Tache tache2;
    private Tache tache3;
    private Tache tache4;

    @BeforeEach
    public void setUp() {
        this.tache1 = new Tache("Cours", "Acheter des stylos", "Alain");
        this.tache2 = new Tache("Administratif", "Faire la déclaration d'impots", "Alain");
        this.tache3 = new Tache("Sport", "Changer de vélo", "Albert");
        this.tache4 = new Tache("Bricolage", "Réparer le pied de la table", "Aline");

        this.tache1.setIsDone(true);
        this.tache4.setIsDone(true);

        this.tache1 = tacheService.saveTache(this.tache1);
        this.tache2 = tacheService.saveTache(this.tache2);
        this.tache3 = tacheService.saveTache(this.tache3);
        this.tache4 = tacheService.saveTache(this.tache4);
    }

    @Test
    void givenANullTache_whenSaveTache_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> tacheService.saveTache(null));
    }

    @Test
    void givenAnExistingTache_whenGetTache_thenTacheIsReturned() {
        Tache fetchedTache = tacheService.findTacheById(tache1.getId());
        assertNotNull(fetchedTache);
    }

    @Test
    void givenANegativeId_whenGetTache_thenThrowException() {
        assertNull(tacheService.findTacheById(-1L));
    }

    @Test
    void given4Taches_whenGetAllTache_thenCardinalIs4() {
        List<Tache> taches = tacheService.findAllTaches();
        assertEquals(4, taches.size(), "Wrong number of Taches (Must be 4)");
    }

    @Test
    void givenATache_whenSaveTache_ThenTacheIsUnchanged() {
        Tache fetchedTache = tacheService.findTacheById(tache1.getId());
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
        tacheService.saveTache(fetchedTache4);
        assertEquals(count, tacheService.countTaches(), "Wrong number of Taches (Must be 4)");
    }

    @Test
    void givenANewTache_whenNewTacheIsSaved_thenTachesCountIsUpdated() {
        long count = tacheService.countTaches();
        tacheService.saveTache(new Tache("Travail", "Répondre aux mails", "Georges"));
        assertEquals(count + 1, tacheService.countTaches(), "Wrong number of Taches (Must be 5)");
    }

    @Test
    void givenANewTache_whenTacheIsSaved_thenTacheHasNonNullDate() {
        Tache newTache = new Tache("Santé", "Re-remplir le kit de 1er secours", "Lea");
        newTache = tacheService.saveTache(newTache);
        assertNotNull(newTache.getDate());
    }

    @Test
    void givenANewTache_whenTacheIsUpdated_thenDateTacheIsUnchanged() {
        Tache newTache = new Tache("Maison", "Ranger le grenier", "Leo");
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

    @Test
    void givenATache_whenTacheIsDeleted_thenTacheCountIsDecrementedByOne() {
        long countach = tacheService.countTaches();
        tacheService.deleteTache(tache2.getId());
        assertEquals(countach - 1, tacheService.countTaches(), "Count of Taches was not decremented by one. Should be 3 and not 4.");
    }

    @Test
    void givenAUser_whenGetTachesForUtilisateur_thenTachesAreForThisUtilisateur() {
        List<Tache> tachesAlain = tacheService.findTacheByUtilisateur("Alain");

        assertAll("Tache list is 2 and Alain is the assignee for ALL taches",
                () -> assertEquals(2, tachesAlain.size()),
                () -> assertEquals("Alain", tachesAlain.get(0).getUtilisateur()),
                () -> assertEquals("Alain", tachesAlain.get(1).getUtilisateur())
        );
    }

    @Test
    void givenCompletedTaches_whenGetCompletedTaches_thenAllReturnedTachesBooleanIsDoneIsTrue() {
        List<Tache> completedTaches = tacheService.findCompletedTaches();
        assertAll("There are only 2 taches and they have been completed.",
                () -> assertEquals(2, completedTaches.size()),
                () -> assertTrue(completedTaches.get(0).getIsDone()),
                () -> assertTrue(completedTaches.get(1).getIsDone())
        );
    }

    @Test
    void givenUncompletedTaches_whenGetunCompletedTaches_thenAllReturnedTachesBooleanIsDoneIsFalse() {
        tache1.setIsDone(true);

        tacheService.saveTache(tache1);

        List<Tache> uncompletedTaches = tacheService.findUncompletedTaches();

        assertAll("There are only 3 taches and they aren't completed.",
                () -> assertEquals(3, uncompletedTaches.size()),
                () -> assertFalse(uncompletedTaches.get(0).getIsDone()),
                () -> assertFalse(uncompletedTaches.get(1).getIsDone()),
                () -> assertFalse(uncompletedTaches.get(2).getIsDone())
        );
    }
}
