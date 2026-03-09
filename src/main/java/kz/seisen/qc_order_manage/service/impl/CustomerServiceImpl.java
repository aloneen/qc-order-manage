package kz.seisen.qc_order_manage.service.impl;

import kz.seisen.qc_order_manage.dto.CustomerDto;
import kz.seisen.qc_order_manage.entity.Customer;
import kz.seisen.qc_order_manage.exception.CustomerNotFoundException;
import kz.seisen.qc_order_manage.mapper.CustomerMapper;
import kz.seisen.qc_order_manage.repository.CustomerRepository;
import kz.seisen.qc_order_manage.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper mapper;
    private final CustomerRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDto> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getById(Long id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id)));
    }

    @Override
    public CustomerDto create(CustomerDto dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public CustomerDto update(Long id, CustomerDto dto) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());

        return mapper.toDto(repository.save(customer));
    }

    @Override
    public void delete(Long id) {
        repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        repository.deleteById(id);
    }
}
