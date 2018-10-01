// Generated code from Butter Knife. Do not modify!
package xyz.warringtons.daterandevu.Adapters;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import xyz.warringtons.daterandevu.R;

public class DateIdeasAdapter$ViewHolder_ViewBinding implements Unbinder {
  private DateIdeasAdapter.ViewHolder target;

  @UiThread
  public DateIdeasAdapter$ViewHolder_ViewBinding(DateIdeasAdapter.ViewHolder target, View source) {
    this.target = target;

    target.dateideaTv = Utils.findRequiredViewAsType(source, R.id.dateideaTv, "field 'dateideaTv'", TextView.class);
    target.invLL = Utils.findRequiredViewAsType(source, R.id.invLL, "field 'invLL'", LinearLayout.class);
    target.completedStatus = Utils.findRequiredViewAsType(source, R.id.completedStatus, "field 'completedStatus'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DateIdeasAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.dateideaTv = null;
    target.invLL = null;
    target.completedStatus = null;
  }
}
