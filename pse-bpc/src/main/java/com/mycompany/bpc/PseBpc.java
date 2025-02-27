package com.mycompany.bpc;

import com.mycompany.bpc.helper.DataHelper;
import com.mycompany.bpc.models.*;


import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.*;
import java.util.*;
/**
 *
 * @author shahwaizshaban
 */
public class PseBpc {
    public static BookingSystem bpc = new BookingSystem();
    public static void main(String[] args) {
        initializeData();
        login();
    }

    private static void initializeData() {

        // Load Data from Files
        List<Patient> patientsData = DataHelper.loadPatients();
        List<Physiotherapist> physiotherapists = DataHelper.loadPhysiotherapists();

        bpc.bulkAddPatients(patientsData);
        bpc.bulkAddPysiotherapists(physiotherapists);


    }

    private static void login() {
        System.out.println("\n🌟 Welcome to Boost Physio Clinic 🌟\n");

        Scanner scanner = new Scanner(System.in);

        System.out.println("\n===============================");
        System.out.println("🔐 Login to Boost Physio Clinic");
        System.out.println("===============================");
        System.out.print("🔹 Enter your User ID: ");

        String userId = scanner.nextLine();

        System.out.print("🔹 Enter Password: ");

        String password = scanner.nextLine();

        Optional<Patient> patient = bpc.getAllPatients().stream().filter(p -> p.getId().equals(userId) && p.getPasswrod().equals(password)).findFirst();
        Optional<Physiotherapist> physio = bpc.getAllPhysiotherapists().stream().filter(p -> p.getId().equals(userId) && p.getPasswrod().equals(password)).findFirst();

        if (patient.isPresent()) {
            Patient patientData = patient.get();
            String patientId = patientData.getId();
            System.out.println("✅ Login Successful! Welcome, " + patient.get().getFullName());
            showPatientMenu(patientId);
        }
        else if (physio.isPresent()) {
            System.out.println("✅ Login Successful! Welcome, " + physio.get().getFullName());
            //showPhysiotherapistMenu(physio.get());
        }
        else {
            System.out.println("❌ Invalid Credentials! Please try again.");
            login();
        }
    }

