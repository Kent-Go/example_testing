package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.Availability;
import au.edu.rmit.sept.webapp.repositories.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    // Save or update availability
    @Override
    public Availability saveAvailability(Availability availability) {
        if (availability == null) {
            return null;
        }
        return availabilityRepository.save(availability);
    }

    // Find availability by date and time
    @Override
    public Availability findAvailabilityByDateAndTime(LocalDate date, LocalTime startTime, LocalTime endTime) {
        return availabilityRepository.findByDateAndStartTimeAndEndTime(date, startTime, endTime).orElse(null);
    }
}
