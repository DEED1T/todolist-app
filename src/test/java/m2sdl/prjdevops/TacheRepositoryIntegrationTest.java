package m2sdl.prjdevops;

import m2sdl.prjdevops.domain.Tache;
import m2sdl.prjdevops.repository.TacheRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DataJpaTest
@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
public class TacheRepositoryIntegrationTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    TacheRepository tacheRepository;

    @Test
    public void givenNewTache_whenSave_thenSuccess() {
        Tache originalTache = new Tache("Sport", "Faire du vélo");
        Tache savedTache = tacheRepository.save(originalTache);
        assertThat(entityManager.find(Tache.class, savedTache.getId())).isEqualTo(originalTache);
    }

    @Test
    public void givenTacheCreated_whenUpdate_thenSuccess() {
        Tache originalTache = new Tache("Maison", "Faire la vaiselle");

        entityManager.persist(originalTache);

        String newTitre = "Cuisine";
        originalTache.setTitre(newTitre);

        tacheRepository.save(originalTache);

        assertThat(entityManager.find(Tache.class, originalTache.getId()).getTitre()).isEqualTo(newTitre);
    }

    @Test
    public void givenTacheCreated_whenFindById_thenSuccess() {
        Tache tache = new Tache("Mecanique", "Changer le soufflet de cardan");

        entityManager.persist(tache);

        Optional<Tache> foundTache = tacheRepository.findById(tache.getId());

        assertThat(foundTache).contains(tache);
    }

    @Test
    public void givenTacheCreated_whenDelete_thenSuccess() {
        Tache tache = new Tache("Jsais pas", "A supprimer car j'ai plus d'idées...");

        entityManager.persist(tache);
        tacheRepository.delete(tache);

        assertThat(entityManager.find(Tache.class, tache.getId())).isNull();
    }
}
