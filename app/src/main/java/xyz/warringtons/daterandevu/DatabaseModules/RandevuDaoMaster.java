package xyz.warringtons.daterandevu.Modules;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Warrington on 8/12/17.
 */

public class RandevuDaoMaster extends DaoMaster {


    public RandevuDaoMaster(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public int getSchemaVersion() {
        return super.getSchemaVersion();
    }
}
