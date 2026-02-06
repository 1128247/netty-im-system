package com.main.client.handler;

import com.main.MsgProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserListResponseHandler extends SimpleChannelInboundHandler<MsgProtos.UserListResponse> {
  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MsgProtos.UserListResponse userListResponse) throws Exception {
    log.info("当前在线的用户: {}", userListResponse.getUserIdsList().asByteStringList());
  }
}
