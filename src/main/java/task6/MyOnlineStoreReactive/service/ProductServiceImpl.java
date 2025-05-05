package task6.MyOnlineStoreReactive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task6.MyOnlineStoreReactive.DTO.ProductDTO;
import task6.MyOnlineStoreReactive.DTOTranslator.ProductDTOTranslator;
import task6.MyOnlineStoreReactive.model.Product;
import task6.MyOnlineStoreReactive.repository.ProductRepository;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final ProductDTOTranslator productDTOTranslator;
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductDTOTranslator productDTOTranslator){
        this.productRepository = productRepository;
        this.productDTOTranslator = productDTOTranslator;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getById(Long id){
        Product product = productRepository.getById(id);
        return productDTOTranslator.ToProductDTO(product);
    }

    @Override
    @Transactional
    public void save(ProductDTO productDTO) {
        Product product = productDTOTranslator.ToProduct(productDTO);
        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public int findCountByFilter(String sampleSearch){
        return productRepository.findCountByFilter(sampleSearch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAllWithPaginationByFilter(String sampleSearch, int pageNumber, int pageSize, String sorting){

        Pageable pageable;

        if (!sorting.isEmpty())
            pageable = PageRequest.of(pageNumber-1, pageSize, Sort.by(sorting));
        else
            pageable = PageRequest.of(pageNumber-1, pageSize);

        return productRepository.findAllWithPaginationByFilter(sampleSearch, pageable).stream().map(productDTOTranslator::ToProductDTO).toList();
    }

}


