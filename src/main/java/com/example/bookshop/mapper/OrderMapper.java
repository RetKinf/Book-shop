package com.example.bookshop.mapper;

import com.example.bookshop.config.MapperConfig;
import com.example.bookshop.dto.order.OrderDto;
import com.example.bookshop.dto.order.OrderItemDto;
import com.example.bookshop.model.Order;
import com.example.bookshop.model.OrderItem;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "orderItems", qualifiedByName = "mapOrderItems")
    OrderDto toDto(Order order);

    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);

    @Named("mapOrderItems")
    default List<OrderItemDto> toDto(Set<OrderItem> orderItems) {
        return orderItems.stream().map(this::toDto).collect(Collectors.toList());
    }
}
