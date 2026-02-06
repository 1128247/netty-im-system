package com.main.client.handler;

import com.main.MsgProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageHandler extends SimpleChannelInboundHandler<MsgProtos.Message> {
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, MsgProtos.Message msg) throws Exception {
    MsgProtos.HeadType type = msg.getType();
    log.info("请求类型: {}", type);
  }
}
