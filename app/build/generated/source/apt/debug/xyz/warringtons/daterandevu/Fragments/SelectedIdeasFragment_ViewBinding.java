// Generated code from Butter Knife. Do not modify!
package xyz.warringtons.daterandevu.Fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import xyz.warringtons.daterandevu.R;

public class SelectedIdeasFragment_ViewBinding implements Unbinder {
  private SelectedIdeasFragment target;

  @UiThread
  public SelectedIdeasFragment_ViewBinding(SelectedIdeasFragment target, View source) {
    this.target = target;

    target.dateIdeasRV = Utils.findRequiredViewAsType(source, R.id.dateIdeasRV, "field 'dateIdeasRV'", RecyclerView.class);
    target.weatherDescTv = Utils.findRequiredViewAsType(source, R.id.weatherDescTv, "field 'weatherDescTv'", TextView.class);
    target.temperatureTv = Utils.findRequiredViewAsType(source, R.id.temperatureTv, "field 'temperatureTv'", TextView.class);
    target.weatherImage = Utils.findRequiredViewAsType(source, R.id.weatherImage, "field 'weatherImage'", ImageView.class);
    target.diceRandomize = Utils.findRequiredViewAsType(source, R.id.diceRandomize, "field 'diceRandomize'", FloatingActionButton.class);
    target.dateIdeas = Utils.findRequiredViewAsType(source, R.id.dateIdeas, "field 'dateIdeas'", TextView.class);
    target.dateCompleted = Utils.findRequiredViewAsType(source, R.id.datesCompleted, "field 'dateCompleted'", TextView.class);
    target.leftLine = Utils.findRequiredView(source, R.id.leftLineSeperator, "field 'leftLine'");
    target.rightLine = Utils.findRequiredView(source, R.id.rightLineSeperator, "field 'rightLine'");
  }

  @Override
  @CallSuper
  public void unbind() {
    SelectedIdeasFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.dateIdeasRV = null;
    target.weatherDescTv = null;
    target.temperatureTv = null;
    target.weatherImage = null;
    target.diceRandomize = null;
    target.dateIdeas = null;
    target.dateCompleted = null;
    target.leftLine = null;
    target.rightLine = null;
  }
}
