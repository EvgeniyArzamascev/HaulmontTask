package project.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Model.Specialization;

@Repository
public interface SpecializationDAO extends JpaRepository<Specialization, Long> {
}
