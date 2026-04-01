package com.evcar.domain.vehicle;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicle_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VehicleImage {

    @Id
    @Column(name = "image_id", length = 20)
    private String imageId;

    @Column(name = "vehicle_id", nullable = false, length = 20)
    private String vehicleId;   // 🔥 Long → String 변경

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Column(name = "image_order", nullable = false)
    private Integer imageOrder;
}