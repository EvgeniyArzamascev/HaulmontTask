package project.Model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "RECIPE")
public class Recipe {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, targetEntity = Person.class)
    @JoinColumn(name = "person_id")
    private Person personMapped;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, targetEntity = Doctor.class)
    @JoinColumn(name = "doctor_id")
    private Doctor doctorMapped;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "duration")
    private String duration;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, targetEntity = Priority.class)
    @JoinColumn(name = "priority_id")
    private Priority priorityMapped;

    public Recipe() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Person getPersonMapped() {
        return personMapped;
    }

    public void setPersonMapped(Person personMapped) {
        this.personMapped = personMapped;
    }

    public Doctor getDoctorMapped() {
        return doctorMapped;
    }

    public void setDoctorMapped(Doctor doctorMapped) {
        this.doctorMapped = doctorMapped;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Priority getPriorityMapped() {
        return priorityMapped;
    }

    public void setPriorityMapped(Priority priorityMapped) {
        this.priorityMapped = priorityMapped;
    }
}
