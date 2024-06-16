package m2sdl.prjdevops;

import io.micrometer.core.instrument.MeterRegistry;
import m2sdl.prjdevops.controller.rest.TacheController;
import m2sdl.prjdevops.domain.Tache;
import m2sdl.prjdevops.repository.TacheRepository;
import m2sdl.prjdevops.service.TacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TacheRestControllerTest {
    private TacheController tacheController;

    @MockBean
    private TacheService tacheService;

    @MockBean
    private Tache tache;

    @BeforeEach
    public void setUp() {
        this.tacheController = new TacheController(tacheService);

        when(tacheService.findTacheById(1L)).thenReturn(tache);
    }

    @Test
    void testAddTache() {
        tacheController.addTache("Finances", "Changer de banque");

        verify(tacheService).saveTache(any(Tache.class));
    }

    @Test
    void testRemoveTache() {
        tacheController.deleteTache(1L);

        verify(tacheService).deleteTache(1L);
    }

    @Test
    void testFindTacheById() {
        tacheController.findTache(1L);

        verify(tacheService).findTacheById(any(Long.class));
    }

    @Test
    void testFindAllTaches() {
        tacheController.findAllTaches();

        verify(tacheService).findAllTaches();
    }

    @Test
    void testUpdateTache() {
        tacheController.addTache("Finances", "Faire un virement");

        tacheController.updateTache(any(Long.class), "Finances", "Faire un virement");
        verify(tacheService).saveTache(any(Tache.class));
    }

}
