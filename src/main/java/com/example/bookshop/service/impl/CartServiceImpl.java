package com.example.bookshop.service.impl;

import com.example.bookshop.dto.cart.CartItemRequestDto;
import com.example.bookshop.dto.cart.CreateCartItemDto;
import com.example.bookshop.dto.cart.ShoppingCartDto;
import com.example.bookshop.exception.EntityNotFoundException;
import com.example.bookshop.mapper.CartMapper;
import com.example.bookshop.model.CartItem;
import com.example.bookshop.model.ShoppingCart;
import com.example.bookshop.model.User;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.repository.CartItemRepository;
import com.example.bookshop.repository.ShoppingCartRepository;
import com.example.bookshop.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final CartMapper cartMapper;

    @Override
    public ShoppingCartDto findAll(Pageable pageable) {
        return cartMapper.toDto(getCurrentShoppingCart());
    }

    @Transactional
    @Override
    public ShoppingCartDto save(CreateCartItemDto requestDto) {
        isBookExist(requestDto.bookId());
        ShoppingCart currentShoppingCart = getCurrentShoppingCart();
        CartItem cartItem = cartMapper.toModel(requestDto);
        cartItem.setShoppingCart(currentShoppingCart);
        cartItemRepository.save(cartItem);
        currentShoppingCart.addItem(cartItem);
        return cartMapper.toDto(currentShoppingCart);
    }

    @Override
    public ShoppingCartDto update(CartItemRequestDto requestDto, Long id) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cart item with id " + id + " not found")
        );
        cartItem.setQuantity(requestDto.quantity());
        cartItemRepository.save(cartItem);
        return cartMapper.toDto(getCurrentShoppingCart());
    }

    @Override
    public ShoppingCartDto delete(Long id) {
        cartItemRepository.deleteById(id);
        return cartMapper.toDto(getCurrentShoppingCart());
    }

    private ShoppingCart getCurrentShoppingCart() {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return shoppingCartRepository.findByUser(user);
    }

    private void isBookExist(Long id) {
        bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book with id " + id + " not found")
        );
    }
}
