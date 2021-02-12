package project.View;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
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
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import project.Model.Specialization;
import project.Service.SpecializationService;

import java.util.Optional;

@Route("Specialization")
@CssImport("./styles/style.css")
public class SpecializationPage extends AppLayout {

    Grid<Specialization> grid = new Grid<>(Specialization.class);
    SpecializationService specializationService;

    public SpecializationPage(SpecializationService specializationService){
        this.specializationService = specializationService;
        createNavbar();
        createDrawer();
        createContent();
    }

    private void createNavbar() {
        H3 head = new H3("Specialization Page");
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
        layout.add(newSpecialization(),configGrid());
        setContent(layout);
    }

    private Button newSpecialization() {
        Button button = new Button("New Specialization", VaadinIcon.SMILEY_O.create());
        button.addClickListener(x -> {
            Dialog dialog = new Dialog();
            VerticalLayout layout = new VerticalLayout();
            Label label = new Label("Add new Specialization");
            label.addClassName("label");
            TextField name = new TextField("Name");

            Button addButton = new Button("Add", VaadinIcon.CHECK_CIRCLE.create());
            Button backButton = new Button("Cancel", VaadinIcon.REPLY.create());
            backButton.addClickListener(backEvent -> {
                dialog.close();
            });
            HorizontalLayout horLayout = new HorizontalLayout();
            horLayout.add(backButton, addButton);
            layout.add(label, name, horLayout);
            dialog.add(layout);
            dialog.open();

            // validation
            Binder<Specialization> binder = new Binder<>(Specialization.class);
            binder.forField(name)
                    .asRequired("Not Empty")
                    .bind(Specialization::getName, Specialization::setName);

            addButton.addClickListener(addEvent -> {
                if (binder.validate().isOk()) {
                    Specialization specialization = new Specialization();
                    specialization.setName(name.getValue());
                    specializationService.save(specialization);
                    grid.setItems(specializationService.findAll());
                    dialog.close();
                    Notification notification = new Notification("New specialization has been created", 1500);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();
                }
            });
        });
        return button;
    }

    private Grid configGrid() {
        grid.setColumns("id", "name");
        grid.addComponentColumn(this::editButton);
        grid.addComponentColumn(this::deleteButton);
        grid.setItems(specializationService.findAll());
        return grid;
    }

    private Button editButton(Specialization specialization) {

        Button button = new Button("Edit");
        button.addClickListener(x -> {
            Dialog dialog = new Dialog();
            Label label = new Label("Edit specialization");
            label.addClassName("label");
            TextField Name = new TextField("Name");

            Optional<Specialization> info = specializationService.findById(specialization.getId()); // запонить значениями
            info.ifPresent(y ->
                    {
                        Name.setValue(y.getName());
                    }
            );

            Button update = new Button("Update", VaadinIcon.FILE_REFRESH.create());
            Button backButton = new Button("Cancel", VaadinIcon.REPLY.create());
            backButton.addClickListener(backEvent -> {
                dialog.close();
            });
            HorizontalLayout horLayout = new HorizontalLayout();
            horLayout.add(backButton, update);

            VerticalLayout layout = new VerticalLayout();
            layout.add(label, Name, horLayout);
            dialog.add(layout);
            dialog.open();

            // validation
            Binder<Specialization> binder = new Binder<>(Specialization.class);
            binder.forField(Name)
                    .asRequired("Not Empty")
                    .bind(Specialization::getName, Specialization::setName);

            update.addClickListener(u -> {
                if (binder.validate().isOk()) {
                    Specialization newSpecialization = new Specialization();
                    newSpecialization.setId(specialization.getId());
                    newSpecialization.setName(Name.getValue());
                    specializationService.save(newSpecialization);
                    dialog.close();
                    grid.setItems(specializationService.findAll());
                }
            });
        });
        return button;
    }

    private Button deleteButton(Specialization specialization) {
        Button button = new Button("Delete");
        button.addClickListener(x -> {

            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Delete Specialization");
            dialog.setText("You wanna delete this specialization?");

            Button delButton = new Button("Delete", VaadinIcon.TRASH.create());
            delButton.addClickListener(e -> {

                try {
                    specializationService.delete(specialization);
                    dialog.close();
                    grid.setItems(specializationService.findAll());
                }
                catch (org.springframework.dao.DataIntegrityViolationException error){
                    dialog.close();
                    ConfirmDialog confirmDialog = new ConfirmDialog("Warning!",
                            "You cannot delete this specialization because he have Doctor", "OK", this::onOK);
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


}
