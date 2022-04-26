package TarsinApp;

import TarsinApp.entity.Appointment;
import TarsinApp.entity.Client;
import TarsinApp.repository.AppointmentRepository;
import TarsinApp.rest.AppointmentController;
import TarsinApp.rest.ClientController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.w3c.dom.Text;

import java.util.Date;

@SpringBootApplication
public class FinalProjectApplication extends Application {

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void init() {
        SpringApplication.run(getClass()).getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("TARSIN Programari");

        Client buttonClient = new Client();
        Appointment appointmentClient = new Appointment();

        AppointmentController appointmentController = new AppointmentController();
        ClientController clientController = new ClientController();


       ObservableList<Appointment> observableList = FXCollections.observableArrayList(appointmentController.getAllAppointments());
       ListView<Appointment> listView = new ListView<>();


        Button addAppointmentButton = new Button("Aaduga Programare");
        Button deleteAppointmentButton = new Button("Sterge Programare");
        Button searchAppointmentButton = new Button("Cauta");

        TextField nameTextField = new TextField();
        TextField phoneTextField = new TextField();
        TextField goFromTextField = new TextField();
        TextField goToTextField = new TextField();
        DatePicker dateGoingDatePicker = new DatePicker();
        DatePicker dateComingDatePicker = new DatePicker();
        DatePicker searchAppointmentsByDatePicker = new DatePicker();
        Label nameLabel = new Label("Nume");
        Label phoneLabel = new Label("Telefon");
        Label goFromLabel = new Label("Plecare din");
        Label goToLabel = new Label("Destinatie");
        Label searchAppointmentLabel = new Label("Vezi programarile pentru data de:");
        Label goingDateLabel = new Label("Data plecare");
        Label comingDateLabel = new Label("Data intorcere OPTIONAL");


        GridPane gridPane = new GridPane();

        gridPane.setVgap(0);
        gridPane.setHgap(40);
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameTextField, 0, 1);
        gridPane.add(phoneLabel, 0, 2);
        gridPane.add(phoneTextField, 0, 3);
        gridPane.add(goFromLabel, 0, 4);
        gridPane.add(goFromTextField, 0, 5);
        gridPane.add(goToLabel, 0, 6);
        gridPane.add(goToTextField, 0, 7);

        gridPane.add(searchAppointmentLabel, 3, 0);
        gridPane.add(searchAppointmentsByDatePicker, 3, 1);
        gridPane.add(searchAppointmentButton, 3, 4);

        gridPane.add(goingDateLabel, 1, 0);
        gridPane.add(dateGoingDatePicker, 1, 1);
        gridPane.add(addAppointmentButton, 1, 4);

        gridPane.add(comingDateLabel, 2, 0);
        gridPane.add(dateComingDatePicker, 2, 1);
        gridPane.add(deleteAppointmentButton, 2, 4);

        gridPane.add(listView,1,9);


        addAppointmentButton.setOnAction(x -> {
            if (nameTextField != null && goFromTextField != null && phoneTextField != null && goToTextField != null) {

                buttonClient.setFullName(nameTextField.getText());
                buttonClient.setPhoneNumber(phoneTextField.getText());
                appointmentClient.setClient(buttonClient);
                appointmentClient.setDateComing(dateComingDatePicker.getValue());
                appointmentClient.setDateGoing(dateGoingDatePicker.getValue());
                appointmentClient.setStartFrom(goFromTextField.getText());
                appointmentClient.setGoTo(goToTextField.getText());
                buttonClient.setAppointment(appointmentClient);

                clientController.saveClient(buttonClient);
            } else try {
                throw new Exception("Completati toate campurile");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });




        Scene scene = new Scene(gridPane, 800, 500);
        stage.setScene(scene);
        stage.show();


    }
}
