package project.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "SPECIALIZATION")
public class Specialization {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "Name")
    private String name;

    @OneToMany(mappedBy = "SpecializationMapped", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Doctor> DoctorList;

    public Specialization() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Doctor> getDoctorList() {
        return DoctorList;
    }

    public void setDoctorList(List<Doctor> doctorList) {
        DoctorList = doctorList;
    }
}
