package kz.seisen.qc_order_manage.service;

import kz.seisen.qc_order_manage.dto.OrderDto;
import kz.seisen.qc_order_manage.entity.Order;
import kz.seisen.qc_order_manage.entity.OrderStatus;
import kz.seisen.qc_order_manage.exception.BusinessException;
import kz.seisen.qc_order_manage.exception.CustomerNotFoundException;
import kz.seisen.qc_order_manage.exception.OrderNotFoundException;
import kz.seisen.qc_order_manage.mapper.OrderMapper;
import kz.seisen.qc_order_manage.repository.CustomerRepository;
import kz.seisen.qc_order_manage.repository.OrderRepository;
import kz.seisen.qc_order_manage.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderMapper mapper;

    @InjectMocks
    private OrderServiceImpl service;

    @Test
    void getAll_returnsMappedPage() {
        Pageable pageable = PageRequest.of(0, 20);
        Order order = new Order();
        OrderDto dto = new OrderDto();
        Page<Order> page = new PageImpl<>(List.of(order));

        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toDto(order)).thenReturn(dto);

        Page<OrderDto> result = service.getAll(pageable);

        assertThat(result.getContent()).containsExactly(dto);
        verify(repository).findAll(pageable);
    }

    @Test
    void getById_found() {
        Order order = new Order();
        OrderDto dto = new OrderDto();

        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(mapper.toDto(order)).thenReturn(dto);

        assertThat(service.getById(1L)).isEqualTo(dto);
    }

    @Test
    void getById_notFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void create_validCustomer_setsStatusNew() {
        OrderDto dto = new OrderDto();
        dto.setCustomerId(1L);
        dto.setAmount(BigDecimal.TEN);

        Order entity = new Order();
        Order saved = new Order();
        OrderDto savedDto = new OrderDto();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(new kz.seisen.qc_order_manage.entity.Customer()));
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(savedDto);

        assertThat(service.create(dto)).isEqualTo(savedDto);
        assertThat(entity.getStatus()).isEqualTo(OrderStatus.NEW);
    }

    @Test
    void create_customerNotFound_throws() {
        OrderDto dto = new OrderDto();
        dto.setCustomerId(99L);

        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(dto))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void update_found_updatesFields() {
        Order order = new Order();
        OrderDto dto = new OrderDto();
        dto.setCustomerId(2L);
        dto.setAmount(BigDecimal.valueOf(200));

        Order saved = new Order();
        OrderDto savedDto = new OrderDto();

        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(customerRepository.findById(2L)).thenReturn(Optional.of(new kz.seisen.qc_order_manage.entity.Customer()));
        when(repository.save(order)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(savedDto);

        assertThat(service.update(1L, dto)).isEqualTo(savedDto);
        assertThat(order.getCustomerId()).isEqualTo(2L);
        assertThat(order.getAmount()).isEqualTo(BigDecimal.valueOf(200));
    }

    @Test
    void update_customerNotFound_throws() {
        Order order = new Order();
        OrderDto dto = new OrderDto();
        dto.setCustomerId(99L);

        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(1L, dto))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void update_orderNotFound_throws() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, new OrderDto()))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void delete_found() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Order()));

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void delete_notFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void pay_newOrder_setsPaid() {
        Order order = new Order();
        order.setStatus(OrderStatus.NEW);
        Order saved = new Order();
        OrderDto dto = new OrderDto();

        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(repository.save(order)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(dto);

        assertThat(service.pay(1L)).isEqualTo(dto);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void pay_nonNewOrder_throwsBusinessException() {
        Order order = new Order();
        order.setStatus(OrderStatus.PAID);

        when(repository.findById(1L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> service.pay(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Only NEW orders can be paid");
    }

    @Test
    void cancel_newOrder_setsCancelled() {
        Order order = new Order();
        order.setStatus(OrderStatus.NEW);
        Order saved = new Order();
        OrderDto dto = new OrderDto();

        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(repository.save(order)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(dto);

        assertThat(service.cancel(1L)).isEqualTo(dto);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    void cancel_paidOrder_throwsBusinessException() {
        Order order = new Order();
        order.setStatus(OrderStatus.PAID);

        when(repository.findById(1L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> service.cancel(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("PAID orders cannot be cancelled");
    }
}
