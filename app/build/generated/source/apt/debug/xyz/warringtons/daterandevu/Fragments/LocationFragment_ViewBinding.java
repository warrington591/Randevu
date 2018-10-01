// Generated code from Butter Knife. Do not modify!
package xyz.warringtons.daterandevu.Fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import xyz.warringtons.daterandevu.R;

public class LocationFragment_ViewBinding implements Unbinder {
  private LocationFragment target;

  @UiThread
  public LocationFragment_ViewBinding(LocationFragment target, View source) {
    this.target = target;

    target.locationRV = Utils.findRequiredViewAsType(source, R.id.locationRecyclerView, "field 'locationRV'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LocationFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.locationRV = null;
  }
}
