package kz.seisen.qc_order_manage.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kz.seisen.qc_order_manage.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long id;

    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;

    private OrderStatus status;
    private LocalDateTime createdAt;
}
