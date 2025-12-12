package com.sportsstore.repository;

import com.sportsstore.model.CartItem;
import com.sportsstore.model.Product;
import com.sportsstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findBySessionId(String sessionId);

    List<CartItem> findByUser(User user);

    Optional<CartItem> findBySessionIdAndProduct(String sessionId, Product product);

    Optional<CartItem> findByUserAndProduct(User user, Product product);

    void deleteBySessionId(String sessionId);

    void deleteByUser(User user);
}
