// Generated code from Butter Knife. Do not modify!
package xyz.warringtons.daterandevu.Fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import xyz.warringtons.daterandevu.R;

public class DateGeneratorFragment_ViewBinding implements Unbinder {
  private DateGeneratorFragment target;

  @UiThread
  public DateGeneratorFragment_ViewBinding(DateGeneratorFragment target, View source) {
    this.target = target;

    target.dateIdeasRV = Utils.findRequiredViewAsType(source, R.id.dateIdeasRV, "field 'dateIdeasRV'", RecyclerView.class);
    target.addDateIdea = Utils.findRequiredViewAsType(source, R.id.addDateIdea, "field 'addDateIdea'", FloatingActionButton.class);
    target.nextBtn = Utils.findRequiredViewAsType(source, R.id.nextBtnToSelected, "field 'nextBtn'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DateGeneratorFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.dateIdeasRV = null;
    target.addDateIdea = null;
    target.nextBtn = null;
  }
}
