package com.example.e_learning.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_learning.databinding.ItemQuizBinding;

import java.util.ArrayList;

public class AdapterItemQuiz extends RecyclerView.Adapter<AdapterItemQuiz.Holder> {


    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    private ArrayList<String> list;
    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuizBinding binding = ItemQuizBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.binding.quiz.setText("Quiz " + (position + 1));
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
        void onClick(String quizId);
    }
}
