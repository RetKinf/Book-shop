package com.example.bookshop.repository;

import com.example.bookshop.model.CartItem;
import com.example.bookshop.model.ShoppingCart;
import com.example.bookshop.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @EntityGraph(attributePaths = {"cartItems", "cartItems.book"})
    ShoppingCart findByUser(User user);

    @Query("SELECT ci FROM CartItem ci "
            + "JOIN FETCH ci.book b "
            + "JOIN ci.shoppingCart sc "
            + "JOIN sc.user u "
            + "WHERE u = :user AND ci.book.id = :bookId"
    )
    Optional<CartItem> cartItemsByUserAndBookId(User user, Long id);
}
