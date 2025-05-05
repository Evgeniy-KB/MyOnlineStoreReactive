package task6.MyOnlineStoreReactive.service;

import task6.MyOnlineStoreReactive.DTO.ProductDTO;

import java.util.List;

public interface ProductService {

    ProductDTO getById(Long id);

    void save(ProductDTO productDTO);

    int findCountByFilter(String sampleSearch);

    List<ProductDTO> findAllWithPaginationByFilter(String sampleSearch, int pageNumber, int pageSize, String sorting);

}


