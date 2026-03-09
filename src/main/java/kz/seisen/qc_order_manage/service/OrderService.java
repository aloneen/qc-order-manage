package kz.seisen.qc_order_manage.service;

import kz.seisen.qc_order_manage.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderDto> getAll(Pageable pageable);
    OrderDto getById(Long id);
    OrderDto create(OrderDto dto);
    OrderDto update(Long id, OrderDto dto);
    void delete(Long id);
    OrderDto pay(Long id);
    OrderDto cancel(Long id);
}
