package task7.controller;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import task7.dto.ProductDto;
import task7.service.CartService;
import task7.service.ProductService;

import java.io.IOException;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CartService cartService;

    public ProductController(ProductService productService, CartService cartService){
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping
    public Mono<String> products(Model model,
                                 @RequestParam(name = "sampleSearch", defaultValue = "", required = false) String sampleSearch,
                                 @RequestParam(name = "pageNumber", defaultValue = "1") Long pageNumber,
                                 @RequestParam(name = "pageSize", defaultValue = "50") Long pageSize,
                                 @RequestParam(name = "sorting", defaultValue = "", required = false) String sorting){
        Flux<ProductDto> products = productService.findAllWithPaginationByFilter(sampleSearch, pageNumber, pageSize, sorting);

        Mono<Long> allPages = productService.findCountByFilter(sampleSearch)
                .flatMap(countProducts -> Mono.just((countProducts / pageSize) + (countProducts % pageSize > 0 ? 1 : 0)));

        model.addAttribute("products", products);
        model.addAttribute("sampleSearch", sampleSearch);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sorting", sorting);
        model.addAttribute("allPages", allPages);

        return Mono.just("products");
    }

    @GetMapping("/{id}")
    public Mono<String> getProduct(Model model,
                                   @PathVariable(value = "id") Long id,
                                   @RequestParam(name = "quantity", defaultValue = "1", required = false) Long quantity) {
        return productService.getById(id)
                .doOnNext(x -> model.addAttribute("product", x))
                .doOnNext(x -> model.addAttribute("quantity", quantity))
                .then(Mono.just("product"));
    }

    @PostMapping("/{id}/add-to-cart")
    public Mono<String> addToCart(@PathVariable("id") Long id, @ModelAttribute ProductDto productDto){
        return cartService.addProductQuantity(id, productDto.getQuantity())
                .then(Mono.just("redirect:/products"));
    }

    @GetMapping("/add")
    public Mono<String> add(Model model) {
        model.addAttribute("product", new ProductDto());
        return Mono.just("add-product");
    }

    @PostMapping(path = "/save")
    public Mono<String> save(@ModelAttribute ProductDto productDto,
                             @RequestPart(value = "pictureFile", required = false) FilePart filePart) throws IOException {
        if (filePart != null) {
            return DataBufferUtils.join(filePart.content())
                    .map(dataBuffer -> dataBuffer.asByteBuffer().array())
                    .map(bytes -> {
                        productDto.setPicture(bytes);
                        return productDto;
                    })
                    .flatMap(productService::save)
                    .then(Mono.just("redirect:/products"));
        }

        return productService.save(productDto)
                .then(Mono.just("redirect:/products"));
    }

}
