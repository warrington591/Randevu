package xyz.warringtons.daterandevu.database;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Warrington on 11/25/17.
 */

@IgnoreExtraProperties
public class RandevuUser {

    public String name;
    public String email;

    // Default constructor required for calls to
    // DataSnapshot.getValue(RandevuUser.class)
    public RandevuUser() {
    }

    public RandevuUser(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
