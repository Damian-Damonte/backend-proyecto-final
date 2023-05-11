package com.dh.digitalbooking.repository;

import com.dh.digitalbooking.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @Query("SELECT p FROM Producto p ORDER BY RAND() LIMIT 4")
    List<Producto> findRandom();

    @Query(
            "SELECT p FROM Producto p WHERE " +
                "(:ciudadId IS NULL OR p.city.id = :ciudadId) " +
                "AND (:categoriaId IS NULL OR p.category.id = :categoryId) " +
                "AND ((:checkIn IS NULL AND :checkOut IS NULL) OR p.id NOT IN " +
                "(SELECT r.product.id FROM Booking r WHERE " +
                    "(r.checkIn <= :checkOut AND r.checkOut >= :checkIn))) " +
                "ORDER BY p.id ASC"
    )
    Page<Producto> findAllFilters(
            @Param("ciudadId") Long ciudadId,
            @Param("categoryId") Long categoryId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            Pageable pageable
    );

    boolean existsByCity_id(Long id);
}
