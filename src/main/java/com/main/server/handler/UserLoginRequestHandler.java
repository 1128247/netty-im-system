package com.main.server.handler;

import com.main.MsgProtos;
import com.main.server.session.ServerSessionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserLoginRequestHandler extends SimpleChannelInboundHandler<MsgProtos.LoginRequest> {
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, MsgProtos.LoginRequest msg) throws Exception {
    log.info("[Server]--收到登录请求: UID={}, Device={}", msg.getUid(), msg.getDeviceId());
    boolean success = true;
    if(success){
      ServerSessionManager.bindSession(msg.getUid(), ctx.channel());
    }
    MsgProtos.Message response = MsgProtos.Message.newBuilder()
        .setType(MsgProtos.HeadType.LOGIN_RESPONSE)
        .setLoginResponse(MsgProtos.LoginResponse.newBuilder()
            .setResult(success)
            .setInfo(success ? "登录成功" : "Token无效")
            .build())
        .build();
    ctx.writeAndFlush(response);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    ServerSessionManager.unbindSession(ctx.channel());
  }
}
