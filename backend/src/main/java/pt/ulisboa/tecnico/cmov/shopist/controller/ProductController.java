package pt.ulisboa.tecnico.cmov.shopist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.ProductNotFoundException;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Product;
import pt.ulisboa.tecnico.cmov.shopist.pojo.ProductImage;
import pt.ulisboa.tecnico.cmov.shopist.service.ProductService;
import pt.ulisboa.tecnico.cmov.shopist.pojo.ProductRating;

import java.util.List;

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
        return productService.findProductRatingByBarcode(barcode).orElse(null);
    }

    @PostMapping("/ratings")
    public ProductRating addProductRating(@RequestParam(required = true) String barcode,
                                          @RequestParam(required = true) Integer rating,
                                          @RequestParam(required = false) Integer prev) {
        return productService.addProductRating(prev, rating, barcode);
    }
}
