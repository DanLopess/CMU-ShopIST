package pt.ulisboa.tecnico.cmov.shopist.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.AddPantryProductsActivity;
import pt.ulisboa.tecnico.cmov.shopist.MainActivity;
import pt.ulisboa.tecnico.cmov.shopist.PantryActivity;
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.PantryProduct;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.ProductAndPrincipalImage;
import pt.ulisboa.tecnico.cmov.shopist.dialog.ProductDetailsDialog;
import pt.ulisboa.tecnico.cmov.shopist.viewModel.ViewModel;

public class PantryProductsAdapter extends RecyclerView.Adapter<PantryProductsAdapter.ViewHolder>{

   private List<PantryProduct> mProducts;
   private Context mContext;

   public PantryProductsAdapter(List<PantryProduct> products) {
       mProducts = products;
   }

    @NonNull
    @Override
    public PantryProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        // Inflate the custom layout
        View ProductView = inflater.inflate(R.layout.pantry_product_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(ProductView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PantryProduct product = mProducts.get(position);

        // Set item views based on your views and data model
        TextView tvItemName = holder.name;
        tvItemName.setText(product.getProduct().productName);

        TextView tvWanted = holder.quantityWanted;
        String wantedText = mContext.getString(R.string.total_wanted) + product.getQttNeeded().toString();
        tvWanted.setText(wantedText);

        EditText editText = holder.quantityAvailable;
        editText.setText(product.getQttAvailable().toString());

        /*ImageView imageView = holder.image;
        if(product.getImage() != null) {
            imageView.setImageBitmap(product.getImage());
        }*/



        holder.productClickableArea.setOnClickListener(v -> {
            onClickProduct(mProducts.get(position));
        });
    }

    private void onClickProduct(PantryProduct pantryProduct) {
       ((PantryActivity) mContext).getViewModel().getProductImage(pantryProduct.getProduct().getProductId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(image -> {
           ProductDetailsDialog productDetailsDialog = new ProductDetailsDialog(mContext, pantryProduct, image );
           productDetailsDialog.show(((PantryActivity) mContext).getSupportFragmentManager(), "product_details");
       });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView quantityWanted;
        // TODO public ImageView image;
        public EditText quantityAvailable;
        public LinearLayout productClickableArea;

        public ViewHolder(View view) {
            super(view);
            // image = view.findViewById(R.id.item_image);
            name = view.findViewById(R.id.product_item_name);
            quantityWanted = view.findViewById(R.id.product_description);
            quantityAvailable = view.findViewById(R.id.product_quant);
            productClickableArea = view.findViewById(R.id.product_item_clickable_area);
        }
    }
}
