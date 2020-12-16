package project.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.DAO.PersonDAO;
import project.DAO.RecipeDAO;
import project.Model.Person;
import project.Model.Recipe;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeDAO recipeDAO;

    public List<Recipe> findAll() {
        return recipeDAO.findAll();
    }

    public List<Recipe> findAll(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return recipeDAO.findAll();
        }else {
            return recipeDAO.search(filterText);
        }
    }

    public Optional<Recipe> findById(Long id){
        return recipeDAO.findById(id);
    }

    public void save(Recipe recipe){
        recipeDAO.save(recipe);
    }

    public void delete(Recipe recipe){
        recipeDAO.delete(recipe);
    }

}
