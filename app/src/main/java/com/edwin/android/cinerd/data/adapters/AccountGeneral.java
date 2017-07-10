package com.edwin.android.cinerd.data.adapters;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.edwin.android.cinerd.R;

/**
 * Created by Edwin Ramirez Ventura on 7/9/2017.
 */


public final class AccountGeneral {

    public static final String ACCOUNT_TYPE = "edwin.com";

    private static final String ACCOUNT_NAME = "CineRD Account";
    public static final String AUTHORITY = "com.edwin.android.cinerd";

    public static Account getAccount() {
        return new Account(ACCOUNT_NAME, ACCOUNT_TYPE);
    }

    public static void createSyncAccount(Context context) {
        boolean created = false;

        Account account = getAccount();
        AccountManager manager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        if (manager.addAccountExplicitly(account, null, null)) {
            long syncFrequency = context.getResources().getInteger(R.integer.time_to_sync);

            ContentResolver.setIsSyncable(account, AUTHORITY, 1);
            ContentResolver.setSyncAutomatically(account, AUTHORITY, true);
            ContentResolver.addPeriodicSync(account, AUTHORITY, Bundle.EMPTY, syncFrequency);

            created = true;
        }

        if(created) {
            MovieSyncAdapter.performSync();
        }
    }
}
