package project.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.DAO.PersonDAO;
import project.Model.Person;
import project.Model.Priority;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonDAO personDAO;

    public List<Person> findAll() {
        return personDAO.findAll();
    }

    public Optional<Person> findById(Long id){
        return personDAO.findById(id);
    }

    public void save(Person person){
        personDAO.save(person);
    }

    public void delete(Person person){
        personDAO.delete(person);
    }


}
