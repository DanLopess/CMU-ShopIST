package pt.ulisboa.tecnico.cmov.shopist.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.cmov.shopist.dto.ProductPrice;
import pt.ulisboa.tecnico.cmov.shopist.dto.ProductRating;
import pt.ulisboa.tecnico.cmov.shopist.service.ProductService;

@Slf4j
@RestController
@RequestMapping(path = "/product")
public class ProductController {
    ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/ratings")
    public ProductRating getProductRatingByBarcode(@RequestParam(required = true) String barcode) {
        log.info("Received get product rating by barcode request. Barcode: " + barcode);
        return productService.findProductRatingByBarcode(barcode).orElse(null);
    }

    @PostMapping("/ratings")
    public ProductRating addProductRating(@RequestParam(required = true) String barcode,
                                          @RequestParam(required = true) Integer rating,
                                          @RequestParam(required = false) Integer prev) {
        log.info("Received put product rating by barcode request. Barcode: " + barcode);
        return productService.addProductRating(prev, rating, barcode);
    }

    @GetMapping("/prices")
    public ProductPrice getProductPriceByBarcode(@RequestParam(required = true) String barcode) {
        log.info("Received get product price by barcode request. Barcode: " + barcode);
        return productService.findProductPriceByBarcode(barcode).orElse(null);
    }

    @PostMapping("/prices")
    public ProductPrice addProductPrice(@RequestParam(required = true) String barcode,
                                          @RequestParam(required = true) Double price) {
        log.info("Received put product price by barcode request. Barcode: " + barcode + " Price: " + price);
        return productService.addProductPrice(price, barcode);
    }
}
