package com.main.server.handler;

import com.main.MsgProtos;
import com.main.server.session.ServerSessionManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageRequestHandler extends SimpleChannelInboundHandler<MsgProtos.MessageRequest> {
  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MsgProtos.MessageRequest messageRequest) throws Exception {
    String content = messageRequest.getContent();
    String msgId = messageRequest.getMsgId();
    String toId = messageRequest.getToId();
    log.info("[Server]--收到消息: {}", content);
    Channel channelByUserID = ServerSessionManager.getChannelByUserID(toId);
    if(channelByUserID != null){
      MsgProtos.Message forwardMsg = MsgProtos.Message.newBuilder()
          .setType(MsgProtos.HeadType.MESSAGE_REQUEST)
          .setMessageRequest(messageRequest)
          .build();
      channelByUserID.writeAndFlush(forwardMsg);
    }
    MsgProtos.Message response = MsgProtos.Message.newBuilder().setType(MsgProtos.HeadType.MESSAGE_RESPONSE)
        .setMessageResponse(MsgProtos.MessageResponse.newBuilder().setMsgId(msgId).setResult(true).setCode(200).build()).build();
    channelHandlerContext.writeAndFlush(response);

  }
}
