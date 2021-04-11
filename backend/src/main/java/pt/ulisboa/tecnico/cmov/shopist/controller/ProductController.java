package pt.ulisboa.tecnico.cmov.shopist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ulisboa.tecnico.cmov.shopist.service.ProductService;

@RestController
@RequestMapping(path = "/product")
public class ProductController {
    ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // TODO upload image to specific product and get images of a product

}
