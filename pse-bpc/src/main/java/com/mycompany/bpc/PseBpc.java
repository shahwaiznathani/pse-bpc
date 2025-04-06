package com.mycompany.bpc;

import com.mycompany.bpc.helper.DataHelper;
import com.mycompany.bpc.models.*;

import java.time.*;
import java.util.*;
/**
 *
 * @author shahwaizshaban
 */
public class PseBpc {
    public static BookingSystem bpc = new BookingSystem();
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeData();
        System.out.println("===============================");
        System.out.println("**Boost Physio Clinic**");
        System.out.println("===============================");
        showDashboard();
    }

    private static void initializeData() {
        List<Patient> patientsData = DataHelper.loadPatients();
        List<Physiotherapist> physiotherapists = DataHelper.loadPhysiotherapists();
        List<Treatment> treatments = DataHelper.loadTreatments();

        bpc.bulkAddPatients(patientsData);
        bpc.bulkAddPhysiotherapists(physiotherapists);
        bpc.bulkAddTreatments(treatments);
    }

    private static void showDashboard() {
        System.out.println("===============================");
        System.out.println("**Dashboard**");
        System.out.println("===============================");
        System.out.println("1. Book an Appointment");
        System.out.println("2. Search Patient Bookings");
        System.out.println("3. Change a Booking");
        System.out.println("4. Search Physiotherapist Bookings");
        System.out.println("5. Add a Patient");
        System.out.println("6. Delete a Patient");
        System.out.println("7. Attend an Appointment");
        System.out.println("===============================");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                bookingMenu();
                break;
            case 2:
                viewPatientBookings();
                break;
            case 3:
                changeBooking();
                break;
            case 4:
                viewPhysiotherapistABookings();
                break;
            case 5:
                addPatient();
                break;
            case 6:
                removePatient();
                break;
            case 7:
                attendBooking();
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
                showDashboard();
        }
    }

    private static void bookingMenu() {
        System.out.println("\n===============================");
        System.out.println("**Appointment Booking**");
        System.out.println("===============================");
        System.out.println("1. Search by Physiotherapist");
        System.out.println("2. Search by Area of Expertise");
        System.out.println("3. Return to Menu");
        System.out.println("===============================");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                bookByPhysiotherapist();
                break;
            case 2:
                bookByAreaOfExpertise();
                break;
            case 3:
                showDashboard();
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
                bookingMenu();
        }
    }

    private static void bookByPhysiotherapist() {
        Long patientId = DataHelper.getValidId(scanner, 1000000, 2000000, "Enter Patient ID: ");
        Physiotherapist selectedPhysio = new Physiotherapist();
        while (true) {
            String input = DataHelper.getStringInput(scanner, "Search Physiotherapist by name (e.g., Dr Shahwaiz): ");
            List<Physiotherapist> lst = bpc.searchPhysiotherapist(input);

            if (lst.isEmpty()) {
                System.out.println("No physiotherapist found. Please try again.");
            }
            else {
                // Print physiotherapists
                for (int i = 0; i < lst.size(); i++) {
                    System.out.println((i + 1) + ". " + lst.get(i).getFullName());
                }
                // Get user selection
                int choice = DataHelper.getValidNumberInput(scanner, 1, lst.size(), "Enter your choice (1-" + lst.size() + "): ");

                // Get the selected physiotherapist
                selectedPhysio = lst.get(choice - 1);
                System.out.println("You selected: " + selectedPhysio.getFullName());
                break; // Exit loop if physiotherapists are found
            }
        }

        // Ask for appointment date
        Treatment selectedTreatment = new Treatment();
        while (true){
            LocalDate appointmentDate = DataHelper.getValidDate(scanner, "Enter appointment date (YYYY-MM-DD): ");

            List<Treatment> availTreatments = bpc.filterTreatmentsByDate(appointmentDate, selectedPhysio.getId());

            if (availTreatments.isEmpty()) {
                System.out.println("No appointments available for " + selectedPhysio.getFullName() + " on " + appointmentDate.toString() + ". Try another Date.");
            }
            else{
                bpc.printCustomTreatments(availTreatments);
                // Let the user select a Slot
                int slotChoice = DataHelper.getValidNumberInput(scanner, 1, availTreatments.size(), "Enter Slot number to book: ");
                selectedTreatment = availTreatments.get(slotChoice - 1);
                break;
            }
        }

        Booking newBooking = new Booking(selectedTreatment.getAppointmentDate(), selectedTreatment.getAppointmentTime(),
                selectedTreatment.getAppointmentDuration(), selectedTreatment.getPhysiotherapistId(), patientId,
                "Booked", selectedTreatment.getName(), selectedTreatment.getId());
        bpc.addBooking(newBooking);
        bpc.updateTreatmentStatus(selectedTreatment, "Booked");

        System.out.println("Booking confirm with Id: " + newBooking.getId());

        showDashboard();
    }

    private static void bookByAreaOfExpertise() {
        Long patientId = DataHelper.getValidId(scanner, 1000000, 2000000, "Enter Patient ID: ");
        List<Physiotherapist> physios = bpc.getAllPhysiotherapists();
        Map<String, Set<Physiotherapist>> expertiseToPhysios = new HashMap<>();

        // Gather all unique treatments and map them to physiotherapists
        for (Physiotherapist physio : physios) {
            for (String expertise : physio.getAllExpertise()) {
                expertiseToPhysios.computeIfAbsent(expertise, k -> new HashSet<>()).add(physio);
            }
        }

        // Display all available treatments
        List<String> expertiseLst = new ArrayList<>(expertiseToPhysios.keySet());

        //Search Feature
        String searchQuery = DataHelper.getStringInput(scanner, "Search for a Expertise:");
        List<String> matchingExpertise = expertiseLst.stream()
                .filter(t -> t.toLowerCase().contains(searchQuery))
                .toList();

        if (matchingExpertise.isEmpty()) {
            System.out.println("No matching treatments found.");
            return;
        }

        System.out.println("Matching Treatments:");
        for (int i = 0; i < matchingExpertise.size(); i++) {
            System.out.println((i + 1) + ". " + matchingExpertise.get(i));
        }

        // Get user selection
        int treatmentChoice = DataHelper.getValidNumberInput(scanner, 1, matchingExpertise.size(), "Enter your choice (1-" + matchingExpertise.size() + "): ");
        String selectedExpertise = matchingExpertise.get(treatmentChoice - 1);

        // Show physiotherapists offering the selected treatment
        List<Physiotherapist> availablePhysios = new ArrayList<>(expertiseToPhysios.get(selectedExpertise));

        Treatment selectedTreatment = new Treatment();
        while (true){
            LocalDate appointmentDate = DataHelper.getValidDate(scanner, "Enter treatment date (YYYY-MM-DD): ");

            List<Treatment> availTreatments = bpc.filterTreatmentsByDateAndPhysiotherapists(appointmentDate, availablePhysios);
            if (availTreatments.isEmpty()) {
                System.out.println("No treatments available on " + appointmentDate.toString() + ". Try another Date.");
            }
            else {
                bpc.printCustomTreatments(availTreatments);
                // Let the user select a Slot
                int slotChoice = DataHelper.getValidNumberInput(scanner, 1, availTreatments.size(), "Enter Slot number to book: ");
                selectedTreatment = availTreatments.get(slotChoice - 1);
                break;
            }
        }

        Booking newBooking = new Booking(selectedTreatment.getAppointmentDate(), selectedTreatment.getAppointmentTime(),
                selectedTreatment.getAppointmentDuration(), selectedTreatment.getPhysiotherapistId(), patientId,
                "Booked", selectedTreatment.getName(), selectedTreatment.getId());
        bpc.addBooking(newBooking);
        bpc.updateTreatmentStatus(selectedTreatment, "Booked");

        System.out.println("Booking confirm with Id: " + newBooking.getId());

        showDashboard();
    }

    private static void changeBooking() {
        System.out.println("\n===============================");
        System.out.println("**Update Booking**");
        System.out.println("===============================");
        System.out.println("1. Cancel a Booking");
        System.out.println("2. Cancel and Book a new Treatment");
        System.out.println("3. Return to Menu");
        System.out.println("===============================");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                cancelPatientBooking();
                showDashboard();
                break;
            case 2:
            /*    cancelPatientBooking();
                bookingMenu();*/
                break;
            case 3:
                showDashboard();
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
                changeBooking();
        }
    } //add cancel and book new logic

    private static void viewPatientBookings() {
        Long patientId = DataHelper.getValidId(scanner, 1000000, 2000000, "Enter Patient ID: ");
        bpc.printBookingsByPatient(patientId);
        showDashboard();
    } //set treatment id in booking for releasing on cancel

    private static void viewPhysiotherapistABookings() {
        Long physioId = DataHelper.getValidId(scanner, 2000000, 3000000, "Enter Physiotherapist ID: ");
        bpc.printBookingsByPhysiotherapist(physioId);
        showDashboard();
    }

    private static void cancelPatientBooking() {
        Long patientId = DataHelper.getValidId(scanner, 1000000, 2000000, "Enter Patient ID: ");
        List<Booking> bookingList = bpc.getBookingsByPatientId(patientId);
        if (bookingList.isEmpty()) {
            System.out.println("\nNo available booking found that have been booked.");
        }
        else{
            bpc.printBookingsByPatient(patientId);
            int appointmentChoice = DataHelper.getValidNumberInput(scanner, 1, bookingList.size(), "Enter booking number you want to cancel (1-" + bookingList.size() + "): ");
            Booking selectedBooking = bookingList.get(appointmentChoice - 1);
            bpc.cancelBooking(selectedBooking);
            System.out.println("\nYou have successfully CANCELLED a booking with id " + selectedBooking.getId() + ".");
        }
    }

    private static void attendBooking() {
        Long patientId = DataHelper.getValidId(scanner, 1000000, 2000000, "Enter Patient ID: ");
        List<Booking> bookingList = bpc.getBookingsByPatientId(patientId);
        if (bookingList.isEmpty()) {
            System.out.println("\nNo available bookings found that have been booked.");
            showDashboard();
        }
        else{
            bpc.printBookingsByPatient(patientId);
            int bookingChoice = DataHelper.getValidNumberInput(scanner, 1, bookingList.size(), "Enter booking number you want to attend (1-" + bookingList.size() + "): ");
            Booking selectedBooking = bookingList.get(bookingChoice - 1);
            bpc.attendBooking(selectedBooking);
            System.out.println("\nBooking have been marked as attended.");
            showDashboard();
        }
    }

    private static void addPatient(){
        String name = DataHelper.getStringInput(scanner, "Enter Patient's Full Name: ");
        String address = DataHelper.getStringInput(scanner, "Enter Patient's Address: ");
        String phoneNumber = DataHelper.getStringInput(scanner, "Enter Patient's Mobile Number: ");
        Long id = bpc.GetNewPatientId();
        Patient newPatient = new Patient(id, name, address, phoneNumber);
        bpc.addPatient(newPatient);
        System.out.println(name + " has been added with Id: "+ id + ".");
        showDashboard();
    }

    private static void removePatient(){
        Long patientId = DataHelper.getValidId(scanner, 1000000, 2000000, "Enter Patient ID for account deletion: ");
        bpc.removePatient(patientId);
        System.out.println( "Patient with Id " + patientId + " has been deleted.");
        showDashboard();
    }
}
