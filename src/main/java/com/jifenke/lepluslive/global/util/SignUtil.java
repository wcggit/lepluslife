package com.jifenke.lepluslive.global.util;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

/**
 * APP签名
 *
 * @author zhangwen【zhangwenit@126.com】 2017/6/8 16:21
 **/
public class SignUtil {

  //<editor-fold desc="服务器钥匙对">
//  /**
//   * 客户端公钥
//   */
//  private static String pub;
//
//  @Value("#{T(org.apache.commons.io.FileUtils).readFileToString(" +
//         "T(org.springframework.util.ResourceUtils).getFile('classpath:key/sign-pub-key.txt')" +
//         ")}")
//  public void setPub(String key) {
//    pub = key;
//  }
//
//  /**
//   * 服务器私钥
//   */
//  private static String pri;
//
//  @Value("#{T(org.apache.commons.io.FileUtils).readFileToString(" +
//         "T(org.springframework.util.ResourceUtils).getFile('classpath:key/sign-pri-key.txt')" +
//         ")}")
//  public void setPri(String key) {
//    pri = key;
//  }

  /**
   * 客户端公钥
   */
  private static final String
      pub =
      "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCb3yYtHzwQ4dX7zL261XUqzN2jhkdrL2"
      + "+EA/u2+qh9MAlyEi94r7cBr2l9QwHUCSOMO6fZd9C9+ag59FjjWp2AV/VMu6OuI/Ucmq"
      + "tMFj2ih9dk4UAyaR4C4MoyO0jJVn2E2u6bSQtrRCH+jQMUF5oPWo5E3FGcrExESTmva2m+vQIDAQAB";

  /**
   * 服务器私钥
   */
  private static final String
      pri =
      "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIZazSanwpGo+eO8odNae/"
      + "M70/eEXltKGo4aQtIoWGcqQJCK5rTV3/TjQWuY+hhTpX4dE2OFvREloZisiQOXB0LmpC"
      + "YvCgFTWZhkRxuGuBaHhAnUjNPzqUwsJW7wSDPPwtaqJvf+bgCRxu7hOPpybrBbKGFQmR"
      + "7C0dWp2ibAG4dzAgMBAAECgYA+TJp2QzIIgtckz9ImA/4XwnU+f8WVBR6dJ/Y2JvkfFU"
      + "uM+quRzN6lWj96mhvV7et+OBNGgbwy2EU/WznJTV3S7q3KLz2yHe6TwQzenQ85hHCGk2+"
      + "C9sgcq40c//WiHBimdZA3AoJpc+273Yw80fAgZxesHRBWsaVBzfmnH3K1UQJBAMBiHZWz"
      + "Lkx/JY1peq0bDbdxjxSOiIAVMjUxTKJEDTnk5FyUbNbBA5kQvfp7XPFGPI5NQ3KTKAc0V"
      + "v24Sy5HhKkCQQCyyF+2XHcoGxbgZYUz53+lXrv8ja+4mLbKYjCzb+LOsIcJgimMGIwi4Z"
      + "OuEiXjpQZreyoXxm5n4UvunIUr1KC7AkEAvMOIo8ocM5LOFeLZK+DPpJ8X9OOlq4cgrHC"
      + "i8NQ05glgyDYaVN0t2pJC1fMVTufLoxTMiS86p1cDxA+ANPiXgQJATHszlsvPnbSZAL3tU"
      + "dsC3De+q7fhKFMMP1/p/ZkrqcgSvJqWM3x81p8xkhHpa405RdmWlD0rFf1nglgrECkR6QJ"
      + "APRl86Ykx3GbqkI0nkIlUi+l2Nf5EqTyLDmoRYLA8y/Rm1PW7jZKZherKDz59NxCvTnYJl"
      + "NZUAqxjJZt1RRZa9g==";


  private static final String ALGORITHM = "RSA";
  //  private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
  private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";
  private static final String CHARSET = "UTF-8";

  /**
   * SHA1WithRSA 验证签名
   *
   * @param str     原字符串
   * @param signStr 签名后字符串
   * @return 验证结果
   */
  public static boolean testSign(String str, String signStr) {
    try {
      X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decode(pub));
      KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
      PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
      Signature signature = Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
      signature.initVerify(publicKey);
      signature.update(str.getBytes(CHARSET));
      byte[] encryptedData = Base64.decode(signStr);
      return signature.verify(encryptedData);
    } catch (Exception e) {
    }
    return false;
  }

}
