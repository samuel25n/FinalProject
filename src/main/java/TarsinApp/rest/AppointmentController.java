//package TarsinApp.rest;
//
//import TarsinApp.entity.Appointment;
//import TarsinApp.repository.AppointmentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//public class AppointmentController {
//
//    @Autowired
//    private AppointmentRepository appointmentRepository;
//
//
//
//    @GetMapping(value = "/appointmentGoing/all")
//    public List<Appointment> getAllAppointments(){
//        return appointmentRepository.findAll();
//    }
//
//    @PostMapping(value = "/appointmentGoing")
//    public void addAppointment(@RequestBody Appointment appointmentGoing){
//        appointmentRepository.save(appointmentGoing);
//    }
//}
