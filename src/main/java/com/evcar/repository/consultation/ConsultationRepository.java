<<<<<<< HEAD
package com.evcar.repository.consultation;

import com.evcar.domain.consultation.Consultation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository extends JpaRepository<Consultation, String> {

    // 마이페이지용 - String userId 기준
    boolean existsByUserIdAndConsultStatus(String userId, String consultStatus);

    List<Consultation> findByUserIdOrderByCreatedAtDesc(String userId);
}
=======
package com.evcar.repository.consultation;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



import com.evcar.domain.consultation.Consultation;

public interface ConsultationRepository extends JpaRepository<Consultation, Integer>{
	
	boolean existsByUserUserIdAndConsultStatus(Integer userId,String consultStatus);
	
	List<Consultation> findByUserUserIdOrderByCreatedAtDesc(Integer userId);
}
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
