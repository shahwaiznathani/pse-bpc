package com.mycompany.bpc;

import com.mycompany.bpc.helper.DataHelper;
import com.mycompany.bpc.models.*;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
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
        System.out.println("**Welcome to Boost Physio Clinic**");
        System.out.println("===============================");
        showDashboard();
    }

    private static void initializeData() {
        List<Patient> patientsData = DataHelper.loadPatients();
        List<Physiotherapist> physiotherapists = DataHelper.loadPhysiotherapists();
        List<Appointment> appointments = DataHelper.loadAppointments();

        bpc.bulkAddPatients(patientsData);
        bpc.bulkAddPhysiotherapists(physiotherapists);
        bpc.bulkAddAppointments(appointments);
    }

    private static void showDashboard() {
        System.out.println("===============================");
        System.out.println("**Dashboard**");
        System.out.println("===============================");
        System.out.println("1. Book an Appointment");
        System.out.println("2. Search Patient Appointments");
        System.out.println("3. Change a Booking");
        System.out.println("4. Search Physiotherapist Appointments");
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
                viewPatientAppointment();
                break;
            case 3:
                changeBooking();
                break;
            case 4:
                viewPhysiotherapistAppointment();
                break;
            case 5:
                addPatient();
                break;
            case 6:
                removePatient();
                break;
            case 7:
                attendAppointment();
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
        }
    }

    private static void bookingMenu() {
        System.out.println("\n===============================");
        System.out.println("**Appointment Booking**");
        System.out.println("===============================");
        System.out.println("1. Search by Physiotherapist");
        System.out.println("2. Search by Offered Treatments");
        System.out.println("3. Search by Area of Expertise");
        System.out.println("4. Return to Menu");
        System.out.println("===============================");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                bookByPhysiotherapist();
                break;
            case 2:
                bookByTreatmentOffered();
                break;
            case 3:
                bookByAreaOfExpertise();
                break;
            case 4:
                showDashboard();
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
        }
    }

    private static void bookByPhysiotherapist() {
        Long patientId = DataHelper.getValidId(scanner, 3000, 4000, "Enter Patient ID: ");
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

        // Display expertise areas
        List<Expertise> expertiseList = selectedPhysio.getAllExpertise();
        if (expertiseList.isEmpty()) {
            System.out.println("This physiotherapist has no expertise listed.");
            return;
        }

        System.out.println("\nAvailable Expertise Areas:");
        for (int i = 0; i < expertiseList.size(); i++) {
            System.out.println((i + 1) + ". " + expertiseList.get(i).getName());
        }

        // Let the user select an expertise
        int expertiseChoice = DataHelper.getValidNumberInput(scanner, 1, expertiseList.size(), "Enter expertise number to see treatments: ");
        Expertise selectedExpertise = expertiseList.get(expertiseChoice - 1);
        System.out.println("You selected: " + selectedExpertise.getName());

        // Show treatments under the selected expertise
        List<String> treatments = selectedExpertise.getTreatments();
        if (treatments.isEmpty()) {
            System.out.println("No treatments available under this expertise.");
            return;
        }

        System.out.println("\nAvailable Treatments:");
        for (int i = 0; i < treatments.size(); i++) {
            System.out.println((i + 1) + ". " + treatments.get(i));
        }

        // Let the user select a treatment
        int treatmentChoice = DataHelper.getValidNumberInput(scanner, 1, treatments.size(), "Enter treatment number to book: ");
        String selectedTreatment = treatments.get(treatmentChoice - 1);

        // Ask for appointment date
        Appointment selectedAppointment = new Appointment();
        while (true){
            LocalDate appointmentDate = DataHelper.getValidDate(scanner, "Enter appointment date (YYYY-MM-DD): ");

            List<Appointment> availAppointments = bpc.filterAppointmentByDate(appointmentDate, selectedPhysio.getId());
            if (availAppointments.isEmpty()) {
                System.out.println("No appointments available for " + selectedPhysio.getFullName() + " on " + appointmentDate.toString() + ". Try another Date.");
            }
            else{
                System.out.println("\nAvailable Time Slots for " + selectedPhysio.getFullName() + " on " + appointmentDate.toString());
                for (int i = 0; i < availAppointments.size(); i++) {
                    String appointmentDuration = DataHelper.getDurationAsString(availAppointments.get(i).getAppointmentTime(), availAppointments.get(i).getAppointmentDuration());
                    System.out.println((i + 1) + ". " + appointmentDuration);
                }

                // Let the user select a Slot
                int slotChoice = DataHelper.getValidNumberInput(scanner, 1, availAppointments.size(), "Enter Slot number to book: ");
                selectedAppointment = availAppointments.get(slotChoice - 1);
                break;
            }
        }

        bpc.updateAppointment(selectedAppointment, patientId, selectedTreatment);

        System.out.println("\nYou have successfully booked:");
        System.out.println("Physiotherapist: " + selectedPhysio.getFullName());
        System.out.println("Expertise: " + selectedExpertise.getName());
        System.out.println("Treatment: " + selectedTreatment);
        System.out.println("Appointment Date: " + selectedAppointment.getAppointmentDate());
        System.out.println("Appointment Time: " + selectedAppointment.getAppointmentTime());

        showDashboard();
    }

    private static void bookByTreatmentOffered() {
        Long patientId = DataHelper.getValidId(scanner, 3000, 4000, "Enter Patient ID: ");
        List<Physiotherapist> physios = bpc.getAllPhysiotherapists();
        Map<String, Set<Physiotherapist>> treatmentToPhysios = new HashMap<>();

        // Gather all unique treatments and map them to physiotherapists
        for (Physiotherapist physio : physios) {
            for (Expertise expertise : physio.getAllExpertise()) {
                for (String treatment : expertise.getTreatments()) {
                    treatmentToPhysios.computeIfAbsent(treatment, k -> new HashSet<>()).add(physio);
                }
            }
        }

        // Display all available treatments
        List<String> treatments = new ArrayList<>(treatmentToPhysios.keySet());
        if (treatments.isEmpty()) {
            System.out.println("No treatments available at the moment.");
            return;
        }

        //Search Feature
        String searchQuery = DataHelper.getStringInput(scanner, "Search for a Treatment: ");
        List<String> matchingTreatments = treatments.stream()
                .filter(t -> t.toLowerCase().contains(searchQuery))
                .collect(Collectors.toList());

        if (matchingTreatments.isEmpty()) {
            System.out.println("No matching treatments found.");
            return;
        }

        System.out.println("Matching Treatments:");
        for (int i = 0; i < matchingTreatments.size(); i++) {
            System.out.println((i + 1) + ". " + matchingTreatments.get(i));
        }

        // Get user selection
        int treatmentChoice = DataHelper.getValidNumberInput(scanner, 1, matchingTreatments.size(), "Enter your choice (1-" + matchingTreatments.size() + "): ");
        String selectedTreatment = matchingTreatments.get(treatmentChoice - 1);

        // Show physiotherapists offering the selected treatment
        List<Physiotherapist> availablePhysios = new ArrayList<>(treatmentToPhysios.get(selectedTreatment));

        System.out.println("Select a Physiotherapist who offers " + selectedTreatment + ":");
        for (int i = 0; i < availablePhysios.size(); i++) {
            System.out.println((i + 1) + ". " + availablePhysios.get(i).getFullName());
        }

        int physioChoice = DataHelper.getValidNumberInput(scanner, 1, availablePhysios.size(), "Enter your choice (1-" + availablePhysios.size() + "): ");
        Physiotherapist selectedPhysio = availablePhysios.get(physioChoice - 1);

        // Ask for appointment date
        Appointment selectedAppointment = new Appointment();
        while (true){
            LocalDate appointmentDate = DataHelper.getValidDate(scanner, "Enter appointment date (YYYY-MM-DD): ");

            List<Appointment> availAppointments = bpc.filterAppointmentByDate(appointmentDate, selectedPhysio.getId());
            if (availAppointments.isEmpty()) {
                System.out.println("No appointments available for " + selectedPhysio.getFullName() + " on " + appointmentDate.toString() + ". Try another Date.");
            }
            else{
                System.out.println("\nAvailable Time Slots for " + selectedPhysio.getFullName() + " on " + appointmentDate.toString());
                for (int i = 0; i < availAppointments.size(); i++) {
                    String appointmentDuration = DataHelper.getDurationAsString(availAppointments.get(i).getAppointmentTime(), availAppointments.get(i).getAppointmentDuration());
                    System.out.println((i + 1) + ". " + appointmentDuration);
                }

                // Let the user select a Slot
                int slotChoice = DataHelper.getValidNumberInput(scanner, 1, availAppointments.size(), "Enter Slot number to book: ");
                selectedAppointment = availAppointments.get(slotChoice - 1);
                break;
            }
        }

        bpc.updateAppointment(selectedAppointment, patientId, selectedTreatment);

        System.out.println("\nYou have successfully booked:");
        System.out.println("Physiotherapist: " + selectedPhysio.getFullName());
        System.out.println("Treatment: " + selectedTreatment);
        System.out.println("Appointment Date: " + selectedAppointment.getAppointmentDate());
        System.out.println("Appointment Time: " + selectedAppointment.getAppointmentTime());

        showDashboard();
    }

    private static void bookByAreaOfExpertise() {
        Long patientId = DataHelper.getValidId(scanner, 3000, 4000, "Enter Patient ID: ");
        List<Physiotherapist> physios = bpc.getAllPhysiotherapists();
        Map<String, Set<Physiotherapist>> expertiseToPhysios = new HashMap<>();

        // Gather all unique treatments and map them to physiotherapists
        for (Physiotherapist physio : physios) {
            for (Expertise expertise : physio.getAllExpertise()) {
                expertiseToPhysios.computeIfAbsent(expertise.getName(), k -> new HashSet<>()).add(physio);
            }
        }

        // Display all available treatments
        List<String> expertiseLst = new ArrayList<>(expertiseToPhysios.keySet());
        if (expertiseLst.isEmpty()) {
            System.out.println("No Expertise available at the moment.");
            return;
        }

        //Search Feature
        String searchQuery = DataHelper.getStringInput(scanner, "Search for a Expertise:");
        List<String> matchingExpertise = expertiseLst.stream()
                .filter(t -> t.toLowerCase().contains(searchQuery))
                .collect(Collectors.toList());

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

        System.out.println("Select a Physiotherapist who offers " + selectedExpertise + ":");
        for (int i = 0; i < availablePhysios.size(); i++) {
            System.out.println((i + 1) + ". " + availablePhysios.get(i).getFullName());
        }

        int physioChoice = DataHelper.getValidNumberInput(scanner, 1, availablePhysios.size(), "Enter your choice (1-" + availablePhysios.size() + "): ");
        Physiotherapist selectedPhysio = availablePhysios.get(physioChoice - 1);

        // Ask for appointment date
        Appointment selectedAppointment = new Appointment();
        while (true){
            LocalDate appointmentDate = DataHelper.getValidDate(scanner, "Enter appointment date (YYYY-MM-DD): ");

            List<Appointment> availAppointments = bpc.filterAppointmentByDate(appointmentDate, selectedPhysio.getId());
            if (availAppointments.isEmpty()) {
                System.out.println("No appointments available for " + selectedPhysio.getFullName() + " on " + appointmentDate.toString() + ". Try another Date.");
            }
            else{
                System.out.println("\nAvailable Time Slots for " + selectedPhysio.getFullName() + " on " + appointmentDate.toString());
                for (int i = 0; i < availAppointments.size(); i++) {
                    String appointmentDuration = DataHelper.getDurationAsString(availAppointments.get(i).getAppointmentTime(), availAppointments.get(i).getAppointmentDuration());
                    System.out.println((i + 1) + ". " + appointmentDuration);
                }

                // Let the user select a Slot
                int slotChoice = DataHelper.getValidNumberInput(scanner, 1, availAppointments.size(), "Enter Slot number to book: ");
                selectedAppointment = availAppointments.get(slotChoice - 1);
                break;
            }
        }

        bpc.updateAppointment(selectedAppointment, patientId, selectedExpertise);

        System.out.println("\nYou have successfully booked:");
        System.out.println("Physiotherapist: " + selectedPhysio.getFullName());
        System.out.println("Expertise: " + selectedExpertise);
        System.out.println("Appointment Date: " + selectedAppointment.getAppointmentDate());
        System.out.println("Appointment Time: " + selectedAppointment.getAppointmentTime());

        showDashboard();
    }

    private static void changeBooking() {
        System.out.println("\n===============================");
        System.out.println("**Update Booking**");
        System.out.println("===============================");
        System.out.println("1. Cancel an Appointment");
        System.out.println("2. Cancel and Book a new Appointment");
        System.out.println("3. Return to Menu");
        System.out.println("===============================");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                cancelPatientAppointment();
                showDashboard();
                break;
            case 2:
                cancelPatientAppointment();
                bookingMenu();
                break;
            case 3:
                showDashboard();
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
        }
    }

    private static void viewPatientAppointment() {
        Long patientId = DataHelper.getValidId(scanner, 3000, 4000, "Enter Patient ID: ");
        bpc.printAppointmentsByPatient(patientId);
        showDashboard();
    }

    public static void viewPhysiotherapistAppointment() {
        Long physioId = DataHelper.getValidId(scanner, 3000, 4000, "Enter Physiotherapist ID: ");
        bpc.printAppointmentsByPhysiotherapist(physioId);
        showDashboard();
    }

    private static void cancelPatientAppointment() {
        Long patientId = DataHelper.getValidId(scanner, 3000, 4000, "Enter Patient ID: ");
        List<Appointment> appointmentList = bpc.getBookedAppointmentsByPatientId(patientId);
        if (appointmentList.isEmpty()) {
            System.out.println("\nNo available appointments found that have been booked.");
        }
        else{
            bpc.printBookedAppointmentsByPatient(patientId);
            int appointmentChoice = DataHelper.getValidNumberInput(scanner, 1, appointmentList.size(), "Enter appointment number you want to cancel (1-" + appointmentList.size() + "): ");
            Appointment selectedAppointment = appointmentList.get(appointmentChoice - 1);
            bpc.cancelAppointment(selectedAppointment);
            System.out.println("\nYou have successfully CANCELLED an appointment");
        }
    }

    private static void attendAppointment() {
        Long patientId = DataHelper.getValidId(scanner, 3000, 4000, "Enter Patient ID: ");
        List<Appointment> appointmentList = bpc.getBookedAppointmentsByPatientId(patientId);
        if (appointmentList.isEmpty()) {
            System.out.println("\nNo available appointments found that have been booked.");
            showDashboard();
        }
        else{
            bpc.printBookedAppointmentsByPatient(patientId);
            int appointmentChoice = DataHelper.getValidNumberInput(scanner, 1, appointmentList.size(), "Enter appointment number you want to attend (1-" + appointmentList.size() + "): ");
            Appointment selectedAppointment = appointmentList.get(appointmentChoice - 1);
            bpc.attendAppointment(selectedAppointment);
            System.out.println("\nAppointment have been marked as attended.");
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
        Long patientId = DataHelper.getValidId(scanner, 3000, 4000, "Enter Patient ID for account deletion: ");
        bpc.removePatient(patientId);
        System.out.println( "Patient with Id " + patientId + " has been deleted.");
        showDashboard();
    }
}
