package com.example.bookshop.mapper;

import com.example.bookshop.config.MapperConfig;
import com.example.bookshop.dto.cart.CartItemResponseDto;
import com.example.bookshop.dto.cart.CreateCartItemDto;
import com.example.bookshop.dto.cart.ShoppingCartDto;
import com.example.bookshop.model.CartItem;
import com.example.bookshop.model.ShoppingCart;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(
            target = "cartItems",
            source = "cartItems",
            qualifiedByName = "mapCartItems"
    )
    @Mapping(target = "id", source = "user.id")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemResponseDto toDto(CartItem cartItem);

    @Named("mapCartItems")
    default List<CartItemResponseDto> mapCartItems(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toDto)
                .toList();
    }

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toModel(CreateCartItemDto requestDto);
}
