package com.example.bookshop.mapper.impl;

import com.example.bookshop.dto.cart.CartItemResponseDto;
import com.example.bookshop.dto.cart.CreateCartItemDto;
import com.example.bookshop.dto.cart.ShoppingCartDto;
import com.example.bookshop.mapper.BookMapper;
import com.example.bookshop.mapper.CartMapper;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.CartItem;
import com.example.bookshop.model.ShoppingCart;
import com.example.bookshop.model.User;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-12T12:11:32+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class CartMapperImpl implements CartMapper {

    private final BookMapper bookMapper;

    @Autowired
    public CartMapperImpl(BookMapper bookMapper) {

        this.bookMapper = bookMapper;
    }

    @Override
    public ShoppingCartDto toDto(ShoppingCart shoppingCart) {
        if ( shoppingCart == null ) {
            return null;
        }

        Long userId = null;
        List<CartItemResponseDto> cartItems = null;
        Long id = null;

        Long id1 = shoppingCartUserId( shoppingCart );
        if ( id1 != null ) {
            userId = id1;
        }
        List<CartItemResponseDto> list = mapCartItems( shoppingCart.getCartItems() );
        if ( list != null ) {
            cartItems = list;
        }
        Long id2 = shoppingCartUserId( shoppingCart );
        if ( id2 != null ) {
            id = id2;
        }

        ShoppingCartDto shoppingCartDto = new ShoppingCartDto( id, userId, cartItems );

        return shoppingCartDto;
    }

    @Override
    public CartItemResponseDto toDto(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }

        Long bookId = null;
        String bookTitle = null;
        Long id = null;
        int quantity = 0;

        Long id1 = cartItemBookId( cartItem );
        if ( id1 != null ) {
            bookId = id1;
        }
        String title = cartItemBookTitle( cartItem );
        if ( title != null ) {
            bookTitle = title;
        }
        if ( cartItem.getId() != null ) {
            id = cartItem.getId();
        }
        quantity = cartItem.getQuantity();

        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto( id, bookId, bookTitle, quantity );

        return cartItemResponseDto;
    }

    @Override
    public CartItem toModel(CreateCartItemDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        CartItem cartItem = new CartItem();

        if ( requestDto.bookId() != null ) {
            cartItem.setBook( bookMapper.bookFromId( requestDto.bookId() ) );
        }
        cartItem.setQuantity( requestDto.quantity() );

        return cartItem;
    }

    private Long shoppingCartUserId(ShoppingCart shoppingCart) {
        User user = shoppingCart.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private Long cartItemBookId(CartItem cartItem) {
        Book book = cartItem.getBook();
        if ( book == null ) {
            return null;
        }
        return book.getId();
    }

    private String cartItemBookTitle(CartItem cartItem) {
        Book book = cartItem.getBook();
        if ( book == null ) {
            return null;
        }
        return book.getTitle();
    }
}
