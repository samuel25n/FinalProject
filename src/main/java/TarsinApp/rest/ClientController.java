//package TarsinApp.rest;
//
//import TarsinApp.entity.Client;
//import TarsinApp.repository.ClientRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//public class ClientController {
//
//    @Autowired
//    private ClientRepository clientRepository;
//
//
//
//    @PostMapping(value = "/client")
//    public Client saveClient(@RequestBody Client client){
//        return clientRepository.save(client);
//    }
//
//    @GetMapping(value = "/client/all")
//    public List<Client> getAllClients(){
//        return clientRepository.findAll();
//    }
//
//    @DeleteMapping(value = "/client/{id}")
//    public void deleteClient(@PathVariable int id){
//        clientRepository.deleteById(id);
//    }
//
//}
