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

public class LocationAdapter$ViewHolder_ViewBinding implements Unbinder {
  private LocationAdapter.ViewHolder target;

  @UiThread
  public LocationAdapter$ViewHolder_ViewBinding(LocationAdapter.ViewHolder target, View source) {
    this.target = target;

    target.locationTitle = Utils.findRequiredViewAsType(source, R.id.locationText, "field 'locationTitle'", TextView.class);
    target.locationImage = Utils.findRequiredViewAsType(source, R.id.locationImage, "field 'locationImage'", ImageView.class);
    target.locationcheckMark = Utils.findRequiredViewAsType(source, R.id.checkmark, "field 'locationcheckMark'", ImageView.class);
    target.locationCard = Utils.findRequiredViewAsType(source, R.id.locationCard, "field 'locationCard'", CardView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LocationAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.locationTitle = null;
    target.locationImage = null;
    target.locationcheckMark = null;
    target.locationCard = null;
  }
}
