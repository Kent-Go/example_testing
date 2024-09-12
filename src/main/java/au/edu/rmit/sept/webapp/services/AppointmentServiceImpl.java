package au.edu.rmit.sept.webapp.services;

import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.repositories.AppointmentRepository;

@Service
public class AppointmentServiceImpl implements AppointmentService {

  private AppointmentRepository appointmentRepository;

  @Autowired
  public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
    this.appointmentRepository = appointmentRepository;
  }

  @Override
  public Collection<Appointment> getAllAppointments() {
    return appointmentRepository.findAll();
  }

  @Override
  public Collection<Appointment> getAppointmentByVetID(int vetID) {
    return appointmentRepository.findByVetID(vetID);
  }

  @Override
  public Collection<Appointment> getAppointmentByPetID(int petID) {
    return appointmentRepository.findByPetID(petID);
  }

  @Override
  public Appointment createAppointment(Appointment appointment) {
    return appointmentRepository.save(appointment);
  }
}