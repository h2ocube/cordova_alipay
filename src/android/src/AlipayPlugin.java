package com.h2ocube.alipay;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import com.alipay.android.app.sdk.AliPay;

public class AlipayPlugin extends CordovaPlugin {
  public static final String LOG_TAG = "AlipayPlugin";
  private static final int RQF_PAY = 1;
  private static final int RQF_LOGIN = 2;

  public Activity activity;
  public Context content;
  public CallbackContext callback_context;

  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    activity = cordova.getActivity();
    content = cordova.getActivity().getApplicationContext();
    callback_context = callbackContext;

    try {
      String info = args.getString(0);
      String sign = Rsa.sign(info, args.getString(1));

      sign = URLEncoder.encode(sign);
      info += "&sign=\"" + sign + "\"&sign_type=\"RSA\"";
      Log.i(LOG_TAG, "info = " + info);

      final String orderInfo = info;

      new Thread(){
        public void run(){
          AliPay alipay = new AliPay(activity, mHandler);
          String result = alipay.pay(orderInfo);
          Log.i(LOG_TAG, "result = " + result);
          Message msg = new Message();
          msg.what = RQF_PAY;
          msg.obj = result;
          mHandler.sendMessage(msg);
        }
      }.start();

    } catch (Exception ex) {
      ex.printStackTrace();
      Toast.makeText(content, "error", Toast.LENGTH_SHORT).show();
      PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR, ex.getMessage());
      pluginResult.setKeepCallback(true);
      callback_context.sendPluginResult(pluginResult);
    }

    return true;
  }

  Handler mHandler = new Handler() {
    public void handleMessage(android.os.Message msg) {
      Result result = new Result((String) msg.obj);
      switch (msg.what) {
      case RQF_PAY:
      case RQF_LOGIN: {
        Toast.makeText(content, result.getResult(), Toast.LENGTH_SHORT).show();
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, result.getResult());
        pluginResult.setKeepCallback(true);
        callback_context.sendPluginResult(pluginResult);
      }
        break;
      default:
        break;
      }
    };
  };
}
