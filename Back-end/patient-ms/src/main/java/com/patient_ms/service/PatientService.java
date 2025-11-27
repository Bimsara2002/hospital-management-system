package com.patient_ms.service;

import com.patient_ms.data.Patient;
import com.patient_ms.data.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepo;

    public List<Patient> getAllPatients(){
        return patientRepo.findAll();
    }
    public Patient getPatientById(int id){
        Optional<Patient> patient=patientRepo.findById(id);
        if(patient.isPresent()) {
            return patient.get();
        }
        return null;
    }

    public Patient createPatient(Patient patient){
        return patientRepo.save(patient);
    }

    public Patient updatePatient(Patient patient){
        return patientRepo.save(patient);
    }

    public void deletePatientById(int id){
        patientRepo.deleteById(id);
    }

    public List<Patient> searchByContact(String name,String contact){
        return patientRepo.searchPatientByContact(name,contact);
    }
}
