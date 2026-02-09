package com.main.server.handler;

import com.main.MsgProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeartBeatArtisanServerHandler extends SimpleChannelInboundHandler<MsgProtos.KeepLiveRequest> {

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if(evt instanceof IdleStateEvent){
      IdleStateEvent event = (IdleStateEvent) evt;
      switch (event.state()){
        case READER_IDLE -> {
          log.debug("超过60未读到客户端数据包，关闭Channel连接");
          ctx.close();
        }
      }
    }else {
      super.userEventTriggered(ctx, evt);
    }
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, MsgProtos.KeepLiveRequest msg) throws Exception {
    String content = msg.getContent();
    String userID = msg.getId();
    if(content.equals("PING")){
      log.debug("收到来自{}的心跳包: {}", userID, content);
      MsgProtos.Message response = MsgProtos.Message.newBuilder().setType(MsgProtos.HeadType.KEEPALIVE_RESPONSE)
              .setKeepLiveResponse(MsgProtos.KeepLiveResponse.newBuilder().setContent("PONG").build()).build();
      log.debug("回复心跳包");
      ctx.channel().writeAndFlush(response);
    }
  }
}
