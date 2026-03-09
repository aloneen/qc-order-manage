package kz.seisen.qc_order_manage.service;

import kz.seisen.qc_order_manage.dto.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    Page<CustomerDto> getAll(Pageable pageable);
    CustomerDto getById(Long id);
    CustomerDto create(CustomerDto dto);
    CustomerDto update(Long id, CustomerDto dto);
    void delete(Long id);
}
