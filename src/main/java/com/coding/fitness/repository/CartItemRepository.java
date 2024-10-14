package com.coding.fitness.repository;

import com.coding.fitness.entity.CartItems;
import com.coding.fitness.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems, Long> {
    Optional<CartItems> findByProductIdAndOrderIdAndUserId(Long productId, Long orderId, Long userId);

    //new update
    List<CartItems> findByUserIdAndOrderIsNull(Long userId);

    List<CartItems> findByUserId(Long userId);

    CartItems findByUserIdAndProductIdAndOrderIsNull(Long userId, Long productId);
}
