package com.example.appointment.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    Optional<Appointment> findByAppNumber(String appNumber);
    @Query("SELECT a.appTime FROM Appointment a WHERE a.doctorId = :doctorId AND a.appDate = :appDate")
    List<String> findBookedSlots(@Param("doctorId") int doctorId, @Param("appDate") String appDate);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctorId = :doctorId AND a.appDate = :appDate AND a.appTime = :appTime")
    int countByDoctorIdAndAppDateAndAppTime(@Param("doctorId") int doctorId, @Param("appDate") String appDate, @Param("appTime") String appTime);



}
