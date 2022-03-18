package io.agora.agora_rtc_engine_example.deepar_video;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.Lifecycle;

import java.lang.ref.WeakReference;

import io.agora.rtc.RtcEngine;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class CameraDeepArViewFactory extends PlatformViewFactory {
  private final WeakReference<Activity> mActivity;
  private final BinaryMessenger mBinaryMessenger;
  private static RtcEngine mRtcEngine;
  private final WeakReference<Lifecycle> lifecycleWeakReference;

  public CameraDeepArViewFactory(WeakReference<Activity> activity, BinaryMessenger binaryMessenger, RtcEngine rtcEngine, WeakReference<Lifecycle> lifecycleWeakReference) {
    super(StandardMessageCodec.INSTANCE);
    this.mActivity =  activity;
    this.mBinaryMessenger=binaryMessenger;
    this.mRtcEngine =rtcEngine;
    this.lifecycleWeakReference=lifecycleWeakReference;
  }
  static void setmRtcEngine(RtcEngine rtcEngine){
    System.out.println("deeparset new rtcengine "+rtcEngine);
    mRtcEngine = rtcEngine;
  }
  @Override
  public PlatformView create(Context context, int id, Object args) {
    System.out.println("deepar create view factory");
    return new CameraDeepArView(mActivity,mBinaryMessenger, mRtcEngine,lifecycleWeakReference,context,id,args);
  }
}