    public static void showPatientMenu(String patientId) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n===============================");
        System.out.println("🏥 Patient Dashboard");
        System.out.println("===============================");
        System.out.println("1️⃣ Book an Appointment");
        System.out.println("2️⃣ View my Appointments");
        System.out.println("3️⃣ Cancel an Appointment");
        System.out.println("4️⃣ Logout");
        System.out.println("===============================");
        System.out.print("🔹 Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1:
                bookingMenu(patientId);
                break;
            case 2:
                viewPatientAppointment(patientId);
                break;
            case 3:
                //cancelAppointment(scanner);
                break;
            case 4:
                System.out.println("\n👋 Thank you for using Boost Physio Clinic! Goodbye!");
                scanner.close();
                return;
            default:
                System.out.println("❌ Invalid choice. Please try again.");
        }
    }

    public static void bookingMenu(String patientId) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n===============================");
        System.out.println("🏥 Appointment Booking");
        System.out.println("===============================");
        System.out.println("1️⃣ Search by Physiotherapist");
        System.out.println("2️⃣ Search by Offered Treatments");
        System.out.println("3️⃣ Return to Menu");
        System.out.println("4️⃣ Logout");
        System.out.println("===============================");
        System.out.print("🔹 Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1:
                bookByPhysiotherapist(patientId);
                break;
            case 2:
                //viewAppointments();
                break;
            case 3:
                showPatientMenu(patientId);
                break;
            case 4:
                System.out.println("\n👋 Thank you for using Boost Physio Clinic! Goodbye!");
                scanner.close();
                return;
            default:
                System.out.println("❌ Invalid choice. Please try again.");
        }
    }

    public static void bookByPhysiotherapist(String patientId) {
        List<Physiotherapist> lst = bpc.getAllPhysiotherapists();

        // Step 1: Print physiotherapists
        System.out.println("Select a Physiotherapist by entering the corresponding number:");
        for (int i = 0; i < lst.size(); i++) {
            System.out.println((i + 1) + ". " + lst.get(i).getFullName());  // Assuming getFullName() returns the name
        }

        // Step 2: Get user selection
        Scanner scanner = new Scanner(System.in);
        int choice = getValidNumberInput(scanner, 1, lst.size(), "Enter your choice (1-" + lst.size() + "): ");

        // Get the selected physiotherapist
        Physiotherapist selectedPhysio = lst.get(choice - 1);
        System.out.println("You selected: " + selectedPhysio.getFullName());

        // Step 3: Display expertise areas
        List<Expertise> expertiseList = selectedPhysio.getAllExpertise();
        if (expertiseList.isEmpty()) {
            System.out.println("This physiotherapist has no expertise listed.");
            return;
        }

        System.out.println("\nAvailable Expertise Areas:");
        for (int i = 0; i < expertiseList.size(); i++) {
            System.out.println((i + 1) + ". " + expertiseList.get(i).getName());
        }

        // Step 4: Let the user select an expertise
        int expertiseChoice = getValidNumberInput(scanner, 1, expertiseList.size(), "Enter expertise number to see treatments: ");
        Expertise selectedExpertise = expertiseList.get(expertiseChoice - 1);
        System.out.println("You selected: " + selectedExpertise.getName());

        // Step 5: Show treatments under the selected expertise
        List<String> treatments = selectedExpertise.getTreatments();
        if (treatments.isEmpty()) {
            System.out.println("No treatments available under this expertise.");
            return;
        }

        System.out.println("\nAvailable Treatments:");
        for (int i = 0; i < treatments.size(); i++) {
            System.out.println((i + 1) + ". " + treatments.get(i));
        }

        // Step 6: Let the user select a treatment
        int treatmentChoice = getValidNumberInput(scanner, 1, treatments.size(), "Enter treatment number to book: ");
        String selectedTreatment = treatments.get(treatmentChoice - 1);

        // Step 7: Ask for appointment date
        LocalDate appointmentDate = getValidDate(scanner, "Enter appointment date (YYYY-MM-DD): ");

        // Step 8: Ask for appointment time
        LocalTime appointmentTime = getValidTime(scanner, "Enter appointment time (HH:MM in 24-hour format): ");

        Appointment newAppointment = new Appointment(appointmentDate, appointmentTime,
               LocalDateTime.now(), selectedPhysio.getId(), patientId,selectedTreatment, "Scheduled");

        bpc.bookAppointment(newAppointment);

        System.out.println("\nYou have successfully booked:");
        System.out.println("Physiotherapist: " + selectedPhysio.getFullName());
        System.out.println("Expertise: " + selectedExpertise.getName());
        System.out.println("Treatment: " + selectedTreatment);
        System.out.println("Appointment Date: " + appointmentDate);
        System.out.println("Appointment Time: " + appointmentTime);

        showPatientMenu(patientId);
    }


    private static int getValidNumberInput(Scanner scanner, int min, int max, String prompt) {
        int choice = -1;
        while (choice < min || choice > max) {
            System.out.print(prompt);
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < min || choice > max) {
                    System.out.println("Invalid choice. Please select a valid number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return choice;
    }

    private static LocalDate getValidDate(Scanner scanner, String prompt) {
        LocalDate date = null;
        while (date == null) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                date = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("The date cannot be in the past. Please enter a future date.");
                    date = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
        return date;
    }

    private static LocalTime getValidTime(Scanner scanner, String prompt) {
        LocalTime time = null;
        while (time == null) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                time = LocalTime.parse(input, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please use HH:MM in 24-hour format.");
            }
        }
        return time;
    }

    public static void viewPatientAppointment(String patientId) {
        bpc.printAppointmentsByPatient(patientId);
        showPatientMenu(patientId);
    }
}
