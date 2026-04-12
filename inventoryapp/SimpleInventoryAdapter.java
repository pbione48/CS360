package edu.snhu.cs360.inventoryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SimpleInventoryAdapter extends RecyclerView.Adapter<SimpleInventoryAdapter.ViewHolder> {

    private final ArrayList<String> itemNames;
    private final ArrayList<Integer> itemQtys;
    private final ArrayList<Integer> itemIds;
    private final Context context;
    private final DatabaseHelper db;

    public SimpleInventoryAdapter(Context context,
                                  ArrayList<String> names,
                                  ArrayList<Integer> qtys,
                                  ArrayList<Integer> ids) {
        this.context = context;
        this.itemNames = names;
        this.itemQtys = qtys;
        this.itemIds = ids;
        this.db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventory_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtItemName.setText(itemNames.get(position));
        holder.txtItemQty.setText("Qty: " + itemQtys.get(position));

        int itemId = itemIds.get(position);

        //
        //
        //
        //
        //   DELETE BUTTON
        holder.btnDelete.setOnClickListener(v -> {
            db.deleteItem(itemId);
            itemNames.remove(position);
            itemQtys.remove(position);
            itemIds.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
        });

        //  EDIT BUTTON (Quantity +1)
        holder.btnEdit.setOnClickListener(v -> {
            int newQty = itemQtys.get(position) + 1;

            db.updateItem(itemId, itemNames.get(position), newQty, "General", "");

            itemQtys.set(position, newQty);
            notifyItemChanged(position);

            Toast.makeText(context, "Quantity Updated", Toast.LENGTH_SHORT).show();
        });
    }

    //  MUST be OUTSIDE onBindViewHolder
    @Override
    public int getItemCount() {
        return itemNames.size();
    }

    // ONLY ONE ViewHolder CLASS
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtItemName, txtItemQty;
        Button btnEdit, btnDelete;

        ViewHolder(View itemView) {
            super(itemView);

            txtItemName = itemView.findViewById(R.id.txtItemName);
            txtItemQty = itemView.findViewById(R.id.txtItemQty);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}