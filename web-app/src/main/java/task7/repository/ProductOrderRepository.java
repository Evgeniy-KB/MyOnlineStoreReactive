package task7.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import task7.model.ProductOrder;

@Repository
public interface ProductOrderRepository extends R2dbcRepository<ProductOrder, Long> {

}
