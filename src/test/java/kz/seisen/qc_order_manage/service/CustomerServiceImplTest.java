package kz.seisen.qc_order_manage.service;

import kz.seisen.qc_order_manage.dto.CustomerDto;
import kz.seisen.qc_order_manage.entity.Customer;
import kz.seisen.qc_order_manage.exception.CustomerNotFoundException;
import kz.seisen.qc_order_manage.mapper.CustomerMapper;
import kz.seisen.qc_order_manage.repository.CustomerRepository;
import kz.seisen.qc_order_manage.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private CustomerMapper mapper;

    @InjectMocks
    private CustomerServiceImpl service;

    @Test
    void getAll_returnsMappedPage() {
        Pageable pageable = PageRequest.of(0, 20);
        Customer customer = new Customer();
        CustomerDto dto = new CustomerDto();
        Page<Customer> page = new PageImpl<>(List.of(customer));

        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toDto(customer)).thenReturn(dto);

        Page<CustomerDto> result = service.getAll(pageable);

        assertThat(result.getContent()).containsExactly(dto);
        verify(repository).findAll(pageable);
    }

    @Test
    void getById_found_returnsDto() {
        Customer customer = new Customer();
        CustomerDto dto = new CustomerDto();

        when(repository.findById(1L)).thenReturn(Optional.of(customer));
        when(mapper.toDto(customer)).thenReturn(dto);

        assertThat(service.getById(1L)).isEqualTo(dto);
    }

    @Test
    void getById_notFound_throwsCustomerNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void create_savesAndReturnsDto() {
        CustomerDto dto = new CustomerDto();
        Customer entity = new Customer();
        Customer saved = new Customer();
        CustomerDto savedDto = new CustomerDto();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(savedDto);

        assertThat(service.create(dto)).isEqualTo(savedDto);
        verify(repository).save(entity);
    }

    @Test
    void update_found_updatesFields() {
        Customer customer = new Customer();
        CustomerDto dto = new CustomerDto();
        dto.setName("New Name");
        dto.setEmail("new@email.com");
        Customer saved = new Customer();
        CustomerDto savedDto = new CustomerDto();

        when(repository.findById(1L)).thenReturn(Optional.of(customer));
        when(repository.save(customer)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(savedDto);

        assertThat(service.update(1L, dto)).isEqualTo(savedDto);
        assertThat(customer.getName()).isEqualTo("New Name");
        assertThat(customer.getEmail()).isEqualTo("new@email.com");
    }

    @Test
    void update_notFound_throws() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, new CustomerDto()))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void delete_found_deletesById() {
        Customer customer = new Customer();
        when(repository.findById(1L)).thenReturn(Optional.of(customer));

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void delete_notFound_throws() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(CustomerNotFoundException.class);
    }
}
