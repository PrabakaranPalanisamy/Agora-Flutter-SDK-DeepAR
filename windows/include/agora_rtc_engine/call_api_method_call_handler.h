#include "third_party/iris/include/iris_rtc_engine.h"
#include <flutter/method_channel.h>
#include <flutter/standard_method_codec.h>

class CallApiMethodCallHandler {
public:
  CallApiMethodCallHandler(agora::iris::rtc::IrisRtcEngine* engine);

  virtual void HandleMethodCall(const flutter::MethodCall<flutter::EncodableValue>& method_call,
      std::unique_ptr<flutter::MethodResult<flutter::EncodableValue>> result) = 0;

  virtual int32_t CallApi(int32_t api_type, const char* params,
    char* result) = 0;

  virtual int32_t CallApi(int32_t api_type, const char* params, void* buffer,
    char* result) = 0;

  virtual const char *CallApiError(int32_t ret) = 0;
protected:
  std::unique_ptr<agora::iris::rtc::IrisRtcEngine> irisRtcEngine_;
};