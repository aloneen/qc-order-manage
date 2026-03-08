package kz.seisen.qc_order_manage.mapper;


import kz.seisen.qc_order_manage.dto.CustomerDto;
import kz.seisen.qc_order_manage.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto toDto(Customer entity);

    Customer toEntity(CustomerDto dto);

    List<CustomerDto> toDtoList(List<Customer> entities);
}
