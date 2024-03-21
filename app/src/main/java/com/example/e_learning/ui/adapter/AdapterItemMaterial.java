package com.example.e_learning.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_learning.databinding.ItemQuizBinding;

import java.util.ArrayList;

public class AdapterItemMaterial extends RecyclerView.Adapter<AdapterItemMaterial.Holder> {
    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    private ArrayList<String> list;
    private AdapterItemMaterial.OnItemClick onItemClick;

    public void setOnItemClick(AdapterItemMaterial.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public AdapterItemMaterial.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuizBinding binding = ItemQuizBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterItemMaterial.Holder holder, int position) {
        holder.binding.quiz.setText("Material " + (position + 1));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        ItemQuizBinding binding;

        public Holder(ItemQuizBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClick != null)
                        onItemClick.onClick(list.get(getLayoutPosition()));
                }
            });
        }
    }

    public interface OnItemClick {
        void onClick(String material);
    }
}
