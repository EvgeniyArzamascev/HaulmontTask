package project.View;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import project.Model.*;
import project.Service.DoctorService;
import project.Service.PersonService;
import project.Service.PriorityService;
import project.Service.RecipeService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Route("Recipe")
@CssImport("./styles/style.css")
public class RecipePage extends AppLayout {

    Grid<Recipe> grid = new Grid<>(Recipe.class);

    RecipeService recipeService;
    DoctorService doctorService;
    PersonService personService;
    PriorityService priorityService;

    public RecipePage(RecipeService recipeService,
                      DoctorService doctorService,
                      PersonService personService,
                      PriorityService priorityService)
    {
        this.recipeService = recipeService;
        this.doctorService = doctorService;
        this.personService = personService;
        this.priorityService = priorityService;
        createNavbar();
        createDrawer();
        createContent();
    }

    private void createNavbar() {
        H3 head = new H3("Recipe Page");
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), head);
        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink mainPage = new RouterLink("Home", HomePage.class);
        RouterLink personPage = new RouterLink("Person List", PersonPage.class);
        RouterLink doctorPage = new RouterLink("Doctor List", DoctorPage.class);
        RouterLink recipePage = new RouterLink("Recipe List", RecipePage.class);
        RouterLink specializationPage = new RouterLink("Specialization List", SpecializationPage.class);
        RouterLink priorityPage = new RouterLink("Priority List", PriorityPage.class);
        VerticalLayout layout = new VerticalLayout();
        layout.add(mainPage, personPage, doctorPage, recipePage, specializationPage, priorityPage);
        addToDrawer(layout);
    }

    private void createContent() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(newRecipe(), filterGrid(), configGrid());
        setContent(layout);
    }

    private TextField filterGrid() {
        TextField field = new TextField();
        field.setPlaceholder("Filter by words...");
        field.setValueChangeMode(ValueChangeMode.LAZY);
        field.addValueChangeListener(x -> {
            grid.setItems(recipeService.findAll(field.getValue()));
        });
        return field;
    }

    private Button newRecipe() {
        Button button = new Button("New Recipe", VaadinIcon.SMILEY_O.create());
        button.addClickListener(x -> {
            Dialog dialog = new Dialog();
            Label label = new Label("Add new Recipe");
            label.addClassName("label");

            TextField description = new TextField("Description");
            TextField duration = new TextField("Duration");

            DatePicker datePicker = new DatePicker("Date");
            datePicker.setMax(LocalDate.now());


            ComboBox<Doctor> doctor = new ComboBox<>("Doctor");
            List<Doctor> doctorList = doctorService.findAll();
            doctor.setItems(doctorList);
            doctor.setItemLabelGenerator(Doctor::getInfo);

            ComboBox<Person> person = new ComboBox<>("Person");
            List<Person> personList = personService.findAll();
            person.setItems(personList);
            person.setItemLabelGenerator(Person::getInfo);

            ComboBox<Priority> priority = new ComboBox<>("Priority");
            List<Priority> priorityList = priorityService.findAll();
            priority.setItems(priorityList);
            priority.setItemLabelGenerator(Priority::getName);


            Button addButton = new Button("Add", VaadinIcon.CHECK_CIRCLE.create());
            addButton.addClassName("addButton");
            Button backButton = new Button("Cancel", VaadinIcon.REPLY.create());
            backButton.addClickListener(backEvent -> {
                dialog.close();
            });

            VerticalLayout layout = new VerticalLayout();
            HorizontalLayout horLayout1 = new HorizontalLayout();
            HorizontalLayout horLayout2 = new HorizontalLayout();
            HorizontalLayout horLayout3 = new HorizontalLayout();
            horLayout1.add(person, doctor, priority);
            horLayout2.add(description, duration, datePicker);
            horLayout3.add(backButton, addButton);
            layout.add(label, horLayout1, horLayout2, horLayout3);
            dialog.add(layout);

            dialog.open();

            // validation
            Binder<Recipe> binder = new Binder<>(Recipe.class);

            binder.forField(description)
                    .asRequired("Not Empty")
                    .bind(Recipe::getDescription, Recipe::setDescription);
            binder.forField(duration)
                    .asRequired("Not Empty")
                    .bind(Recipe::getDuration, Recipe::setDuration);
            binder.forField(doctor)
                    .withValidator(value -> value.getRecipeList() != null, "Not Empty")
                    .bind(Recipe::getDoctorMapped, Recipe::setDoctorMapped);
            binder.forField(person)
                    .withValidator(value -> value.getRecipeList() != null, "Not Empty")
                    .bind(Recipe::getPersonMapped, Recipe::setPersonMapped);
            binder.forField(priority)
                    .withValidator(value -> value.getRecipeList() != null, "Not Empty")
                    .bind(Recipe::getPriorityMapped, Recipe::setPriorityMapped);
            binder.forField(datePicker)
                    .asRequired("Date not Empty")
                    .bind(Recipe::getDate, Recipe::setDate);

            addButton.addClickListener(click -> {
                if (binder.validate().isOk()) {
                    Recipe recipe = new Recipe();
                    recipe.setDescription(description.getValue());
                    recipe.setPersonMapped(person.getValue());
                    recipe.setDoctorMapped(doctor.getValue());
                    recipe.setDuration(duration.getValue());
                    recipe.setPriorityMapped(priority.getValue());
                    recipe.setDate(datePicker.getValue());

                    recipeService.save(recipe);
                    grid.setItems(recipeService.findAll());
                    dialog.close();

                    Notification notification = new Notification("New recipe has been created", 1500);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();
                }
            });
        });
        return button;
    }

    private Grid configGrid() {
        grid.setColumns("id", "description", "date", "duration");

        grid.addColumn(p -> {
            Person person = p.getPersonMapped();
            return person.getInfo();
        }).setHeader("Person");

        grid.addColumn(d -> {
            Doctor doctor = d.getDoctorMapped();
            return doctor.getInfo();
        }).setHeader("Doctor");

        grid.addColumn(prior -> {
            Priority priority = prior.getPriorityMapped();
            return priority.getName();
        }).setHeader("Priority");


        grid.addComponentColumn(this::editButton);
        grid.addComponentColumn(this::deleteButton);

        grid.getColumns().forEach(x -> x.setAutoWidth(true));
        grid.setItems(recipeService.findAll());
        return grid;
    }

    private Button editButton(Recipe recipe) {
        Button button = new Button("Edit");
        button.addClickListener(x -> {
            Dialog dialog = new Dialog();
            Label label = new Label("Edit Recipe");
            label.addClassName("label");

            TextField description = new TextField("Description");
            TextField duration = new TextField("Duration");

            DatePicker datePicker = new DatePicker("Date");
            datePicker.setMax(LocalDate.now());

            ComboBox<Doctor> doctor = new ComboBox<>("Doctor");
            List<Doctor> doctorList = doctorService.findAll();
            doctor.setItems(doctorList);
            doctor.setItemLabelGenerator(Doctor::getInfo);

            ComboBox<Person> person = new ComboBox<>("Person");
            List<Person> personList = personService.findAll();
            person.setItems(personList);
            person.setItemLabelGenerator(Person::getInfo);

            ComboBox<Priority> priority = new ComboBox<>("Priority");
            List<Priority> priorityList = priorityService.findAll();
            priority.setItems(priorityList);
            priority.setItemLabelGenerator(Priority::getName);

            Optional<Recipe> Info = recipeService.findById(recipe.getId());
            Info.ifPresent(y ->
                    {
                        description.setValue(y.getDescription());
                        duration.setValue(y.getDuration());
                        datePicker.setValue(y.getDate());
                        doctor.setValue(y.getDoctorMapped());
                        person.setValue(y.getPersonMapped());
                        priority.setValue(y.getPriorityMapped());
                    }
            );
            Button update = new Button("Update", VaadinIcon.FILE_REFRESH.create());
            update.addClassName("updateButton");
            Button backButton = new Button("Cancel", VaadinIcon.REPLY.create());
            backButton.addClickListener(backEvent -> {
                dialog.close();
            });

            VerticalLayout layout = new VerticalLayout();
            HorizontalLayout horLayout1 = new HorizontalLayout();
            HorizontalLayout horLayout2 = new HorizontalLayout();
            HorizontalLayout horLayout3 = new HorizontalLayout();
            horLayout1.add(person, doctor, priority);
            horLayout2.add(description, duration, datePicker);
            horLayout3.add(backButton, update);
            layout.add(label, horLayout1, horLayout2, horLayout3);
            dialog.add(layout);
            dialog.open();

            // validation
            Binder<Recipe> binder = new Binder<>(Recipe.class);
            binder.forField(description)
                    .asRequired("Not Empty")
                    .bind(Recipe::getDescription, Recipe::setDescription);
            binder.forField(duration)
                    .asRequired("Not Empty")
                    .bind(Recipe::getDuration, Recipe::setDuration);
            binder.forField(doctor)
                    .withValidator(value -> value.getRecipeList() != null, "Not Empty")
                    .bind(Recipe::getDoctorMapped, Recipe::setDoctorMapped);
            binder.forField(person)
                    .withValidator(value -> value.getRecipeList() != null, "Not Empty")
                    .bind(Recipe::getPersonMapped, Recipe::setPersonMapped);
            binder.forField(priority)
                    .withValidator(value -> value.getRecipeList() != null, "Not Empty")
                    .bind(Recipe::getPriorityMapped, Recipe::setPriorityMapped);
            binder.forField(datePicker)
                    .asRequired("Date not Empty")
                    .bind(Recipe::getDate, Recipe::setDate);

            update.addClickListener(u -> {
                if (binder.validate().isOk()) {
                    Recipe newRecipe = new Recipe();
                    newRecipe.setId(recipe.getId());
                    newRecipe.setDescription(description.getValue());
                    newRecipe.setPersonMapped(person.getValue());
                    newRecipe.setDoctorMapped(doctor.getValue());
                    newRecipe.setDuration(duration.getValue());
                    newRecipe.setPriorityMapped(priority.getValue());
                    newRecipe.setDate(datePicker.getValue());

                    recipeService.save(newRecipe);
                    dialog.close();
                    grid.setItems(recipeService.findAll());
                }
            });

        });
        return button;
    }

    private Button deleteButton(Recipe recipe) {
        Button button = new Button("Delete");
        button.addClickListener(x -> {
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Delete Recipe");
            dialog.setText("You wanna delete this recipe?");

            Button delButton = new Button("Delete", VaadinIcon.TRASH.create());
            delButton.addClickListener(e -> {
                recipeService.delete(recipe);
                dialog.close();
                grid.setItems(recipeService.findAll());
            });
            dialog.setConfirmButton(delButton);
            dialog.setCancelButton("Cancel", this::onCancel);
            dialog.open();
        });
        return button;
    }

    private void onCancel(ConfirmDialog.CancelEvent cancelEvent) {}

}
