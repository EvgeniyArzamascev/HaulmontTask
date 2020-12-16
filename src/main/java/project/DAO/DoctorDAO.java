package project.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.Model.Doctor;
import project.Model.Recipe;

import java.util.List;

@Repository
public interface DoctorDAO extends JpaRepository<Doctor, Long> {
}
