package TarsinApp;

import TarsinApp.Exceptions.EmptyFields;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileOutputStream;
import java.time.LocalDate;
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

    // METODA DE AUTOSIZE pentru celulele din fisierul XLS generat de butonul Export
    public void autoSizeColumns(Workbook workbook) {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            if (sheet.getPhysicalNumberOfRows() > 0) {
                Row row = sheet.getRow(sheet.getFirstRowNum());
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    sheet.autoSizeColumn(columnIndex);
                }
            }
        }
    }

    // METODA de trimitere Email cu codul de verificare pentru inregistrarea unui nou User
    public static void sendMessageCode(int code, String email) {
        String sendTo = email;
        String sendFrom = "itschooltesting@gmail.com";
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("itschooltesting@gmail.com", "ItSchool123");
            }
        });

        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sendFrom));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
            message.setSubject("Cod de activare");
            message.setText("Acesta este codul de activare pentru aplicatia Tarsin Programari: " + code + " Va rugam sa il introduceti in aplicatie");
            System.out.println("sending...");
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException m) {
            m.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("TARSIN Programari");


        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(10, 10);

        LocalDate today = LocalDate.now();


        // Stergere programari vechi la pornirea programului
        Optional<Appointment> optionalAppointment = appointmentRepository.findAll().stream().findFirst();
        if (optionalAppointment.isPresent()) {
            List<Appointment> appointmentList = new ArrayList<>(appointmentRepository.findAll());

            appointmentList.forEach(appointment -> {

                if (appointment.getDateGoing().isBefore(today)) {
                    appointmentRepository.deleteById(appointment.getId());
                }
            });
        }

        //  Lista vizibila pe pagina unde se adauga programari

        ObservableList<Appointment> observableList = FXCollections.observableArrayList(appointmentRepository.findAll());
        ListView<Appointment> listView = new ListView<>(observableList);
        listView.setPrefWidth(450);


        // Lista vizibila pe pagina unde cautam programari
        ObservableList<Appointment> searchAPPsObservableList = FXCollections.observableArrayList(appointmentRepository.findAll());
        ListView<Appointment> seeAPPsLISTVIEW = new ListView<>(searchAPPsObservableList);
        seeAPPsLISTVIEW.setPrefWidth(450);


        //---------------------
        List<User> userList = new ArrayList<>(userRepository.findAll());

        //---------Butoane
        Button finishRegisterButton = new Button("Finalizare inregistrare");
        Button registerREGISTERButton = new Button("Inregistreaza-te");
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        Button backtologinButton = new Button("Inapoi");
        Button addAppointmentButton = new Button("Aaduga Programare");
        Button deleteAppointmentButton = new Button("Sterge Programare");
        Button searchAppointmentButton = new Button("Cauta");
        Button updateAppointmentButton = new Button("Modifica");
        Button exportButton = new Button("Export as CSV");
        Button showAllAppointmentsButton = new Button("Arata toate");
        Button toAddingButton = new Button("Adauga programari");
        Button toSearchAppointmentsButton = new Button("Vezi programari");
        Button backToMainWindowButton = new Button("Inapoi");
        Button backToMainWindowButton1 = new Button("Inapoi");


        // Labels
        Label blankLabel4 = new Label("");
        Label usernameRegisterLabel = new Label("Username:");
        Label passwordRegisterLabel = new Label("Parola:");
        Label emailRegisterLabel = new Label("Email:");
        Label nameLabel = new Label("*Nume");
        Label phoneLabel = new Label("*Telefon");
        Label goFromLabel = new Label("*Plecare din");
        Label goToLabel = new Label("*Destinatie");
        Label searchAppointmentLabel = new Label("Vezi programarile pentru data de:");
        Label goingDateLabel = new Label("*Data plecare");
        Label comingDateLabel = new Label("Data intorcere OPTIONAL");
        Label principalTitleLabel = new Label("TARSIN");
        Label blankLabel = new Label("");
        Label blankLabel2 = new Label("");
        Label blankLabel3 = new Label("");
        Label deleteLabelInfo = new Label("Pentru stergere/editare, selectati client");
        Label loginUSERNAMElabel = new Label("Nume utilizator");
        Label loginPASSWORDlabel = new Label("Parola");
        Label codeLabelRegister = new Label("Introduceti mai jos codul primit prin email");


        // TEXTFIELDS

        TextField nameTextField = new TextField();
        TextField phoneTextField = new TextField();
        TextField goFromTextField = new TextField();
        TextField goToTextField = new TextField();
        TextField loginUSERNAMEtextField = new TextField();
        TextField emailRegisterTXTField = new TextField();
        TextField usernameRegisterTXTField = new TextField();
        TextField register2TXTFieldCODE = new TextField();


        //DATEPICKERS

        DatePicker dateGoingDatePicker = new DatePicker();
        DatePicker dateComingDatePicker = new DatePicker();
        DatePicker searchAppointmentsByDatePicker = new DatePicker();


        // GridPanes
        GridPane principalWindowGridPane = new GridPane();
        GridPane loginGridPane = new GridPane();
        GridPane registerGridPane = new GridPane();
        GridPane register2Gridpane = new GridPane();
        GridPane addingAppointmentGridpane = new GridPane();
        GridPane seeAppointmentsGridPane = new GridPane();

        // Scenes

        Scene searchAppointmentScene = new Scene(seeAppointmentsGridPane, 900, 500);
        Scene registerScene = new Scene(registerGridPane);
        Scene register2Scene = new Scene(register2Gridpane, 900, 500);
        Scene principalWindowScene = new Scene(principalWindowGridPane);
        Scene loginScene = new Scene(loginGridPane, 900, 500);
        Scene AddingAppointmentScene = new Scene(addingAppointmentGridpane, 900, 500);


        backToMainWindowButton1.autosize();
        backToMainWindowButton1.setStyle("-fx-background-color:#F580A2");
        backToMainWindowButton.autosize();
        backToMainWindowButton.setStyle("-fx-background-color:#F580A2");
        toAddingButton.setAlignment(Pos.CENTER);
        toSearchAppointmentsButton.setAlignment(Pos.CENTER);
        principalTitleLabel.setFont(new Font("Elephant", 40));
        principalTitleLabel.setTextFill(Color.web("#C70039"));

//        Label searchByGoingToLocationLabel = new Label("Cauta dupa destinatie");
//        TextField searchByGoingToLocationTXTFIELD = new TextField();


        AnchorPane.setBottomAnchor(backToMainWindowButton, 10.0);
        AnchorPane.setRightAnchor(backToMainWindowButton, 10.0);
        AnchorPane.setBottomAnchor(backToMainWindowButton1, 10.0);
        AnchorPane.setRightAnchor(backToMainWindowButton1, 10.0);


        //--------- LOGIN

        PasswordField loginPASSWORDtextField = new PasswordField();

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
        loginGridPane.setPadding(new Insets(40, 40, 40, 40));

        //---------- REGISTER


        PasswordField passwordRegisterTXTField = new PasswordField();

        registerGridPane.add(usernameRegisterLabel, 0, 2);
        registerGridPane.add(usernameRegisterTXTField, 0, 3);
        registerGridPane.add(passwordRegisterLabel, 0, 4);
        registerGridPane.add(passwordRegisterTXTField, 0, 5);
        registerGridPane.add(emailRegisterLabel, 0, 6);
        registerGridPane.add(emailRegisterTXTField, 0, 7);
        registerGridPane.add(registerREGISTERButton, 0, 8);
        registerGridPane.setPrefWidth(900);
        registerGridPane.setPrefHeight(500);
        registerGridPane.setAlignment(Pos.CENTER);


        //--------- register code


        finishRegisterButton.autosize();


        register2Gridpane.add(codeLabelRegister, 0, 0);
        register2Gridpane.add(register2TXTFieldCODE, 0, 1);
        register2Gridpane.add(finishRegisterButton, 0, 3);
        register2Gridpane.add(backtologinButton, 0, 4);

        register2Gridpane.setAlignment(Pos.CENTER);


        //-----------------------------------------------------------------------------------------


        //------------ Asezarea celor create mai sus in pagina deschisa de aplicatie --------------


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
        addingAppointmentGridpane.add(updateAppointmentButton, 3, 8);
        addingAppointmentGridpane.add(backToMainWindowButton1, 3, 10);


        //---------------------------------------------------------------------


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

        stage.setScene(loginScene);
        stage.show();


        //--------------- Functionalitati butoane -----------------------------

        // Buton catre pagina de inregistrare
        registerButton.setOnAction(rr -> stage.setScene(registerScene));
        // Buton catre login page
        backtologinButton.setOnAction(az -> stage.setScene(loginScene));
        // Buton catre pagina de adaugare programari
        toAddingButton.setOnAction(toAddingButtonAction -> stage.setScene(AddingAppointmentScene));
        // Buton catre meniul prinicpal
        backToMainWindowButton.setOnAction(goBackButtonAction -> stage.setScene(principalWindowScene));
        // Buton catre meniul prinicpal
        backToMainWindowButton1.setOnAction(goBackButtonAction -> stage.setScene(principalWindowScene));


        // Buton catre pagina de cautare programari
        toSearchAppointmentsButton.setOnAction(seeAppointmentsButtonAction -> {
            int appointmentsCOUNT = (int) appointmentRepository.findAll().stream().count();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pana acum sunt " + appointmentsCOUNT + " persoane programate");
            alert.show();
            searchAPPsObservableList.removeAll();
            seeAPPsLISTVIEW.setItems(searchAPPsObservableList);
            searchAPPsObservableList.setAll(appointmentRepository.findAll());
            seeAPPsLISTVIEW.setItems(searchAPPsObservableList);

            stage.setScene(searchAppointmentScene);
        });

        // Buton de afisare toate programarile valabile
        showAllAppointmentsButton.setOnAction(sh -> {
            searchAPPsObservableList.removeAll();
            seeAPPsLISTVIEW.setItems(searchAPPsObservableList);
            searchAPPsObservableList.setAll(appointmentRepository.findAll());
            seeAPPsLISTVIEW.setItems(searchAPPsObservableList);
        });


        // Button de inregistrare
        registerREGISTERButton.setOnAction(aasa -> {
            int ok=0;
            if (passwordRegisterTXTField.getText().isEmpty() || usernameRegisterTXTField.getText().isEmpty() || emailRegisterTXTField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Completati toate campurile");
                alert.show();
            } else {
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getUsername().equals(usernameRegisterTXTField.getText())) {
                        System.out.println("Username folosit");
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Username folosit. Alegeti alt username");
                        alert.show();
                        ok=1;
                    }

                }
                    if (ok==1) {
                        System.out.println("Username folosit");
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Username folosit. Alegeti alt username");
                        alert.show();
                    } else {


                        stage.setScene(register2Scene);
                        Random random = new Random();
                        int codeVerify = random.nextInt(9999);

                        sendMessageCode(codeVerify, emailRegisterTXTField.getText());

                        finishRegisterButton.setOnAction(rgr -> {
                            String codeVerifyString = String.valueOf(codeVerify);
                            try {

                                if (passwordRegisterTXTField.getText().isEmpty() || usernameRegisterTXTField.getText().isEmpty() || emailRegisterTXTField.getText().isEmpty()) {
                                    Alert alert = new Alert(Alert.AlertType.WARNING, "Completati toate campurile");
                                    alert.show();
                                } else {
                                    User user = new User();
                                    user.setUsername(usernameRegisterTXTField.getText());
                                    user.setPassword(passwordRegisterTXTField.getText());
                                    user.setEmail(emailRegisterTXTField.getText());


                                    if (register2TXTFieldCODE.getText().equals(codeVerifyString)) {
                                        try {
                                            userList.add(user);
                                            userRepository.save(user);
                                            stage.setScene(loginScene);
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Utilizator adaugat cu succes !");
                                            alert.show();
                                        } catch (Exception e) {
                                            Alert alert = new Alert(Alert.AlertType.ERROR, "Nume de utilizator deja folosit");
                                            alert.show();
                                            e.printStackTrace();
                                        }
                                        usernameRegisterTXTField.clear();
                                        passwordRegisterTXTField.clear();
                                        emailRegisterTXTField.clear();
                                        register2TXTFieldCODE.clear();
                                        stage.setScene(loginScene);
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.WARNING, "Cod invalid");
                                        alert.show();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        });
                    }

            }
        });

        // Buton logare
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
                        throw new EmptyFields("Campuri goale");
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
                    listView.setOnMouseClicked(mouseEvent -> listView.getSelectionModel().getSelectedItem().setClient(client));
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

                    nameTextField.clear();
                    phoneTextField.clear();
                    goFromTextField.clear();
                    goToTextField.clear();
                    dateGoingDatePicker.getEditor().clear();
                    dateComingDatePicker.getEditor().clear();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });


        // Buton update Client

        updateAppointmentButton.setOnAction(aaaa -> {
            if (listView.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Selectati un client");
                alert.show();
            } else {
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


        //Buton cautare dupa data de plecare
        searchAppointmentButton.setOnAction(searchMethod -> {

            searchAPPsObservableList.removeAll();
            seeAPPsLISTVIEW.setItems(searchAPPsObservableList);
            searchAPPsObservableList.setAll(appointmentRepository.findByDateGoing(searchAppointmentsByDatePicker.getValue()));
            seeAPPsLISTVIEW.setItems(searchAPPsObservableList);


        });

        // Buton export
        exportButton.setOnAction(exportButtonAction -> {

            try {
                Workbook workbook = new HSSFWorkbook();
                Sheet spreadsheet = workbook.createSheet("sample");

                Row row = spreadsheet.createRow(0);
                row.createCell(0).setCellValue("Nume");
                row.createCell(1).setCellValue("Telefon");
                row.createCell(2).setCellValue("Plecare din");
                row.createCell(3).setCellValue("Plecare catre");
                row.createCell(4).setCellValue("Data plecarii");
                row.createCell(5).setCellValue("Data intoarcerii");
                int i;
                for (i = 0; i < searchAPPsObservableList.size(); i++) {
                    row = spreadsheet.createRow(i + 1);

                    row.createCell(0).setCellValue(searchAPPsObservableList.get(i).getClient().getFullName());
                    row.createCell(1).setCellValue(searchAPPsObservableList.get(i).getClient().getPhoneNumber());
                    row.createCell(2).setCellValue(searchAPPsObservableList.get(i).getStartFrom());
                    row.createCell(3).setCellValue(searchAPPsObservableList.get(i).getGoTo());
                    row.createCell(4).setCellValue(searchAPPsObservableList.get(i).getDateGoing().toString());
                    if (searchAPPsObservableList.get(i).getDateComing() != null) {
                        row.createCell(5).setCellValue(searchAPPsObservableList.get(i).getDateComing().toString());
                    } else {
                        row.createCell(5).setCellValue("-");
                    }
                }

                autoSizeColumns(workbook);

                FileOutputStream fileOut = new FileOutputStream("Programari tarsin.xls");
                workbook.write(fileOut);
                fileOut.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Programarile au fost exportate. Verificati " +
                        "folderul unde este aplicatia. ATENTIE ! Dupa fiecare export, fisierul va fi rescris, va " +
                        "rugam printati sau salvati fisierul ca si copie daca doriti sa il pastrati");
                alert.show();


            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        // listview click pe event

        listView.setOnMouseClicked(msevnt -> {

            dateGoingDatePicker.getEditor().setText(listView.getSelectionModel().getSelectedItem().getDateGoing().toString());
            dateComingDatePicker.setValue(listView.getSelectionModel().getSelectedItem().getDateComing());
            nameTextField.setText(listView.getSelectionModel().getSelectedItem().getClient().getFullName());
            phoneTextField.setText(listView.getSelectionModel().getSelectedItem().getClient().getPhoneNumber());
            goFromTextField.setText(listView.getSelectionModel().getSelectedItem().getClient().getAppointment().getStartFrom());
            goToTextField.setText(listView.getSelectionModel().getSelectedItem().getClient().getAppointment().getGoTo());
        });


        //--------------------------------------------------------------------------


    }
}
