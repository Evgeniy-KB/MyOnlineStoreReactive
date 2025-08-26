package task7.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import task7.dto.ProductDto;

public interface ProductService {

    Mono<ProductDto> getById(Long id);

    Mono<ProductDto> save(ProductDto productDto);

    Mono<Long> findCountByFilter(String sampleSearch);

    Flux<ProductDto> findAllWithPaginationByFilter(String sampleSearch, Long pageNumber, Long pageSize, String sorting);
}


