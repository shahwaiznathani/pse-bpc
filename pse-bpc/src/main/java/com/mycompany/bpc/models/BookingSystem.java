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
    public static Scanner scanner = new Scanner(System.in);

    public BookingSystem() {
        this.physiotherapists = new ArrayList<>();
        this.patients = new ArrayList<>();
        this.treatments = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }

    public void initializeData() {
        this.patients = DataHelper.loadPatients();
        this.physiotherapists = DataHelper.loadPhysiotherapists();
        this.treatments = DataHelper.loadTreatments();
    }

    //Patient Functions
    public List<Patient> getPatients(){
        return this.patients;
    }

    public Long addPatient(String name, String address, String phoneNumber) {
        Long id = GetNewPatientId();
        Patient newPatient = new Patient(id, name, address, phoneNumber);
        this.patients.add(newPatient);
        return id;
    }

    public Patient getPatientById(Long patientId) {
        return this.patients.stream()
                .filter(p -> p.getId().equals(patientId))
                .findFirst().orElse(null);
    }

    public boolean removePatient(Long patientId) {
        if(getPatientById(patientId)!=null){
            this.patients.removeIf(obj -> Objects.equals(obj.getId(), patientId));
            cancelBookingByPatientId(patientId);
            return true;
        }
        else{
            return false;
        }
    }

    public Long GetNewPatientId(){
        return this.patients.stream()
                .mapToLong(Patient::getId)
                .max()
                .orElse(2000000) + 1;
    }


    //Physiotherapist Functions
    public Physiotherapist getPhysiotherapistById(Long physiotherapistId) {
        return physiotherapists.stream()
                .filter(p -> p.getId().equals(physiotherapistId))
                .findFirst().orElse(null);
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

    private List<Treatment> filterTreatmentsByPhysiotherapist(Long physiotherapistId) {
        return treatments.stream()
                .filter(appointment -> appointment.getStatus().toLowerCase().contains("confirmed")
                        && appointment.getPhysiotherapistId().equals(physiotherapistId))
                .sorted(
                        Comparator.comparing(Treatment::getAppointmentDate)
                                .thenComparing(Treatment::getAppointmentTime)
                )
                .collect(Collectors.toList());
    }

    private List<Treatment> filterTreatmentsByExpertise(List<Physiotherapist> physiotherapists, String expertise) {
        Set<Long> physiotherapistIds = physiotherapists.stream()
                .map(Physiotherapist::getId)
                .collect(Collectors.toSet());

        return treatments.stream()
                .filter(appointment -> appointment.getStatus().toLowerCase().contains("confirmed")
                        && physiotherapistIds.contains(appointment.getPhysiotherapistId())
                        && appointment.getExpertise().equalsIgnoreCase(expertise))
                .sorted(
                        Comparator.comparing(Treatment::getAppointmentDate)
                                .thenComparing(Treatment::getAppointmentTime)
                )
                .collect(Collectors.toList());
    }

    public void printCustomTreatments(List<Treatment> customTreatments) {
        System.out.println("*** Treatment Available ***");
        System.out.printf("%-5s | %-10s | %-20s | %-35s | %-15s | %-15s\n",
                "S.No", "Treatment Id", "Physiotherapist", "Treatment", "Date", "Time");
        System.out.println("---------------------------------------------------------------------------------------------------------");
        int counter = 0;
        for (Treatment treatment : customTreatments) {
            counter++;
            Physiotherapist physiotherapist = getPhysiotherapistById(treatment.getPhysiotherapistId());
            String appointmentDuration = DataHelper.getDurationAsString(treatment.getAppointmentTime(), treatment.getAppointmentDuration());
            System.out.printf("%-5s |%-10s | %-20s | %-35s | %-15s | %-15s\n",
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
        return this.bookings.stream()
                .filter(b -> b.getId().equalsIgnoreCase(bookingId))
                .findFirst().orElse(null);
    }

    public List<Booking> getBookingsByPatientId(Long patientId) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : this.bookings) {
            if (booking.getPatientId() != null && booking.getPatientId().equals(patientId)) {
                result.add(booking);
            }
        }
        return result;
    }

    public List<Booking> getBookingsByPhysiotherapistId(Long physiotherapistId) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : this.bookings) {
            if (booking.getPhysiotherapistId() != null && booking.getPhysiotherapistId().equals(physiotherapistId)) {
                result.add(booking);
            }
        }
        return result;
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }

    public boolean cancelBooking(Booking booking) {
        if(booking.getStatus().equalsIgnoreCase("cancelled") ||
                booking.getStatus().equalsIgnoreCase("attended") ){
            return false;
        }
        else{
            booking.setStatus("Cancelled");
            String treatmentId = booking.getTreatmentId();
            updateTreatmentStatusById(treatmentId);
            booking.setTreatmentId(null);
            return true;
        }
    }

    public void attendBooking(Booking booking) {
        if(booking.getStatus().equalsIgnoreCase("cancelled") ||
                booking.getStatus().equalsIgnoreCase("attended") ){
            System.out.println("Cannot attend a booking that has already been marked Cancelled/Attended");
        }
        else{
            booking.setStatus("Attended");
            System.out.println("You have successfully attended a booking with id " + booking.getId() + ".");
        }
    }

    public boolean validateBookingConflict(LocalDate bookingDate, LocalTime bookingTime, Long patientId) {
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

    public void bookByPhysiotherapist(Long patientId, String bookingId) {
        try{
            Physiotherapist selectedPhysio = selectPhysiotherapistByName();
            Treatment selectedTreatment = selectTreatment(selectedPhysio);
            boolean isBookingValid = validateBookingConflict(selectedTreatment.getAppointmentDate(), selectedTreatment.getAppointmentTime(), patientId);
            if(isBookingValid){
                boolean confirmation = DataHelper.getYesOrNo(scanner, "Please confirm if you want to proceed with your selection?");
                if(confirmation){
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
            }
            else{
                System.out.println("Cannot book more than one booking at the same time");
            }
        }
        catch (Exception e){
            System.out.println("Error booking treatments by Physiotherapist: " + e.getMessage());
        }
    }

    public void bookByAreaOfExpertise(Long patientId, String bookingId){
        try{
            String selectedExpertise = selectExpertise();
            List<Physiotherapist> availablePhysios = searchPhysiotherapistByExpertise(selectedExpertise);
            Treatment selectedTreatment = selectTreatmentByExpertise(availablePhysios, selectedExpertise); //Need to pass expertise here
            boolean isBookingValid = validateBookingConflict(selectedTreatment.getAppointmentDate(), selectedTreatment.getAppointmentTime(), patientId);
            if(isBookingValid){
                boolean confirmation = DataHelper.getYesOrNo(scanner, "Please confirm if you want to proceed with your selection?");
                if(confirmation){
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
            }
            else{
                System.out.println("Cannot book more than one booking at the same time");
            }
        }
        catch(Exception e){
            System.out.println("Error booking treatments by Expertise: " + e.getMessage());
        }
    }

    private Physiotherapist selectPhysiotherapistByName(){
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

    private String selectExpertise(){
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

    private Treatment selectTreatment(Physiotherapist physiotherapist){
        Treatment selectedTreatment = null;
        List<Treatment> availTreatments = filterTreatmentsByPhysiotherapist(physiotherapist.getId());
        if (availTreatments == null || availTreatments.isEmpty()) {
            System.out.println("No appointments available for " + physiotherapist.getFullName());
        }
        else{
            printCustomTreatments(availTreatments);
            int slotChoice = DataHelper.getValidNumberInput(scanner, 1, availTreatments.size(), "Enter Slot number to book: ");
            selectedTreatment = availTreatments.get(slotChoice - 1);
        }
        return selectedTreatment;
    }

    private Treatment selectTreatmentByExpertise(List<Physiotherapist> availablePhysios, String expertise){
        Treatment selectedTreatment = null;
        List<Treatment> availTreatments = filterTreatmentsByExpertise(availablePhysios, expertise);
        if(availTreatments.isEmpty()){
            System.out.println("No treatments available for the selected Expertise.");
        }
        else{
            printCustomTreatments(availTreatments);
            int treatmentChoice = DataHelper.getValidNumberInput(scanner, 1, availTreatments.size(), "Enter Slot number to book: ");
            selectedTreatment = availTreatments.get(treatmentChoice - 1);
        }
        return selectedTreatment;
    }

    private Map<String, Set<Physiotherapist>> getAllExpertiseByPhysiotherapist(){
        Map<String, Set<Physiotherapist>> expertiseToPhysios = new HashMap<>();

        // Gather all unique treatments and map them to physiotherapists
        for (Physiotherapist physio : this.physiotherapists) {
            for (String expertise : physio.getExpertise()) {
                expertiseToPhysios.computeIfAbsent(expertise, _ -> new HashSet<>()).add(physio);
            }
        }
        return expertiseToPhysios;
    }

    private List<Physiotherapist> searchPhysiotherapistByExpertise(String selectedExpertise){
        Map<String, Set<Physiotherapist>> expertiseToPhysios = getAllExpertiseByPhysiotherapist();
        return new ArrayList<>(expertiseToPhysios.get(selectedExpertise));
    }

    public void generateBookingReport(LocalDate reportMonth) {
        try{
            if(!this.bookings.isEmpty()){
                List<Booking> filteredBookings = filterBookingsByMonth(reportMonth);
                if (filteredBookings.isEmpty()) {
                    System.out.println("No bookings found for " + reportMonth.getMonth() + " " + reportMonth.getYear());
                }
                else {
                    System.out.println("\n*** Clinic Report for " + reportMonth.getMonth() + " " + reportMonth.getYear() + " ***");
                    printBookingsByPhysiotherapist(filteredBookings);
                    printBookingCountByPhysiotherapist(reportMonth);
                }
            }
            else{
                System.out.println("No bookings found. Hence, report cannot be generated.");
            }
        }
        catch(Exception e){
            System.out.println("Error generating booking report: " + e.getMessage());
        }
    }

    private List<Booking> filterBookingsByMonth(LocalDate reportMonth){
        return this.bookings.stream()
                .filter(booking -> booking.getBookingDate().getMonthValue() == reportMonth.getMonthValue()
                        && booking.getBookingDate().getYear() == reportMonth.getYear())
                .sorted(
                        Comparator.comparing(Booking::getPhysiotherapistId)
                                .thenComparing(Booking::getBookingDate)
                                .thenComparing(Booking::getBookingTime)
                )
                .toList();
    }

    private Map<Long, Long> getAttendedBookingCountByPhysiotherapist(LocalDate reportMonth){
        return bookings.stream()
                .filter(booking -> booking.getBookingDate().getMonthValue() == reportMonth.getMonthValue()
                        && booking.getBookingDate().getYear() == reportMonth.getYear() && booking.getStatus().equalsIgnoreCase("attended"))
                .collect(Collectors.groupingBy(
                        Booking::getPhysiotherapistId,
                        Collectors.counting()
                ));
    }

    private void printBookingsByPhysiotherapist(List<Booking> filteredBookings){
        Long lastPhysioId = 0L;
        for (Booking booking : filteredBookings) {
            Physiotherapist physiotherapist = getPhysiotherapistById(booking.getPhysiotherapistId());
            Long currentPhysioId = booking.getPhysiotherapistId();
            if (!currentPhysioId.equals(lastPhysioId)) {
                System.out.println("\n** Bookings for " + physiotherapist.getFullName() + " **");
                printHeader();
                lastPhysioId = currentPhysioId;
            }
            printBookingDetails(booking);
        }
    }

    private void printBookingCountByPhysiotherapist(LocalDate reportMonth){
        Map<Long, Long> bookingsCountByPhysio = getAttendedBookingCountByPhysiotherapist(reportMonth);
        if(!bookingsCountByPhysio.isEmpty()){
            System.out.println("\n\n\n*** Bookings Attended By Physiotherapist in " + reportMonth.getMonth() + " " + reportMonth.getYear() + " ***");
            System.out.printf("%-20s | %s\n",
                    "Physiotherapist", "Booking Count");
            System.out.println("------------------------------------");
            bookingsCountByPhysio.entrySet().stream()
                    .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                    .forEach(entry -> {
                        Physiotherapist physio = getPhysiotherapistById(entry.getKey());
                        System.out.printf("%-20s | %s\n",
                                physio.getFullName(),
                                entry.getValue());
                    });
            System.out.println("------------------------------------");
        }
        else{
            System.out.println("No attended bookings found for " + reportMonth.getMonth() + " " + reportMonth.getYear() + ".");
        }
    }

    public void printBookingDetails(Booking booking) {
        Patient patient = getPatientById(booking.getPatientId());
        Physiotherapist physiotherapist = getPhysiotherapistById(booking.getPhysiotherapistId());
        String statusIcon = getStatusIcon(booking.getStatus());
        String bookingDuration = DataHelper.getDurationAsString(booking.getBookingTime(), booking.getBookingDuration());
        System.out.printf("%-10s | %-15s | %-20s | %-35s | %-15s | %-15s | %s\n",
                booking.getId(),
                patient == null ? "": patient.getFullName(),
                physiotherapist.getFullName(),
                booking.getTreatmentName(),
                booking.getBookingDate(),
                bookingDuration,
                statusIcon);
    }

    public void printBookingsByPhysiotherapist(Long physiotherapistId) {
        List<Booking> filteredBookings = getBookingsByPhysiotherapistId(physiotherapistId);
        if (filteredBookings.isEmpty()) {
            System.out.println("No bookings found for physiotherapist " + physiotherapistId);
        }
        else{
            System.out.println("*** Bookings ***");
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
            System.out.println("*** Bookings ***");
            printHeader();
            for (Booking booking : filteredBookings) {
                printBookingDetails(booking);
            }
            System.out.println("---------------------------------------------------------------------------------------------------------");
        }
    }

    public void printHeader() {
        System.out.printf("%-10s | %-15s | %-20s | %-35s | %-15s | %-15s | %-10s\n",
                "Booking Id", "Patient", "Physiotherapist", "Treatment", "Date", "Time", "Status");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
    }

    public String getStatusIcon(String status) {
        return switch (status.toLowerCase()) {
            case "attended" -> "🎉 Attended";
            case "cancelled" -> "❌ Cancelled";
            case "booked" -> "⏳ Booked";
            default -> status;
        };
    }

    private void cancelBookingByPatientId(Long patientId){
        for(Booking booking : bookings){
            if(booking.getPatientId().equals(patientId)
                    && booking.getStatus().equalsIgnoreCase("booked")){
                cancelBooking(booking);
            }
        }
    }
}
