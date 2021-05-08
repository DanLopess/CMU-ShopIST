package pt.ulisboa.tecnico.cmov.shopist.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.MainActivity;
import pt.ulisboa.tecnico.cmov.shopist.PantryActivity;
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.dialog.ProductDetailsDialog;

public class ListOfProductsAdapter extends RecyclerView.Adapter<ListOfProductsAdapter.ViewHolder>{

    private List<Product> mProducts;
    private final Context mContext;


    public ListOfProductsAdapter(Context context, Observable<List<Product>> lists) {
        lists.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(items -> {
           mProducts = items;
           this.notifyDataSetChanged();
        });
        mContext = context;
    }

    @NonNull
    @Override
    public ListOfProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listView = inflater.inflate(R.layout.list_item_list_of_products, parent, false);
        return new ViewHolder(listView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product prod = mProducts.get(position);

        View.OnClickListener layoutClickListener = v -> {
            ProductDetailsDialog productDetailsDialog = new ProductDetailsDialog(mContext, prod);
            productDetailsDialog.show(((MainActivity) mContext).getSupportFragmentManager(), "product_details");
        };

        PopupMenu.OnMenuItemClickListener menuItemClickListener = item -> {
            if (item.getItemId() == R.id.product_options_delete) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle(R.string.delete_product)
                        .setMessage(R.string.delete_product_confirmation)
                        .setPositiveButton(R.string.delete, (dialog, which) -> {
                            ((MainActivity) mContext).getViewModel().deleteProduct(mProducts.get(position));
                            notifyDataSetChanged();
                        })
                        .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
                builder.create().show();
                return true;
            }
            return false;
        };

        View.OnClickListener itemOptionsListener = v -> {
            PopupMenu listOptionsMenu = new PopupMenu(v.getContext(), v);
            MenuInflater inflater1 = listOptionsMenu.getMenuInflater();
            inflater1.inflate(R.menu.product_options_list_menu, listOptionsMenu.getMenu());
            listOptionsMenu.setOnMenuItemClickListener(menuItemClickListener);
            listOptionsMenu.show();
        };

        // Set item views based on your views and data model
        TextView prodName = holder.name;
        prodName.setText(prod.getProductName());

        TextView prodDesc = holder.desc;
        prodDesc.setText(prod.getProductDescription());

        ImageView imageView = holder.image;
        if(prod.getThumbnailPath() != null) {
            ((MainActivity) mContext).getViewModel().getProductImage(prod.getThumbnailPath()).
                    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(image -> {
                imageView.setImageBitmap(Bitmap.createScaledBitmap(image, imageView.getWidth(), imageView.getHeight(), false));
            });
        }

        // Set popup menu for each list item
        holder.options.setOnClickListener(itemOptionsListener);

        holder.layout.setOnClickListener(layoutClickListener);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView desc;
        public ImageView image;
        public LinearLayout layout;
        public ImageButton options;

        public ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.prodName_tv);
            desc = view.findViewById(R.id.prodInfoText_tv);
            image = view.findViewById(R.id.product_image);
            layout = view.findViewById(R.id.product_item_clickable_area);
            options = view.findViewById(R.id.product_options_bt);
        }
    }
}