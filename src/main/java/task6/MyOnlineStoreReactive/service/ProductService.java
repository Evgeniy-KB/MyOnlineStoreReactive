package task6.MyOnlineStoreReactive.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import task6.MyOnlineStoreReactive.DTO.ProductDTO;

public interface ProductService {

    Mono<ProductDTO> getById(Long id);

    Mono<ProductDTO> save(ProductDTO productDTO);

    Mono<Long> findCountByFilter(String sampleSearch);

    Flux<ProductDTO> findAllWithPaginationByFilter(String sampleSearch, Long pageNumber, Long pageSize, String sorting);
}


