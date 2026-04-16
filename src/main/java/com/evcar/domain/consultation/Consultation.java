package com.evcar.domain.consultation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

import com.evcar.domain.user.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "consultation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Consultation {

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    private static final String STATUS_COMPLETED = "COMPLETED";
    private static final String STATUS_CANCELED = "CANCELED";

    @Id
    @Column(name = "consult_id", nullable = false, length = 20)
    private String consultId;

    @Column(name = "user_id", nullable = false, length = 20)
    private String userId;

    @Column(name = "vehicle_id", nullable = false, length = 20)
    private String vehicleId;

    @Column(name = "preferred_datetime", nullable = false, length = 20)
    private String preferredDatetime;

    @Column(name = "budget", nullable = false)
    @Builder.Default
    private Integer budget = 0;

    @Column(name = "purchase_plan", nullable = false, length = 50)
    @Builder.Default
    private String purchasePlan = "";

    @Column(name = "consult_content", nullable = false, columnDefinition = "TEXT")
    private String consultContent;

    @Column(name = "consult_status", nullable = false, length = 20)
    @Builder.Default
    private String consultStatus = STATUS_PENDING;

    @Column(name = "consult_result", columnDefinition = "TEXT")
    private String consultResult;

    @Column(name = "admin_reply", columnDefinition = "TEXT")
    private String adminReply;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        if (this.updatedAt == null) {
            this.updatedAt = now;
        }
        if (this.consultStatus == null || this.consultStatus.isBlank()) {
            this.consultStatus = STATUS_PENDING;
        }
        if (this.budget == null) {
            this.budget = 0;
        }
        if (this.purchasePlan == null) {
            this.purchasePlan = "";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateConsultStatus(String consultStatus) {
        this.consultStatus = consultStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateReply(String consultResult, String adminReply) {
        this.consultResult = consultResult;
        this.adminReply = adminReply;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateAdminProcess(String consultStatus, String consultResult, String adminReply) {
        this.consultStatus = consultStatus;
        this.consultResult = consultResult;
        this.adminReply = adminReply;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean canBeCanceled() {
        return STATUS_PENDING.equals(this.consultStatus) || STATUS_IN_PROGRESS.equals(this.consultStatus);
    }

    public void cancel() {
        if (!canBeCanceled()) {
            throw new IllegalArgumentException("취소할 수 없는 상담 상태입니다.");
        }
        this.consultStatus = STATUS_CANCELED;
        this.updatedAt = LocalDateTime.now();
    }

}