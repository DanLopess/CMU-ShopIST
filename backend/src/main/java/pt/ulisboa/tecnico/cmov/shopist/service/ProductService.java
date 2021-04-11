package pt.ulisboa.tecnico.cmov.shopist.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.ProductExistsException;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ProductService {
    private final Set<Product> products;

    public ProductService() {
        this.products = new HashSet<>();
    }

    public void addProduct(Product p) throws ProductExistsException {
        if (!products.add(p)) {
            throw new ProductExistsException("Product already exists in server.");
        }
    }

    public void addImageToProduct(MultipartFile file, String productName) {

    }

    public List<MultipartFile> getProductImagesByName(String productName) {
        return new ArrayList<>();
    }

    public void deleteImageFromProduct(String productName, String imageName) {

    }
}
