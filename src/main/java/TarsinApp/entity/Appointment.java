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
import java.util.UUID;

@Entity
@Getter
@Setter

public class Appointment {

    @Id
    @GeneratedValue
    private Integer id;
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

    @Override
    public String toString() {
        return "" + client.getFullName() +
                " " + startFrom +
                " -> " + goTo +
                " pe " + dateGoing +
                " retur - " + dateComing
                ;
    }
}
