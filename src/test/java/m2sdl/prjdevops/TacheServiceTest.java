package m2sdl.prjdevops;

import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import m2sdl.prjdevops.repository.TacheRepository;
import m2sdl.prjdevops.service.TacheService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TacheServiceTest {

    private TacheService tacheService;

    @MockBean
    private TacheRepository tacheRepository;

    @BeforeEach
    public void setUp() {
        this.tacheService = new TacheService(tacheRepository, new CompositeMeterRegistry());
    }

    @Test
    void testRepositoryType() {
        assertThat(tacheService.getTacheRepository(), instanceOf(CrudRepository.class));
    }

    @Test
    void testFindByIdFromCrudRepositoryIsInvokedWhenTacheIsFoundById() {
        assertNull(tacheService.findTacheById(0L));
        verify(tacheService.getTacheRepository()).findById(0L);
    }

    @Test
    void testFindAllFromCrudRepositoryIsInvokedWhenFindAllTaches() {
        tacheService.findAllTaches();
        verify(tacheService.getTacheRepository()).findAll();
    }

}
