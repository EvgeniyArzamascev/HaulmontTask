package project.View;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import project.Model.*;
import project.Service.*;

import java.util.List;
import java.util.Optional;


@Route("Doctor")
@CssImport("./styles/style.css")
public class DoctorPage extends AppLayout {

    Grid<Doctor> grid = new Grid<>(Doctor.class);
    Grid<Recipe> recipeGrid = new Grid<>(Recipe.class);
    DoctorService doctorService;
    SpecializationService specializationService;
    RecipeService recipeService;

    public DoctorPage(DoctorService doctorService,
                      SpecializationService specializationService,
                      RecipeService recipeService)
    {
        this.doctorService = doctorService;
        this.specializationService = specializationService;
        this.recipeService = recipeService;
        createNavbar();
        createDrawer();
        createContent();
    };

    private void createNavbar() {
        H3 head = new H3("Doctor Page");
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), head);
        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink MainPage = new RouterLink("Home", HomePage.class);
        RouterLink PersonPage = new RouterLink("Person List", PersonPage.class);
        RouterLink DoctorPage = new RouterLink("Doctor List", DoctorPage.class);
        RouterLink RecipePage = new RouterLink("Recipe List", RecipePage.class);
        RouterLink SpecializationPage = new RouterLink("Specialization List", SpecializationPage.class);
        RouterLink PriorityPage = new RouterLink("Priority List", PriorityPage.class);
        VerticalLayout layout = new VerticalLayout();
        layout.add(MainPage, PersonPage, DoctorPage, RecipePage, SpecializationPage, PriorityPage);
        addToDrawer(layout);
    }

    private void createContent() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(newDoctor(),configGrid());
        setContent(layout);
    }

    private Button newDoctor() {
        Button button = new Button("New Doctor", VaadinIcon.SMILEY_O.create());
        button.addClickListener(x -> {

            Dialog dialog = new Dialog();
            VerticalLayout layout = new VerticalLayout();

            Label label = new Label("Add new Doctor");
            label.addClassName("label");
            TextField name = new TextField("Name");
            TextField surname = new TextField("SurName");
            TextField middlename = new TextField("Middle Name");

            ComboBox<Specialization> spec = new ComboBox<>("Specialization");
            List<Specialization> specializationList = specializationService.findAll();
            spec.setItems(specializationList);
            spec.setItemLabelGenerator(Specialization::getName);

            Button addButton = new Button("Add", VaadinIcon.CHECK_CIRCLE.create());

            Button backButton = new Button("Cancel", VaadinIcon.REPLY.create());
            backButton.addClickListener(backEvent -> {
                dialog.close();
            });

            HorizontalLayout horLayout = new HorizontalLayout();
            horLayout.add(backButton, addButton);
            layout.add(label, surname, name, middlename, spec, horLayout);
            dialog.add(layout);
            dialog.open();

            // validation
            Binder<Doctor> binder = new Binder<>(Doctor.class);

            binder.forField(name)
                    .asRequired("Not Empty")
                    .bind(Doctor::getName, Doctor::setName);
            binder.forField(surname)
                    .asRequired("Not Empty")
                    .bind(Doctor::getSurName, Doctor::setSurName);
            binder.forField(middlename)
                    .asRequired("Not Empty")
                    .bind(Doctor::getMiddleName, Doctor::setMiddleName);
            binder.forField(spec)
                    .withValidator(value -> value.getDoctorList() != null, "Not Empty")
                    .bind(Doctor::getSpecializationMapped, Doctor::setSpecializationMapped);

            addButton.addClickListener(addEvent -> {
                if (binder.validate().isOk()) {
                    Doctor newDoctor = new Doctor();
                    newDoctor.setName(name.getValue());
                    newDoctor.setSurName(surname.getValue());
                    newDoctor.setMiddleName(middlename.getValue());
                    newDoctor.setSpecializationMapped(spec.getValue());

                    doctorService.save(newDoctor);
                    grid.setItems(doctorService.findAll());
                    dialog.close();

                    Notification notification = new Notification("New doctor has been created", 1500);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();
                }
            });
        });
        return button;
    }

    private Grid configGrid() {
        grid.setColumns("id", "surName", "name", "middleName");

        grid.addColumn(x -> {
            Specialization spec = x.getSpecializationMapped();
            return spec.getName();
        }).setHeader("Specialization");

        grid.addComponentColumn(this::EditButton);
        grid.addComponentColumn(this::DeleteButton);
        grid.addComponentColumn(this::StatisticsButton);

        grid.setItems(doctorService.findAll());
        grid.getColumns().forEach(x -> x.setAutoWidth(true));
        return grid;
    }

    private Button EditButton(Doctor doctor) {
        Button button = new Button("Edit");
        button.addClickListener(x -> {

            Dialog dialog = new Dialog();
            Label label = new Label("Edit Doctor");
            label.addClassName("label");
            TextField name = new TextField("Name");
            TextField surname = new TextField("SurName");
            TextField middlename = new TextField("Middle Name");

            ComboBox<Specialization> spec = new ComboBox<>("Specialization");
            List<Specialization> specializationList = specializationService.findAll();
            spec.setItems(specializationList);
            spec.setItemLabelGenerator(Specialization::getName);

            Optional<Doctor> Info = doctorService.findById(doctor.getId());
            Info.ifPresent(y ->
                    {
                        name.setValue(y.getName());
                        surname.setValue(y.getSurName());
                        middlename.setValue(y.getMiddleName());
                        spec.setValue(y.getSpecializationMapped());
                    }
            );

            VerticalLayout layout = new VerticalLayout();
            HorizontalLayout horLayout = new HorizontalLayout();

            Button update = new Button("Update", VaadinIcon.FILE_REFRESH.create());

            Button backButton = new Button("Cancel", VaadinIcon.REPLY.create());
            backButton.addClickListener(backEvent -> {
                dialog.close();
            });

            horLayout.add(backButton, update);
            layout.add(label, surname, name, middlename, spec, horLayout);
            dialog.add(layout);
            dialog.open();

            // validation
            Binder<Doctor> binder = new Binder<>(Doctor.class);

            binder.forField(name)
                    .asRequired("Not Empty")
                    .bind(Doctor::getName, Doctor::setName);
            binder.forField(surname)
                    .asRequired("Not Empty")
                    .bind(Doctor::getSurName, Doctor::setSurName);
            binder.forField(middlename)
                    .asRequired("Not Empty")
                    .bind(Doctor::getMiddleName, Doctor::setMiddleName);
            binder.forField(spec)
                    .withValidator(value -> value.getDoctorList() != null, "Not Empty")
                    .bind(Doctor::getSpecializationMapped, Doctor::setSpecializationMapped);

            update.addClickListener(u -> {
                if (binder.validate().isOk()) {
                    Doctor newDoctor = new Doctor();
                    newDoctor.setId(doctor.getId());
                    newDoctor.setName(name.getValue());
                    newDoctor.setSurName(surname.getValue());
                    newDoctor.setMiddleName(middlename.getValue());
                    newDoctor.setSpecializationMapped(spec.getValue());

                    doctorService.save(newDoctor);
                    dialog.close();
                    grid.setItems(doctorService.findAll());
                }
            });
        });
        return button;
    }

    private Button DeleteButton(Doctor doctor) {
        Button button = new Button("Delete");
        button.addClickListener(x -> {

            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Delete Doctor");
            dialog.setText("You wanna delete this doctor?");

            Button delButton = new Button("Delete", VaadinIcon.TRASH.create());
            delButton.addClickListener(e -> {
                
                try {
                    doctorService.delete(doctor);
                    dialog.close();
                    grid.setItems(doctorService.findAll());
                } catch (org.springframework.dao.DataIntegrityViolationException error){
                    dialog.close();
                    ConfirmDialog confirmDialog = new ConfirmDialog("Warning!",
                            "You cannot delete this doctor because he have Recipe", "OK", this::onOK);
                    confirmDialog.open();
                }
            });
            dialog.setConfirmButton(delButton);
            dialog.setCancelButton("Cancel", this::onCancel);
            dialog.open();
        });
        return button;
    }

    private void onOK(ConfirmDialog.ConfirmEvent confirmEvent) {}

    private void onCancel(ConfirmDialog.CancelEvent cancelEvent) {}

    private Button StatisticsButton(Doctor doctor) {
        Button button = new Button("Statistics");
        button.addClickListener(click -> {
            recipeGrid.setColumns("id", "description", "date", "duration");

            recipeGrid.addColumn(p -> {
                Person person = p.getPersonMapped();
                return person.getInfo();
            }).setHeader("Person");

            recipeGrid.addColumn(d -> {
                Doctor doc = d.getDoctorMapped();
                return doc.getInfo();
            }).setHeader("Doctor");

            recipeGrid.addColumn(prior -> {
                Priority priority = prior.getPriorityMapped();
                return priority.getName();
            }).setHeader("Priority");

            recipeGrid.setItems(doctor.getRecipeList());
            recipeGrid.getColumns().forEach(x -> x.setAutoWidth(true));
            VerticalLayout layout = new VerticalLayout();
            Button backButton = new Button("Back to the Doctor List", VaadinIcon.ARROW_BACKWARD.create());
            backButton.addClickListener(x -> {createContent();});
            layout.add(backButton, recipeGrid);
            setContent(layout);
        });
        return  button;
    }


}
