package TarsinApp.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString
@Setter
@Getter

public class Client {

    @Id
    @GeneratedValue
    private Integer id;

    private String fullName;
    private String phoneNumber;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "appointment_going_id")
    private Appointment appointment;



}
