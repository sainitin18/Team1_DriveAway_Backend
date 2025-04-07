//package com.DriveAway.project.repository;
//
//import com.DriveAway.project.model.Rental;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface RentalRepository extends JpaRepository<Rental, Long> {
//    
//    List<Rental> findByUserIdAndRentalStatus(Long userId, String status);
//
//    List<Rental> findByRentalStatus(String status);
//
//    int countByCarIdAndRentalStatus(Long carId, String status);
//
//    boolean existsByUserIdAndCarIdAndRentalStatus(Long userId, Long carId, String status);
//}
package com.DriveAway.project.repository;

import com.DriveAway.project.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    // Use explicit JPQL queries to avoid naming convention issues
    @Query("SELECT r FROM Rental r WHERE r.user.userId = :userId AND r.rentalStatus = :status")
    List<Rental> findByUserAndStatus(@Param("userId") Long userId, @Param("status") String status);
    
    List<Rental> findByRentalStatus(String status);
    List<Rental> findByRentalStatusIn(List<String> statuses);
    
    @Query("SELECT COUNT(r) FROM Rental r WHERE r.car.carId = :carId AND r.rentalStatus = :status")
    int countByCarAndStatus(@Param("carId") Long carId, @Param("status") String status);
    
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
           "FROM Rental r WHERE r.user.userId = :userId AND r.car.carId = :carId AND r.rentalStatus = :status")
    boolean existsByUserIdCarIdAndStatus(@Param("userId") Long userId, 
                                   @Param("carId") Long carId, 
                                   @Param("status") String status);
    List<Rental> findAll();

}