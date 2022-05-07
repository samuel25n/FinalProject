package TarsinApp;

import TarsinApp.entity.Appointment;
import TarsinApp.entity.Client;
import TarsinApp.entity.User;
import TarsinApp.repository.AppointmentRepository;
import TarsinApp.repository.ClientRepository;
import TarsinApp.repository.UserRepository;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@EnableScheduling
@SpringBootApplication
public class FinalProjectApplication extends Application {

    static AppointmentRepository appointmentRepository;
    static ClientRepository clientRepository;
    static UserRepository userRepository;


    public static void main(String[] args) {
        ConfigurableApplicationContext appContext = SpringApplication.run(FinalProjectApplication.class, args);
        appointmentRepository = appContext.getBean(AppointmentRepository.class);
        clientRepository = appContext.getBean(ClientRepository.class);
        userRepository = appContext.getBean(UserRepository.class);
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("TARSIN Programari");


        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(10, 10);

        LocalDate today = LocalDate.now();

        Optional<Appointment> optionalAppointment = appointmentRepository.findAll().stream().findFirst();
        if (optionalAppointment.isPresent()) {
            List<Appointment> appointmentList = new ArrayList<>(appointmentRepository.findAll());

            appointmentList.forEach(appointment -> {

                if (appointment.getDateGoing().isBefore(today)) {
                    appointmentRepository.deleteById(appointment.getId());
                }
            });
        }

        //---------- Creare lista vizibila in program

        ObservableList<Appointment> observableList = FXCollections.observableArrayList(appointmentRepository.findAll());
        ListView<Appointment> listView = new ListView<>(observableList);
        listView.setPrefWidth(450);


        //---------------------


        //------------------------

        Button addAppointmentButton = new Button("Aaduga Programare");
        Button deleteAppointmentButton = new Button("Sterge Programare");
        Button searchAppointmentButton = new Button("Cauta");
        Button updateAppointmentButton = new Button("Modifica");

        Button exportButton = new Button("Export as CSV");

        Button showAllAppointmentsButton = new Button("Arata toate");


        //---

        Button toAddingButton = new Button("Adauga programari");
        toAddingButton.setAlignment(Pos.CENTER);
        Button toSearchAppointmentsButton = new Button("Vezi programari");
        toSearchAppointmentsButton.setAlignment(Pos.CENTER);


        Label searchByDATELabel = new Label("Cauta dupa data plecarii");
        Label searchByGoingLocationLabel = new Label("Cauta dupa destinatie");

        //---

        Button backToMainWindowButton = new Button("Inapoi");
        backToMainWindowButton.autosize();
        backToMainWindowButton.setStyle("-fx-background-color:#F580A2");

        AnchorPane.setBottomAnchor(backToMainWindowButton, 10.0);
        AnchorPane.setRightAnchor(backToMainWindowButton, 10.0);

        Button backToMainWindowButton1 = new Button("Inapoi");
        backToMainWindowButton1.autosize();
        backToMainWindowButton1.setStyle("-fx-background-color:#F580A2");

        AnchorPane.setBottomAnchor(backToMainWindowButton1, 10.0);
        AnchorPane.setRightAnchor(backToMainWindowButton1, 10.0);

        TextField nameTextField = new TextField();
        TextField phoneTextField = new TextField();
        TextField goFromTextField = new TextField();
        TextField goToTextField = new TextField();
        DatePicker dateGoingDatePicker = new DatePicker();
        DatePicker dateComingDatePicker = new DatePicker();
        DatePicker searchAppointmentsByDatePicker = new DatePicker();
        Label nameLabel = new Label("*Nume");
        Label phoneLabel = new Label("*Telefon");
        Label goFromLabel = new Label("*Plecare din");
        Label goToLabel = new Label("*Destinatie");
        Label searchAppointmentLabel = new Label("Vezi programarile pentru data de:");
        Label goingDateLabel = new Label("*Data plecare");
        Label comingDateLabel = new Label("Data intorcere OPTIONAL");

        Label principalTitleLabel = new Label("TARSIN");
        principalTitleLabel.setFont(new Font("Elephant", 40));
        principalTitleLabel.setTextFill(Color.web("#C70039"));

        Label blankLabel = new Label("");
        Label blankLabel2 = new Label("");
        Label blankLabel3 = new Label("");
        Label deleteLabelInfo = new Label("Pentru stergere/editare, selectati client");


        //--------- LOGIN

        Label loginUSERNAMElabel = new Label("Nume utilizator");
        TextField loginUSERNAMEtextField = new TextField();
        Label loginPASSWORDlabel = new Label("Parola");
        PasswordField loginPASSWORDtextField = new PasswordField();


        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");


        GridPane loginGridPane = new GridPane();


        loginGridPane.setPadding(new Insets(40, 40, 40, 40));
        loginGridPane.add(loginUSERNAMElabel, 0, 2);
        loginGridPane.add(loginUSERNAMEtextField, 0, 3);
        loginGridPane.add(loginPASSWORDlabel, 0, 4);
        loginGridPane.add(loginPASSWORDtextField, 0, 5);
        loginGridPane.add(loginButton, 0, 6);
        loginGridPane.add(registerButton, 0, 7);
        loginButton.autosize();


        loginGridPane.add(blankLabel3, 0, 1);
        loginGridPane.setPrefWidth(900);
        loginGridPane.setPrefHeight(500);
        loginGridPane.setAlignment(Pos.CENTER);


        List<User> userList = new ArrayList<>(userRepository.findAll());
//


        //---------- REGISTER

        GridPane registerGridPane = new GridPane();
        loginGridPane.setPadding(new Insets(40, 40, 40, 40));


        Label usernameRegisterLabel = new Label("Username:");
        Label passwordRegisterLabel = new Label("Parola:");


        TextField usernameRegisterTXTField = new TextField();
        PasswordField passwordRegisterTXTField = new PasswordField();

        Button registerREGISTERButton = new Button("Inregistreaza-te");


        registerGridPane.add(usernameRegisterLabel, 0, 2);
        registerGridPane.add(usernameRegisterTXTField, 0, 3);
        registerGridPane.add(passwordRegisterLabel, 0, 4);
        registerGridPane.add(passwordRegisterTXTField, 0, 5);
        registerGridPane.add(registerREGISTERButton, 0, 6);


        registerGridPane.setPrefWidth(900);
        registerGridPane.setPrefHeight(500);
        registerGridPane.setAlignment(Pos.CENTER);


        Scene registerScene = new Scene(registerGridPane);


        //-----------------------------------------------------------------------------------------

        ObservableList<Appointment> searchAPPsObservableList = FXCollections.observableArrayList(appointmentRepository.findAll());
        ListView<Appointment> seeAPPsLISTVIEW = new ListView<>(searchAPPsObservableList);
        seeAPPsLISTVIEW.setPrefWidth(450);

        //------------ Asezarea celor create mai sus in pagina deschisa de aplicatie --------------


        GridPane addingAppointmentGridpane = new GridPane();

        addingAppointmentGridpane.setVgap(1);
        addingAppointmentGridpane.setHgap(40);

        addingAppointmentGridpane.setPadding(new Insets(20, 20, 20, 20));

        addingAppointmentGridpane.add(nameLabel, 0, 0);
        addingAppointmentGridpane.add(nameTextField, 0, 1);
        addingAppointmentGridpane.add(phoneLabel, 0, 2);
        addingAppointmentGridpane.add(phoneTextField, 0, 3);
        addingAppointmentGridpane.add(goFromLabel, 0, 4);
        addingAppointmentGridpane.add(goFromTextField, 0, 5);
        addingAppointmentGridpane.add(goToLabel, 0, 6);
        addingAppointmentGridpane.add(goToTextField, 0, 7);


        addingAppointmentGridpane.add(goingDateLabel, 1, 0);
        addingAppointmentGridpane.add(dateGoingDatePicker, 1, 1);
        addingAppointmentGridpane.add(addAppointmentButton, 1, 4);

        addingAppointmentGridpane.add(comingDateLabel, 2, 0);
        addingAppointmentGridpane.add(dateComingDatePicker, 2, 1);

        addingAppointmentGridpane.add(blankLabel2, 3, 6);// gol
        addingAppointmentGridpane.add(deleteLabelInfo, 3, 7);
        addingAppointmentGridpane.add(deleteAppointmentButton, 3, 9);

        addingAppointmentGridpane.add(blankLabel, 0, 8);  // gol

        addingAppointmentGridpane.add(listView, 1, 8, 2, 3);
        addingAppointmentGridpane.add(updateAppointmentButton,3,8);
        addingAppointmentGridpane.add(backToMainWindowButton1, 3, 10);


        //---------------------------------------------------------------------
        GridPane principalWindowGridPane = new GridPane();
        Label blankLabel4 = new Label("");

        principalWindowGridPane.setPadding(new Insets(40, 40, 40, 40));
        principalWindowGridPane.add(toAddingButton, 0, 2);
        principalWindowGridPane.add(toSearchAppointmentsButton, 0, 3);
        toSearchAppointmentsButton.setPrefHeight(30);
        toSearchAppointmentsButton.setPrefWidth(200);
        toAddingButton.setPrefWidth(200);
        toAddingButton.setPrefHeight(30);
        principalWindowGridPane.add(blankLabel4, 0, 1);
        principalWindowGridPane.setPrefWidth(900);
        principalWindowGridPane.setPrefHeight(500);
        principalWindowGridPane.setAlignment(Pos.CENTER);


        principalWindowGridPane.add(principalTitleLabel, 0, 0);

        Scene principalWindowScene = new Scene(principalWindowGridPane);


        GridPane seeAppointmentsGridPane = new GridPane();
        seeAppointmentsGridPane.setVgap(1);
        seeAppointmentsGridPane.setHgap(10);

        seeAppointmentsGridPane.setPadding(new Insets(20, 20, 20, 20));

        seeAppointmentsGridPane.add(searchAppointmentLabel, 0, 0);
        seeAppointmentsGridPane.add(searchAppointmentsByDatePicker, 1, 0);
        seeAppointmentsGridPane.add(searchAppointmentButton, 3, 0);

        seeAppointmentsGridPane.add(seeAPPsLISTVIEW, 1, 1);
        seeAppointmentsGridPane.add(backToMainWindowButton, 3, 3);
        seeAppointmentsGridPane.add(exportButton, 0, 1);

        seeAppointmentsGridPane.add(showAllAppointmentsButton, 3, 1);

        Scene searchAppointmentScene = new Scene(seeAppointmentsGridPane, 900, 500);

        Scene loginScene = new Scene(loginGridPane, 900, 500);


        Scene AddingAppointmentScene = new Scene(addingAppointmentGridpane, 900, 500);
        stage.setScene(loginScene);
        stage.show();


        //--------------- Functionalitati butoane -----------------------------

        toAddingButton.setOnAction(toAddingButtonAction -> {
            stage.setScene(AddingAppointmentScene);
        });


        toSearchAppointmentsButton.setOnAction(seeAppointmentsButtonAction -> {
            int appointmentsCOUNT = (int) appointmentRepository.findAll().stream().count();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pana acum sunt " + appointmentsCOUNT + " persoane programate");
            alert.show();
            stage.setScene(searchAppointmentScene);
        });

        backToMainWindowButton.setOnAction(goBackButtonAction -> {
            stage.setScene(principalWindowScene);
        });
        backToMainWindowButton1.setOnAction(goBackButtonAction -> {
            stage.setScene(principalWindowScene);
        });


        showAllAppointmentsButton.setOnAction(sh -> {
            searchAPPsObservableList.removeAll();
            seeAPPsLISTVIEW.setItems(searchAPPsObservableList);
            searchAPPsObservableList.setAll(appointmentRepository.findAll());
            seeAPPsLISTVIEW.setItems(searchAPPsObservableList);
        });

        registerButton.setOnAction(rr -> {
            stage.setScene(registerScene);
        });

        registerREGISTERButton.setOnAction(rgr -> {

            try {

                if (passwordRegisterTXTField.getText().isEmpty() || usernameRegisterTXTField.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Completati toate campurile");
                    alert.show();
                } else {
                    User user = new User();
                    user.setUsername(usernameRegisterTXTField.getText());
                    user.setPassword(passwordRegisterTXTField.getText());
                    try {
                        userList.add(user);
                        userRepository.save(user);
                        stage.setScene(loginScene);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Utilizator adaugat cu succes !");
                        alert.show();
                    }catch (Exception e){
                        Alert alert = new Alert(Alert.AlertType.ERROR,"Nume de utilizator deja folosit");
                        alert.show();
                        e.printStackTrace();
                    }
                    usernameRegisterTXTField.clear();
                    passwordRegisterTXTField.clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        loginButton.setOnAction(lgl -> {
            AtomicBoolean found = new AtomicBoolean(false);
            try {
                User user = new User();
                user.setUsername(loginUSERNAMEtextField.getText());
                user.setPassword(loginPASSWORDtextField.getText());
                userList.forEach(user1 -> {
                    if (user1.getUsername().equalsIgnoreCase(user.getUsername()) && user1.getPassword().equalsIgnoreCase(user.getPassword())) {
                        found.set(true);
                    }
                });
                if (found.get()) {
                    stage.setScene(principalWindowScene);

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Nume utilizator sau parola gresita");
                    alert.show();
                }
                loginUSERNAMEtextField.clear();
                loginPASSWORDtextField.clear();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        // Buton adaugare Client

        addAppointmentButton.setOnAction(x -> {

            try {
                if (nameTextField.getText().isEmpty() || goFromTextField.getText().isEmpty() || phoneTextField.getText().isEmpty()
                        || goToTextField.getText().isEmpty() || dateGoingDatePicker.getValue() == null) {
                    try {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Compleltati toate campurile obligatorii");
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

                    listView.getItems().add(appointment);

                    clientRepository.save(client);

                    nameTextField.clear();
                    phoneTextField.clear();
                    goFromTextField.clear();
                    goToTextField.clear();
                    dateGoingDatePicker.getEditor().clear();
                    dateComingDatePicker.getEditor().clear();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Programarea a fost salvata");
                    alert.show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //Buton Stergere Client

        deleteAppointmentButton.setOnAction(x -> {
            Client client = new Client();

            try {
                try {
                    listView.setOnMouseClicked(mouseEvent -> {
                        listView.getSelectionModel().getSelectedItem().setClient(client);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (client.getFullName() != null) {
                    try {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Selectati un client");
                        alert.show();
                        throw new Exception("Client neselectat");
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Selectati un client");
                        alert.show();
                        e.printStackTrace();

                    }
                } else {

                    appointmentRepository.deleteById(listView.getSelectionModel().getSelectedItem().getId());

                    observableList.remove(listView.getSelectionModel().getSelectedItem());
                    listView.setItems(observableList);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        listView.setOnMouseClicked(msevnt ->{

            dateGoingDatePicker.getEditor().setText(listView.getSelectionModel().getSelectedItem().getDateGoing().toString());
            dateComingDatePicker.setValue(listView.getSelectionModel().getSelectedItem().getDateComing());
            nameTextField.setText(listView.getSelectionModel().getSelectedItem().getClient().getFullName());
            phoneTextField.setText(listView.getSelectionModel().getSelectedItem().getClient().getPhoneNumber());
            goFromTextField.setText(listView.getSelectionModel().getSelectedItem().getClient().getAppointment().getStartFrom());
            goToTextField.setText(listView.getSelectionModel().getSelectedItem().getClient().getAppointment().getGoTo());
        });


        updateAppointmentButton.setOnAction(aaaa ->{
            if (listView.getSelectionModel().getSelectedItem() == null){
                Alert alert = new Alert(Alert.AlertType.WARNING,"Selectati un client");
                alert.show();
            }else
            {
                appointmentRepository.deleteById(listView.getSelectionModel().getSelectedItem().getId());
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
                clientRepository.save(client);
                observableList.setAll(FXCollections.observableArrayList(appointmentRepository.findAll()));

                listView.setItems(observableList);



                nameTextField.clear();
                phoneTextField.clear();
                goFromTextField.clear();
                goToTextField.clear();
                dateGoingDatePicker.getEditor().clear();
                dateComingDatePicker.getEditor().clear();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Programarea a fost modificata");
                alert.show();

            }
        });




        //---------------------- Buton cautare programari pentru o anumita data ---------------------


        searchAppointmentButton.setOnAction(searchMethod -> {

            searchAPPsObservableList.removeAll();
            seeAPPsLISTVIEW.setItems(searchAPPsObservableList);
            searchAPPsObservableList.setAll(appointmentRepository.findByDateGoing(searchAppointmentsByDatePicker.getValue()));
            seeAPPsLISTVIEW.setItems(searchAPPsObservableList);


        });


        exportButton.setOnAction(exportButtonAction -> {

            try {
                Workbook workbook = new HSSFWorkbook();
                Sheet spreadsheet = workbook.createSheet("sample");

                Row row = spreadsheet.createRow(0);


            } catch (Exception e) {
                e.printStackTrace();
            }

        });


        //--------------------------------------------------------------------------


    }
}
