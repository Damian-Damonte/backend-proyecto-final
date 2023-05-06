package com.dh.digitalbooking.repository;

import com.dh.digitalbooking.entity.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
    @Query("SELECT COUNT(c) FROM Ciudad c WHERE c.country.id = :countryId")
    int existsByPaisId(@Param("countryId") Long countryId);
}
