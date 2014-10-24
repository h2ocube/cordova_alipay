var Alipay = function(){}

Alipay.prototype.settings = {
  partner: '',
  seller_id: '',
  private_key: '',
  locked: false,
  debug: false
}

Alipay.prototype.pay = function(opts, succ, fail){
  var _this = this;

  if(this.settings.locked){
    return false;
  }

  this.settings.locked = true;

  var opts = this.merge({
    out_trade_no: new Date().getTime(),
    subject: '商品标题',
    body: '商品描述',
    total_fee: '0.01',
    notify_url: 'http://m.alipay.com',
    return_url: 'http://m.alipay.com'
  }, opts);

  var succ_func = function(e){
    _this.settings.locked = false;
    if(typeof succ !== 'undefined'){
      succ(e);
    }else{
      alert(e);
    }
  }

  var fail_func = function(e){
    _this.settings.locked = false;
    if(typeof fail !== 'undefined'){
      fail(e);
    }else{
      alert(e);
    }
  }

  var params = 'partner="' + this.settings.partner + '"&seller_id="' + this.settings.seller_id + '"&service="mobile.securitypay.pay"&_input_charset="UTF-8"&payment_type="1"&it_b_pay="1m"';

  for(var k in opts){
    params += '&' + k + '="' + encodeURI(opts[k]) + '"';
  };

  if(opts.debug){
    alert(params);
  }

  cordova.exec(succ_func, fail_func, 'Alipay', 'pay', [params, this.settings.private_key]);

  return true;
};

Alipay.prototype.merge = function(defaults, target){
  if(typeof target === 'undefined'){
    return defaults;
  }
  
  for(var k in defaults){
    if(!target.hasOwnProperty(k)){
      target[k] = defaults[k];
    }
  }

  return target;
};

module.exports = new Alipay();
