package com.yojiokisoft.savetheearth;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by taoka on 14/02/08.
 */
public class SettingDao {
    private static SettingDao mInstance = null;
    private static SharedPreferences mSharedPref = null;
    private static Context mContext;

    /**
     * コンストラクタは公開しない
     * インスタンスを取得する場合は、getInstanceを使用する.
     */
    private SettingDao() {
    }

    /**
     * インスタンスの取得.
     *
     * @return SettingDao
     */
    public static SettingDao getInstance() {
        if (mInstance == null) {
            mInstance = new SettingDao();
            mContext = App.getInstance().getAppContext();
            mSharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        }
        return mInstance;
    }

    /**
     * @return 植樹した木の数
     */
    public int getTreeCount() {
        int val = mSharedPref.getInt("TreeCount", 0);
        return val;
    }

    /**
     * 植樹した木の数のセット
     *
     * @param treeCount 植樹した木の数
     * @return true=正常終了
     */
    public boolean setTreeCount(int treeCount) {
        return mSharedPref.edit().putInt("TreeCount", treeCount).commit();
    }
}
