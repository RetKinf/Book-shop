package com.example.bookshop.service.impl;

import com.example.bookshop.dto.cart.CartItemRequestDto;
import com.example.bookshop.dto.cart.CreateCartItemDto;
import com.example.bookshop.dto.cart.ShoppingCartDto;
import com.example.bookshop.exception.EntityNotFoundException;
import com.example.bookshop.mapper.CartMapper;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.CartItem;
import com.example.bookshop.model.ShoppingCart;
import com.example.bookshop.model.User;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.repository.CartItemRepository;
import com.example.bookshop.repository.ShoppingCartRepository;
import com.example.bookshop.service.CartService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final CartMapper cartMapper;

    @Override
    public ShoppingCartDto findAll(Authentication authentication) {
        return cartMapper.toDto(
                shoppingCartRepository.findByUser((User) authentication.getPrincipal())
        );
    }

    @Override
    public ShoppingCartDto save(
            CreateCartItemDto requestDto,
            Authentication authentication
    ) {
        Book book = bookRepository.findById(requestDto.bookId()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Book with id " + requestDto.bookId() + " not found"
                )
        );
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(
                (User) authentication.getPrincipal()
        );
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        Optional<CartItem> existingItem = cartItems.stream()
                .filter(item -> item.getBook().getId().equals(requestDto.bookId()))
                .findFirst();
        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + requestDto.quantity());
        } else {
            CartItem cartItem = cartMapper.toModel(requestDto);
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setBook(book);
            shoppingCart.addItem(cartItem);
        }
        shoppingCartRepository.save(shoppingCart);
        return cartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto update(
            CartItemRequestDto requestDto,
            Long id,
            Authentication authentication
    ) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(
                (User) authentication.getPrincipal()
        );
        Optional<CartItem> optionalItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(id))
                .findFirst();
        CartItem cartItem = optionalItem.orElseThrow(
                () -> new EntityNotFoundException(
                        "Item with id " + id + " not found"
                )
        );
        cartItem.setQuantity(requestDto.quantity());
        shoppingCartRepository.save(shoppingCart);
        return cartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto delete(Long id, Authentication authentication) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(
                (User) authentication.getPrincipal()
        );
        Optional<CartItem> cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(id))
                .findFirst();
        if (cartItem.isPresent()) {
            CartItem item = cartItem.get();
            shoppingCart.getCartItems().remove(item);
            shoppingCartRepository.save(shoppingCart);
        } else {
            throw new EntityNotFoundException(
                    "Item with id " + id + " not found"
            );
        }
        return cartMapper.toDto(shoppingCart);
    }

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
}
