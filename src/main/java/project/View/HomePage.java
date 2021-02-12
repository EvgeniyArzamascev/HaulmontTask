package project.View;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import org.vaadin.alejandro.PdfBrowserViewer;


@Route("")
@CssImport("./styles/style.css")
public class HomePage extends AppLayout {

    public HomePage(){
        createNavbar();   // Заголовок страницы (верхняя часть)
        createDrawer();   // Боковая панель
        createContent();  // Рабочая область

       /* String url = "jdbc:hsqldb:file:HSQLDB/DataBase";
        String username = "SA";
        String password = "";
        System.out.println("Connecting...");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }*/

    }

    private void createNavbar() {
        H3 head = new H3("Home Page");
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
        Label title = new Label("Haulmont Test Task");
        title.addClassName("homeLabel");

        StreamResource streamResource = new StreamResource(
                "HaulmontTask.PDF", () -> getClass().getResourceAsStream("/static/HaulmontTask.PDF"));
        PdfBrowserViewer pdf = new PdfBrowserViewer(streamResource);
        pdf.setHeight("700px");

        Label imageLabel = new Label("Logic model DataBase");
        imageLabel.addClassName("homeLabel");

        StreamResource resourceImage = new StreamResource(
                "LogicModelDB.png", () -> getClass().getResourceAsStream("/static/LogicModelDB.png"));
        Image image = new Image(resourceImage, "Model");
        image.addClassName("image");

        layout.add(title, pdf, imageLabel, image);
        setContent(layout);
    }
}
