package com.turf.management.service;

import com.turf.management.model.Turf;
import com.turf.management.repository.TurfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TurfService {

    @Autowired
    private TurfRepository turfRepository;

    // 1. Add New Turf
    public void addTurf(Turf turf) {
        turfRepository.save(turf);
    }

    // 2. Get All Turfs (For Table View)
    public List<Turf> getAllTurfs() {
        return turfRepository.findAll();
    }

    // 3. Delete Turf
    public void deleteTurf(Long id) {
        turfRepository.deleteById(id);
    }

    // 4. Update Turf
    public void updateTurf(Turf turf) {
        // Hibernate save method works as Update if ID exists
        turfRepository.save(turf);
    }

    // 5. Count Total Turfs (For Home Analytics)
    public long getTurfCount() {
        return turfRepository.count();
    }
}