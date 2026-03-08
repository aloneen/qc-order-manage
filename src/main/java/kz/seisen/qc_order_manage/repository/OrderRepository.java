package kz.seisen.qc_order_manage.repository;

import kz.seisen.qc_order_manage.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
