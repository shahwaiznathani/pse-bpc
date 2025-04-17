package com.mycompany.bpc.helper;
import com.mycompany.bpc.models.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class DataHelper {
    private static final String PATIENTS_FILE = "/Users/shahwaizshaban/Desktop/resources/patients.txt";
    private static final String PHYSIOTHERAPISTS_FILE = "/Users/shahwaizshaban/Desktop/resources/physiotherapists.txt";
    private static final String APPOINTMENTS_FILE = "/Users/shahwaizshaban/Desktop/resources/appointments.txt";

    public static List<Patient> loadPatients() {
        List<Patient> patients = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATIENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                patients.add(new Patient(Long.valueOf(data[0]), data[1], data[2], data[3]));
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return patients;
    }

    public static List<Physiotherapist> loadPhysiotherapists() {
        List<Physiotherapist> physiotherapists = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(PHYSIOTHERAPISTS_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Split the line into its parts: ID, name, address, phone, password, expertise with treatments
                String[] data = line.split(",");

                if (data.length < 5) continue;  // Skip invalid lines (e.g., less than 6 parts)

                // Read basic details
                Long id = Long.valueOf(data[0].trim());
                String fullName = data[1].trim();
                String address = data[2].trim();
                String phoneNumber = data[3].trim();

                // Extract the expertise list, which is the last element in the data array
                String expertiseString = data[4].trim();

                // Split the expertise by the '|' character and add to the list
                String[] expertiseArray = expertiseString.split("\\|");
                List<String> expertiseList = new ArrayList<>();

                // Add each expertise into the list (trim any whitespace)
                for (String expertise : expertiseArray) {
                    expertiseList.add(expertise.trim());
                }

                // Create and add a new Physiotherapist to the list
                Physiotherapist physiotherapist = new Physiotherapist(id, fullName, address, phoneNumber, expertiseList);
                physiotherapists.add(physiotherapist);
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return physiotherapists;
    }

    public static List<Treatment> loadTreatments() {
        List<Treatment> appointments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(APPOINTMENTS_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Split the line into its parts: appointmentDate, appointmentTime, physiotherapistId, patientId, status
                String[] data = line.split(",");

                if (data.length < 7) continue;  // Skip invalid lines

                // Read details and trim whitespace
                LocalDate appointmentDate = LocalDate.parse(data[0].trim());
                LocalTime appointmentTime = LocalTime.parse(data[1].trim());
                Long appointmentDuration = Long.valueOf(data[2].trim());
                Long physiotherapistId = Long.valueOf(data[3].trim());
                String status = data[4].trim();
                String treatmentName = data[5].trim();
                String expertise = data[6].trim();

                // Create and add an Appointment object
                appointments.add(new Treatment(appointmentDate, appointmentTime, appointmentDuration, physiotherapistId, status, treatmentName, expertise));
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return appointments;
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
            } catch (NumberFormatException e) {
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
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return choice;
    }

    public static LocalDate getValidDate(Scanner scanner, String prompt) {
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

    public static String getStringInput(Scanner scanner, String prompt){
        System.out.println(prompt);
        String inputString = scanner.nextLine();
        return inputString.toLowerCase();
    }

    public static String getDurationAsString(LocalTime time, long duration) {
        return time.toString() + " - " + time.plusMinutes(duration).toString();
    }

    public static String getValidBookingId(Scanner scanner, String prompt) {
        String bookingId = "";
        while (true) {
            System.out.print(prompt);
            bookingId = scanner.nextLine().trim();

            // Validate using regex: B- followed by one or more digits
            if (bookingId.matches("B-[1-9]\\d*")) {
                break;
            } else {
                System.out.println("Invalid Booking ID. Format must be: B-{PositiveNumber} (e.g., B-123)");
            }
        }
        return bookingId;
    }

    public static boolean getYesOrNo(Scanner scanner, String prompt) {
        String input = "";
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("Y")) {
                return true;
            } else if (input.equals("N")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'Y' for yes or 'N' for no.");
            }
        }
    }
}
