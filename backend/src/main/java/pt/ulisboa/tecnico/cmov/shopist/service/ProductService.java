package pt.ulisboa.tecnico.cmov.shopist.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.ProductExistsException;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Product;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class ProductService {
    private Set<Product> products;

    public ProductService() {
        this.products = new HashSet<>();
    }

    public void addProduct(Product p) throws ProductExistsException {
        if (!products.add(p)) {
            throw new ProductExistsException("Product already exists in server.");
        }
    }
}
