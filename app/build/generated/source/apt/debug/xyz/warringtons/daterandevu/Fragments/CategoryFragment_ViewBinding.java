// Generated code from Butter Knife. Do not modify!
package xyz.warringtons.daterandevu.Fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import xyz.warringtons.daterandevu.R;

public class CategoryFragment_ViewBinding implements Unbinder {
  private CategoryFragment target;

  @UiThread
  public CategoryFragment_ViewBinding(CategoryFragment target, View source) {
    this.target = target;

    target.activitesRV = Utils.findRequiredViewAsType(source, R.id.activitesRV, "field 'activitesRV'", RecyclerView.class);
    target.beginDating = Utils.findRequiredViewAsType(source, R.id.beginDating, "field 'beginDating'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CategoryFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.activitesRV = null;
    target.beginDating = null;
  }
}
