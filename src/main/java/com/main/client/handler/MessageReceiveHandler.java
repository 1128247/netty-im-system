package com.main.client.handler;

import com.main.MsgProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageReceiveHandler extends SimpleChannelInboundHandler<MsgProtos.MessageRequest> {
  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MsgProtos.MessageRequest messageRequest) throws Exception {
    log.info("收到服务端转发的消息: {}, 发送人为: {}", messageRequest.getContent(), messageRequest.getFromId());
  }
}
