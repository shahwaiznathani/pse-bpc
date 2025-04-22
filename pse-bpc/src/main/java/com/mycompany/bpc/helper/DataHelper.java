package com.mycompany.bpc.helper;
import com.mycompany.bpc.models.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class DataHelper {
    private static final String PATIENTS_FILE = "data/patients.txt";
    private static final String PHYSIOTHERAPISTS_FILE = "data/physiotherapists.txt";
    private static final String APPOINTMENTS_FILE = "data/appointments.txt";

    public static List<Patient> loadPatients() {
        List<Patient> patients = new ArrayList<>();
        try (InputStream inputStream = DataHelper.class.getClassLoader().getResourceAsStream(PATIENTS_FILE)) {
            if (inputStream == null) {
                System.out.println("File not found: " + PATIENTS_FILE);
                return patients;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                patients.add(new Patient(Long.valueOf(data[0]), data[1], data[2], data[3]));
            }

        }
        catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return patients;
    }

    public static List<Physiotherapist> loadPhysiotherapists() {
        List<Physiotherapist> physiotherapists = new ArrayList<>();

        try (InputStream inputStream = DataHelper.class.getClassLoader().getResourceAsStream(PHYSIOTHERAPISTS_FILE)) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] data = line.split(",");

                        if (data.length < 5) continue;

                        Long id = Long.valueOf(data[0].trim());
                        String fullName = data[1].trim();
                        String address = data[2].trim();
                        String phoneNumber = data[3].trim();
                        String expertiseString = data[4].trim();

                        String[] expertiseArray = expertiseString.split("\\|");
                        List<String> expertiseList = new ArrayList<>();
                        for (String expertise : expertiseArray) {
                            expertiseList.add(expertise.trim());
                        }

                        Physiotherapist physiotherapist = new Physiotherapist(id, fullName, address, phoneNumber, expertiseList);
                        physiotherapists.add(physiotherapist);
                    }
            }
        }
        catch (IOException | NullPointerException e) {
            System.out.println("Error loading physiotherapists: " + e.getMessage());
        }

        return physiotherapists;
    }

    public static List<Treatment> loadTreatments() {
        List<Treatment> treatments = new ArrayList<>();

        try (InputStream inputStream = DataHelper.class.getClassLoader().getResourceAsStream(APPOINTMENTS_FILE)) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");

                    if (data.length < 7) continue;

                    LocalDate appointmentDate = LocalDate.parse(data[0].trim());
                    LocalTime appointmentTime = LocalTime.parse(data[1].trim());
                    Long appointmentDuration = Long.valueOf(data[2].trim());
                    Long physiotherapistId = Long.valueOf(data[3].trim());
                    String status = data[4].trim();
                    String treatmentName = data[5].trim();
                    String expertise = data[6].trim();

                    treatments.add(new Treatment(appointmentDate, appointmentTime, appointmentDuration, physiotherapistId, status, treatmentName, expertise));
                }
            }
        }
        catch (IOException | NullPointerException e) {
            System.out.println("Error loading treatments: " + e.getMessage());
        }

        return treatments;
    }

    public static int getValidNumberInput(Scanner scanner, int min, int max, String prompt) {
        int choice = -1;
        while (choice < min || choice > max) {
            System.out.print(prompt);
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < min || choice > max) {
                    System.out.println("Invalid choice. Please enter a valid number.");
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return choice;
    }

    public static long getValidId(Scanner scanner, int min, int max, String prompt) {
        long choice = -1;
        while (choice < min || choice > max) {
            System.out.print(prompt);
            try {
                choice = Long.parseLong(scanner.nextLine());
                if (choice < min || choice > max) {
                    System.out.println("Invalid choice. Please select a valid ID.");
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return choice;
    }

    public static LocalDate getValidMonthYear(Scanner scanner, String prompt) {
        LocalDate date = null;
        while (date == null) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                // Parse only year and month, assume day = 1
                date = LocalDate.parse(input + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            catch (DateTimeParseException e) {
                System.out.println("Invalid format. Please use YYYY-MM.");
            }
        }
        return date;
    }

    public static String getStringInput(Scanner scanner, String prompt){
        System.out.println(prompt);
        String inputString = scanner.nextLine();
        return inputString.toLowerCase();
    }

    public static String getDurationAsString(LocalTime time, long duration) {
        try{
            return time.toString() + " - " + time.plusMinutes(duration).toString();
        }
        catch(Exception e){
            System.out.println("Error loading treatments: " + e.getMessage());
            return null;
        }
    }

    public static String getValidBookingId(Scanner scanner, String prompt) {
        String bookingId = "";
        while (true) {
            System.out.print(prompt);
            bookingId = scanner.nextLine().trim();
            if (bookingId.matches("B-[1-9]\\d*")) {
                break;
            } else {
                System.out.println("Invalid Booking ID. Format must be: B-{PositiveNumber} (e.g., B-123)");
            }
        }
        return bookingId;
    }

    public static boolean getYesOrNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y")) {
                return true;
            } else if (input.equals("N")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'Y' for yes or 'N' for no.");
            }
        }
    }

    public static void printBanner(){
        System.out.println("===============================");
        System.out.println("***Boost Physio Clinic***");
        System.out.println("===============================");
    }

    public static void printDashboard(){
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
        System.out.println("8. View Clinic Report");
        System.out.println("9. Exit Program");
        System.out.println("===============================");
    }

    public static void printBookingMenu(){
        System.out.println("===============================");
        System.out.println("**Appointment Booking**");
        System.out.println("===============================");
        System.out.println("1. Search by Physiotherapist");
        System.out.println("2. Search by Area of Expertise");
        System.out.println("3. Return to Menu");
        System.out.println("===============================");
    }

    public static void printChangeBookingMenu(){
        System.out.println("===============================");
        System.out.println("**Update Booking**");
        System.out.println("===============================");
        System.out.println("1. Cancel a Booking");
        System.out.println("2. Cancel and Book a new Treatment");
        System.out.println("3. Return to Menu");
        System.out.println("===============================");
    }
}
