// Generated code from Butter Knife. Do not modify!
package xyz.warringtons.daterandevu.Adapters;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import xyz.warringtons.daterandevu.R;

public class ActivitiesAdapter$ViewHolder_ViewBinding implements Unbinder {
  private ActivitiesAdapter.ViewHolder target;

  @UiThread
  public ActivitiesAdapter$ViewHolder_ViewBinding(ActivitiesAdapter.ViewHolder target, View source) {
    this.target = target;

    target.activityTitle = Utils.findRequiredViewAsType(source, R.id.activityText, "field 'activityTitle'", TextView.class);
    target.activityImage = Utils.findRequiredViewAsType(source, R.id.activityImage, "field 'activityImage'", ImageView.class);
    target.checkMark = Utils.findRequiredViewAsType(source, R.id.checkmark, "field 'checkMark'", ImageView.class);
    target.activityCard = Utils.findRequiredViewAsType(source, R.id.activityCard, "field 'activityCard'", CardView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ActivitiesAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.activityTitle = null;
    target.activityImage = null;
    target.checkMark = null;
    target.activityCard = null;
  }
}
