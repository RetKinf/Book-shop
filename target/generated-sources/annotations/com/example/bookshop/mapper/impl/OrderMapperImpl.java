package com.example.bookshop.mapper.impl;

import com.example.bookshop.dto.order.OrderDto;
import com.example.bookshop.dto.order.OrderItemDto;
import com.example.bookshop.mapper.OrderMapper;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Order;
import com.example.bookshop.model.OrderItem;
import com.example.bookshop.model.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-12T12:11:31+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDto toDto(Order order) {
        if ( order == null ) {
            return null;
        }

        Long userId = null;
        List<OrderItemDto> orderItems = null;
        Long id = null;
        LocalDateTime orderDate = null;
        BigDecimal total = null;
        String status = null;

        Long id1 = orderUserId( order );
        if ( id1 != null ) {
            userId = id1;
        }
        List<OrderItemDto> list = toDto( order.getOrderItems() );
        if ( list != null ) {
            orderItems = list;
        }
        if ( order.getId() != null ) {
            id = order.getId();
        }
        if ( order.getOrderDate() != null ) {
            orderDate = order.getOrderDate();
        }
        if ( order.getTotal() != null ) {
            total = order.getTotal();
        }
        if ( order.getStatus() != null ) {
            status = order.getStatus().name();
        }

        OrderDto orderDto = new OrderDto( id, userId, orderItems, orderDate, total, status );

        return orderDto;
    }

    @Override
    public OrderItemDto toDto(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        Long bookId = null;
        Long id = null;
        int quantity = 0;

        Long id1 = orderItemBookId( orderItem );
        if ( id1 != null ) {
            bookId = id1;
        }
        if ( orderItem.getId() != null ) {
            id = orderItem.getId();
        }
        quantity = orderItem.getQuantity();

        OrderItemDto orderItemDto = new OrderItemDto( id, bookId, quantity );

        return orderItemDto;
    }

    @Override
    public List<OrderItemDto> toOrderItemDtoList(Set<OrderItem> orderItems) {
        if ( orderItems == null ) {
            return null;
        }

        List<OrderItemDto> list = new ArrayList<OrderItemDto>( orderItems.size() );
        for ( OrderItem orderItem : orderItems ) {
            list.add( toDto( orderItem ) );
        }

        return list;
    }

    private Long orderUserId(Order order) {
        User user = order.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private Long orderItemBookId(OrderItem orderItem) {
        Book book = orderItem.getBook();
        if ( book == null ) {
            return null;
        }
        return book.getId();
    }
}
