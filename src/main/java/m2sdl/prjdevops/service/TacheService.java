package m2sdl.prjdevops.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import lombok.Getter;
import m2sdl.prjdevops.domain.Tache;
import m2sdl.prjdevops.repository.TacheRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Service
public class TacheService {

    private final TacheRepository tacheRepository;
    private final Counter counterTacheOperations;
    private final Timer timerAddTache;

    //SonarLint java:S6813
    //https://rules.sonarsource.com/java/RSPEC-3306
    public TacheService(TacheRepository tacheRepository, CompositeMeterRegistry meterRegistry) {
        this.tacheRepository = tacheRepository;

        this.counterTacheOperations = Counter.builder("tache_operations")
                .description("Counts how many times a Tache has been created, updated and deleted.")
                .tags("tache", "create", "update", "delete")
                .register(meterRegistry);

        this.timerAddTache = Timer.builder("add.tache.bdd.responsetime")
                .description("Total and max time of inserting a Tache in Database.")
                .tags("database", "time", "tache", "max", "total time", "count")
                .register(meterRegistry);
    }


    public long countTaches() {
        return tacheRepository.count();
    }

    public Tache findTacheById(long id) {
        return tacheRepository
                .findById(id)
                .orElse(null);
    }

    public List<Tache> findAllTaches() {
        List<Tache> allTaches = new ArrayList<>();

        this.tacheRepository.findAll().forEach(allTaches::add);

        //Ordonner par date
        allTaches.sort(Comparator.comparing(Tache::getDate));

        return allTaches;
    }

    public Tache saveTache(Tache tache) {
        if (tache == null) throw new IllegalArgumentException("Tache cannot be null");
        if (tache.getDate() == null) tache.setDate(LocalDateTime.now());

        counterTacheOperations.increment();
        return timerAddTache.record(() -> this.tacheRepository.save(tache));
    }

    public void deleteTache(long id) {
        // Pour s'assurer que la tache existe.
        if (this.findTacheById(id) != null) {
            this.tacheRepository.deleteById(id);
            counterTacheOperations.increment();
        }
    }
}
