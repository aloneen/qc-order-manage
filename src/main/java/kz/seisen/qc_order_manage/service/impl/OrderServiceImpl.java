package kz.seisen.qc_order_manage.service.impl;

import kz.seisen.qc_order_manage.dto.OrderDto;
import kz.seisen.qc_order_manage.entity.Order;
import kz.seisen.qc_order_manage.mapper.OrderMapper;
import kz.seisen.qc_order_manage.repository.OrderRepository;
import kz.seisen.qc_order_manage.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper mapper;
    private final OrderRepository repository;


    @Override
    public List<OrderDto> getAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public OrderDto getById(Long id) {
        return mapper.toDto(repository.findById(id).orElse(null));
    }

    @Override
    public OrderDto create(OrderDto dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public OrderDto update(Long id, OrderDto dto) {
        Order oldOrder = repository.findById(id).orElse(null);

        if(Objects.isNull(oldOrder) || Objects.isNull(dto)) {
            return null;
        }

        oldOrder.setCustomerId(dto.getCustomerId());
        oldOrder.setStatus(dto.getStatus());
        oldOrder.setAmount(dto.getAmount());

        return mapper.toDto(repository.save(oldOrder));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
