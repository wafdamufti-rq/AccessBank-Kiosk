package com.kiosk.accessbank.listener;

import com.kiosk.accessbank.source.model.Account;
import com.kiosk.accessbank.source.model.CustomerAccount;

public interface OnAccountListener {
    void onClick(CustomerAccount data);
}
