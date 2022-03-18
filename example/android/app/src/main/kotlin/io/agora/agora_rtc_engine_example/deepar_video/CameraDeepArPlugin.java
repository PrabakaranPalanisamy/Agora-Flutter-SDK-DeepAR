package io.agora.agora_rtc_engine_example.deepar_video;

import android.Manifest;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.lang.ref.WeakReference;
import java.util.List;

import io.agora.rtc.RtcEngine;
import io.agora.rtc.base.RtcEnginePlugin;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;

//import ai.deepar.ar.DeepAR;


/**
 * CameraDeepArPlugin
 */
public class CameraDeepArPlugin implements FlutterPlugin, ActivityAware, RtcEnginePlugin {

  private WeakReference<Activity> activity;
  private WeakReference<Lifecycle> lifecycleWeakReference;
  private FlutterPluginBinding pluginBinding;
  private BinaryMessenger binaryMessenger;
  private FlutterEngine flutterEngine;
  private RtcEngine rtcEngine;


  public CameraDeepArPlugin(WeakReference<Activity> registrar) {
    this.activity = registrar;

  }

  public CameraDeepArPlugin() {
  }

  public void registerWith(FlutterEngine flutterEngine, WeakReference<Activity> activity, WeakReference<Lifecycle> lifecycleWeakReference) {
    if (activity == null) return;
    System.out.println("deepar registering");
    this.binaryMessenger = flutterEngine.getDartExecutor();
    this.activity = activity;
    this.flutterEngine = flutterEngine;
    this.lifecycleWeakReference = lifecycleWeakReference;
//    final CameraDeepArPlugin plugin = new CameraDeepArPlugin(activity);
    //registrar.activity().getApplication().registerActivityLifecycleCallbacks(plugin);


//    System.out.println("deepar registered");
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
//        lifecycle = FlutterLifecycleAdapter.getActivityLifecycle(binding);
//        lifecycle.addObserver(this);
//    final CameraDeepArViewFactory factory = new CameraDeepArViewFactory(binding.getActivity(),pluginBinding.getBinaryMessenger());
    System.out.println("deepar rtcengine onAttachedToActivity plugin registerd");

    if (this.rtcEngine != null) {
      final CameraDeepArViewFactory factory = new CameraDeepArViewFactory(this.activity, binaryMessenger, this.rtcEngine, lifecycleWeakReference);
      this.flutterEngine
        .getPlatformViewsController()
        .getRegistry()
        .registerViewFactory("deep_ar_camera", factory);
    }
//    pluginBinding
//      .getPlatformViewRegistry()
//      .registerViewFactory(
//        "deep_ar_camera", factory);
//    System.out.println("deepar binding");

  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {

  }

  private void checkForPermission(final MethodChannel.Result result) {
    Dexter.withContext(activity.get())
      .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
      .withListener(new MultiplePermissionsListener() {
        @Override
        public void onPermissionsChecked(MultiplePermissionsReport report) {
          result.success(report.areAllPermissionsGranted());
        }

        @Override
        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken token) {
          token.continuePermissionRequest();
        }
      })
      .check();
  }


  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    System.out.println("deepar attched to enging");
//    pluginBinding = binding;
//    final CameraDeepArViewFactory factory = new CameraDeepArViewFactory(activity,binaryMessenger);
//
//    pluginBinding
//      .getPlatformViewRegistry()
//      .registerViewFactory(
//        "deep_ar_camera", factory);
//    System.out.println("deepar binding");
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    pluginBinding = null;
  }

  @Override
  public void onRtcEngineCreated(@Nullable RtcEngine rtcEngine) {

    this.rtcEngine = rtcEngine;
    System.out.println("deepar rtcengine created plugin registering" + this.rtcEngine);
    final CameraDeepArViewFactory factory = new CameraDeepArViewFactory(this.activity, binaryMessenger, this.rtcEngine, lifecycleWeakReference);
    boolean result = this.flutterEngine
      .getPlatformViewsController()
      .getRegistry()
      .registerViewFactory("deep_ar_camera", factory);
    if (!result)
      CameraDeepArViewFactory.setmRtcEngine(rtcEngine);
    System.out.println("deepar rtcengine created plugin registerd" + result);
  }

  @Override
  public void onRtcEngineDestroyed() {
    this.rtcEngine = null;
  }
}
