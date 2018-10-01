// Generated code from Butter Knife. Do not modify!
package xyz.warringtons.daterandevu.Adapters;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import xyz.warringtons.daterandevu.R;

public class YelpBusinessAdapter$ViewHolder_ViewBinding implements Unbinder {
  private YelpBusinessAdapter.ViewHolder target;

  @UiThread
  public YelpBusinessAdapter$ViewHolder_ViewBinding(YelpBusinessAdapter.ViewHolder target, View source) {
    this.target = target;

    target.yelpName = Utils.findRequiredViewAsType(source, R.id.yelpName, "field 'yelpName'", TextView.class);
    target.yelpImage = Utils.findRequiredViewAsType(source, R.id.yelpImage, "field 'yelpImage'", ImageView.class);
    target.yelpRating = Utils.findRequiredViewAsType(source, R.id.yelpRating, "field 'yelpRating'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    YelpBusinessAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.yelpName = null;
    target.yelpImage = null;
    target.yelpRating = null;
  }
}
