package com.evcar.repository.consultation;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



import com.evcar.domain.consultation.Consultation;

public interface ConsultationRepository extends JpaRepository<Consultation, Integer>{
	
	boolean existsByUserUserIdAndConsultStatus(Integer userId,String consultStatus);
	
	List<Consultation> findByUserUserIdOrderByCreatedAtDesc(Integer userId);
}
