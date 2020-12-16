package project.Model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PERSON")
public class Person {

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

    @Column(name = "Phone")
    private String phone;

    @OneToMany(mappedBy = "PersonMapped", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Recipe> RecipeList;


    public Person() {}

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Recipe> getRecipeList() {
        return RecipeList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        RecipeList = recipeList;
    }
}
