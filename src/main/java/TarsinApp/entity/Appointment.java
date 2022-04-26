package TarsinApp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@ToString
@Getter
@Setter

public class Appointment {

    @Id
    @GeneratedValue
    private int id;
    private String goTo;
    private String startFrom;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate dateGoing;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate dateComing;

    @OneToOne
    @JoinColumn(name = "client_id")
    Client client;



    //-------------------------- GETTERS AND SETTERS

//    public int getId() {
//        return id;
//    }
//
//    public String getGoTo() {
//        return goTo;
//    }
//
//    public void setGoTo(String goTo) {
//        this.goTo = goTo;
//    }
//
//    public String getStartFrom() {
//        return startFrom;
//    }
//
//    public void setStartFrom(String startFrom) {
//        this.startFrom = startFrom;
//    }
//
//    public LocalDate getDateGoing() {
//        return dateGoing;
//    }
//
//    public void setDateGoing(LocalDate dateGoing) {
//        this.dateGoing = dateGoing;
//    }
//
//    public LocalDate getDateComing() {
//        return dateComing;
//    }
//
//    public void setDateComing(LocalDate dateComing) {
//        this.dateComing = dateComing;
//    }

    //--------------------------------------
}
