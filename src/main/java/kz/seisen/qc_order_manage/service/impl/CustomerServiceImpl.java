package kz.seisen.qc_order_manage.service.impl;

import kz.seisen.qc_order_manage.dto.CustomerDto;
import kz.seisen.qc_order_manage.dto.OrderDto;
import kz.seisen.qc_order_manage.entity.Customer;
import kz.seisen.qc_order_manage.entity.Order;
import kz.seisen.qc_order_manage.mapper.CustomerMapper;
import kz.seisen.qc_order_manage.mapper.OrderMapper;
import kz.seisen.qc_order_manage.repository.CustomerRepository;
import kz.seisen.qc_order_manage.repository.OrderRepository;
import kz.seisen.qc_order_manage.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper mapper;
    private final CustomerRepository repository;


    @Override
    public List<CustomerDto> getAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public CustomerDto getById(Long id) {
        return mapper.toDto(repository.findById(id).orElse(null));
    }

    @Override
    public CustomerDto create(CustomerDto dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public CustomerDto update(Long id, CustomerDto dto) {
        Customer oldCustomer = repository.findById(id).orElse(null);

        if(Objects.isNull(oldCustomer) || Objects.isNull(dto)) {
            return null;
        }

        oldCustomer.setName(dto.getName());
        oldCustomer.setEmail(dto.getEmail());

        return mapper.toDto(repository.save(oldCustomer));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
