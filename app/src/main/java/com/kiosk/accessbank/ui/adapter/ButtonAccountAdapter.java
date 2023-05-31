package com.kiosk.accessbank.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiosk.accessbank.databinding.ItemButtonBinding;
import com.kiosk.accessbank.listener.OnAccountListener;
import com.kiosk.accessbank.source.model.Account;

import java.util.ArrayList;
import java.util.List;

public class ButtonAccountAdapter extends RecyclerView.Adapter<ButtonAccountAdapter.ButtonAccountViewHolder> {

     OnAccountListener listener;
    public ButtonAccountAdapter(OnAccountListener listener){
        this.listener = listener;
    }

    private List<Account> data = new ArrayList<>();

    public void setData(List<Account> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ButtonAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =  LayoutInflater.from(parent.getContext());

        return new ButtonAccountViewHolder(ItemButtonBinding.inflate(layoutInflater,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonAccountViewHolder holder, int position) {
        holder.bind(data.get(position),position+1);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ButtonAccountViewHolder extends RecyclerView.ViewHolder {

        private ItemButtonBinding binding;

        public ButtonAccountViewHolder(ItemButtonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Account data, int i){
            binding.description.setText(data.getName());
            binding.cardButton.setOnClickListener(v -> listener.onClick(data));
            binding.textCount.setText(String.valueOf(i));
        }
    }


}
