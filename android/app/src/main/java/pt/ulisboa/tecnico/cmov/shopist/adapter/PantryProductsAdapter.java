package pt.ulisboa.tecnico.cmov.shopist.adapter;

import android.app.AlertDialog;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.provider.MediaStore;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ulisboa.tecnico.cmov.shopist.MainActivity;
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

        PopupMenu.OnMenuItemClickListener menuItemClickListener = item -> {switch (item.getItemId()) {
            case R.id.pantry_product_options_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle(R.string.delete_product)
                        .setMessage(R.string.delete_product_confirmation)
                        .setPositiveButton(R.string.delete, (dialog, which) -> {
                            ((MainActivity) mContext).getViewModel().deletePantryProduct(product.getPantry().pantryId, product.getProduct().productId);
                            notifyDataSetChanged();
                        })
                        .setNegativeButton(R.string.cancel, (dialog, which) -> {dialog.dismiss();});
                builder.create().show();
                return true;
            default:
                return false;
        }};

        View.OnClickListener itemOptionsListener = v -> {
            PopupMenu listOptionsMenu = new PopupMenu(v.getContext(), v);
            MenuInflater inflater1 = listOptionsMenu.getMenuInflater();
            inflater1.inflate(R.menu.pantry_product_options_menu, listOptionsMenu.getMenu());
            listOptionsMenu.setOnMenuItemClickListener(menuItemClickListener);
            listOptionsMenu.show();
        };

        // Set item views based on your views and data model
        TextView tvItemName = holder.name;
        tvItemName.setText(product.getProduct().productName);

        TextView tvWanted = holder.quantityWanted;
        String wantedText = mContext.getString(R.string.total_wanted) + product.getQttNeeded().toString();
        tvWanted.setText(wantedText);

        /*ImageView imageView = holder.image;
        if(product.getImage() != null) {
            imageView.setImageBitmap(product.getImage());
        }*/

        holder.options.setOnClickListener(itemOptionsListener);

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
        public ImageButton options;
        public LinearLayout productClickableArea;

        public ViewHolder(View view) {
            super(view);
            // image = view.findViewById(R.id.item_image);
            name = view.findViewById(R.id.product_item_name);
            quantityWanted = view.findViewById(R.id.product_description);
            options = view.findViewById(R.id.pantry_product_options_bt);
            productClickableArea = view.findViewById(R.id.product_item_clickable_area);
        }
    }
}
