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
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import project.Model.Person;
import project.Service.PersonService;

import java.util.Optional;

@Route("Person")
@CssImport("./styles/style.css")
public class PersonPage extends AppLayout {

    Grid<Person> grid = new Grid<>(Person.class);
    PersonService personService;

    public PersonPage(PersonService personService){
        this.personService = personService;
        createNavbar();
        createDrawer();
        createContent();
    }

    private void createNavbar() {
        H3 head = new H3("Person Page");
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
        layout.add(newPerson(),configGrid());
        setContent(layout);
    }

    private Button newPerson() {
        Button button = new Button("New Person", VaadinIcon.SMILEY_O.create());
        button.addClickListener(x -> {

            Dialog dialog = new Dialog();
            Label label = new Label("Add new Person");
            label.addClassName("label");

            TextField name = new TextField("Name");
            TextField surname = new TextField("SurName");
            TextField middlename = new TextField("Middle Name");

            TextField phone = new TextField("Phone");
            phone.setPattern("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
            phone.setPlaceholder("+7(XXX)XXX-XX-XX");

            VerticalLayout layout = new VerticalLayout();
            HorizontalLayout horLayout = new HorizontalLayout();
            Button addButton = new Button("Add", VaadinIcon.CHECK_CIRCLE.create());
            Button backButton = new Button("Cancel", VaadinIcon.REPLY.create());
            horLayout.add(backButton, addButton);
            layout.add(label, surname, name, middlename, phone, horLayout);
            dialog.add(layout);
            dialog.open();

            // validation
            Binder<Person> binder = new Binder<>(Person.class);

            binder.forField(name)
                    .asRequired("Not Empty")
                    .bind(Person::getName, Person::setName);
            binder.forField(surname)
                    .asRequired("Not Empty")
                    .bind(Person::getSurName, Person::setSurName);
            binder.forField(middlename)
                    .asRequired("Not Empty")
                    .bind(Person::getMiddleName, Person::setMiddleName);
            binder.forField(phone)
                    .asRequired("Not Empty")
                    .withValidator(new RegexpValidator("Wrong phone number",
                            "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$"))
                    .bind(Person::getPhone, Person::setPhone);

            addButton.addClickListener(addEvent -> {
                if (binder.validate().isOk()) {
                    Person newPerson = new Person();
                    newPerson.setSurName(surname.getValue());
                    newPerson.setName(name.getValue());
                    newPerson.setMiddleName(middlename.getValue());
                    newPerson.setPhone(phone.getValue());

                    personService.save(newPerson);
                    grid.setItems(personService.findAll());
                    dialog.close();

                    Notification notification = new Notification("New person has been created", 1500);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();
                }
            });
            backButton.addClickListener(backEvent -> {
                dialog.close();
            });
        });
        return button;
    }

    private Grid configGrid() {
        grid.setColumns("id", "surName", "name", "middleName", "phone");
        grid.addComponentColumn(this::editButton);
        grid.addComponentColumn(this::deleteButton);
        grid.setItems(personService.findAll());
        grid.getColumns().forEach(x -> x.setAutoWidth(true));
        return grid;
    }

    private Button editButton(Person person) {
        Button button = new Button("Edit");
        button.addClickListener(x -> {
            Dialog dialog = new Dialog();
            Label label = new Label("Edit Person");
            label.addClassName("label");
            TextField name = new TextField("Name");
            TextField surname = new TextField("SurName");
            TextField middlename = new TextField("Middle Name");
            TextField phone = new TextField("Phone");

            Optional<Person> info = personService.findById(person.getId());
            info.ifPresent(y ->
                    {
                        name.setValue(y.getName());
                        surname.setValue(y.getSurName());
                        middlename.setValue(y.getMiddleName());
                        phone.setValue(y.getPhone());
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
            layout.add(label, surname, name, middlename, phone, horLayout);
            dialog.add(layout);
            dialog.open();

            // validation
            Binder<Person> binder = new Binder<>(Person.class);

            binder.forField(name)
                    .asRequired("Not Empty")
                    .bind(Person::getName, Person::setName);
            binder.forField(surname)
                    .asRequired("Not Empty")
                    .bind(Person::getSurName, Person::setSurName);
            binder.forField(middlename)
                    .asRequired("Not Empty")
                    .bind(Person::getMiddleName, Person::setMiddleName);
            binder.forField(phone)
                    .asRequired("Not Empty")
                    .withValidator(new RegexpValidator("Wrong phone number",
                            "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$"))
                    .bind(Person::getPhone, Person::setPhone);

            update.addClickListener(u -> {
                if (binder.validate().isOk()) {
                    Person newPerson = new Person();
                    newPerson.setId(person.getId());
                    newPerson.setName(name.getValue());
                    newPerson.setSurName(surname.getValue());
                    newPerson.setMiddleName(middlename.getValue());
                    newPerson.setPhone(phone.getValue());

                    personService.save(newPerson);
                    dialog.close();
                    grid.setItems(personService.findAll());
                }
            });
        });
        return button;
    }

    private Button deleteButton(Person person) {
        Button button = new Button("Delete");
        button.addClickListener(x -> {
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Delete Person");
            dialog.setText("You wanna delete this person?");

            Button delButton = new Button("Delete", VaadinIcon.TRASH.create());
            delButton.addClickListener(e -> {

                try {
                    personService.delete(person);
                    dialog.close();
                    grid.setItems(personService.findAll());
                } catch (org.springframework.dao.DataIntegrityViolationException error) {
                    dialog.close();
                    ConfirmDialog confirmDialog = new ConfirmDialog("Warning!",
                            "You cannot delete this person because he have Recipe", "OK", this::onOK);
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
