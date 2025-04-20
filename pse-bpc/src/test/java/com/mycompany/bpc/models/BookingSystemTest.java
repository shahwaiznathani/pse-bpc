package com.mycompany.bpc.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;

class BookingSystemTest {
    private BookingSystem bookingSystem;

    @BeforeEach
    void setUp() {
        bookingSystem = new BookingSystem();
        bookingSystem.initializeData();
    }

    @Test
    void testAttendBooking() {
        Booking booking1 = new Booking(
                LocalDate.of(2025, 5, 1),
                LocalTime.of(10, 0),
                50L,
                1000001L,
                2000001L,
                "Booked",
                "Strength & Conditioning Plans",
                "T-1");
        Booking booking2 = new Booking(
                LocalDate.of(2025, 5, 1),
                LocalTime.of(10, 0),
                50L,
                1000001L,
                2000001L,
                "Cancelled",
                "Strength & Conditioning Plans",
                null);
        bookingSystem.addBooking(booking1);
        bookingSystem.addBooking(booking2);
        bookingSystem.attendBooking(booking1);
        bookingSystem.attendBooking(booking2);

        assertEquals("Attended", booking1.getStatus());
        assertEquals("Cancelled", booking2.getStatus());
    }

    @Test
    void testCancelBooking() {
        Booking booking1 = new Booking(
                LocalDate.of(2025, 5, 1),
                LocalTime.of(9, 0),
                50L,
                1000001L,
                2000001L,
                "Booked",
                "Strength & Conditioning Plans",
                "T-1");
        Booking booking2 = new Booking(
                LocalDate.of(2025, 5, 1),
                LocalTime.of(10, 0),
                50L,
                1000001L,
                2000001L,
                "Attended",
                "Strength & Conditioning Plans",
                "T-2");
        bookingSystem.addBooking(booking1);
        bookingSystem.addBooking(booking2);
        bookingSystem.cancelBooking(booking1);
        bookingSystem.cancelBooking(booking2);

        assertEquals("Cancelled", booking1.getStatus());
        assertNull(booking1.getTreatmentId());
        assertEquals("Attended", booking2.getStatus());
        assertNotNull(booking2.getTreatmentId());
    }

    @Test
    void testValidateBookingConflict() {
        Booking booking1 = new Booking(
                LocalDate.of(2025, 5, 1),
                LocalTime.of(9, 0),
                50L,
                1000001L,
                2000001L,
                "Booked",
                "Strength & Conditioning Plans",
                "T-1");

        bookingSystem.addBooking(booking1);
        boolean res1 = bookingSystem.validateBookingConflict(LocalDate.of(2025, 5, 1),
                LocalTime.of(9, 0),
                2000001L);
        boolean res2 = bookingSystem.validateBookingConflict(LocalDate.of(2025, 5, 2),
                LocalTime.of(9, 0),
                2000001L);
        boolean res3 = bookingSystem.validateBookingConflict(LocalDate.of(2025, 5, 1),
                LocalTime.of(10, 0),
                2000001L);

        assertFalse(res1);
        assertTrue(res2);
        assertTrue(res3);

    }

    @Test
    void testGetStatusIcon() {
        assertEquals("🎉 Attended", bookingSystem.getStatusIcon("attended"));
        assertEquals("❌ Cancelled", bookingSystem.getStatusIcon("cancelled"));
        assertEquals("⏳ Booked", bookingSystem.getStatusIcon("booked"));
        assertEquals("Confirmed", bookingSystem.getStatusIcon("Confirmed"));
    }

    @Test
    void testAddPatient() {
        String name = "Shahwaiz Shaban";
        String address = "13 Lime Avenue";
        String phone = "07123456789";
        Long newPatientId = bookingSystem.addPatient(name, address, phone);

        assertNotNull(newPatientId, "Patient ID should not be null");

        Patient addedPatient = bookingSystem.getPatients().stream()
                .filter(p -> p.getId().equals(newPatientId))
                .findFirst()
                .orElse(null);

        assertNotNull(addedPatient, "Patient should be found in the system");
        assertEquals(name, addedPatient.getFullName());
        assertEquals(address, addedPatient.getAddress());
        assertEquals(phone, addedPatient.getPhoneNumber());
    }

    @Test
    void testRemoveExistingPatient() {
        String name = "Shahwaiz Shaban";
        String address = "13 Lime Avenue";
        String phone = "07123456789";
        Long newPatientId = bookingSystem.addPatient(name, address, phone);

        boolean result = bookingSystem.removePatient(newPatientId);

        assertTrue(result, "Patient should be removed successfully");

        // Verify patient is gone
        boolean stillExists = bookingSystem.getPatients().stream()
                .anyMatch(p -> p.getId().equals(newPatientId));
        assertFalse(stillExists, "Patient should no longer exist in the list");
    }

    @Test
    void testRemoveNonExistingPatient() {
        boolean result = bookingSystem.removePatient(9999L);

        assertFalse(result, "Removing non-existing patient should return false");
    }

    @Test
    void testGetNewPatientId() {
        BookingSystem newBookingSystem = new BookingSystem();
        Long id1 = newBookingSystem.GetNewPatientId();
        assertEquals(2000001L, id1, "Should return 2000001 when patient list is empty");

        String name = "Shahwaiz Shaban";
        String address = "13 Lime Avenue";
        String phone = "07123456789";
        newBookingSystem.addPatient(name, address, phone);

        Long id2 = newBookingSystem.GetNewPatientId();

        assertEquals(id1 + 1, id2, "Should return next available patient ID");
    }
}