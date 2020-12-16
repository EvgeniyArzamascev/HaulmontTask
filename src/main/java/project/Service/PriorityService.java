package project.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.DAO.PriorityDAO;
import project.Model.Priority;

import java.util.List;
import java.util.Optional;

@Service
public class PriorityService  {

    @Autowired
    private PriorityDAO priorityDAO;

    public List<Priority> findAll() {
        return priorityDAO.findAll();
    }

    public Optional<Priority> findById(Long id){
        return priorityDAO.findById(id);
    }

    public void save(Priority priority){
        priorityDAO.save(priority);
    }

    public void delete(Priority priority){
        priorityDAO.delete(priority);
    }

}
