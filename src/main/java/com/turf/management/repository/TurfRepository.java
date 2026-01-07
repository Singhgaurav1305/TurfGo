package com.turf.management.repository;

import com.turf.management.model.Turf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurfRepository extends JpaRepository<Turf, Long> {
}