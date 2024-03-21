package com.example.e_learning.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_learning.data.InfoStudent;
import com.example.e_learning.databinding.ItemGradesBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterItemGrades extends RecyclerView.Adapter<AdapterItemGrades.Holder> {
    private ArrayList<InfoStudent> list;

    public void setList(ArrayList<InfoStudent> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGradesBinding binding = ItemGradesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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

    public static class Holder extends RecyclerView.ViewHolder {
        ItemGradesBinding binding;

        public Holder(ItemGradesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(InfoStudent infoStudent) {
            binding.studentName.setText("Student name: " + infoStudent.getStudentName());
            binding.studentId.setText("Student ID: " + infoStudent.getStudentId());

            StringBuilder stGradeText = new StringBuilder("Student Grade: ");
            HashMap<String, Integer> stGrade = infoStudent.getStGrade();
            for (Map.Entry<String, Integer> entry : stGrade.entrySet()) {
                int value = entry.getValue();
                stGradeText.append(value);
            }
            binding.quizGrade.setText(stGradeText.toString());

            binding.numOfAttendance.setText("Number of times of attendance: " + infoStudent.getAttendance());
        }


    }
}
