package com.main.client.sender;

import com.main.MsgProtos;
import com.main.client.converter.ChatMsgConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatSender extends BaseSender{


  public void sendMessage(String content, String toId){
    if(!isConnect()){
      log.info("还未建立连接");
      return;
    }
    if(!isLogin()){
      log.info("还未登录");
    }
    MsgProtos.Message message = ChatMsgConverter.build(content, toId, getSession());
    super.sendMsg(message);
  }
}
