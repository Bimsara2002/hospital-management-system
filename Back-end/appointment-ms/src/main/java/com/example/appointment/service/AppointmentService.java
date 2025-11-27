package com.example.appointment.service;


import com.example.appointment.DoctorDTO;
import com.example.appointment.data.Appointment;
import com.example.appointment.data.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Save new appointment
    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    // Get all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Get appointment by ID
    public Optional<Appointment> getAppointmentById(int id) {
        return appointmentRepository.findById(id);
    }

    // Delete appointment
    public void deleteAppointmentById(int id) {
        appointmentRepository.deleteById(id);
    }

    // Find by app number
    public Optional<Appointment> getAppointmentByNumber(String appNumber) {
        return appointmentRepository.findByAppNumber(appNumber);
    }

    // Update appointment by ID
    public Appointment updateAppointment(int id, Appointment newAppointmentData) {
        Optional<Appointment> optional = appointmentRepository.findById(id);
        if (optional.isPresent()) {
            Appointment existing = optional.get();

            existing.setDoctorId(newAppointmentData.getDoctorId());
            existing.setPatientId(newAppointmentData.getPatientId());
            existing.setAppDate(newAppointmentData.getAppDate());
            existing.setAppTime(newAppointmentData.getAppTime());
            existing.setAppNumber(newAppointmentData.getAppNumber());
            existing.setAppFee(newAppointmentData.getAppFee());
            existing.setStatus(newAppointmentData.getStatus());

            return appointmentRepository.save(existing);
        } else {
            return null;
        }
    }
//    public List<String> getAvailableSlots(int doctorId, String date) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        // Call Doctor Microservice to get doctor details
//        String doctorServiceUrl = "http://localhost:8083/doctor-app/doctors/" + doctorId;
//        DoctorDTO doctor = restTemplate.getForObject(doctorServiceUrl, DoctorDTO.class);
//
//        if (doctor == null) {
//            return List.of(); // Doctor not found
//        }
//
//        // Prepare available days
//        Set<String> allowedDays = Arrays.stream(doctor.getAvailableDays().split(","))
//                .map(String::trim)
//                .map(String::toLowerCase)
//                .collect(Collectors.toSet());
//
//        // Get day of the week from the provided date
//        LocalDate selectedDate = LocalDate.parse(date);
//        String selectedDay = selectedDate.getDayOfWeek()
//                .getDisplayName(TextStyle.FULL, Locale.ENGLISH)
//                .toLowerCase();
//
//        if (!allowedDays.contains(selectedDay)) {
//            return List.of(); // Doctor not available on this day
//        }
//
//        // Get all available time slots
//        List<String> allSlots = Arrays.stream(doctor.getTimeSlots().split(","))
//                .map(String::trim)
//                .collect(Collectors.toList());
//
//        // Get already booked slots from DB
//        List<String> bookedSlots = appointmentRepository.findBookedSlots(doctorId, date);
//
//        // Return slots that are not booked
//        return allSlots.stream()
//                .filter(slot -> !bookedSlots.contains(slot))
//                .collect(Collectors.toList());
//    }
//
//    public int getNextAppointmentNumber(int doctorId, String date, String timeSlot) {
//        // Count existing appointments for doctor, date, timeSlot
//        int existingCount = appointmentRepository.countByDoctorIdAndAppDateAndAppTime(doctorId, date, timeSlot);
//
//        // Each appointment takes 10 minutes, so next number = existing + 1
//        return existingCount + 1;
//    }

    public List<String> getAvailableSlots(int doctorId, String date) {
        RestTemplate restTemplate = new RestTemplate();

        String doctorServiceUrl = "http://localhost:8083/doctor-app/doctors/" + doctorId;
        DoctorDTO doctor = restTemplate.getForObject(doctorServiceUrl, DoctorDTO.class);

        if (doctor == null) {
            return List.of();
        }

        Set<String> allowedDays = Arrays.stream(doctor.getAvailableDays().split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        LocalDate selectedDate = LocalDate.parse(date);
        String selectedDay = selectedDate.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                .toLowerCase();

        if (!allowedDays.contains(selectedDay)) {
            return List.of();
        }

        List<String> allSlots = new ArrayList<>();

        // Assume doctor.getTimeSlots() returns something like "13:00-14:00,15:00-16:00"
        String[] ranges = doctor.getTimeSlots().split(",");

        for (String range : ranges) {
            String[] times = range.trim().split("-");
            if (times.length == 2) {
                allSlots.addAll(generate10MinSlots(times[0], times[1]));
            }
        }

        List<String> bookedSlots = appointmentRepository.findBookedSlots(doctorId, date);

        return allSlots.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .collect(Collectors.toList());
    }

    private List<String> generate10MinSlots(String startTime, String endTime) {
        List<String> slots = new ArrayList<>();

        String[] startParts = startTime.split(":");
        String[] endParts = endTime.split(":");

        int startHour = Integer.parseInt(startParts[0]);
        int startMinute = Integer.parseInt(startParts[1]);
        int endHour = Integer.parseInt(endParts[0]);
        int endMinute = Integer.parseInt(endParts[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, startHour);
        calendar.set(Calendar.MINUTE, startMinute);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.HOUR_OF_DAY, endHour);
        endCalendar.set(Calendar.MINUTE, endMinute);

        while (calendar.before(endCalendar)) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            String slot = String.format("%02d:%02d", hour, minute);
            slots.add(slot);

            calendar.add(Calendar.MINUTE, 10);
        }

        return slots;
    }

    public int getNextAppointmentNumber(int doctorId, String date, String timeSlot) {
        RestTemplate restTemplate = new RestTemplate();

        String doctorServiceUrl = "http://localhost:8083/doctor-app/doctors/" + doctorId;
        DoctorDTO doctor = restTemplate.getForObject(doctorServiceUrl, DoctorDTO.class);

        if (doctor == null) {
            return 0;
        }

        List<String> allSlots = new ArrayList<>();
        String[] ranges = doctor.getTimeSlots().split(",");

        for (String range : ranges) {
            String[] times = range.trim().split("-");
            if (times.length == 2) {
                allSlots.addAll(generate10MinSlots(times[0], times[1]));
            }
        }

        // Find index of selected timeSlot (starting from 1)
        int position = allSlots.indexOf(timeSlot);
        if (position == -1) {
            return 0; // Invalid slot
        }
        return position + 1;
    }

}



