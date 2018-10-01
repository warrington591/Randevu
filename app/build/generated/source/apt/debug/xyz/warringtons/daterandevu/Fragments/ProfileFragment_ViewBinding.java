// Generated code from Butter Knife. Do not modify!
package xyz.warringtons.daterandevu.Fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import xyz.warringtons.daterandevu.R;

public class ProfileFragment_ViewBinding implements Unbinder {
  private ProfileFragment target;

  @UiThread
  public ProfileFragment_ViewBinding(ProfileFragment target, View source) {
    this.target = target;

    target.userEmail = Utils.findRequiredViewAsType(source, R.id.userEmailTV, "field 'userEmail'", TextView.class);
    target.userName = Utils.findRequiredViewAsType(source, R.id.user_profile_name, "field 'userName'", TextView.class);
    target.userProfilePhoto = Utils.findRequiredViewAsType(source, R.id.user_profile_photo, "field 'userProfilePhoto'", ImageButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProfileFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.userEmail = null;
    target.userName = null;
    target.userProfilePhoto = null;
  }
}
