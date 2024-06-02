package m2sdl.prjdevops.service;

import lombok.Getter;
import m2sdl.prjdevops.domain.Tache;
import m2sdl.prjdevops.repository.TacheRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Getter
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
                .orElse(null);
    }

    public List<Tache> findAllTaches() {
        List<Tache> allTaches = new ArrayList<>();

        this.tacheRepository.findAll().forEach(allTaches::add);
        allTaches.sort(Comparator.comparing(Tache::getDate));

        return allTaches;
    }

    public Tache saveTache(Tache tache) {
        if (tache == null) throw new IllegalArgumentException("Tache cannot be null");
        if (tache.getDate() == null) tache.setDate(LocalDateTime.now());
        return this.tacheRepository.save(tache);
    }

    public void deleteTache(long id) {
        this.tacheRepository.deleteById(id);
    }
}
