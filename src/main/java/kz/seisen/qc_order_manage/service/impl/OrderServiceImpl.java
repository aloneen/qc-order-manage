package kz.seisen.qc_order_manage.service.impl;

import kz.seisen.qc_order_manage.dto.OrderDto;
import kz.seisen.qc_order_manage.entity.Order;
import kz.seisen.qc_order_manage.entity.OrderStatus;
import kz.seisen.qc_order_manage.exception.BusinessException;
import kz.seisen.qc_order_manage.exception.CustomerNotFoundException;
import kz.seisen.qc_order_manage.exception.OrderNotFoundException;
import kz.seisen.qc_order_manage.mapper.OrderMapper;
import kz.seisen.qc_order_manage.repository.CustomerRepository;
import kz.seisen.qc_order_manage.repository.OrderRepository;
import kz.seisen.qc_order_manage.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderMapper mapper;
    private final OrderRepository repository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getById(Long id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id)));
    }

    @Override
    public OrderDto create(OrderDto dto) {
        customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(dto.getCustomerId()));

        Order order = mapper.toEntity(dto);
        order.setStatus(OrderStatus.NEW);
        return mapper.toDto(repository.save(order));
    }

    @Override
    public OrderDto update(Long id, OrderDto dto) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(dto.getCustomerId()));

        order.setCustomerId(dto.getCustomerId());
        order.setAmount(dto.getAmount());

        return mapper.toDto(repository.save(order));
    }

    @Override
    public void delete(Long id) {
        repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        repository.deleteById(id);
    }

    @Override
    public OrderDto pay(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() != OrderStatus.NEW) {
            throw new BusinessException("Only NEW orders can be paid");
        }

        order.setStatus(OrderStatus.PAID);
        return mapper.toDto(repository.save(order));
    }

    @Override
    public OrderDto cancel(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == OrderStatus.PAID) {
            throw new BusinessException("PAID orders cannot be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        return mapper.toDto(repository.save(order));
    }
}
