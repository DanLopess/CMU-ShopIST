package pt.ulisboa.tecnico.cmov.shopist.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.ulisboa.tecnico.cmov.shopist.dto.ProductPrice;
import pt.ulisboa.tecnico.cmov.shopist.pojo.ProductRating;

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
            ProductRating prodRating = new ProductRating(rating);
            productsRatings.put(barcode, prodRating);
            return prodRating;
        }
    }

    /*public String addImageToProduct(ProductImage productImage) {
        Optional<Product> product = findProductById(productImage.getProductId());
        if (product.isPresent() && productImage.getImageBytes() != null) {
            product.get().addImage(productImage);
            // TODO save to file?
            return "Image added successfully";
        } else {
            return "Product id not found";
        }
    }

    public List<ProductImage> getProductImagesByName(String productName) {
        Optional<Product> product = findProductByName(productName);
        if (product.isPresent() && product.get().getImages() != null) {
            return new ArrayList<>(product.get().getImages());
        } else {
            return new ArrayList<>();
        }
    }

    public List<ProductImage> getProductImagesById(String productId) {
        Optional<Product> product = findProductById(productId);
        if (product.isPresent() && product.get().getImages() != null) {
            return new ArrayList<>(product.get().getImages());
        } else {
            return new ArrayList<>();
        }
    }*/
}
