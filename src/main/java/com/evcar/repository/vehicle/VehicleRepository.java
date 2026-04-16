package com.evcar.repository.vehicle;

import com.evcar.domain.vehicle.Vehicle;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {

	List<Vehicle> findAllByOrderByBrandAscModelNameAsc();
	
	List<Vehicle> findByVehicleStatusOrderByCreatedAtDesc(String vehicleStatus);

    List<Vehicle> findByVehicleStatusAndBrandOrderByCreatedAtDesc(String vehicleStatus, String brand);

    List<Vehicle> findByVehicleStatusAndVehicleClassOrderByCreatedAtDesc(String vehicleStatus, String vehicleClass);

    List<Vehicle> findByVehicleStatusAndBrandAndVehicleClassOrderByCreatedAtDesc(
            String vehicleStatus,
            String brand,
            String vehicleClass
    );

    @Query("""
            select v
            from Vehicle v
            where (:vehicleStatus is null or v.vehicleStatus = :vehicleStatus)
              and (
                    :keyword is null
                    or trim(:keyword) = ''
                    or lower(v.brand) like lower(concat('%', :keyword, '%'))
                    or lower(v.modelName) like lower(concat('%', :keyword, '%'))
                    or (:normalizedKeyword is not null and lower(v.modelName) like lower(concat('%', :normalizedKeyword, '%')))
                  )
            order by v.createdAt desc
            """)
    List<Vehicle> searchAdminVehicleList(
            @Param("vehicleStatus") String vehicleStatus,
            @Param("keyword") String keyword,
            @Param("normalizedKeyword") String normalizedKeyword
    );

    @Query("""
            select v.vehicleId
            from Vehicle v
            where v.vehicleId like concat(:prefix, '%')
            order by v.vehicleId desc
            limit 1
            """)
    Optional<String> findTopVehicleIdByPrefix(@Param("prefix") String prefix);
    Optional<Vehicle> findByVehicleId(String vehicleId);
}