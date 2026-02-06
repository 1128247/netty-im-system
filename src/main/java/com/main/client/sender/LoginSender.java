package com.main.client.sender;

import com.main.MsgProtos;
import com.main.client.converter.LoginMsgConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginSender extends BaseSender {

  public void sendLoginMsg(){
    if(!isConnect()){
      log.info("还没有建立连接");
      return;
    }
    log.info("构造登录消息");
    MsgProtos.Message message = LoginMsgConverter.build(getUser(), getSession());
    log.info("发送登录消息");
    super.sendMsg(message);

  }
}
