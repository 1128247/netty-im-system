package com.main.client.handler;

import com.main.MsgProtos;
import com.main.client.command.CommandClient;
import com.main.client.session.SessionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginResponseHandler extends SimpleChannelInboundHandler<MsgProtos.LoginResponse> {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, MsgProtos.LoginResponse msg) throws Exception {
    SessionManager instance = SessionManager.getInstance();
    log.info("[Client]--处理LoginResponse请求 ");
    boolean result = msg.getResult();
    if(result){
      log.info("[Client]:登录成功");
      instance.setUserId("admin");
    }
    CommandClient.LOGIN_LATCH.countDown();
  }
}
