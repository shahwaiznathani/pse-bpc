package com.mycompany.bpc;

import com.mycompany.bpc.helper.DataHelper;
import com.mycompany.bpc.models.*;

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
        System.out.println("8. Exit Program");
        System.out.println("===============================");

        int choice = DataHelper.getValidNumberInput(scanner, 1, 8, "Enter your choice (1-" + 8 + "): ");

        switch (choice) {
            case 1:
                bookingMenu(null);
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
            case 8:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
                showDashboard();
        }
    }

    private static void bookingMenu(String bookingId) {
        System.out.println("\n===============================");
        System.out.println("**Appointment Booking**");
        System.out.println("===============================");
        System.out.println("1. Search by Physiotherapist");
        System.out.println("2. Search by Area of Expertise");
        System.out.println("3. Return to Menu");
        System.out.println("===============================");

        int choice = DataHelper.getValidNumberInput(scanner, 1, 3, "Enter your choice (1-" + 3 + "): ");

        switch (choice) {
            case 1:
                bookAppointmentByPhysiotherapist(bookingId);
                break;
            case 2:
                bookAppointmentByAreaOfExpertise(bookingId);
                break;
            case 3:
                showDashboard();
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
                bookingMenu(null);
        }
    }

    private static void bookAppointmentByPhysiotherapist(String bookingId) {
        if(bookingId != null){
            bpc.bookByPhysiotherapist(null, bookingId);
        }
        else{
            Long patientId = DataHelper.getValidId(scanner, 2000000, 9000000, "Enter Patient ID: ");
            bpc.bookByPhysiotherapist(patientId, null);
        }
        showDashboard();
    }

    private static void bookAppointmentByAreaOfExpertise(String bookingId) {
        if(bookingId != null){
            bpc.bookByAreaOfExpertise(null, bookingId);
        }
        else{
            Long patientId = DataHelper.getValidId(scanner, 2000000, 9000000, "Enter Patient ID: ");
            bpc.bookByAreaOfExpertise(patientId, null);
        }
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

        int choice = DataHelper.getValidNumberInput(scanner, 1, 3, "Enter your choice (1-" + 3 + "): ");

        switch (choice) {
            case 1:
                cancelPatientBooking();
                showDashboard();
                break;
            case 2:
                String bookingId = cancelPatientBooking();
                if(bookingId!=null){
                    bookingMenu(bookingId);
                }
                else{
                    showDashboard();
                }
                break;
            case 3:
                showDashboard();
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
                changeBooking();
        }
    }

    private static void viewPatientBookings() {
        Long patientId = DataHelper.getValidId(scanner, 2000000, 9000000, "Enter Patient ID: ");
        bpc.printBookingsByPatient(patientId);
        showDashboard();
    }

    private static void viewPhysiotherapistABookings() {
        Long physioId = DataHelper.getValidId(scanner, 1000000, 2000000, "Enter Physiotherapist ID: ");
        bpc.printBookingsByPhysiotherapist(physioId);
        showDashboard();
    }

    private static String cancelPatientBooking() {
        String bookingId = DataHelper.getValidBookingId(scanner,"Enter Booking ID for Cancellation: ");
        Booking booking = bpc.getBookingById(bookingId);
        if (booking == null) {
            System.out.println("\nNo available booking found for entered ID");
        }
        else{
            bpc.printHeader();
            bpc.printBookingDetails(booking);
            if(booking.getStatus().equalsIgnoreCase("cancelled") ||
                    booking.getStatus().equalsIgnoreCase("attended") ){
                System.out.println("\nCannot CANCEL/UPDATE a booking that has already been marked Cancelled/Attended");
            }
            else{
                boolean confirmation = DataHelper.getYesOrNo(scanner, "Are you sure you want to cancel the above booking?");
                if(confirmation){
                    bpc.cancelBooking(booking);
                    System.out.println("\nYou have successfully CANCELLED a booking with id " + booking.getId() + ".");
                    return bookingId;
                }
            }
        }
        return null;
    }

    private static void attendBooking() {
        String bookingId = DataHelper.getValidBookingId(scanner,"Enter Booking ID to mark Attendance: ");
        Booking booking = bpc.getBookingById(bookingId);
        if (booking == null) {
            System.out.println("\nNo available booking found for entered ID");
        }
        else{
            bpc.printHeader();
            bpc.printBookingDetails(booking);;
            if(booking.getStatus().equalsIgnoreCase("cancelled") ||
                booking.getStatus().equalsIgnoreCase("attended") ){
                System.out.println("\nCannot ATTEND a booking that has already been marked Cancelled/Attended");
            }
            else{
                boolean confirmation = DataHelper.getYesOrNo(scanner, "Are you sure you want to attend the above booking?");
                if(confirmation){
                    bpc.attendBooking(booking);
                    System.out.println("\nYou have successfully Attended a booking with id " + booking.getId() + ".");
                }
            }
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
        Long patientId = DataHelper.getValidId(scanner, 2000000, 9000000, "Enter Patient ID for account deletion: ");
        bpc.removePatient(patientId);
        System.out.println( "Patient with Id " + patientId + " has been deleted.");
        showDashboard();
    }
}
