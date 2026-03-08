package kz.seisen.qc_order_manage.service;

import kz.seisen.qc_order_manage.dto.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAll();
    OrderDto getById(Long id);
    OrderDto create(OrderDto dto);
    OrderDto update(Long id, OrderDto dto);
    void delete(Long id);

}
