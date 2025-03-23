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
                patients.add(new Patient(Long.valueOf(data[0]), data[1], data[2], data[3], data[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
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

                if (data.length < 6) continue;  // Skip invalid lines (e.g., less than 6 parts)

                // Read basic details
                Long id = Long.valueOf(data[0].trim());
                String fullName = data[1].trim();
                String address = data[2].trim();
                String phoneNumber = data[3].trim();
                String password = data[4].trim();

                // Process expertise and treatments
                List<Expertise> expertiseList = new ArrayList<>();

                // Iterate over each expertise/treatment pair (starting from the 5th element in the data array)
                for (int i = 5; i < data.length; i++) {
                    String expertiseWithTreatments = data[i].trim();

                    // Split expertise and its treatments
                    String[] expertiseParts = expertiseWithTreatments.split(":");
                    if (expertiseParts.length < 2) continue;  // Skip invalid expertise data

                    String expertiseName = expertiseParts[0].trim();
                    String[] rawTreatments = expertiseParts[1].split("\\|");
                    List<String> treatments = new ArrayList<>();

                    for (String treatment : rawTreatments) {
                        treatments.add(treatment.trim());
                    }

                    // Create an Expertise object and add it to the list
                    expertiseList.add(new Expertise(expertiseName, treatments));
                }

                // Create and add a new Physiotherapist to the list
                Physiotherapist physiotherapist = new Physiotherapist(id, fullName, address, phoneNumber, password, expertiseList);
                physiotherapists.add(physiotherapist);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return physiotherapists;
    }

    public static List<Appointment> loadAppointments() {
        List<Appointment> appointments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(APPOINTMENTS_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Split the line into its parts: appointmentDate, appointmentTime, physiotherapistId, patientId, status
                String[] data = line.split(",");

                if (data.length < 5) continue;  // Skip invalid lines

                // Read details and trim whitespace
                LocalDate appointmentDate = LocalDate.parse(data[0].trim());
                LocalTime appointmentTime = LocalTime.parse(data[1].trim());
                Long appointmentDuration = Long.valueOf(data[2].trim());
                Long physiotherapistId = Long.valueOf(data[3].trim());
                String status = data[4].trim();

                // Create and add an Appointment object
                appointments.add(new Appointment(appointmentDate, appointmentTime, appointmentDuration, physiotherapistId, status));
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                    System.out.println("Invalid choice. Please select a valid number.");
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

    public static LocalTime getValidTime(Scanner scanner, String prompt) {
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

    public static String getDurationAsString(LocalTime time, long duration) {
        return time.toString() + " - " + time.plusMinutes(duration).toString();
    }
}
