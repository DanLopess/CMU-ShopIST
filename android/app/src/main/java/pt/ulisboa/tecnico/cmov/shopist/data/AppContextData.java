package pt.ulisboa.tecnico.cmov.shopist.data;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class AppContextData extends Application {

    List<ProductList> pantryLists = new ArrayList<>();
    List<ProductList> shoppingLists = new ArrayList<>();
    List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public void addPantryList (String name, String description, String location) {
        pantryLists.add(new ProductList(name, description, "Pantry", location));
    }

    public void addShoppingList (String name, String description, String location) {
        shoppingLists.add(new ProductList(name, description, "Pantry", location));
    }
}
