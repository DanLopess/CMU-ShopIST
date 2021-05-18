package pt.ulisboa.tecnico.cmov.shopist.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.ulisboa.tecnico.cmov.shopist.dto.ProductPrice;
import pt.ulisboa.tecnico.cmov.shopist.dto.ProductRating;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class ProductService {
    private final Map<String, ProductRating> productsRatings;
    private final Map<String, ProductPrice> productsPrices; // barcode -> productPrice
    private final Map<String, String> productsImages;

    public ProductService() {
        this.productsRatings = new HashMap<>();
        this.productsPrices = new HashMap<>();
        this.productsImages = new HashMap<>();
    }

    public Optional<ProductRating> findProductRatingByBarcode(String barcode) {
        if (barcode == null) return Optional.empty();
        return Optional.ofNullable(productsRatings.get(barcode));
    }

    public ProductRating addProductRating(Integer prevRating, Integer rating, String barcode) {
        Optional<ProductRating> productRating = findProductRatingByBarcode(barcode);
        if (productRating.isPresent()) {
            productRating.get().addRating(prevRating, rating);
            return productRating.get();
        } else {
            var prodRating = new ProductRating(rating);
            productsRatings.put(barcode, prodRating);
            return prodRating;
        }
    }

    public Optional<ProductPrice> findProductPriceByBarcode(String barcode) {
        if (barcode == null) return Optional.empty();
        var productPrice = productsPrices.get(barcode);
        if (productPrice == null) {
            productPrice = new ProductPrice();
            productsPrices.put(barcode, productPrice);
        }
        return Optional.of(productPrice);
    }

    public ProductPrice addProductPrice(Double price, String barcode) {
        Optional<ProductPrice> productPrice = findProductPriceByBarcode(barcode);
        if (productPrice.isPresent()) {
            productPrice.get().addPrice(price);
            return productPrice.get();
        } else {
            var newProdPrice = new ProductPrice(price);
            productsPrices.put(barcode, newProdPrice);
            return newProdPrice;
        }
    }
}
