package kz.seisen.qc_order_manage.mapper;


import kz.seisen.qc_order_manage.dto.OrderDto;
import kz.seisen.qc_order_manage.entity.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toDto(Order entity);

    Order toEntity(OrderDto dto);

    List<OrderDto> toDtoList(List<Order> entities);
}
