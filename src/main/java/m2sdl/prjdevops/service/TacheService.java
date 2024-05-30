package m2sdl.prjdevops.service;

import jakarta.persistence.EntityNotFoundException;
import m2sdl.prjdevops.domain.Tache;
import m2sdl.prjdevops.repository.TacheRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TacheService {

    private final TacheRepository tacheRepository;

    //SonarLint java:S6813
    //https://rules.sonarsource.com/java/RSPEC-3306
    public TacheService(TacheRepository tacheRepository) {
        this.tacheRepository = tacheRepository;
    }

    public long countTaches() {
        return tacheRepository.count();
    }

    public Tache findTacheById(long id) {
        return tacheRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tache with id " + id + " not found"));
    }

    public List<Tache> findAllTaches() {
        List<Tache> allTaches = new ArrayList<>();

        this.tacheRepository.findAll().forEach(allTaches::add);

        return allTaches;
    }

    public Tache saveTache(Tache tache) {
        Objects.requireNonNull(tache, "Tache must not be null");
        tache.setDate(LocalDateTime.now());
        return this.tacheRepository.save(tache);
    }

    public void deleteTache(long id) {
        this.tacheRepository.deleteById(id);
    }
}
