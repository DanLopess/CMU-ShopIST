package pt.ulisboa.tecnico.cmov.shopist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import pt.ulisboa.tecnico.cmov.shopist.pojo.PantryProductList;

public class PantryActivity extends AppCompatActivity {

    public static final int ADD_PRODUCTS_REQ = 1;

    private PantryProductList mlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        initialize();
    }

    public void addProducts(MenuItem item) {
        Intent intent = new Intent(this, AddProductsActivity.class);
        startActivityForResult(intent, ADD_PRODUCTS_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //TODO hanlde activity return
        if (requestCode == ADD_PRODUCTS_REQ && resultCode == RESULT_OK) {
            //mlist.(data.getSerializableExtra());
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initialize() {
        Toolbar toolbar = findViewById(R.id.select_products_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        mlist = (PantryProductList) getIntent().getSerializableExtra("List");

        TextView title = findViewById(R.id.add_product_title);
        title.setText(mlist.getName());
    }
}