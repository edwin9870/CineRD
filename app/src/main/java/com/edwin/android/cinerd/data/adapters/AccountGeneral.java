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

    /**
     * This is the type of account we are using. i.e. we can specify our app or apps
     * to have different types, such as 'read-only', 'sync-only', & 'admin'.
     */
    public static final String ACCOUNT_TYPE = "edwin.com";

    /**
     * This is the name that appears in the Android 'Accounts' settings.
     */
    private static final String ACCOUNT_NAME = "CineRD Account";
    public static final String AUTHORITY = "com.edwin.android.cinerd";



    /**
     * Gets the standard sync account for our app.
     *
     * @return {@link Account}
     */
    public static Account getAccount() {
        return new Account(ACCOUNT_NAME, ACCOUNT_TYPE);
    }

    /**
     * Creates the standard sync account for our app.
     *
     * @param context {@link Context}
     */
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
            MoviesSyncAdapter.performSync();
        }
    }
}
