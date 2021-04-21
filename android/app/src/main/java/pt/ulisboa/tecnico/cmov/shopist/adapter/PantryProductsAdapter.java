package pt.ulisboa.tecnico.cmov.shopist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ulisboa.tecnico.cmov.shopist.R;

public class PantryProductsAdapter extends RecyclerView.Adapter<PantryProductsAdapter.ViewHolder>{

//    private List<PantryProduct> mProducts;
//
//    public PantryProductsAdapter(List<PantryProduct> products) {
//        mProducts = products;
//    }

    @NonNull
    @Override
    public PantryProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View ProductView = inflater.inflate(R.layout.product_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(ProductView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        PantryProduct product = mProducts.get(position);

        // Set item views based on your views and data model
        TextView tvItemName = holder.name;
//        tvItemName.setText(product.getProduct().name());

        TextView tvWanted = holder.quantityWanted;
//        String wantedText = R.string.total_wanted + product.getQuantityWanted().toString();
//        tvWanted.setText(wantedText);
//
//        EditText editText = holder.quantityAvailable;
//        editText.setText(product.getQuantityAvailable());

        /*ImageView imageView = holder.image;
        if(product.getImage() != null) {
            imageView.setImageBitmap(product.getImage());
        }*/
    }

    @Override
    public int getItemCount() {

//        return mProducts.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView quantityWanted;
        // public ImageView image;
        public EditText quantityAvailable;

        public ViewHolder(View view) {
            super(view);
            // image = view.findViewById(R.id.item_image);
            name = view.findViewById(R.id.product_item_name);
            quantityWanted = view.findViewById(R.id.product_quant_wanted);
            quantityAvailable = view.findViewById(R.id.product_quant);
        }
    }
}
