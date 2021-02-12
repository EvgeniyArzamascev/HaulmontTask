package project.Model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PRIORITY")
public class Priority {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "Name")
    private String name;

    @OneToMany(mappedBy = "priorityMapped", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Recipe> recipeList;

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

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }
}
