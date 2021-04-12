package pt.ulisboa.tecnico.cmov.shopist.pojo;

import android.app.Application;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class AppContextData extends Application {

    List<ProductList> pantryLists = new ArrayList<>();
    List<ProductList> shoppingLists = new ArrayList<>();
    List<Product> products = new ArrayList<>();
    List<ProductImage> productImages = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public void addProductImage(ProductImage image) {
        productImages.add(image);
    }

    public void addPantryList (String name, String description, Location location) {
        pantryLists.add(new PantryProductList(name, description, ProductList.Category.PANTRY, location));
    }

    public void addShoppingList (String name, String description, Location location) {
        shoppingLists.add(new ShoppingProductList(name, description, ProductList.Category.SHOPPING, location));
    }
}
