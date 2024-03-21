package com.example.e_learning.ui.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_learning.R;
import com.example.e_learning.data.Chat;
import com.example.e_learning.databinding.ItemChatBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class AdapterItemChat extends RecyclerView.Adapter<AdapterItemChat.Holder> {
    OnItemLongClick onItemLongClick;

    public void setOnItemLongClick(OnItemLongClick onItemLongClick) {
        this.onItemLongClick = onItemLongClick;
    }

    public void setList(List<Chat> list) {
        this.list = list;
    }

    private List<Chat> list;

    public AdapterItemChat() {
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ItemChatBinding binding =
                ItemChatBinding.inflate(LayoutInflater.from(parent.getContext())
                        , parent, false);

        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class Holder extends RecyclerView.ViewHolder {


        ItemChatBinding binding;

        public Holder(ItemChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    onItemLongClick.onLongClick(list.get(getLayoutPosition()));
                    return false;
                }
            });
        }

        public void bind(Chat message) {
            binding.message.setText(message.getMessage());
            binding.name.setText(message.getUserName());

            if (message.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
                binding.chatMessage.setGravity(Gravity.END);
                binding.name.setText("you");
                binding.message.setBackgroundResource(R.drawable.background_new_course);
            } else {
                binding.chatMessage.setGravity(Gravity.START);
                binding.message.setBackgroundResource(R.drawable.background_reciver_message);
            }
        }
    }

    interface OnItemLongClick {
        void onLongClick(Chat message);
    }
}
