package com.example.taplalkozaskovetoalkalmazas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder> implements Filterable {
    private ArrayList<FoodItem> FoodItems;
    private ArrayList<FoodItem> FoodItemsAll;
    private Context context;
    private int lastPosition = -1;


    FoodItemAdapter(Context context, ArrayList<FoodItem> items){
        this.FoodItems=items;
        this.FoodItemsAll=items;
        this.context = context;



    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder( FoodItemAdapter.ViewHolder holder, int position) {
        FoodItem currentItem = FoodItems.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context,R.anim.item_animation);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return FoodItems.size();
    }

    @Override
    public Filter getFilter() {
        return foodFilter;
    }

    private Filter foodFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<FoodItem> filtered = new ArrayList<>();
            FilterResults result = new FilterResults();

            if(charSequence == null || charSequence.length() == 0){
                result.count = FoodItemsAll.size();
                result.values = FoodItemsAll;
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(FoodItem item : FoodItemsAll){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filtered.add(item);
                    }
                }

                result.count = filtered.size();
                result.values = filtered;
            }

            return result;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            FoodItems = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameText;
        private TextView caloryText;
        public ViewHolder(View itemView) {
            super(itemView);

            this.nameText = itemView.findViewById(R.id.itemFood);
            this.caloryText = itemView.findViewById(R.id.calory);

            itemView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((FoodBaseActivity)context).DeleteFood(nameText.getText().toString(), caloryText.getText().toString());
                }
            });
        }

        public void bindTo(FoodItem currentItem) {
            nameText.setText(currentItem.getName());
            caloryText.setText(currentItem.getCalory());
        }
    }
}

