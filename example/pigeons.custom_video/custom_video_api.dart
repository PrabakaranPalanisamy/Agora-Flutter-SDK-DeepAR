import 'package:pigeon/pigeon.dart';

/// CustomCaptureVideo API definition
@HostApi()
abstract class CustomCaptureVideoApi {

  /// A binding function for setExternalVideoSource on Android/iOS
  void setExternalVideoSource(bool enabled, bool useTexture, bool pushMmode);

  /// start pushExternalVideoFrame on Android/iOS
  void startRenderer();

  /// Stop pushExternalVideoFrame
  void stoRenderer();
}
