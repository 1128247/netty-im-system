package com.main.client.handler;

import com.main.MsgProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageResponseHandler extends SimpleChannelInboundHandler<MsgProtos.MessageResponse> {
  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MsgProtos.MessageResponse messageResponse) throws Exception {
    log.info("[Client]---[MessageResponseHandler]--接受请求响应");
    if(messageResponse.getResult() && messageResponse.getCode() == 200){
      log.info("发送消息成功, 消息ID: {}", messageResponse.getMsgId());
    }
  }
}
