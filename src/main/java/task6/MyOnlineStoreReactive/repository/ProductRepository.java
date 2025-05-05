package task6.MyOnlineStoreReactive.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import task6.MyOnlineStoreReactive.model.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT COUNT(1) FROM Product p WHERE LOWER(p.title) LIKE CONCAT('%',LOWER(?1),'%') or LOWER(p.description) LIKE CONCAT('%',LOWER(?1),'%')")
    int findCountByFilter(String sampleSearch);

    @Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE CONCAT('%',LOWER(?1),'%') or LOWER(p.description) LIKE CONCAT('%',LOWER(?1),'%')")
    List<Product> findAllWithPaginationByFilter(String sampleSearch, Pageable pageable);
}
