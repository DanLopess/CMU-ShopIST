package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import pt.ulisboa.tecnico.cmov.shopist.data.Product;
import pt.ulisboa.tecnico.cmov.shopist.data.ProductList;

public class PantryActivity extends AppCompatActivity {
    private ProductList mlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        mlist = (ProductList) getIntent().getSerializableExtra("List");
        setSupportActionBar(findViewById(R.id.pantry_toolbar));

        TextView title = findViewById(R.id.pantry_list_title);
        title.setText(mlist.getName());
    }
}