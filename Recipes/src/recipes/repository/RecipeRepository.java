package recipes.repository;

import org.springframework.data.repository.CrudRepository;
import recipes.entity.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends CrudRepository<Recipe, Integer> {
    Optional<Recipe> findRecipeById(int id);
    Optional<List<Recipe>> findAllByNameContainsIgnoreCase(String name);
    Optional<List<Recipe>> findAllByCategoryLikeIgnoreCase(String category);
}
