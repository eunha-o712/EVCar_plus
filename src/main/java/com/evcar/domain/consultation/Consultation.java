package com.evcar.domain.consultation;

import com.evcar.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CONSULTATION")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Consultation {
	
	private static final String STATUS_PENDING="대기";
	private static final String STATUS_IN_PROGRESS="진행중";
	private static final String STATUS_COMPLETED="완료";
	private static final String STATUS_CANCELED="취소";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consult_id", nullable = false)
    private Integer consultId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "preferred_datetime", nullable = false)
    private LocalDateTime preferredDatetime;

    @Column(name = "budget", nullable = false)
    private Integer budget;

    @Column(name = "purchase_plan", nullable = false, length = 50)
    private String purchasePlan;

    @Column(name = "consult_content", nullable = false, columnDefinition = "TEXT")
    private String consultContent;

    @Column(name = "consult_status", nullable = false, length = 20)
    private String consultStatus;

    @Column(name = "consult_result", length = 20)
    private String consultResult;

    @Column(name = "admin_reply", columnDefinition = "TEXT")
    private String adminReply;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        if(this.consultStatus==null||this.consultStatus.isBlank()) {
        	this.consultStatus=STATUS_PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    public boolean canBeCanceled() {
    	return STATUS_PENDING.equals(this.consultStatus)||STATUS_IN_PROGRESS.equals(this.consultStatus);
    }
    public void cancel() {
    	if(!canBeCanceled()) {
    		throw new IllegalArgumentException("취소할 수 없는 상담 상태입니다.");
    	}
        this.consultStatus =  STATUS_CANCELED;
    }
    public boolean isInProgress() {
    	return STATUS_IN_PROGRESS.equals(this.consultStatus);
    }
    public boolean isCompleted() {
    	return STATUS_COMPLETED.equals(this.consultStatus);
    }
    public boolean isCanceled() {
    	return STATUS_CANCELED.equals(this.consultStatus);
    }
}