package task6.MyOnlineStoreReactive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import task6.MyOnlineStoreReactive.DTO.ProductDTO;
import task6.MyOnlineStoreReactive.service.CartService;
import task6.MyOnlineStoreReactive.service.ProductService;

import java.io.IOException;
import java.util.List;

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
    public String products(Model model,
                           @RequestParam(name = "sampleSearch", defaultValue = "", required = false) String sampleSearch,
                           @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
                           @RequestParam(name = "pageSize", defaultValue = "50") int pageSize,
                           @RequestParam(name = "sorting", defaultValue = "", required = false) String sorting){

        int productCount = productService.findCountByFilter(sampleSearch);
        int allPages = (productCount / pageSize) + (productCount % pageSize > 0 ? 1 : 0);

        List<ProductDTO> products = productService.findAllWithPaginationByFilter(sampleSearch, pageNumber, pageSize, sorting);

        model.addAttribute("products", products);
        model.addAttribute("sampleSearch", sampleSearch);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sorting", sorting);
        model.addAttribute("allPages", allPages);

        return "products";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable(value = "id") Long id,
                             @RequestParam(name = "quantity", defaultValue = "1", required = false) int quantity,
                             Model model) {
        ProductDTO productDTO = productService.getById(id);

        model.addAttribute("product", productDTO);
        model.addAttribute("quantity", quantity);

        return "product";
    }

    @PostMapping("/{id}/add-to-cart")
    public String addToCart(@PathVariable("id") Long id,
                            @RequestParam(name = "quantity", defaultValue = "1") int quantity){
        cartService.addProductQuantity(id, quantity);
        return "redirect:/products";
    }

    @GetMapping("/add")
    public String add(Model model) {
        ProductDTO productDTO = new ProductDTO();
        model.addAttribute("product", productDTO);

        return "add-product";
    }

    @PostMapping(path = "/save")
    public String save(@ModelAttribute ProductDTO productDTO,
                       @RequestPart(value = "pictureFile", required = false) MultipartFile file) throws IOException {
        if (file != null) {
            System.out.println(file);
            productDTO.setPicture(file.getBytes());
        }
        productService.save(productDTO);

        return "redirect:/products"; // Возвращаем страницу, чтобы она перезагрузилась
    }

}
