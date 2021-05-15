package pt.ulisboa.tecnico.cmov.shopist.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.ProductExistsException;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Product;
import pt.ulisboa.tecnico.cmov.shopist.pojo.ProductImage;

import java.util.*;
import java.util.List;

@Slf4j
@Service
public class ProductService {
    private final Set<Product> products;

    public ProductService() {
        this.products = new HashSet<>();
    }

    public Optional<Product> findProductById(String id) {
        if (id == null) return Optional.empty();
        return products.stream().filter(p -> id.equalsIgnoreCase(p.getId())).findAny();
    }

    public Optional<Product> findProductByName(String name) {
        if (name == null) return Optional.empty();
        return products.stream().filter(p -> name.equalsIgnoreCase(p.getName())).findAny();
    }

    public void addProduct(Product p) throws ProductExistsException {
        if (!products.add(p)) {
            throw new ProductExistsException("Product already exists in server.");
        }
    }

    public String addImageToProduct(ProductImage productImage) {
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
    }
}
