package xyz.warringtons.daterandevu.Interfaces;

import xyz.warringtons.daterandevu.Modules.Activities;
import xyz.warringtons.daterandevu.database.Category;

/**
 * Created by Warrington on 8/12/17.
 */

public interface ActivityCallBack {
    void perform(String value);
    void perform(String value, boolean status);

    void perform(Category value, boolean status);
    void perform(Activities activities, String value, boolean status);

}
