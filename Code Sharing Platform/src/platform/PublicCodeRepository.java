package platform;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublicCodeRepository extends CrudRepository<PublicCode, Long> {
    Optional<PublicCode> findByUuid(String uuid);
}
