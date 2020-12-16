package project.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Model.Person;

@Repository
public interface PersonDAO extends JpaRepository<Person, Long> {
}
