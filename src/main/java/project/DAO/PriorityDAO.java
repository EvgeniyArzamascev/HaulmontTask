package project.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Model.Priority;


@Repository
public interface PriorityDAO extends JpaRepository<Priority, Long> {
}
