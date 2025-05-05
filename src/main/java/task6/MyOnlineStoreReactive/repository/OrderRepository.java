package task6.MyOnlineStoreReactive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import task6.MyOnlineStoreReactive.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
