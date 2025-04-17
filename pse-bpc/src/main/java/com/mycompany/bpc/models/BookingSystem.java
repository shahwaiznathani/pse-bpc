package com.mycompany.bpc.models;

import com.mycompany.bpc.helper.DataHelper;

import java.awt.print.Book;
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
    public static Scanner scanner = new Scanner(System.in);

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

    public Patient getPatientById(Long patientId) {
        return patients.stream()
                .filter(p -> p.getId().equals(patientId))
                .findFirst().orElse(null);
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

    public Physiotherapist getPhysiotherapistById(Long physiotherapistId) {
        return physiotherapists.stream()
                .filter(p -> p.getId().equals(physiotherapistId))
                .findFirst().orElse(null);
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
    public void updateTreatmentStatus(Treatment treatment, String status) {
        treatment.setStatus(status);
    }

    public void updateTreatmentStatusById(String treatmentId){
        treatments.stream()
                .filter(b -> b.getId().equalsIgnoreCase(treatmentId))
                .findFirst().ifPresent(treatment -> treatment.setStatus("confirmed"));
    }

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
        System.out.printf("%-5s | %-10s | %-20s | %-30s | %-15s | %-15s\n",
                "S.No", "Treatment Id", "Physiotherapist", "Treatment", "Date", "Time");
        System.out.println("---------------------------------------------------------------------------------------------------------");
        int counter = 0;
        for (Treatment treatment : customTreatments) {
            counter++;
            Physiotherapist physiotherapist = getPhysiotherapistById(treatment.getPhysiotherapistId());
            String appointmentDuration = DataHelper.getDurationAsString(treatment.getAppointmentTime(), treatment.getAppointmentDuration());
            System.out.printf("%-5s |%-10s | %-20s | %-30s | %-15s | %-15s\n",
                    counter,
                    treatment.getId(),
                    physiotherapist.getFullName(),
                    treatment.getName(),
                    treatment.getAppointmentDate(),
                    appointmentDuration);
        }
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }


    //Booking Functions
    public Booking getBookingById(String bookingId) {
        return bookings.stream()
                .filter(b -> b.getId().equalsIgnoreCase(bookingId))
                .findFirst().orElse(null);
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

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public void cancelBooking(Booking booking) {
        booking.setStatus("Cancelled");
        String treatmentId = booking.getTreatmentId();
        updateTreatmentStatusById(treatmentId);
        booking.setTreatmentId(null);
    }

    public void attendBooking(Booking booking) {
        booking.setStatus("Attended");
    }

    public boolean validateBookingTime(LocalDate bookingDate, LocalTime bookingTime, Long patientId) {
        List <Booking> patientBookings = getBookingsByPatientId(patientId);
        if(!patientBookings.isEmpty()){
            for (Booking booking : patientBookings) {
                if (booking.getBookingDate().equals(bookingDate) && booking.getBookingTime().equals(bookingTime) && booking.getStatus().equalsIgnoreCase("booked")) {
                    return false;
                }
            }
        }
        return true;
    }

    public void printBookingDetails(Booking booking) {
        Patient patient = getPatientById(booking.getPatientId());
        Physiotherapist physiotherapist = getPhysiotherapistById(booking.getPhysiotherapistId());
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
        for (Booking booking : bookings) {
            printBookingDetails(booking);
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
            for (Booking booking : filteredBookings) {
                printBookingDetails(booking);
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
            for (Booking booking : filteredBookings) {
                printBookingDetails(booking);
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

    public void bookByPhysiotherapist(Long patientId, String bookingId) {
        Physiotherapist selectedPhysio = selectPhysiotherapistByName();
        Treatment selectedTreatment = selectTreatment(selectedPhysio);
        boolean isBookingValid = validateBookingTime(selectedTreatment.getAppointmentDate(), selectedTreatment.getAppointmentTime(), patientId);
        if(isBookingValid){
            if(bookingId != null){
                Booking booking = getBookingById(bookingId);

                booking.setTreatmentId(selectedTreatment.getId());
                booking.setTreatmentName(selectedTreatment.getName());
                booking.setBookingDate(selectedTreatment.getAppointmentDate());
                booking.setBookingTime(selectedTreatment.getAppointmentTime());
                booking.setBookingDuration(selectedTreatment.getAppointmentDuration());
                booking.setPhysiotherapistId(selectedTreatment.getPhysiotherapistId());
                booking.setStatus("Booked");

                updateTreatmentStatus(selectedTreatment, "Booked");
                System.out.println("Booking " + bookingId + " Updated.");
            }
            else{
                Booking newBooking = new Booking(selectedTreatment.getAppointmentDate(), selectedTreatment.getAppointmentTime(),
                        selectedTreatment.getAppointmentDuration(), selectedTreatment.getPhysiotherapistId(), patientId,
                        "Booked", selectedTreatment.getName(), selectedTreatment.getId());

                addBooking(newBooking);
                updateTreatmentStatus(selectedTreatment, "Booked");
                System.out.println("Booking confirmed with Id: " + newBooking.getId());
            }
        }
        else{
            System.out.println("Cannot book more than one booking at the same time");
        }
    }

    public void bookByAreaOfExpertise(Long patientId, String bookingId){
        String selectedExpertise = selectExpertise();
        List<Physiotherapist> availablePhysios = searchPhysiotherapistByExpertise(selectedExpertise);
        Treatment selectedTreatment = selectTreatmentByExpertise(availablePhysios, selectedExpertise); //Need to pass expertise here
        boolean isBookingValid = validateBookingTime(selectedTreatment.getAppointmentDate(), selectedTreatment.getAppointmentTime(), patientId);
        if(isBookingValid){
            if(bookingId != null){
                Booking booking = getBookingById(bookingId);

                booking.setTreatmentId(selectedTreatment.getId());
                booking.setTreatmentName(selectedTreatment.getName());
                booking.setBookingDate(selectedTreatment.getAppointmentDate());
                booking.setBookingTime(selectedTreatment.getAppointmentTime());
                booking.setBookingDuration(selectedTreatment.getAppointmentDuration());
                booking.setPhysiotherapistId(selectedTreatment.getPhysiotherapistId());
                booking.setStatus("Booked");

                updateTreatmentStatus(selectedTreatment, "Booked");
                System.out.println("Booking " + bookingId + " Updated.");
            }
            else{
                Booking newBooking = new Booking(selectedTreatment.getAppointmentDate(), selectedTreatment.getAppointmentTime(),
                        selectedTreatment.getAppointmentDuration(), selectedTreatment.getPhysiotherapistId(), patientId,
                        "Booked", selectedTreatment.getName(), selectedTreatment.getId());
                addBooking(newBooking);
                updateTreatmentStatus(selectedTreatment, "Booked");
                System.out.println("Booking confirmed with Id: " + newBooking.getId());
            }
        }
        else{
            System.out.println("Cannot book more than one booking at the same time");
        }
    }

    public Physiotherapist selectPhysiotherapistByName(){
        Physiotherapist selectedPhysio;
        while (true) {
            String input = DataHelper.getStringInput(scanner, "Search Physiotherapist by name (e.g., Dr Shahwaiz): ");
            List<Physiotherapist> lst = searchPhysiotherapist(input);

            if (lst.isEmpty()) {
                System.out.println("No physiotherapist found. Please try again.");
            }
            else {
                // Print physiotherapists
                for (int i = 0; i < lst.size(); i++) {
                    System.out.println((i + 1) + ". " + lst.get(i).getFullName());
                }
                int choice = DataHelper.getValidNumberInput(scanner, 1, lst.size(), "Enter your choice (1-" + lst.size() + "): ");

                selectedPhysio = lst.get(choice - 1);
                System.out.println("You selected: " + selectedPhysio.getFullName());
                break;
            }
        }
        return selectedPhysio;
    }

    public String selectExpertise(){
        Map<String, Set<Physiotherapist>> expertiseToPhysios = getAllExpertiseByPhysiotherapist();
        List<String> expertiseLst = new ArrayList<>(expertiseToPhysios.keySet());
        String selectedExpertise;
        while(true){
            String searchQuery = DataHelper.getStringInput(scanner, "Search for a Expertise:");
            List<String> matchingExpertise = expertiseLst.stream()
                    .filter(t -> t.toLowerCase().contains(searchQuery))
                    .toList();

            if (matchingExpertise.isEmpty()) {
                System.out.println("No matching Expertise found. Please try again.");
            }
            else{
                System.out.println("Matching Expertise:");
                for (int i = 0; i < matchingExpertise.size(); i++) {
                    System.out.println((i + 1) + ". " + matchingExpertise.get(i));
                }

                // Get user selection
                int expertiseChoice = DataHelper.getValidNumberInput(scanner, 1, matchingExpertise.size(), "Enter your choice (1-" + matchingExpertise.size() + "): ");
                selectedExpertise = matchingExpertise.get(expertiseChoice - 1);
                break;
            }
        }
        return selectedExpertise;
    }

    public Treatment selectTreatment(Physiotherapist physiotherapist){
        Treatment selectedTreatment;
        while (true){
            LocalDate appointmentDate = DataHelper.getValidDate(scanner, "Enter appointment date (YYYY-MM-DD): ");

            List<Treatment> availTreatments = filterTreatmentsByDate(appointmentDate, physiotherapist.getId());

            if (availTreatments.isEmpty()) {
                System.out.println("No appointments available for " + physiotherapist.getFullName() + " on " + appointmentDate.toString() + ". Try another Date.");
            }
            else{
                //Print Available Appointments
                printCustomTreatments(availTreatments);
                int slotChoice = DataHelper.getValidNumberInput(scanner, 1, availTreatments.size(), "Enter Slot number to book: ");
                selectedTreatment = availTreatments.get(slotChoice - 1);
                break;
            }
        }
        return selectedTreatment;
    }

    public Treatment selectTreatmentByExpertise(List<Physiotherapist> availablePhysios, String expertise){
        Treatment selectedTreatment;
        while (true){
            LocalDate appointmentDate = DataHelper.getValidDate(scanner, "Enter treatment date (YYYY-MM-DD): ");

            List<Treatment> availTreatments = filterTreatmentsByDateAndPhysiotherapists(appointmentDate, availablePhysios);
            if (availTreatments.isEmpty()) {
                System.out.println("No treatments available on " + appointmentDate.toString() + ". Try another Date.");
            }
            else {
                List<Treatment> filteredTreatments = availTreatments.stream()
                        .filter(appointment -> appointment.getExpertise().equalsIgnoreCase(expertise))
                        .toList();
                if(filteredTreatments.isEmpty()){
                    System.out.println("No treatments available for the selected Expertise on " + appointmentDate.toString() + ". Try another Date.");
                }
                else{
                    printCustomTreatments(filteredTreatments);
                    int treatmentChoice = DataHelper.getValidNumberInput(scanner, 1, filteredTreatments.size(), "Enter Slot number to book: ");
                    selectedTreatment = filteredTreatments.get(treatmentChoice - 1);
                    break;
                }
            }
        }
        return selectedTreatment;
    }

    public Map<String, Set<Physiotherapist>> getAllExpertiseByPhysiotherapist(){
        Map<String, Set<Physiotherapist>> expertiseToPhysios = new HashMap<>();

        // Gather all unique treatments and map them to physiotherapists
        for (Physiotherapist physio : physiotherapists) {
            for (String expertise : physio.getAllExpertise()) {
                expertiseToPhysios.computeIfAbsent(expertise, k -> new HashSet<>()).add(physio);
            }
        }
        return expertiseToPhysios;
    }

    public List<Physiotherapist> searchPhysiotherapistByExpertise(String selectedExpertise){
        Map<String, Set<Physiotherapist>> expertiseToPhysios = getAllExpertiseByPhysiotherapist();
        return new ArrayList<>(expertiseToPhysios.get(selectedExpertise));
    }
}
