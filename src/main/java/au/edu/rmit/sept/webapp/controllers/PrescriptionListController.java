package au.edu.rmit.sept.webapp.controllers;

import org.springframework.ui.Model;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.PetOwner;
import au.edu.rmit.sept.webapp.models.Medicine;
import au.edu.rmit.sept.webapp.models.Order;
import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.models.PrescribedMedication;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;

import au.edu.rmit.sept.webapp.services.PetService;
import au.edu.rmit.sept.webapp.services.UserService;
import au.edu.rmit.sept.webapp.services.PetOwnerService;
import au.edu.rmit.sept.webapp.services.MedicineService;
import au.edu.rmit.sept.webapp.services.OrderService;
import au.edu.rmit.sept.webapp.services.AppointmentService;
import au.edu.rmit.sept.webapp.services.PrescribedMedicationService;

@Controller
public class PrescriptionListController {
    @Autowired
    private UserService userService;

    @Autowired
    private PetOwnerService petOwnerService;

    @Autowired
    private PetService petService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PrescribedMedicationService prescribedMedicationService;

    @GetMapping("/prescriptions")
    public String showPrescriptionList(@RequestParam("petOwnerId") int petOwnerId, Model model) {
        List<PrescribedMedication> prescriptions = new ArrayList<PrescribedMedication>();

        // Get all the pet based on the pet owner id
        Collection<Pet> pets = petService.getPetsByPetOwnerID(petOwnerId);

        // For each pet, get the prescribed medication
        for (Pet pet : pets) {
            // Get the appointments for the pet
            Collection<Appointment> appointments = appointmentService.getAppointmentByPetID(pet.getId());
            
            // For each appointment, get the prescribed medication
            appointments.forEach(appointment -> {
                List<PrescribedMedication> prescribedMedications = prescribedMedicationService.getPrescribedMedicationByAppointmentID(appointment.getId());

                // For each prescribed medication, get the medicine and set the appointment and pet
                prescribedMedications.forEach(prescribedMedication -> {
                    Medicine medicine = medicineService.getMedicineByID(prescribedMedication.getMedicineID());
                    prescribedMedication.setMedicine(medicine);
                    prescribedMedication.setAppointment(appointment);
                    prescribedMedication.getAppointment().setPet(pet);
                    Order order = orderService.getOrderByID(prescribedMedication.getOrderID());
                    prescribedMedication.setOrder(order);
                });
                prescriptions.addAll(prescribedMedications);
            });
        }

        PetOwner petOwner = petOwnerService.getPetOwnerByPetOwnerID(petOwnerId);
        petOwner.setUser(userService.getUserByUserID(petOwner.getUserID()));

        model.addAttribute("prescriptions", prescriptions);
        model.addAttribute("petOwner", petOwner);

        return "prescriptionList";
    }
}
