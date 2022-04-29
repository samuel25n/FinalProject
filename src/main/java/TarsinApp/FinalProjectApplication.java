package TarsinApp;

import TarsinApp.entity.Appointment;
import TarsinApp.entity.Client;
import TarsinApp.repository.AppointmentRepository;
import TarsinApp.repository.ClientRepository;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Date;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@SpringBootApplication
public class FinalProjectApplication extends Application {

    static AppointmentRepository appointmentRepository;
    static ClientRepository clientRepository;


    public static void main(String[] args) {
        ConfigurableApplicationContext appContext = SpringApplication.run(FinalProjectApplication.class, args);
        appointmentRepository = appContext.getBean(AppointmentRepository.class);
        clientRepository = appContext.getBean(ClientRepository.class);
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("TARSIN Programari");


        //---------- Creare lista vizibila in program

        ObservableList<Appointment> observableList = FXCollections.observableArrayList(appointmentRepository.findAll());
        ListView<Appointment> listView = new ListView<>(observableList);

        //---------------------------------------------


        //----------- Creare butoane, textFields, etc.

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

        Label blankLabel = new Label("");
        Label blankLabel2 = new Label("");
        Label blankLabel3 = new Label("");
        Label deleteLabelInfo = new Label("Pentru stergere/editare, selectati client");

        //-----------------------------------------------------------------------------------------



        //------------ Asezarea celor create mai sus in pagina deschisa de aplicatie --------------


        GridPane gridPane = new GridPane();

        gridPane.setVgap(1);
        gridPane.setHgap(40);

        gridPane.setPadding(new Insets(20,20,20,20));

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

        gridPane.add(blankLabel2, 3, 6);// gol
        gridPane.add(deleteLabelInfo,3,7);
        gridPane.add(deleteAppointmentButton, 3, 8);

        gridPane.add(blankLabel,0,8);  // gol

        gridPane.add(listView,1,8,2,3);


        //---------------------------------------------------------------------


        //--------------- Functionalitati butoane -----------------------------



                        // Buton adaugare Client

        addAppointmentButton.setOnAction(x -> {
                    if (nameTextField.getText().isEmpty() || goFromTextField.getText().isEmpty() || phoneTextField.getText().isEmpty()
                            || goToTextField.getText().isEmpty() || dateGoingDatePicker.getValue() == null) {
                        try {
                            Alert alert = new Alert(Alert.AlertType.WARNING,"Compleltati toate campurile obligatorii");
                            alert.show();
                            throw new Exception("Campuri goale");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Client client = new Client();
                        Appointment appointment = new Appointment();

                        client.setFullName(nameTextField.getText());
                        client.setPhoneNumber(phoneTextField.getText());
                        appointment.setClient(client);
                        appointment.setDateComing(dateComingDatePicker.getValue());
                        appointment.setDateGoing(dateGoingDatePicker.getValue());
                        appointment.setStartFrom(goFromTextField.getText());
                        appointment.setGoTo(goToTextField.getText());
                        client.setAppointment(appointment);

                        observableList.add(appointment);
                        listView.getItems().add(appointment);

                        clientRepository.save(client);

                        nameTextField.clear();
                        phoneTextField.clear();
                        goFromTextField.clear();
                        goToTextField.clear();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Programarea a fost salvata");
                        alert.show();

                    }
                });

                    //Buton Stergere Client

        deleteAppointmentButton.setOnAction(x -> {
            if (nameTextField.getText().isEmpty() || goFromTextField.getText().isEmpty() || phoneTextField.getText().isEmpty()
                    || goToTextField.getText().isEmpty() || dateGoingDatePicker.getValue() == null) {
                try {
                    Alert alert = new Alert(Alert.AlertType.WARNING,"Selectati un client");
                    alert.show();
                    throw new Exception("Client neselectat");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else{
                Client client = new Client();
                Appointment appointment = new Appointment();

                client.setFullName(nameTextField.getText());
                client.setPhoneNumber(phoneTextField.getText());
                appointment.setClient(client);
                appointment.setDateComing(dateComingDatePicker.getValue());
                appointment.setDateGoing(dateGoingDatePicker.getValue());
                appointment.setStartFrom(goFromTextField.getText());
                appointment.setGoTo(goToTextField.getText());


                clientRepository.delete(client);

//to work at

            }

            //TO DO
            // Search Button

            searchAppointmentButton.setOnAction(searchMethod-> {

                
            });

            // Update Client
        });







        //--------------------------------------------------------------------------



        Scene scene = new Scene(gridPane, 800, 500);
        stage.setScene(scene);
        stage.show();


    }
}
