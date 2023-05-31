package com.kiosk.accessbank.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiosk.accessbank.databinding.ItemButtonBinding;
import com.kiosk.accessbank.listener.OnServiceListener;
import com.kiosk.accessbank.source.model.Service;

import java.util.ArrayList;
import java.util.List;

public class ButtonServiceAdapter extends RecyclerView.Adapter<ButtonServiceAdapter.ButtonServiceViewHolder> {

    OnServiceListener listener;

    public ButtonServiceAdapter(OnServiceListener listener) {
        this.listener = listener;
    }

    private List<Service> data = new ArrayList<>();

    public void setData(List<Service> data) {
        this.data = data;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ButtonServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        return new ButtonServiceViewHolder(ItemButtonBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonServiceViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ButtonServiceViewHolder extends RecyclerView.ViewHolder {

        private ItemButtonBinding binding;

        public ButtonServiceViewHolder(ItemButtonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Service data) {
            binding.description.setText(data.getName());
            binding.textCount.setVisibility(View.GONE);

            binding.imageIcon.setImageResource(data.getIcon());

            binding.cardButton.setOnClickListener(v -> listener.onClick(data));
        }
    }


}
