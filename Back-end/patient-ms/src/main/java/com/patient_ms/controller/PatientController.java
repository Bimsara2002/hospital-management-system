package com.patient_ms.controller;

import com.patient_ms.data.Patient;
import com.patient_ms.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/patients")
@CrossOrigin(origins = "http://localhost:3000")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping(path = "/patients")
    public List<Patient> getAllPatients(){
        return patientService.getAllPatients();
    }
    @GetMapping(path = "/patients/{id}")
    public Patient getPatientById(@PathVariable int id){
        return patientService.getPatientById(id);
    }
    @PostMapping(path = "/patients")
    public Patient createPatient(@RequestBody Patient patient){
        return patientService.createPatient(patient);
    }
    @PutMapping(path = "/patients/{id}")
    public Patient updatePatient(@RequestBody Patient patient){
        return patientService.updatePatient(patient);
    }

    @DeleteMapping(path = "/patients/{id}")
    public void deletePatient(@PathVariable int id){
        patientService.deletePatientById(id);

    }

    @GetMapping(path = "/patients",params={"name","contact"})
    public List<Patient> searchPatientsByContact(@RequestParam String name,@RequestParam String contact){
        return patientService.searchByContact(name,contact);
    }

}
