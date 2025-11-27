package com.patient_ms.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Integer> {

    @Query("select p from Patient p where p.name=?1 and p.contactNumber=?2")
    public List<Patient> searchPatientByContact(String name,String contact);

}

