package com.main.client.handler;

import com.main.MsgProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeartBeatArtisanClientHandler extends SimpleChannelInboundHandler<MsgProtos.KeepLiveResponse> {

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if(evt instanceof IdleStateEvent){
      IdleStateEvent event = (IdleStateEvent) evt;
      switch (event.state()){
        case WRITER_IDLE -> {
          MsgProtos.Message message = MsgProtos.Message.newBuilder().setType(MsgProtos.HeadType.KEEPALIVE_REQUEST)
              .setKeepLiveRequest(MsgProtos.KeepLiveRequest.newBuilder().setContent("PING").build()).build();
          log.debug("发送心跳包");
          ctx.writeAndFlush(message);
        }
      }
    }else {
      super.userEventTriggered(ctx, evt);
    }
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, MsgProtos.KeepLiveResponse msg) throws Exception {
    String content = msg.getContent();
    if("PONG".equals(content)){
      log.debug("收到服务器回复的心跳包");
    }
  }
}
