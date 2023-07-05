package com.kiosk.accessbank.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiosk.accessbank.R;
import com.kiosk.accessbank.databinding.ItemButtonBinding;
import com.kiosk.accessbank.listener.OnAccountListener;
import com.kiosk.accessbank.source.model.Account;
import com.kiosk.accessbank.source.model.CustomerAccount;

import java.util.ArrayList;
import java.util.List;

public class ButtonAccountAdapter extends RecyclerView.Adapter<ButtonAccountAdapter.ButtonAccountViewHolder> {

    OnAccountListener listener;
    public ButtonAccountAdapter(OnAccountListener listener){
        this.listener = listener;
    }

    private List<CustomerAccount> data = new ArrayList<>();

    public void setData(List<CustomerAccount> data) {
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

        private Boolean hide = true;

        public ButtonAccountViewHolder(ItemButtonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CustomerAccount data, int i){
            binding.description.setText(data.account_no);
            binding.accountName.setText(data.account_name);
            validateHideBalance(data);
            binding.hideBalance.setOnClickListener(v -> {
                hide = !hide;
                validateHideBalance(data);
            });

            binding.cardButton.setOnClickListener(v -> listener.onClick(data));
            binding.textCount.setText(String.valueOf(i));
        }

        private void validateHideBalance(CustomerAccount data) {
            if (hide){
                binding.balance.setText("xxxxxx");
                binding.hideBalance.setIconResource(R.drawable.eye_slash_regular);
            }else{
                binding.balance.setText(data.currency_code+" "+ data.net_balance);
                binding.hideBalance.setIconResource(R.drawable.eye_regular);

            }
        }
    }


}
