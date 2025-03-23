package com.mycompany.bpc.models;

import com.mycompany.bpc.helper.DataHelper;

import java.util.*;
import java.util.stream.Collectors;
import java.time.*;
/**
 *
 * @author shahwaizshaban
 */
public class BookingSystem {
    private List<Physiotherapist> physiotherapists;
    private List<Patient> patients;
    private List<Appointment> appointments;

    public BookingSystem(List<Physiotherapist> physiotherapists, List<Patient> patients, List<Appointment> appointments) {
        this.physiotherapists = physiotherapists;
        this.patients = patients;
        this.appointments = appointments;
    }

    public BookingSystem() {
        this.physiotherapists = new ArrayList<>();
        this.patients = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    //Patient Functions
    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void bulkAddPatients(List<Patient> patientsList) {
        patients.addAll(patientsList);
    }

    public Optional<Patient> getPatientById(Long patientId) {
        return patients.stream()
                .filter(p -> p.getId().equals(patientId))
                .findFirst();
    }

    public List<Patient> getAllPatients() {
        return patients;
    }

    public void removePatient(Patient patient) {
        patients.remove(patient);
    }



    //Physiotherapist Functions
    public void addPysiotherapist(Physiotherapist physiotherapist) {
        physiotherapists.add(physiotherapist);
    }

    public void bulkAddPysiotherapists(List<Physiotherapist> physiotherapistsList) {
        physiotherapists.addAll(physiotherapistsList);
    }

    public Optional<Physiotherapist> getPhysiotherapistById(Long physiotherapistId) {
        return physiotherapists.stream()
                .filter(p -> p.getId().equals(physiotherapistId))
                .findFirst();
    }

    public List<Physiotherapist> getAllPhysiotherapists() {
        return physiotherapists;
    }

    public void removePysiotherapist(Physiotherapist physiotherapist) {
        physiotherapists.remove(physiotherapist);
    }

    public List<Physiotherapist> findPhysiotherapistsByTreatment(String treatment) {
        List<Physiotherapist> result = new ArrayList<>();

        for (Physiotherapist physio : physiotherapists) {
            for (Expertise expertise : physio.getAllExpertise()) {
                if (expertise.getTreatments().contains(treatment)) {
                    result.add(physio);
                    break; // Stop checking further once a match is found
                }
            }
        }
        return result;
    }


    //Appointment Functions
    public boolean bookAppointment(Appointment newAppointment) {
        Patient patient = getPatientById(newAppointment.getPatientId()).get();
        Physiotherapist physiotherapist = getPhysiotherapistById(newAppointment.getPhysiotherapistId()).get();
        for (Appointment existingAppointment : appointments) {
            if (existingAppointment.getPhysiotherapistId().equals(newAppointment.getPhysiotherapistId()) &&
                    existingAppointment.getAppointmentDate().equals(newAppointment.getAppointmentDate())) {

                // Conflict found, reject the appointment
                System.out.println("❌ ERROR: Conflict! Physiotherapist " + patient.getFullName() +
                        " already has an appointment at " + newAppointment.getAppointmentDate());
                return false;
            }
        }

        // ✅ No conflict, appointment is added
        appointments.add(newAppointment);
        System.out.println("✅ Appointment booked successfully for " + patient.getFullName() +
                " with " + physiotherapist.getFullName() + " on " + newAppointment.getAppointmentDate());
        return true;
    }

    public void bulkAddAppointments(List<Appointment> appointmentsList) {
        appointments.addAll(appointmentsList);
    }

    public void cancelAppointment(Appointment appointment) {
        appointment.setStatus("Cancelled by Patient");
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatientId() != null && appointment.getPatientId().equals(patientId)) {
                result.add(appointment);
            }
        }
        return result;
    }

    // Function to print all appointments for a specific patient
    public void printAppointmentsByPatient(Long patientId) {
        List<Appointment> filteredAppointments = getAppointmentsByPatientId(patientId);
        System.out.println("\n*** YOUR APPOINTMENTS ***\n");
        printHeader();
        int counter = 0;
        for (Appointment appointment : filteredAppointments) {
            counter++;
            printAppointmentDetails(counter, appointment);
        }
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }

    // Function to print all appointments for a specific physiotherapist
    public void printAppointmentsByPhysiotherapist(String physiotherapistId) {
        System.out.println("\n *** APPOINTMENTS FOR PHYSIOTHERAPIST: " + physiotherapistId + " *** \n");
        printHeader();
        int counter = 0;
        for (Appointment appointment : appointments) {
            counter++;
            if (appointment.getPhysiotherapistId().equals(physiotherapistId)) {
                printAppointmentDetails(counter, appointment);
            }
        }
        System.out.println("------------------------------------------------------\n");
    }

    // Function to print all appointments
    public void printAllAppointments() {
        System.out.println("\n*** ALL APPOINTMENTS ***\n");
        printHeader();
        int counter = 0;
        for (Appointment appointment : appointments) {
            counter++;
            printAppointmentDetails(counter, appointment);
        }
        System.out.println("------------------------------------------------------\n");
    }

    // Helper function to print the header
    private void printHeader() {
        System.out.printf("%-5s | %-15s | %-20s | %-20s | %-15s | %-15s | %-10s\n",
                "S.No", "Patient", "Physiotherapist", "Treatment", "Date", "Time", "Status");
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }

    // Helper function to print formatted appointment details
    private void printAppointmentDetails(int SNo, Appointment appointment) {
        Patient patient = getPatientById(appointment.getPatientId()).get();
        Physiotherapist physiotherapist = getPhysiotherapistById(appointment.getPhysiotherapistId()).get();
        String statusIcon = getStatusIcon(appointment.getStatus());
        String appointmentDuration = DataHelper.getDurationAsString(appointment.getAppointmentTime(), appointment.getAppointmentDuration());
        System.out.printf("%-5s | %-15s | %-20s | %-20s | %-15s | %-15s | %s\n",
                SNo,
                patient.getFullName(),
                physiotherapist.getFullName(),
                appointment.getTreatment(),
                appointment.getAppointmentDate(),
                appointmentDuration,
                statusIcon);
    }

    // Function to get an emoji icon for the status
    private String getStatusIcon(String status) {
        switch (status.toLowerCase()) {
            case "scheduled": return "✅ Scheduled";
            case "completed": return "🎉 Completed";
            case "cancelled": return "❌ Cancelled";
            case "pending": return "⏳ Pending";
            default: return status;
        }
    }

    public List<Physiotherapist> searchPhysiotherapist(String name) {
        return physiotherapists.stream()
                .filter(p -> (name == null || p.getFullName().toLowerCase().contains(name.toLowerCase())))
                .collect(Collectors.toList());
    }

    // Filter by date
    public List<Appointment> filterAppointmentByDate(LocalDate date, Long physiotherapistId) {
        return appointments.stream()
                .filter(appointment -> appointment.getAppointmentDate().equals(date) && appointment.getPhysiotherapistId().equals(physiotherapistId))
                .collect(Collectors.toList());
    }

    public boolean updateAppointment(Appointment selectedAppointment, Long patientId, String treatment) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentDate().equals(selectedAppointment.getAppointmentDate()) &&
                    appointment.getAppointmentTime().equals(selectedAppointment.getAppointmentTime()) &&
                    appointment.getPhysiotherapistId().equals(selectedAppointment.getPhysiotherapistId()) &&
                    appointment.getStatus().equalsIgnoreCase("Confirmed")) {

                // Update appointment details
                appointment.setPatientId(patientId);
                appointment.setStatus("Booked");
                appointment.setBookingDate(LocalDateTime.now());
                appointment.setTreatment(treatment);
                return true;  // Successfully updated
            }
        }
        return false;  // No matching appointment found
    }

    public void getallTreatments() {

    }
}
