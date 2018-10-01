// Generated code from Butter Knife. Do not modify!
package xyz.warringtons.daterandevu.Fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import xyz.warringtons.daterandevu.R;

public class InitialScreen_ViewBinding implements Unbinder {
  private InitialScreen target;

  private View view2131230884;

  @UiThread
  public InitialScreen_ViewBinding(final InitialScreen target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.link_signup, "field 'signupText' and method 'OnCLick'");
    target.signupText = Utils.castView(view, R.id.link_signup, "field 'signupText'", TextView.class);
    view2131230884 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.OnCLick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    InitialScreen target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.signupText = null;

    view2131230884.setOnClickListener(null);
    view2131230884 = null;
  }
}
