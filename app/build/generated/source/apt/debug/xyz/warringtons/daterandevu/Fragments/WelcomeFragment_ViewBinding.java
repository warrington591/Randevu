// Generated code from Butter Knife. Do not modify!
package xyz.warringtons.daterandevu.Fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import xyz.warringtons.daterandevu.R;

public class WelcomeFragment_ViewBinding implements Unbinder {
  private WelcomeFragment target;

  private View view2131230895;

  private View view2131230848;

  private View view2131230940;

  @UiThread
  public WelcomeFragment_ViewBinding(final WelcomeFragment target, View source) {
    this.target = target;

    View view;
    target.emailET = Utils.findRequiredViewAsType(source, R.id.emailET, "field 'emailET'", EditText.class);
    target.passwordET = Utils.findRequiredViewAsType(source, R.id.passwordET, "field 'passwordET'", EditText.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressBar, "field 'progressBar'", ProgressBar.class);
    view = Utils.findRequiredView(source, R.id.logIn, "method 'LogIn'");
    view2131230895 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.LogIn();
      }
    });
    view = Utils.findRequiredView(source, R.id.forgotPassword, "method 'forgotPassword'");
    view2131230848 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.forgotPassword();
      }
    });
    view = Utils.findRequiredView(source, R.id.register, "method 'register'");
    view2131230940 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.register();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    WelcomeFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.emailET = null;
    target.passwordET = null;
    target.progressBar = null;

    view2131230895.setOnClickListener(null);
    view2131230895 = null;
    view2131230848.setOnClickListener(null);
    view2131230848 = null;
    view2131230940.setOnClickListener(null);
    view2131230940 = null;
  }
}
