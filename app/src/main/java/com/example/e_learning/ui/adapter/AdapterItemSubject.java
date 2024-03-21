package com.example.e_learning.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_learning.data.Course;
import com.example.e_learning.databinding.ItemDocSubjectBinding;

import java.util.ArrayList;

public class AdapterItemSubject extends RecyclerView.Adapter<AdapterItemSubject.Holder> {


    public void setList(ArrayList<Course> list) {
        this.list = list;
    }

    private ArrayList<Course> list;
    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDocSubjectBinding binding = ItemDocSubjectBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        ItemDocSubjectBinding binding;

        public Holder(ItemDocSubjectBinding binding) {
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

        public void bind(Course course) {
            binding.courseName.setText(course.getCourseName());
        }
    }

    public interface OnItemClick {
        void onClick(Course course);
    }
}
