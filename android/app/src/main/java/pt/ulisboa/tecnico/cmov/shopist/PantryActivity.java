package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Objects;

import pt.ulisboa.tecnico.cmov.shopist.pojo.ProductList;

public class PantryActivity extends AppCompatActivity {
    private ProductList mlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        configToolbar();

        mlist = (ProductList) getIntent().getSerializableExtra("List");

        TextView title = findViewById(R.id.pantry_list_title);
        title.setText(mlist.getName());
    }

    public void createNewProduct(MenuItem item) {
    }

    private void configToolbar() {
        setSupportActionBar(findViewById(R.id.pantry_toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
    }
}