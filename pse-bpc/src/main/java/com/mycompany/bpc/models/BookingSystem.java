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

    public void removePatient(Long patientId) {
        patients.removeIf(obj -> Objects.equals(obj.getId(), patientId));
    }

    public Long GetNewPatientId(){
        return patients.stream()
                .mapToLong(Patient::getId)
                .max()
                .orElse(0) + 1;
    }


    //Physiotherapist Functions
    public void bulkAddPhysiotherapists(List<Physiotherapist> physiotherapistsList) {
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
        appointment.setStatus("Cancelled");
    }

    public void attendAppointment(Appointment appointment) {
        appointment.setStatus("Attended");
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

    public List<Appointment> getBookedAppointmentsByPatientId(Long patientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatientId() != null && appointment.getPatientId().equals(patientId) && appointment.getStatus().equalsIgnoreCase("booked")) {
                result.add(appointment);
            }
        }
        return result;
    }

    // Function to print all appointments for a specific patient
    public void printAppointmentsByPatient(Long patientId) {
        List<Appointment> filteredAppointments = getAppointmentsByPatientId(patientId);
        System.out.println("\n*** APPOINTMENTS ***\n");
        printHeader();
        int counter = 0;
        for (Appointment appointment : filteredAppointments) {
            counter++;
            printAppointmentDetails(counter, appointment);
        }
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }

    public void printBookedAppointmentsByPatient(Long patientId) {
        List<Appointment> filteredAppointments = getBookedAppointmentsByPatientId(patientId);
        System.out.println("\n*** APPOINTMENTS ***\n");
        printHeader();
        int counter = 0;
        for (Appointment appointment : filteredAppointments) {
            counter++;
            printAppointmentDetails(counter, appointment);
        }
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }

    // Function to print all appointments for a specific physiotherapist
    public void printAppointmentsByPhysiotherapist(Long physiotherapistId) {
        System.out.println("\n*** APPOINTMENTS ***\n");
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
        return switch (status.toLowerCase()) {
            case "confirmed" -> "✅ Confirmed";
            case "attended" -> "🎉 Attended";
            case "cancelled" -> "❌ Cancelled";
            case "booked" -> "⏳ Booked";
            default -> status;
        };
    }

    public List<Physiotherapist> searchPhysiotherapist(String name) {
        return physiotherapists.stream()
                .filter(p -> (name == null || p.getFullName().toLowerCase().contains(name.toLowerCase())))
                .collect(Collectors.toList());
    }

    // Filter by date
    public List<Appointment> filterAppointmentByDate(LocalDate date, Long physiotherapistId) {
        return appointments.stream()
                .filter(appointment -> appointment.getStatus().toLowerCase().contains("confirmed") &&
                        appointment.getAppointmentDate().equals(date) &&
                        appointment.getPhysiotherapistId().equals(physiotherapistId))
                .collect(Collectors.toList());
    }

    public void updateAppointment(Appointment selectedAppointment, Long patientId, String treatment) {
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
            }
        }
    }
}
