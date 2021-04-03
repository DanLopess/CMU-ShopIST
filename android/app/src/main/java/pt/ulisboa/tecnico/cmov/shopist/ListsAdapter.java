package pt.ulisboa.tecnico.cmov.shopist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ViewHolder>{

    private final List<ProductList> mLists;

    public ListsAdapter(List<ProductList> lists) {
        mLists = lists;
    }

    @NonNull
    @Override
    public ListsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View listView = inflater.inflate(R.layout.list_of_lists_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(listView);

        LinearLayout layout = listView.findViewById(R.id.listItemLinearLayout);
        layout.setOnClickListener(v -> {
            TextView textView = v.findViewById(R.id.listName_textView);
            Log.d("Click List", textView.getText().toString());
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductList list = mLists.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.name;
        textView.setText(list.getName());
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.listName_textView);
        }
    }
}