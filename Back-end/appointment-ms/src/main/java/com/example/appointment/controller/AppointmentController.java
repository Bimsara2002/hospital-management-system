package com.example.appointment.controller;

import com.example.appointment.data.Appointment;
import com.example.appointment.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // Create new appointment
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        Appointment saved = appointmentService.saveAppointment(appointment);
        return ResponseEntity.ok(saved);
    }

    // Get all appointments
    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    // Get appointment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable int id) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
        return appointment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update appointment by ID
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable int id, @RequestBody Appointment newData) {
        Appointment updated = appointmentService.updateAppointment(id, newData);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete appointment by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable int id) {
        appointmentService.deleteAppointmentById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-slots")
    public List<String> getAvailableSlots(
            @RequestParam int doctorId,
            @RequestParam String date
    ) {
        return appointmentService.getAvailableSlots(doctorId, date);
    }

    @GetMapping("/next-app-number")
    public int getNextAppNumber(
            @RequestParam int doctorId,
            @RequestParam String date,
            @RequestParam String timeSlot) {
        return appointmentService.getNextAppointmentNumber(doctorId, date, timeSlot);
    }


}
