package com.mycompany.bpc.helper;
import com.mycompany.bpc.models.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class DataHelper {
    // Get file path inside the resources folder
    private static final String PATIENTS_FILE = "/Users/shahwaizshaban/Desktop/resources/patients.txt";
    private static final String PHYSIOTHERAPISTS_FILE = "/Users/shahwaizshaban/Desktop/resources/physiotherapists.txt";

    // ✅ Read Patients from File
    public static List<Patient> loadPatients() {
        List<Patient> patients = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATIENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                patients.add(new Patient(data[0], data[1], data[2], data[3], data[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patients;
    }

    // ✅ Read Physiotherapists from File

    public static List<Physiotherapist> loadPhysiotherapists() {
        List<Physiotherapist> physiotherapists = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(PHYSIOTHERAPISTS_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Split the line into its parts: ID, name, address, phone, password, expertise with treatments
                String[] data = line.split(",");

                if (data.length < 6) continue;  // Skip invalid lines (e.g., less than 6 parts)

                // Read basic details
                String id = data[0].trim();
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
                    List<String> treatments = Arrays.asList(expertiseParts[1].split("\\|"));

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
}
