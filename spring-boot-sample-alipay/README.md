# AliPay

## 密钥

### 工具下载

https://opendocs.alipay.com/open/291/introduce

- ﻿[WINDOWS](https://ideservice.alipay.com/ide/getPluginUrl.htm?clientType=assistant&platform=win&channelType=WEB)（windows 版本工具请不要安装在含有空格的目录路径下，否则会导致公私钥乱码的问题）
- ﻿[MAC_OSX](https://ideservice.alipay.com/ide/getPluginUrl.htm?clientType=assistant&platform=mac&channelType=WEB)

### 普通公钥方式

- 生成 `应用公钥PublicKey` `应用私钥PrivateKey`

- 将 `应用公钥PublicKey` 上传支付宝 [沙箱环境](https://openhome.alipay.com/platform/appDaily.htm)

- 程序使用 `应用私钥PrivateKey` `支付宝公钥AlipayPublicKey`

### 公钥证书方式

## SDK

### 服务端 SDK（新版）

https://opendocs.alipay.com/open/54/00y8k9

```xml
<!-- https://mvnrepository.com/artifact/com.alipay.sdk/alipay-easysdk -->
<dependency>
    <groupId>com.alipay.sdk</groupId>
    <artifactId>alipay-easysdk</artifactId>
    <version>2.2.0</version>
</dependency>
```

### 服务端 SDK（老版）

https://opendocs.alipay.com/open/54/103419

```xml
<!-- https://mvnrepository.com/artifact/com.alipay.sdk/alipay-sdk-java -->
<dependency>
    <groupId>com.alipay.sdk</groupId>
    <artifactId>alipay-sdk-java</artifactId>
    <version>4.11.66.ALL</version>
</dependency>
```

## 手机网站支付

- 调用时序图

https://opendocs.alipay.com/open/203/105285

- API

https://opendocs.alipay.com/apis/api_1/alipay.trade.wap.pay


## 电脑网站支付

- 调用时序图

https://opendocs.alipay.com/open/270/105899

- API

https://opendocs.alipay.com/apis/api_1/alipay.trade.page.pay

## App支付接口

- 调用时序图

https://opendocs.alipay.com/open/270/105899

- API

https://opendocs.alipay.com/apis/api_1/alipay.trade.app.pay
