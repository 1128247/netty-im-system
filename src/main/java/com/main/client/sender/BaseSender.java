package com.main.client.sender;

import com.main.MsgProtos;
import com.main.client.session.SessionManager;
import com.main.common.entty.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public abstract class  BaseSender {
  private User user;
  private SessionManager session;

  public boolean isConnect(){
    return session.isConnect();
  }

  public boolean isLogin(){
    if(null == session){
      log.info("session is null");
      return false;
    }
    return session.isLogin();
  }

  public void sendMsg(MsgProtos.Message message){
    if(null == session || !isConnect()){
      log.info("连接未建立");
      return;
    }
    Channel channel = session.getChannel();
    ChannelFuture channelFuture = channel.writeAndFlush(message);
    channelFuture.addListener(future -> {
      if(future.isSuccess()){
        sendSuccess(message);
      }else {
        sendFailed(message);
      }
    });
  }
  protected void sendSuccess(MsgProtos.Message message) {
    log.info("发送成功");

  }

  protected void sendFailed(MsgProtos.Message message) {
    log.info("发送失败");
  }
}
