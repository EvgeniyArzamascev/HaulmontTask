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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import project.Model.Priority;
import project.Service.PriorityService;

import java.util.Optional;

@Route("Priority")
@CssImport("./styles/style.css")
public class PriorityPage extends AppLayout {

    Grid<Priority> grid = new Grid<>(Priority.class);
    PriorityService priorityService;

    public PriorityPage(PriorityService priorityService){
        this.priorityService = priorityService;
        createNavbar();
        createDrawer();
        createContent();
    }

    private void createNavbar() {
        H3 head = new H3("Priority Page");
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
        layout.add(newPriority(),configGrid());
        setContent(layout);
    }

    private Button newPriority() {
        Button button = new Button("New Priority", VaadinIcon.SMILEY_O.create());
        button.addClickListener(x -> {
            Dialog dialog = new Dialog();
            Label label = new Label("Add new Priority");
            label.addClassName("label");
            TextField name = new TextField("Name");
            Button addButton = new Button("Add", VaadinIcon.CHECK_CIRCLE.create());
            Button backButton = new Button("Cancel", VaadinIcon.REPLY.create());
            backButton.addClickListener(backEvent -> {
                dialog.close();
            });
            VerticalLayout layout = new VerticalLayout();
            HorizontalLayout horlayout = new HorizontalLayout();
            horlayout.add(backButton, addButton);
            layout.add(label, name, horlayout);
            dialog.add(layout);
            dialog.open();

            // validation
            Binder<Priority> binder = new Binder<>(Priority.class);
            binder.forField(name)
                    .asRequired("Not Empty")
                    .bind(Priority::getName, Priority::setName);


            addButton.addClickListener(addEvent -> {
                if (binder.validate().isOk()) {
                    Priority priority = new Priority();
                    priority.setName(name.getValue());
                    priorityService.save(priority);
                    grid.setItems(priorityService.findAll());
                    dialog.close();

                    Notification notification = new Notification("New priority has been created", 1500);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();
                }
            });
        });
        return button;
    }

    private Grid configGrid() {
        grid.setColumns("id", "name");
        grid.addComponentColumn(this::EditButton);
        grid.addComponentColumn(this::DeleteButton);
        grid.setItems(priorityService.findAll());
        return  grid;
    }

    private Button DeleteButton(Priority priority) {
        Button button = new Button("Delete");
        button.addClickListener(x -> {
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Delete Priority");
            dialog.setText("You wanna delete this priority?");

            Button delButton = new Button("Delete", VaadinIcon.TRASH.create());
            delButton.addClickListener(e -> {
                priorityService.delete(priority);
                dialog.close();
                grid.setItems(priorityService.findAll());
            });
            dialog.setConfirmButton(delButton);
            dialog.setCancelButton("Cancel", this::onCancel);
            dialog.open();
        });
        return button;
    }

    private void onCancel(ConfirmDialog.CancelEvent cancelEvent) {}

    private Button EditButton(Priority priority) {
        Button button = new Button("Edit");
        button.addClickListener(x -> {
            Dialog dialog = new Dialog();
            Label label = new Label("Edit priority");
            label.addClassName("label");
            TextField Name = new TextField("Name");

            Optional<Priority> Info = priorityService.findById(priority.getId()); // запонить значениями
            Info.ifPresent(y ->
                    {
                        Name.setValue(y.getName());
                    }
            );

            Button update = new Button("Update", VaadinIcon.FILE_REFRESH.create());
            Button backButton = new Button("Cancel", VaadinIcon.REPLY.create());
            backButton.addClickListener(backEvent -> {
                dialog.close();
            });

            VerticalLayout layout = new VerticalLayout();
            HorizontalLayout horlayout = new HorizontalLayout();
            horlayout.add(backButton, update);
            layout.add(label, Name, horlayout);
            dialog.add(layout);
            dialog.open();

            // validation
            Binder<Priority> binder = new Binder<>(Priority.class);
            binder.forField(Name)
                    .withValidator(value -> value.length() > 1, "Not Empty")
                    .bind(Priority::getName, Priority::setName);

            update.addClickListener(u -> {
                if (binder.validate().isOk()) {
                    Priority newPriority = new Priority();
                    newPriority.setId(priority.getId());
                    newPriority.setName(Name.getValue());
                    priorityService.save(newPriority);
                    dialog.close();
                    grid.setItems(priorityService.findAll());
                }
            });
        });

        return button;
    }

}
