package com.topjohnwu.magisk.superuser;

import android.content.ContentValues;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;


public class Policy {
    public static final int INTERACTIVE = 0;
    public static final int DENY = 1;
    public static final int ALLOW = 2;

    public int uid, policy;
    public long until;
    public boolean logging = true, notification = true;
    public String packageName, appName;
    public PackageInfo info;

    public Policy(int uid, PackageManager pm) throws PackageManager.NameNotFoundException {
        String[] pkgs = pm.getPackagesForUid(uid);
        if (pkgs != null && pkgs.length > 0) {
            info = pm.getPackageInfo(pkgs[0], 0);
            this.uid = uid;
            packageName = pkgs[0];
            appName = info.applicationInfo.loadLabel(pm).toString();
        } else throw new PackageManager.NameNotFoundException();
    }

    public Policy(Cursor c) {
        uid = c.getInt(c.getColumnIndex("uid"));
        packageName = c.getString(c.getColumnIndex("package_name"));
        appName = c.getString(c.getColumnIndex("app_name"));
        policy = c.getInt(c.getColumnIndex("policy"));
        until = c.getLong(c.getColumnIndex("until"));
        logging = c.getInt(c.getColumnIndex("logging")) != 0;
        notification = c.getInt(c.getColumnIndex("notification")) != 0;
    }
    
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put("uid", uid);
        values.put("package_name", packageName);
        values.put("app_name", appName);
        values.put("policy", policy);
        values.put("until", until);
        values.put("logging", logging ? 1 : 0);
        values.put("notification", notification ? 1 : 0);
        return values;
    }
}
