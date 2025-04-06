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
    private List<Treatment> treatments;
    private List<Booking> bookings;

    public BookingSystem() {
        this.physiotherapists = new ArrayList<>();
        this.patients = new ArrayList<>();
        this.treatments = new ArrayList<>();
        this.bookings = new ArrayList<>();
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

    public List<Physiotherapist> searchPhysiotherapist(String name) {
        return physiotherapists.stream()
                .filter(p -> (name == null || p.getFullName().toLowerCase().contains(name.toLowerCase())))
                .collect(Collectors.toList());
    }


    //Treatment Functions
    public void bulkAddTreatments(List<Treatment> treatementsList) {
        treatments.addAll(treatementsList);
    }

    public List<Treatment> filterTreatmentsByDate(LocalDate date, Long physiotherapistId) {
        return treatments.stream()
                .filter(appointment -> appointment.getStatus().toLowerCase().contains("confirmed") &&
                        appointment.getAppointmentDate().equals(date) &&
                        appointment.getPhysiotherapistId().equals(physiotherapistId))
                .collect(Collectors.toList());
    }

    public List<Treatment> filterTreatmentsByDateAndPhysiotherapists(LocalDate date, List<Physiotherapist> physiotherapists) {
        Set<Long> physiotherapistIds = physiotherapists.stream()
                .map(Physiotherapist::getId)
                .collect(Collectors.toSet());

        return treatments.stream()
                .filter(appointment -> appointment.getStatus().toLowerCase().contains("confirmed") &&
                        appointment.getAppointmentDate().equals(date) &&
                        physiotherapistIds.contains(appointment.getPhysiotherapistId()))
                .sorted(Comparator.comparing(Treatment::getAppointmentTime)) // Sort by time
                .collect(Collectors.toList());
    }

    public void printCustomTreatments(List<Treatment> customTreatments) {
        System.out.println("\n*** Treatment Available ***\n");
        System.out.printf("%-5s | %-20s | %-30s | %-15s | %-15s\n",
                "S.No", "Physiotherapist", "Treatment", "Date", "Time");
        System.out.println("---------------------------------------------------------------------------------------------------------");
        int counter = 0;
        for (Treatment appointment : customTreatments) {
            counter++;
            Physiotherapist physiotherapist = getPhysiotherapistById(appointment.getPhysiotherapistId()).get();
            String appointmentDuration = DataHelper.getDurationAsString(appointment.getAppointmentTime(), appointment.getAppointmentDuration());
            System.out.printf("%-5s | %-20s | %-30s | %-15s | %-15s\n",
                    counter,
                    physiotherapist.getFullName(),
                    appointment.getName(),
                    appointment.getAppointmentDate(),
                    appointmentDuration);
        }
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }


    //Booking Functions
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public void cancelBooking(Booking booking) {
        booking.setStatus("Cancelled");
    }

    public void attendBooking(Booking booking) {
        booking.setStatus("Attended");
    }

    public void updateTreatmentStatus(Treatment treatment, String status) {
        treatment.setStatus(status);
    }

    public List<Booking> getBookingsByPatientId(Long patientId) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getPatientId() != null && booking.getPatientId().equals(patientId)) {
                result.add(booking);
            }
        }
        return result;
    }

    public List<Booking> getBookingsByPhysiotherapistId(Long physiotherapistId) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getPhysiotherapistId() != null && booking.getPhysiotherapistId().equals(physiotherapistId)) {
                result.add(booking);
            }
        }
        return result;
    }

    public void printBookingDetails(int SNo, Booking booking) {
        Patient patient = getPatientById(booking.getPatientId()).get();
        Physiotherapist physiotherapist = getPhysiotherapistById(booking.getPhysiotherapistId()).get();
        String statusIcon = getStatusIcon(booking.getStatus());
        String bookingDuration = DataHelper.getDurationAsString(booking.getBookingTime(), booking.getBookingDuration());
        System.out.printf("%-10s | %-15s | %-20s | %-20s | %-15s | %-15s | %s\n",
                booking.getId(),
                patient.getFullName(),
                physiotherapist.getFullName(),
                booking.getTreatmentName(),
                booking.getBookingDate(),
                bookingDuration,
                statusIcon);
    }

    public void printAllBookings() {
        System.out.println("\n*** ALL Bookings ***\n");
        printHeader();
        int counter = 0;
        for (Booking booking : bookings) {
            counter++;
            printBookingDetails(counter, booking);
        }
        System.out.println("------------------------------------------------------\n");
    }

    public void printBookingsByPhysiotherapist(Long physiotherapistId) {
        List<Booking> filteredBookings = getBookingsByPhysiotherapistId(physiotherapistId);
        if (filteredBookings.isEmpty()) {
            System.out.println("No bookings found for physiotherapist " + physiotherapistId);
        }
        else{
            System.out.println("\n*** BOOKING ***\n");
            printHeader();
            int counter = 0;
            for (Booking booking : filteredBookings) {
                counter++;
                printBookingDetails(counter, booking);
            }
            System.out.println("---------------------------------------------------------------------------------------------------------");
        }
    }

    public void printBookingsByPatient(Long patientId) {
        List<Booking> filteredBookings = getBookingsByPatientId(patientId);
        if (filteredBookings.isEmpty()) {
            System.out.println("No bookings found for patient " + patientId);
        }
        else{
            System.out.println("\n*** BOOKING ***\n");
            printHeader();
            int counter = 0;
            for (Booking booking : filteredBookings) {
                counter++;
                printBookingDetails(counter, booking);
            }
            System.out.println("---------------------------------------------------------------------------------------------------------");
        }
    }

    public void printHeader() {
        System.out.printf("%-10s | %-15s | %-20s | %-20s | %-15s | %-15s | %-10s\n",
                "Booking Id", "Patient", "Physiotherapist", "Treatment", "Date", "Time", "Status");
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }

    public String getStatusIcon(String status) {
        return switch (status.toLowerCase()) {
            case "confirmed" -> "✅ Confirmed";
            case "attended" -> "🎉 Attended";
            case "cancelled" -> "❌ Cancelled";
            case "booked" -> "⏳ Booked";
            default -> status;
        };
    }
}
