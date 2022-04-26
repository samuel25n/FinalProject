package TarsinApp.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@ToString
@Setter
@Getter

public class Client {

    @Id
    @GeneratedValue
    private int id;

    private String fullName;
    private String phoneNumber;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    public Appointment getAppointment() {
        return appointment;
    }

    //----------------------GETTERS AND SETTERS


//    public int getId() {
//        return id;
//    }
//
//    public String getFullName() {
//        return fullName;
//    }
//
//    public void setFullName(String fullName) {
//        this.fullName = fullName;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    public void setAppointment(Appointment appointment) {
//        this.appointment = appointment;
//    }

    //----------------------------------------
}
