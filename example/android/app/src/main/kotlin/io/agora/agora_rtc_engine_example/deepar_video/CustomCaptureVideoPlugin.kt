//package io.agora.agora_rtc_engine_example.deepar_video
//
//import ai.deepar.ar.*
//import android.app.Activity
//import android.content.pm.ActivityInfo
//import android.graphics.Bitmap
//import android.media.Image
//import android.opengl.GLSurfaceView
//import android.util.DisplayMetrics
//import android.util.Log
//import android.util.Size
//import android.view.Surface
//import android.widget.FrameLayout
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageAnalysis
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.LifecycleOwner
//import io.agora.agora_rtc_engine_example.R
//import io.agora.rtc.RtcEngine
//import io.agora.rtc.base.RtcEnginePlugin
//import io.agora.rtc.video.VideoEncoderConfiguration
//import java.lang.ref.WeakReference
//import java.nio.ByteBuffer
//import java.nio.ByteOrder
//import java.util.concurrent.ExecutionException
//
//class CustomCaptureVideoPlugin(private val activity: WeakReference<Activity>,private val lifecycleOwner: Lifecycle) :
//  RtcEnginePlugin,
//  CustomCaptureVideo.CustomCaptureVideoApi, AREventListener,LifecycleOwner {
//
//  private var rtcEngine: RtcEngine? = null
//  private var deepAR: DeepAR? = null
//  private var surfaceView: GLSurfaceView? = null
//  private var renderer: DeepARRenderer? = null
//  private val defaultLensFacing = CameraSelector.LENS_FACING_FRONT
//  private val lensFacing = defaultLensFacing
//  private var callInProgress = false
// private var local:FrameLayout?=null;
//  private val remoteViewContainer: FrameLayout? = null
//  private var cameraProviderFuture: com.google.common.util.concurrent.ListenableFuture<ProcessCameraProvider>? = null
//  private lateinit var buffers: Array<ByteBuffer?>
//  private var currentBuffer = 0
//  private val NUMBER_OF_BUFFERS = 2
//
//  override fun onRtcEngineCreated(rtcEngine: RtcEngine?) {
//    this.rtcEngine = rtcEngine
//    setupCamera()
//    activity.get()!!.setContentView(R.layout.activity_main)
//    local = activity.get()!!.findViewById<FrameLayout>(R.id.localPreview)
//    deepAR = DeepAR(activity.get()!!.baseContext)
//    deepAR!!.setLicenseKey("0a4db3a09c7942c67ae00ebf43cbf715683fdc937361215f0e9dbd7f1a39bb1903e7449453a16e0f")
//    deepAR!!.initialize(activity.get()!!.baseContext, this)
//    setupVideoConfig()
//    renderer = DeepARRenderer(deepAR, rtcEngine)
//
//    renderer!!.isCallInProgress=true;
//    setupLocalFeed()
//  }
//  private fun getFilterPath(filterName: String): String? {
//    return if (filterName == "none") {
//      null
//    } else "file:///android_asset/$filterName"
//  }
////  override fun onResume() {
////    super.onResume()
////    if (surfaceView != null) {
////      surfaceView!!.onResume()
////    }
////  }
////
////  override fun onPause() {
////    super.onPause()
////    if (surfaceView != null) {
////      surfaceView!!.onPause()
////    }
////  }
//
//
//  private fun setupVideoConfig() {
////    rtcEngine!!.enableVideo()
//    rtcEngine!!.setExternalVideoSource(true, true, true)
//
//    // Please go to this page for detailed explanation
//    // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
//    rtcEngine!!.setVideoEncoderConfiguration(
//      VideoEncoderConfiguration( // Agora seems to work best with "Square" resolutions (Aspect Ratio 1:1)
//        // At least when used in combination with DeepAR
//        VideoEncoderConfiguration.VD_640x480,
//        VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
//        VideoEncoderConfiguration.STANDARD_BITRATE,
//        VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
//      )
//    )
//  }
//  override fun onRtcEngineDestroyed() {
//
//    rtcEngine = null
//  }
//
//  private fun setupLocalFeed() {
//    surfaceView = GLSurfaceView(activity.get()!!.baseContext)
//    surfaceView?.setEGLContextClientVersion(2)
//    surfaceView?.setEGLConfigChooser(8, 8, 8, 8, 16, 0)
//    surfaceView?.setEGLContextFactory(DeepARRenderer.MyContextFactory(renderer))
//    surfaceView?.setRenderer(renderer)
//    surfaceView?.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY)
//    local!!.addView(surfaceView)
//  }
//
////  override fun setExternalVideoSource(
////    enabled: Boolean?,
////    useTexture: Boolean?,
////    pushMmode: Boolean?
////  ) {
////    rtcEngine?.setExternalVideoSource(enabled!!,useTexture!!,pushMmode!!);
////  }
//
//  private fun setupCamera() {
//    cameraProviderFuture = ProcessCameraProvider.getInstance(activity.get()!!.baseContext)
//    cameraProviderFuture?.addListener(Runnable {
//      try {
//        val cameraProvider = cameraProviderFuture?.get()
//        if (cameraProvider != null) {
//          bindImageAnalysis(cameraProvider)
//        }
//      } catch (e: ExecutionException) {
//        e.printStackTrace()
//      } catch (e: InterruptedException) {
//        e.printStackTrace()
//      }
//    }, ContextCompat.getMainExecutor(activity.get()!!.baseContext))
//  }
//
//  private fun bindImageAnalysis(cameraProvider: ProcessCameraProvider) {
//    val cameraPreset = CameraResolutionPreset.P640x480
//    val width: Int
//    val height: Int
//    val orientation: Int = getScreenOrientation()
//    if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE || orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//      width = cameraPreset.width
//      height = cameraPreset.height
//    } else {
//      width = cameraPreset.height
//      height = cameraPreset.width
//    }
//    buffers = arrayOfNulls(2)
////    buffers =  arrayOf(ByteBuffer.allocateDirect(NUMBER_OF_BUFFERS))
//    println("bufer ${buffers.size}")
//    for (i in 0 until NUMBER_OF_BUFFERS) {
//      buffers[i] = ByteBuffer.allocateDirect(width * height * 3)
//      buffers.get(i)?.order(ByteOrder.nativeOrder())
//      buffers.get(i)?.position(0)
//    }
//    val imageAnalysis = ImageAnalysis.Builder().setTargetResolution(Size(width, height))
//      .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
//    imageAnalysis.setAnalyzer(
//      ContextCompat.getMainExecutor(activity.get()!!.baseContext)
//    ) { image -> //image.getImageInfo().getTimestamp();
//      val byteData: ByteArray
//      val yBuffer = image.planes[0].buffer
//      val uBuffer = image.planes[1].buffer
//      val vBuffer = image.planes[2].buffer
//      val ySize = yBuffer.remaining()
//      val uSize = uBuffer.remaining()
//      val vSize = vBuffer.remaining()
//      byteData = ByteArray(ySize + uSize + vSize)
//
//      //U and V are swapped
//      yBuffer[byteData, 0, ySize]
//      vBuffer[byteData, ySize, vSize]
//      uBuffer[byteData, ySize + vSize, uSize]
//      buffers.get(currentBuffer)?.put(byteData)
//      buffers.get(currentBuffer)?.position(0)
//      deepAR?.receiveFrame(
//        buffers.get(currentBuffer),
//        image.width, image.height,
//        image.imageInfo.rotationDegrees,
//        lensFacing == CameraSelector.LENS_FACING_FRONT,
//        DeepARImageFormat.YUV_420_888,
//        image.planes[1].pixelStride
//      )
//      currentBuffer =
//        (currentBuffer + 1) % NUMBER_OF_BUFFERS
//      image.close()
//    }
//    val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
//    cameraProvider.unbindAll()
//    cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis)
//  }
////  private fun setupRemoteVideo(uid: Int) {
////    remoteViewContainer=
////    if (remoteViewContainer!!.getChildCount() >= 1) {
////      return
////    }
////    val surfaceView = RtcEngine.CreateRendererView(activity.get()!!.baseContext)
////    remoteViewContainer!!.addView(VideoCanvas)
////    mRtcEngine.setupRemoteVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid))
////    surfaceView.tag = uid
////    setObserver()
//////        setRemoteViewWeight(1.f);
////  }
//
//  /*
//        get interface orientation from
//        https://stackoverflow.com/questions/10380989/how-do-i-get-the-current-orientation-activityinfo-screen-orientation-of-an-a/10383164
//     */
//  private fun getScreenOrientation(): Int {
//    val rotation: Int = activity.get()!!.getWindowManager().getDefaultDisplay().getRotation()
//    val dm = DisplayMetrics()
//    activity.get()!!.getWindowManager().getDefaultDisplay().getMetrics(dm)
//    val width = dm.widthPixels
//    val height = dm.heightPixels
//    val orientation: Int
//    // if the device's natural orientation is portrait:
//    orientation = if ((rotation == Surface.ROTATION_0
//        || rotation == Surface.ROTATION_180) && height > width ||
//      (rotation == Surface.ROTATION_90
//        || rotation == Surface.ROTATION_270) && width > height
//    ) {
//      when (rotation) {
//        Surface.ROTATION_0 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        Surface.ROTATION_90 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//        Surface.ROTATION_180 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
//        Surface.ROTATION_270 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
//        else -> {
//          Log.e(
//           "TAG",
//            "Unknown screen orientation. Defaulting to " +
//              "portrait."
//          )
//          ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        }
//      }
//    } else {
//      when (rotation) {
//        Surface.ROTATION_0 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//        Surface.ROTATION_90 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        Surface.ROTATION_180 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
//        Surface.ROTATION_270 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
//        else -> {
//          Log.e(
//            "TAG",
//            "Unknown screen orientation. Defaulting to " +
//              "landscape."
//          )
//          ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//        }
//      }
//    }
//    return orientation
//  }
//
////  override fun startRenderer() {
////    renderer!!.isCallInProgress = true
////  }
////
////  override fun stoRenderer() {
////    renderer!!.isCallInProgress = false
////  }
//
//  override fun screenshotTaken(p0: Bitmap?) {
//
//  }
//
//  override fun videoRecordingStarted() {
//
//  }
//
//  override fun videoRecordingFinished() {
//
//  }
//
//  override fun videoRecordingFailed() {
//
//  }
//
//  override fun videoRecordingPrepared() {
//
//  }
//
//  override fun shutdownFinished() {
//
//  }
//
//  override fun initialized() {
//    deepAR!!.switchEffect("mask", getFilterPath("alien"))
////    startRenderer()
//  }
//
//  override fun faceVisibilityChanged(p0: Boolean) {
//
//  }
//
//  override fun imageVisibilityChanged(p0: String?, p1: Boolean) {
//
//  }
//
//  override fun frameAvailable(p0: Image?) {
//
//  }
//
//  override fun error(p0: ARErrorType?, p1: String?) {
//
//  }
//
//  override fun effectSwitched(p0: String?) {
//
//  }
//
//  override fun getLifecycle(): Lifecycle {
//    return lifecycleOwner
//  }
//}
