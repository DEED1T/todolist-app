package m2sdl.prjdevops.repository;

import m2sdl.prjdevops.domain.Tache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TacheRepository extends CrudRepository<Tache, Long> {
}
