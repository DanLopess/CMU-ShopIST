package pt.ulisboa.tecnico.cmov.shopist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ulisboa.tecnico.cmov.shopist.data.ProductList;

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ViewHolder>{

    private final List<ProductList> mLists;

    public ListsAdapter(List<ProductList> lists) {
        mLists = lists;
    }

    @NonNull
    @Override
    public ListsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View listView = inflater.inflate(R.layout.list_of_lists_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(listView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductList list = mLists.get(position);
        String category = list.getCategory(); // used to launch activity
        Context context = holder.itemView.getContext();

        // Set up listeners
        View.OnClickListener itemViewGroupListener = v -> {
            Intent intent = null;
            if (category.equals("Pantry"))
                intent = new Intent(context, PantryActivity.class);
            else if (category.equals("Shopping")) {}
            //TODO intent = new Intent(holder.itemView.getContext(), ShoppingActivity.class);
            if (intent != null) {
                intent.putExtra("List", list);
                context.startActivity(intent);
            }
        };
        PopupMenu.OnMenuItemClickListener menuItemClickListener = item -> {switch (item.getItemId()) {
            case R.id.list_options_delete:
                mLists.remove(position);
                this.notifyDataSetChanged();
                return true;
            case R.id.list_options_sync:
                // TODO sync with server
                return true;
            default:
                return false;
        }};
        View.OnClickListener itemOptionsListener = v -> {
            PopupMenu listOptionsMenu = new PopupMenu(v.getContext(), v);
            MenuInflater inflater1 = listOptionsMenu.getMenuInflater();
            inflater1.inflate(R.menu.list_options_menu, listOptionsMenu.getMenu());
            listOptionsMenu.setOnMenuItemClickListener(menuItemClickListener);
            listOptionsMenu.show();
        };

        // Set item views based on your views and data model
        TextView textView = holder.name;
        textView.setText(list.getName());

        // Set listener to start new activity
        holder.itemView.setOnClickListener(itemViewGroupListener);

        // Set popup menu for each list item
        holder.options.setOnClickListener(itemOptionsListener);
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageButton options;

        public ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.listName_textView);
            options = view.findViewById(R.id.list_options_bt);
        }
    }
}