package recipes.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.entity.Recipe;
import recipes.repository.RecipeRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RestController()
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired
    public RecipeRepository repository;

    @GetMapping(value = "/search", params = "name")
    public List<Recipe> searchByName(HttpServletResponse response,
                                     @RequestParam(value = "name") String name) {
        Optional<List<Recipe>> records = repository.findAllByNameContainsIgnoreCase(name);
        if (records.isEmpty()) {
            response.setStatus(200);
            return new ArrayList<>();
        } else {
            response.setStatus(200);
            return records.get()
                    .stream()
                    .sorted(Comparator.comparing(Recipe::getDate).reversed())
                    .collect(Collectors.toList());
        }
    }

    @GetMapping(value = "/search", params = "category")
    public List<Recipe> searchByCategory(HttpServletResponse response,
                                         @RequestParam(value = "category") String category) {
        Optional<List<Recipe>> records = repository.findAllByCategoryLikeIgnoreCase(category);
        if (records.isEmpty()) {
            response.setStatus(200);
            return new ArrayList<>();
        } else {
            response.setStatus(200);
            return records.get()
                    .stream()
                    .sorted(Comparator.comparing(Recipe::getDate).reversed())
                    .collect(Collectors.toList());
        }
    }


    @GetMapping("/{id}")
    public Recipe view(HttpServletResponse response, @PathVariable int id) {
        response.setStatus(202);
        Optional<Recipe> record = repository.findRecipeById(id);
            if (record.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            } else {
                response.setStatus(200);
                return record.get();
            }
    }

    @PostMapping("/new")
    public String create(HttpServletRequest request,
                         @Valid @RequestBody Recipe recipe) {
        recipe.setCreator(request.getUserPrincipal().getName());
        recipe.setDate(LocalDateTime.now());
        repository.save(recipe);
        return String.format("{ \"id\":%s }", recipe.id());
    }

    @DeleteMapping("/{id}")
    public void delete(HttpServletRequest request,
                       HttpServletResponse response,
                       @PathVariable int id) {
        response.setStatus(202);
        Optional<Recipe> record = repository.findRecipeById(id);
            if (record.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            } else {
                Optional<String> authUser = Optional.of(request.getUserPrincipal().getName());
                Optional<String> creator = Optional.ofNullable(record.get().getCreator());
                if (creator.orElse("").matches(authUser.orElse(""))
                        || creator.isEmpty()) {
                    repository.deleteById(id);
                    response.setStatus(204);
                } else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                }
            }
    }

    @PutMapping("/{id}")
    public void update(HttpServletRequest request,
                       HttpServletResponse response,
                       @Valid @RequestBody Recipe recipe,
                       @PathVariable int id) {
        response.setStatus(202);
        Optional<Recipe> record = repository.findRecipeById(id);
        if (record.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            if (record.get().getCreator().equals(request.getUserPrincipal().getName())
                    || record.get().getCreator().isEmpty()) {
                recipe.setDate(LocalDateTime.now());
                recipe.setId(record.get().id());
                repository.save(recipe);
                response.setStatus(204);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
    }
}
