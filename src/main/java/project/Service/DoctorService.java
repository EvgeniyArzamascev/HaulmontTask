package project.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.DAO.DoctorDAO;
import project.DAO.RecipeDAO;
import project.Model.Doctor;
import project.Model.Person;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorDAO doctorDAO;

    public List<Doctor> findAll() {
        return doctorDAO.findAll();
    }

    public Optional<Doctor> findById(Long id){
        return doctorDAO.findById(id);
    }

    public void save(Doctor doctor){
        doctorDAO.save(doctor);
    }

    public void delete(Doctor doctor){
        doctorDAO.delete(doctor);
    }

}
