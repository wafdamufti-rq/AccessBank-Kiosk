package com.kiosk.accessbank.source.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class AccountSummary implements Parcelable {

    @SerializedName("AccountName")
    private String accountName;
    @SerializedName("AccountNo")
    private String accountNo;
    @SerializedName("AccountOpenDate")
    private String accountOpenDate;
    @SerializedName("AccountStatus")
    private String accountStatus;
    @SerializedName("AcctOpeningBalance")
    private long acctOpeningBalance;
    @SerializedName("AmountBlocked")
    private long amountBlocked;
    @SerializedName("amt_od_limit")
    private long amtOdLimit;
    @SerializedName("AmtOdLimit")
    private long availableBalance;
    @SerializedName("BranchAddress")
    private String branchAddress;
    @SerializedName("BranchCity")
    private String branchCity;
    @SerializedName("BranchCode")
    private String branchCode;
    @SerializedName("BranchName")
    private String branchName;
    @SerializedName("BranchState")
    private String branchState;
    @SerializedName("BVN")
    private String bvn;

    @SerializedName("ClearedBalance")
    private long clearedBalance;
    @SerializedName("ClosingBalance")
    private long closingBalance;
    @SerializedName("CurrencyCode")
    private String currencyCode;
    @SerializedName("CurrencyName")
    private String currencyName;
    @SerializedName("CustID")
    private String custID;
    @SerializedName("HasCOT")
    private long hasCOT;
    @SerializedName("hasImage")
    private long hasImage;
    @SerializedName("IsStaff")
    private String isStaff;
    @SerializedName("LoanStatus")
    private String loanStatus;
    @SerializedName("Lodgement")
    private long lodgement;
    @SerializedName("NetBalance")
    private long netBalance;
    @SerializedName("ProductCode")
    private String productCode;
    @SerializedName("ProductCodeDesc")
    private String productCodeDesc;
    @SerializedName("TotalBalance")
    private long totalBalance;
    @SerializedName("UnclearedBalance")
    private long unclearedBalance;
    @SerializedName("Withdrawal")
    private long withdrawal;
    @SerializedName("CustomerSegment")
    private String customerSegment;
    @SerializedName("hasFixedDeposit")

    private String hasFixedDeposit;
    @SerializedName("tier")
    private String tier;
    @SerializedName("ac_class_type")
    private String acClassType;
    @SerializedName("customer_type")
    private String customerType;

    protected AccountSummary(Parcel in) {
        accountName = in.readString();
        accountNo = in.readString();
        accountOpenDate = in.readString();
        accountStatus = in.readString();
        acctOpeningBalance = in.readLong();
        amountBlocked = in.readLong();
        amtOdLimit = in.readLong();
        availableBalance = in.readLong();
        branchAddress = in.readString();
        branchCity = in.readString();
        branchCode = in.readString();
        branchName = in.readString();
        branchState = in.readString();
        bvn = in.readString();
        clearedBalance = in.readLong();
        closingBalance = in.readLong();
        currencyCode = in.readString();
        currencyName = in.readString();
        custID = in.readString();
        hasCOT = in.readLong();
        hasImage = in.readLong();
        isStaff = in.readString();
        loanStatus = in.readString();
        lodgement = in.readLong();
        netBalance = in.readLong();
        productCode = in.readString();
        productCodeDesc = in.readString();
        totalBalance = in.readLong();
        unclearedBalance = in.readLong();
        withdrawal = in.readLong();
        customerSegment = in.readString();
        hasFixedDeposit = in.readString();
        tier = in.readString();
        acClassType = in.readString();
        customerType = in.readString();
    }

    public static final Creator<AccountSummary> CREATOR = new Creator<AccountSummary>() {
        @Override
        public AccountSummary createFromParcel(Parcel in) {
            return new AccountSummary(in);
        }

        @Override
        public AccountSummary[] newArray(int size) {
            return new AccountSummary[size];
        }
    };

    public String getAccountName() { return accountName; }
    public void setAccountName(String value) { this.accountName = value; }

    public String getAccountNo() { return accountNo; }
    public void setAccountNo(String value) { this.accountNo = value; }

    public String getAccountOpenDate() { return accountOpenDate; }
    public void setAccountOpenDate(String value) { this.accountOpenDate = value; }

    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String value) { this.accountStatus = value; }

    public long getAcctOpeningBalance() { return acctOpeningBalance; }
    public void setAcctOpeningBalance(long value) { this.acctOpeningBalance = value; }

    public long getAmountBlocked() { return amountBlocked; }
    public void setAmountBlocked(long value) { this.amountBlocked = value; }

    public long getAmtOdLimit() { return amtOdLimit; }
    public void setAmtOdLimit(long value) { this.amtOdLimit = value; }

    public long getAvailableBalance() { return availableBalance; }
    public void setAvailableBalance(long value) { this.availableBalance = value; }

    public String getBranchAddress() { return branchAddress; }
    public void setBranchAddress(String value) { this.branchAddress = value; }

    public String getBranchCity() { return branchCity; }
    public void setBranchCity(String value) { this.branchCity = value; }

    public String getBranchCode() { return branchCode; }
    public void setBranchCode(String value) { this.branchCode = value; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String value) { this.branchName = value; }

    public String getBranchState() { return branchState; }
    public void setBranchState(String value) { this.branchState = value; }

    public String getBvn() { return bvn; }
    public void setBvn(String value) { this.bvn = value; }

    public long getClearedBalance() { return clearedBalance; }
    public void setClearedBalance(long value) { this.clearedBalance = value; }

    public long getClosingBalance() { return closingBalance; }
    public void setClosingBalance(long value) { this.closingBalance = value; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String value) { this.currencyCode = value; }

    public String getCurrencyName() { return currencyName; }
    public void setCurrencyName(String value) { this.currencyName = value; }

    public String getCustID() { return custID; }
    public void setCustID(String value) { this.custID = value; }

    public long getHasCOT() { return hasCOT; }
    public void setHasCOT(long value) { this.hasCOT = value; }

    public long getHasImage() { return hasImage; }
    public void setHasImage(long value) { this.hasImage = value; }

    public String getIsStaff() { return isStaff; }
    public void setIsStaff(String value) { this.isStaff = value; }

    public String getLoanStatus() { return loanStatus; }
    public void setLoanStatus(String value) { this.loanStatus = value; }

    public long getLodgement() { return lodgement; }
    public void setLodgement(long value) { this.lodgement = value; }

    public long getNetBalance() { return netBalance; }
    public void setNetBalance(long value) { this.netBalance = value; }

    public String getProductCode() { return productCode; }
    public void setProductCode(String value) { this.productCode = value; }

    public String getProductCodeDesc() { return productCodeDesc; }
    public void setProductCodeDesc(String value) { this.productCodeDesc = value; }

    public long getTotalBalance() { return totalBalance; }
    public void setTotalBalance(long value) { this.totalBalance = value; }

    public long getUnclearedBalance() { return unclearedBalance; }
    public void setUnclearedBalance(long value) { this.unclearedBalance = value; }

    public long getWithdrawal() { return withdrawal; }
    public void setWithdrawal(long value) { this.withdrawal = value; }

    public String getCustomerSegment() { return customerSegment; }
    public void setCustomerSegment(String value) { this.customerSegment = value; }

    public String getHasFixedDeposit() { return hasFixedDeposit; }
    public void setHasFixedDeposit(String value) { this.hasFixedDeposit = value; }

    public String getTier() { return tier; }
    public void setTier(String value) { this.tier = value; }

    public String getACClassType() { return acClassType; }
    public void setACClassType(String value) { this.acClassType = value; }

    public String getCustomerType() { return customerType; }
    public void setCustomerType(String value) { this.customerType = value; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(accountName);
        dest.writeString(accountNo);
        dest.writeString(accountOpenDate);
        dest.writeString(accountStatus);
        dest.writeLong(acctOpeningBalance);
        dest.writeLong(amountBlocked);
        dest.writeLong(amtOdLimit);
        dest.writeLong(availableBalance);
        dest.writeString(branchAddress);
        dest.writeString(branchCity);
        dest.writeString(branchCode);
        dest.writeString(branchName);
        dest.writeString(branchState);
        dest.writeString(bvn);
        dest.writeLong(clearedBalance);
        dest.writeLong(closingBalance);
        dest.writeString(currencyCode);
        dest.writeString(currencyName);
        dest.writeString(custID);
        dest.writeLong(hasCOT);
        dest.writeLong(hasImage);
        dest.writeString(isStaff);
        dest.writeString(loanStatus);
        dest.writeLong(lodgement);
        dest.writeLong(netBalance);
        dest.writeString(productCode);
        dest.writeString(productCodeDesc);
        dest.writeLong(totalBalance);
        dest.writeLong(unclearedBalance);
        dest.writeLong(withdrawal);
        dest.writeString(customerSegment);
        dest.writeString(hasFixedDeposit);
        dest.writeString(tier);
        dest.writeString(acClassType);
        dest.writeString(customerType);
    }
}
