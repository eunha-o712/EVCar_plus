package com.evcar.domain.user;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "user_id", nullable = false, length = 20)
    private String userId;

    @Column(name = "login_id", nullable = false, unique = true, length = 20)
    private String loginId;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "address_detail", nullable = false, length = 255)
    private String addressDetail;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false, length = 10)
    @Builder.Default
    private UserStatus userStatus = UserStatus.ACTIVE;

    @Column(name = "has_vehicle", length = 10)
    private String hasVehicle;

    @Column(name = "vehicle_model", length = 50)
    private String vehicleModel;

    @Column(name = "vehicle_year", length = 10)
    private String vehicleYear;

    @Column(name = "driving_distance")
    private Integer drivingDistance;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "withdrawn_at")
    private LocalDateTime withdrawnAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.role == null) this.role = UserRole.USER;
        if (this.userStatus == null) this.userStatus = UserStatus.ACTIVE;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateMyPageInfo(
            String name,
            LocalDate birthDate,
            String gender,
            String phone,
            String address,
            String addressDetail,
            String email,
            String hasVehicle,
            String vehicleModel,
            String vehicleYear,
            Integer drivingDistance
    ) {
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.addressDetail = addressDetail;
        this.email = email;
        this.hasVehicle = hasVehicle;
        this.vehicleModel = vehicleModel;
        this.vehicleYear = vehicleYear;
        this.drivingDistance = drivingDistance;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void withdraw() {
        this.userStatus = UserStatus.WITHDRAWN;
        this.withdrawnAt = LocalDateTime.now();
    }
}