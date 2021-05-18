package pt.ulisboa.tecnico.cmov.shopist.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.cmov.shopist.dto.ProductPrice;
import pt.ulisboa.tecnico.cmov.shopist.pojo.ProductRating;
import pt.ulisboa.tecnico.cmov.shopist.service.ProductService;

@Slf4j
@RestController
@RequestMapping(path = "/product")
public class ProductController {
    ProductService productService;

    // send price per barcode
    // get price per barcode
    // save image per barcode
    // get images per barcode

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /* @PostMapping
    public String addImageToProduct(@RequestBody ProductImage productImage) {
        return productService.addImageToProduct(productImage);
    }

    @GetMapping("/images")
    public List<ProductImage> getProductImagesById(@RequestParam(required = false) String productId,
                                                   @RequestParam(required = false) String productName) throws ProductNotFoundException {
        if (productId != null) {
            return productService.getProductImagesById(productId);
        } else if (productName != null) {
            return productService.getProductImagesByName(productName);
        } else {
            throw new ProductNotFoundException("Must insert product id or product name");
        }
    }*/

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
