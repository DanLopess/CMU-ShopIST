package pt.ulisboa.tecnico.cmov.shopist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Product;

public class SelectProductsAdapter extends RecyclerView.Adapter<SelectProductsAdapter.ViewHolder>{

    private List<Product> mProducts;

    public SelectProductsAdapter(List<Product> products) {
        mProducts = products;
    }

    @NonNull
    @Override
    public SelectProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View ProductView = inflater.inflate(R.layout.select_product_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(ProductView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = mProducts.get(position);

        // Set item views based on your views and data model
        TextView tvItemName = holder.name;
        tvItemName.setText(product.getName());

        TextView tvItemDescription = holder.description;
        tvItemDescription.setText(product.getDescription());

        /*ImageView imageView = holder.image;
        if(product.getImage() != null) {
            imageView.setImageBitmap(product.getImage());
        }*/
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView description;
        // public ImageView image;

        public ViewHolder(View view) {
            super(view);
            // image = view.findViewById(R.id.item_image);
            name = view.findViewById(R.id.product_item_name);
            description = view.findViewById(R.id.product_description);
        }
    }
}
