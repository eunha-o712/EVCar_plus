package com.evcar.repository.mypage;

import com.evcar.dto.mypage.MyWishlistResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class MyWishlistQueryRepositoryImpl implements MyWishlistQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MyWishlistResponseDto> findMyWishlistByUserId(String userId) {
        String sql = """
                SELECT
                    w.wishlist_id,
                    v.brand,
                    v.model_name,
                    v.vehicle_class,
                    v.price_basic,
                    v.image_url,
                    w.vehicle_id
                FROM wishlist w
                INNER JOIN vehicle v
                    ON w.vehicle_id = v.vehicle_id
                WHERE w.user_id = :userId
                ORDER BY w.created_at DESC
                """;

        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery(sql)
                .setParameter("userId", userId)
                .getResultList();

        return rows.stream()
                .map(row -> {
                    String wishlistId = row[0] != null ? String.valueOf(row[0]) : null;
                    String brand = row[1] != null ? String.valueOf(row[1]) : null;
                    String modelName = row[2] != null ? String.valueOf(row[2]) : null;
                    String vehicleClass = row[3] != null ? String.valueOf(row[3]) : null;
                    Integer priceBasic = row[4] != null ? ((Number) row[4]).intValue() : null;
                    String imageUrl = row[5] != null ? String.valueOf(row[5]) : null;
                    String vehicleId = row[6] != null ? String.valueOf(row[6]) : null;

                    return MyWishlistResponseDto.builder()
                            .wishlistId(wishlistId)
                            .brand(brand)
                            .modelName(modelName)
                            .vehicleClass(vehicleClass)
                            .priceBasic(priceBasic)
                            .imageUrl(imageUrl)
                            .detailUrl("/vehicle/detail?vehicleId=" + vehicleId)
                            .build();
                })
                .toList();
    }

    @Override
    @Transactional
    public void deleteMyWishlistByUserIdAndWishlistId(String userId, String wishlistId) {
        String sql = """
                DELETE FROM wishlist
                WHERE user_id = :userId
                  AND wishlist_id = :wishlistId
                """;

        int affectedRows = entityManager.createNativeQuery(sql)
                .setParameter("userId", userId)
                .setParameter("wishlistId", wishlistId)
                .executeUpdate();

        if (affectedRows == 0) {
            throw new IllegalArgumentException("관심차량 정보를 찾을 수 없습니다.");
        }
    }
}