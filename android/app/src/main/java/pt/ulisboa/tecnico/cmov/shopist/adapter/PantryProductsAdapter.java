package pt.ulisboa.tecnico.cmov.shopist.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.PantryActivity;
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.PantryProduct;
import pt.ulisboa.tecnico.cmov.shopist.dialog.ProductDetailsDialog;

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
        View ProductView = inflater.inflate(R.layout.list_item_pantry_product, parent, false);
        return new ViewHolder(ProductView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PantryProduct product = mProducts.get(position);

        PopupMenu.OnMenuItemClickListener menuItemClickListener = item -> {
            if (item.getItemId() == R.id.pantry_store_product_options_delete) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle(R.string.delete_product)
                        .setMessage(R.string.delete_product_confirmation)
                        .setPositiveButton(R.string.delete, (dialog, which) -> {
                            ((PantryActivity) mContext).getViewModel()
                                    .deletePantryProduct(product);
                            notifyDataSetChanged();
                        })
                        .setNegativeButton(R.string.cancel, (dialog, which) -> {
                            dialog.dismiss();
                        });
                builder.create().show();
                return true;
            }
            else
                return false;
        };

        View.OnClickListener itemOptionsListener = v -> {
            PopupMenu listOptionsMenu = new PopupMenu(v.getContext(), v);
            MenuInflater inflater1 = listOptionsMenu.getMenuInflater();
            inflater1.inflate(R.menu.options_pantry_store_product_menu, listOptionsMenu.getMenu());
            listOptionsMenu.setOnMenuItemClickListener(menuItemClickListener);
            listOptionsMenu.show();
        };

        View.OnClickListener consumeListener = v -> {
            if (product.getQttAvailable() > 0) {
                product.increaseQttNeeded();
                product.decreaseQttAvailable();
                notifyDataSetChanged();
            } else
                Toast.makeText(mContext, "You don't have this product available in this pantry", Toast.LENGTH_LONG).show();
        };

        // Set item views based on your views and data model
        TextView tvItemName = holder.name;
        tvItemName.setText(product.getProduct().productName);

        TextView tvWanted = holder.info;
        String infoText = mContext.getString(R.string.available) + ": " + product.getQttAvailable() +
                " / " + mContext.getString(R.string.needed) + ": " + product.getQttNeeded();
        tvWanted.setText(infoText);

        /*ImageView imageView = holder.image;
        if(product.getImage() != null) {
            imageView.setImageBitmap(product.getImage());
        }*/

        holder.options.setOnClickListener(itemOptionsListener);

        holder.consumeBt.setOnClickListener(consumeListener);

        holder.productClickableArea.setOnClickListener(v -> {
            onClickProduct(mProducts.get(position));
        });
    }

    private void onClickProduct(PantryProduct pantryProduct) {
       ((PantryActivity) mContext).getViewModel().getProductImage(pantryProduct.getProduct().getProductId())
               .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(image -> {
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
        public TextView info;
        // TODO public ImageView image;
        public ImageButton options;
        public Button consumeBt;
        public LinearLayout productClickableArea;

        public ViewHolder(View view) {
            super(view);
            // image = view.findViewById(R.id.item_image);
            name = view.findViewById(R.id.storeProdName_tv);
            info = view.findViewById(R.id.storeProdInfoText_tv);
            options = view.findViewById(R.id.store_product_options_bt);
            consumeBt = view.findViewById(R.id.addToCart_bt);
            productClickableArea = view.findViewById(R.id.product_item_clickable_area);
        }
    }
}
