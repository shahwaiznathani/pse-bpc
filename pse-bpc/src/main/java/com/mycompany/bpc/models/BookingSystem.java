package com.mycompany.bpc.models;

import java.util.ArrayList;
import java.util.*;

/**
 *
 * @author shahwaizshaban
 */
public class BookingSystem {
    private List<Physiotherapist> physiotherapists;
    private List<Patient> patients;
    private List<Appointment> appointments;

    // Constructor, Getters, Setters
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

    public Optional<Patient> getPatientById(String patientId) {
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

    public Optional<Physiotherapist> getPhysiotherapistById(String physiotherapistId) {
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

    public void cancelAppointment(Appointment appointment) {
        appointments.remove(appointment);
    }

    public void searchByExpertise(String expertise) {
        // Implement search logic
    }

    public void searchByPhysiotherapist(Physiotherapist physiotherapist) {
        // Implement search logic
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    // Function to print all appointments for a specific patient
    public void printAppointmentsByPatient(String patientId) {
        System.out.println("\n🔹🔹🔹 APPOINTMENTS FOR PATIENT: " + patientId + " 🔹🔹🔹\n");
        printHeader();
        for (Appointment appointment : appointments) {
            if (appointment.getPatientId().equals(patientId)) {
                printAppointmentDetails(appointment);
            }
        }
        System.out.println("------------------------------------------------------\n");
    }

    // Function to print all appointments for a specific physiotherapist
    public void printAppointmentsByPhysiotherapist(String physiotherapistId) {
        System.out.println("\n🩺🔹🔹 APPOINTMENTS FOR PHYSIOTHERAPIST: " + physiotherapistId + " 🔹🔹🩺\n");
        printHeader();
        for (Appointment appointment : appointments) {
            if (appointment.getPhysiotherapistId().equals(physiotherapistId)) {
                printAppointmentDetails(appointment);
            }
        }
        System.out.println("------------------------------------------------------\n");
    }

    // Function to print all appointments
    public void printAllAppointments() {
        System.out.println("\n📅🔹🔹 ALL APPOINTMENTS 🔹🔹📅\n");
        printHeader();
        for (Appointment appointment : appointments) {
            printAppointmentDetails(appointment);
        }
        System.out.println("------------------------------------------------------\n");
    }

    // Helper function to print the header
    private void printHeader() {
        System.out.printf("%-15s | %-20s | %-20s | %-15s | %-10s\n",
                "Patient", "Physiotherapist", "Treatment", "Date", "Status");
        System.out.println("--------------------------------------------------------------------------------------");
    }

    // Helper function to print formatted appointment details
    private void printAppointmentDetails(Appointment appointment) {
        Patient patient = getPatientById(appointment.getPatientId()).get();
        Physiotherapist physiotherapist = getPhysiotherapistById(appointment.getPhysiotherapistId()).get();
        String statusIcon = getStatusIcon(appointment.getStatus());

        System.out.printf("%-15s | %-20s | %-20s | %-15s | %s\n",
                patient.getFullName(),
                physiotherapist.getFullName(),
                appointment.getTreatment(),
                appointment.getAppointmentDate(),
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
}
