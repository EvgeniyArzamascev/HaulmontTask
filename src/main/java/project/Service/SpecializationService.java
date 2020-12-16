package project.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.DAO.SpecializationDAO;
import project.Model.Priority;
import project.Model.Specialization;

import java.util.List;
import java.util.Optional;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationDAO specializationDAO;

    public List<Specialization> findAll() {
        return specializationDAO.findAll();
    }

    public Optional<Specialization> findById(Long id){
        return specializationDAO.findById(id);
    }

    public void save(Specialization specialization){
        specializationDAO.save(specialization);
    }

    public void delete(Specialization specialization){
        specializationDAO.delete(specialization);
    }

}
