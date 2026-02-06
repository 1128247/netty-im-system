package com.main.client.session;

import com.main.MsgProtos;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class SessionManager {

  public static final SessionManager INSTANCE = new SessionManager();
  private Channel channel;
  private boolean isLogin = false;
  private boolean isConnect = false;
  private String userId;

  public static SessionManager getInstance(){
    return INSTANCE;
  }

  public void bindSession(Channel channel){
    this.channel = channel;
  }

  public void sendMessage(MsgProtos.Message message){
    if(channel!=null && channel.isActive()){
      channel.writeAndFlush(message);
      log.info("发送消息成功，消息ID为: {}", message.getMessageRequest().getMsgId());
    }else {
      log.error("发送消息失败channel已经断开: {}", message.getMessageRequest().getMsgId());
    }
  }
  public void setUserId(String userId){
    this.userId = userId;
    this.isLogin = true;
  }
}
