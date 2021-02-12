package project.Model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "DOCTOR")
public class Doctor {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "Name")
    private String name;

    @Column(name = "SurName")
    private String surName;

    @Column(name = "MiddleName")
    private String middleName;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, targetEntity = Specialization.class)
    @JoinColumn(name = "specialization_id")
    private Specialization specializationMapped;


    @OneToMany(mappedBy = "doctorMapped", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Recipe> recipeList;

    public Doctor() {}

    public String getInfo(){
        String info = getSurName() + " " + getName() + " " + getMiddleName();
        return info;
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

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Specialization getSpecializationMapped() {
        return specializationMapped;
    }

    public void setSpecializationMapped(Specialization specializationMapped) {
        this.specializationMapped = specializationMapped;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }
}
