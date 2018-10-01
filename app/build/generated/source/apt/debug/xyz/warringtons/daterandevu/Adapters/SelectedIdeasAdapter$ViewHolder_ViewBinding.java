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

public class SelectedIdeasAdapter$ViewHolder_ViewBinding implements Unbinder {
  private SelectedIdeasAdapter.ViewHolder target;

  @UiThread
  public SelectedIdeasAdapter$ViewHolder_ViewBinding(SelectedIdeasAdapter.ViewHolder target, View source) {
    this.target = target;

    target.dateideaTv = Utils.findRequiredViewAsType(source, R.id.dateideaTv, "field 'dateideaTv'", TextView.class);
    target.invLL = Utils.findRequiredViewAsType(source, R.id.invLL, "field 'invLL'", LinearLayout.class);
    target.dateIdeaImage = Utils.findRequiredViewAsType(source, R.id.dateIdeaImage, "field 'dateIdeaImage'", ImageView.class);
    target.yelpIcon = Utils.findRequiredViewAsType(source, R.id.yelpIcon, "field 'yelpIcon'", ImageView.class);
    target.googleIcon = Utils.findRequiredViewAsType(source, R.id.googleMapIcon, "field 'googleIcon'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SelectedIdeasAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.dateideaTv = null;
    target.invLL = null;
    target.dateIdeaImage = null;
    target.yelpIcon = null;
    target.googleIcon = null;
  }
}
