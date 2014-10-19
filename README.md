# Cordova Android Alipay Plugin

Developing, don't use at production environment.

## Installation

    cordova plugin add com.h2ocube.alipay

## Use

    alipay.settings = {
      partner: '2088***',
      seller_id: 'ben@h2ocube.com',
      private_key: '***'
    }

    alipay.pay({
      out_trade_no: new Date().getTime(),
      subject: '商品标题',
      body: '商品描述',
      total_fee: '0.01',
      notify_url: 'http://m.alipay.com',
      return_url: 'http://m.alipay.com'
    });

### Supported Platforms

- Android
