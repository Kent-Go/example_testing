package au.edu.rmit.sept.webapp.controllers;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.models.AppointmentType;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.PetOwner;
import au.edu.rmit.sept.webapp.models.User;

import java.time.LocalDate;
import java.util.Collection;

import au.edu.rmit.sept.webapp.services.AppointmentService;
import au.edu.rmit.sept.webapp.services.PetService;
import au.edu.rmit.sept.webapp.services.AppointmentTypeService;
import au.edu.rmit.sept.webapp.services.PetOwnerService;
import au.edu.rmit.sept.webapp.services.UserService;

@Controller
public class VetDashboardController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PetService petService;

    @Autowired
    private AppointmentTypeService appointmentTypeService;

    @Autowired
    private PetOwnerService petOwnerService;

    @Autowired
    private UserService userService;

    @GetMapping("/vetDashboard")
    public String showVetDashboard(@RequestParam(value = "userId", required = false) Integer userId,
            @RequestParam(value = "vetId", required = false) Integer vetId, Model model) {

        // Default or handle missing parameters as needed
        if (vetId == null) {
            model.addAttribute("errorMessage", "Vet ID is missing");
            return "errorPage"; // Make sure this template exists in src/main/resources/templates/
        }

        // Add attributes to the model
        model.addAttribute("userId", userId);
        model.addAttribute("vetId", vetId);

        // Get current week start date
        LocalDate startDate = LocalDate.now()
                .with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        model.addAttribute("weekStart", startDate);

        // get today date
        LocalDate todayDate = LocalDate.now();
        model.addAttribute("today", todayDate);

        // get today appointment
        Collection<Appointment> appointments = appointmentService.getAppointmentsByVetIDAndDate(vetId, todayDate);

        for (Appointment appointment : appointments) {
            Pet pet = petService.getPetByPetID(appointment.getPetID());
            AppointmentType appointmentType = appointmentTypeService
                    .getAppointmentTypeByAppointmentTypeID(appointment.getAppointmentTypeID());
            PetOwner petOwner = petOwnerService.getPetOwnerByPetOwnerID(pet.getPetOwnerID());
            User user = userService.getUserByUserID(petOwner.getUserID());

            // Store the pet and appointment type info into each appointment
            appointment.setPet(pet);
            appointment.setAppointmentType(appointmentType);

            petOwner.setUser(user);

            // store the pet owner into pet entity
            pet.setPetOwner(petOwner);
        }
        model.addAttribute("appointments", appointments);

        return "vetDashboard"; // Corresponds to vetDashboard.html
    }
}
