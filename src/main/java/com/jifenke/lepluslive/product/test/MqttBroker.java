//package com.jifenke.lepluslive.product.test;
//
//import com.ibm.mqtt.MqttClient;
//import com.ibm.mqtt.MqttException;
//import com.ibm.mqtt.MqttSimpleCallback;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
///**
// * Created by wcg on 16/3/9.
// */
//public class MqttBroker {
//  private final static Log logger = LogFactory.getLog(MqttBroker.class);// 日志对象
//  // 连接参数
//  private final static String CONNECTION_STRING = "tcp://127.0.0.1:9901";
//  private final static boolean CLEAN_START = true;
//  private final static short KEEP_ALIVE = 30;// 低耗网络，但是又需要及时获取数据，心跳30s
//  private final static String CLIENT_ID = "master";// 客户端标识
//  private final static int[] QOS_VALUES = { 0, 0, 2, 0 };// 对应主题的消息级别
//  private final static String[] TOPICS = { "Test/TestTopics/Topic1",
//                                           "Test/TestTopics/Topic2", "Test/TestTopics/Topic3",
//                                           "client/keepalive" };
//  private static MqttBroker instance = new MqttBroker();
//
//  private MqttClient mqttClient;
//
//  /**
//   * 返回实例对象
//   *
//   * @return
//   */
//  public static MqttBroker getInstance() {
//    return instance;
//  }
//
//  /**
//   * 重新连接服务
//   */
//  private void connect() throws MqttException {
//    logger.info("connect to mqtt broker.");
//    mqttClient = new MqttClient(CONNECTION_STRING);
//    logger.info("***********register Simple Handler***********");
//    SimpleCallbackHandler simpleCallbackHandler = new SimpleCallbackHandler();
//    mqttClient.registerSimpleHandler(simpleCallbackHandler);// 注册接收消息方法
//    mqttClient.connect(CLIENT_ID, CLEAN_START, KEEP_ALIVE);
//    logger.info("***********subscribe receiver topics***********");
//    mqttClient.subscribe(TOPICS, QOS_VALUES);// 订阅接主题
//
//    logger.info("***********CLIENT_ID:" + CLIENT_ID);
//    /**
//     * 完成订阅后，可以增加心跳，保持网络通畅，也可以发布自己的消息
//     */
//    mqttClient.publish("keepalive", "keepalive".getBytes(), QOS_VALUES[0],
//                       true);// 增加心跳，保持网络通畅
//  }
//
//  /**
//   * 发送消息
//   *
//   * @param clientId
//   * @param messageId
//   */
//  public void sendMessage(String clientId, String message) {
//    try {
//      if (mqttClient == null || !mqttClient.isConnected()) {
//        connect();
//      }
//
//      logger.info("send message to " + clientId + ", message is "
//                  + message);
//      // 发布自己的消息
//      mqttClient.publish("GMCC/client/" + clientId, message.getBytes(),
//                         0, false);
//    } catch (MqttException e) {
//      logger.error(e.getCause());
//      e.printStackTrace();
//    }
//  }
//
//  /**
//   * 简单回调函数，处理server接收到的主题消息
//   *
//   * @author Join
//   *
//   */
//  class SimpleCallbackHandler implements MqttSimpleCallback {
//
//    /**
//     * 当客户机和broker意外断开时触发 可以再此处理重新订阅
//     */
//    @Override
//    public void connectionLost() throws Exception {
//      // TODO Auto-generated method stub
//      System.out.println("客户机和broker已经断开");
//    }
//
//    /**
//     * 客户端订阅消息后，该方法负责回调接收处理消息
//     */
//    @Override
//    public void publishArrived(String topicName, byte[] payload, int Qos,
//                               boolean retained) throws Exception {
//      // TODO Auto-generated method stub
//      System.out.println("订阅主题: " + topicName);
//      System.out.println("消息数据: " + new String(payload));
//      System.out.println("消息级别(0,1,2): " + Qos);
//      System.out.println("是否是实时发送的消息(false=实时，true=服务器上保留的最后消息): "
//                         + retained);
//    }
//
//  }
//
//  public static void main(String[] args) {
//    new MqttBroker().sendMessage("client", "message");
//  }
//}
