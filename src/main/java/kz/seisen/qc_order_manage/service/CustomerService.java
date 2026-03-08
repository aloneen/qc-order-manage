package kz.seisen.qc_order_manage.service;

import kz.seisen.qc_order_manage.dto.CustomerDto;
import kz.seisen.qc_order_manage.dto.OrderDto;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getAll();
    CustomerDto getById(Long id);
    CustomerDto create(CustomerDto dto);
    CustomerDto update(Long id, CustomerDto dto);
    void delete(Long id);
}
