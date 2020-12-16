package project.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.Model.Recipe;

import java.util.List;

@Repository
public interface RecipeDAO extends JpaRepository<Recipe, Long> {

    @Query("select c from Recipe c " +
            "where lower(c.PersonMapped.surName) like lower(concat('%', :searchTerm, '%')) "
            +
            "or lower(c.PriorityMapped.name) like lower(concat('%', :searchTerm, '%'))"
            +
            "or lower(c.description) like lower(concat('%', :searchTerm, '%'))"
            +
            "or lower(c.DoctorMapped.surName) like lower(concat('%', :searchTerm, '%'))")
    List<Recipe> search(@Param("searchTerm") String searchTerm);

}
