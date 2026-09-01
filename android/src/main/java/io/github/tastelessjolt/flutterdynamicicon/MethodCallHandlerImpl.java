package io.github.tastelessjolt.flutterdynamicicon;

import android.content.Context;
import android.content.pm.ActivityInfo;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

class MethodCallHandlerImpl implements MethodChannel.MethodCallHandler {

  Context context;

  public MethodCallHandlerImpl(Context c) {
    context = c;
  }

  @Override
  public void onMethodCall(MethodCall call, MethodChannel.Result result) {
    if (call.method.equals("mSupportsAlternateIcons")) {
      result.success(true);
    } else if (call.method.equals("mGetAlternateIconName")) {
      ActivityInfo activityInfo = IconChanger.getCurrentEnabledAlias(context);
      result.success(activityInfo != null ? Helper.getIconNameFromActivity(activityInfo.name) : null);
    } else if (call.method.equals("mSetAlternateIconName")) {
      String iconName = call.argument("iconName");
      // The channel MUST be answered: a handler that returns without calling
      // result leaves the Dart future hanging forever, so the caller never
      // learns the icon changed and anything it meant to do afterwards -
      // persisting the choice, updating state - simply never runs.
      try {
        IconChanger.enableIcon(context, iconName);
        result.success(null);
      } catch (Exception e) {
        result.error("Failed to set icon", e.getMessage(), null);
      }
    } else if (call.method.equals("mGetApplicationIconBadgeNumber")) {
      result.error("Not supported", "Not supported on Android", null);
    } else if (call.method.equals("mSetApplicationIconBadgeNumber")) {
      result.error("Not supported", "Not supported on Android", null);
    } else {
      result.notImplemented();
    }
  }
}