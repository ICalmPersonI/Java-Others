package recipes.repository;

import org.springframework.data.repository.CrudRepository;
import recipes.entity.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String name);
}
