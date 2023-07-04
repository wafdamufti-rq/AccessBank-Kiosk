package com.kiosk.accessbank.source.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class CustomerAccount implements Parcelable {
    public String account_no;
    public String account_name;
    public String net_balance;
    public String product_name;
    public String email_address;
    public String mobile_no;
    public String account_officer;
    public String currency_code;
    public String branch_code;
    public String branch_name;
    public String profitcenter_miscode;
    public String accountofficer_miscode;
    public String team_miscode;
    public int amountcredit_mtd;
    public int amountdebit_mtd;
    public String pnd_status;
    public String pnc_status;
    public String frozen_status;
    public String block_status;
    public String dormant_status;
    public String record_status;
    public String auth_status;

    protected CustomerAccount(Parcel in) {
        account_no = in.readString();
        account_name = in.readString();
        net_balance = in.readString();
        product_name = in.readString();
        email_address = in.readString();
        mobile_no = in.readString();
        account_officer = in.readString();
        currency_code = in.readString();
        branch_code = in.readString();
        branch_name = in.readString();
        profitcenter_miscode = in.readString();
        accountofficer_miscode = in.readString();
        team_miscode = in.readString();
        amountcredit_mtd = in.readInt();
        amountdebit_mtd = in.readInt();
        pnd_status = in.readString();
        pnc_status = in.readString();
        frozen_status = in.readString();
        block_status = in.readString();
        dormant_status = in.readString();
        record_status = in.readString();
        auth_status = in.readString();
    }

    public static final Creator<CustomerAccount> CREATOR = new Creator<CustomerAccount>() {
        @Override
        public CustomerAccount createFromParcel(Parcel in) {
            return new CustomerAccount(in);
        }

        @Override
        public CustomerAccount[] newArray(int size) {
            return new CustomerAccount[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(account_no);
        dest.writeString(account_name);
        dest.writeString(net_balance);
        dest.writeString(product_name);
        dest.writeString(email_address);
        dest.writeString(mobile_no);
        dest.writeString(account_officer);
        dest.writeString(currency_code);
        dest.writeString(branch_code);
        dest.writeString(branch_name);
        dest.writeString(profitcenter_miscode);
        dest.writeString(accountofficer_miscode);
        dest.writeString(team_miscode);
        dest.writeInt(amountcredit_mtd);
        dest.writeInt(amountdebit_mtd);
        dest.writeString(pnd_status);
        dest.writeString(pnc_status);
        dest.writeString(frozen_status);
        dest.writeString(block_status);
        dest.writeString(dormant_status);
        dest.writeString(record_status);
        dest.writeString(auth_status);
    }
}
