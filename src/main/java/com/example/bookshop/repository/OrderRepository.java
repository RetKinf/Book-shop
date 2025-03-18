package com.example.bookshop.repository;

import com.example.bookshop.model.Order;
import com.example.bookshop.model.OrderItem;
import com.example.bookshop.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(
            "SELECT o "
                    + "FROM Order o "
                    + "WHERE o.id = :orderId AND o.user.id = :userId"
    )
    Optional<Order> findByIdAndUser(
            @Param("userId") Long userId,
            @Param("orderId") Long orderId
    );

    @Query(
            "SELECT oi "
                    + "FROM OrderItem oi "
                    + "JOIN oi.order o "
                    + "JOIN o.user u "
                    + "WHERE oi.id = :itemId AND u = :user AND o.id = :orderId"
    )
    Optional<OrderItem> findItemByUserAndId(
            @Param("user") User user,
            @Param("itemId") Long itemId,
            @Param("orderId") Long orderId
    );

    @EntityGraph(attributePaths = {"orderItems", "orderItems.book"})
    Page<Order> findAllByUser(User user, Pageable pageable);
}
