package com.main.common;

import com.main.MsgProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PacketDispatcher extends MessageToMessageDecoder<MsgProtos.Message> {

  @Override
  protected void decode(ChannelHandlerContext ctx, MsgProtos.Message msg, List<Object> out) throws Exception {
    MsgProtos.HeadType type = msg.getType();
    log.debug("请求类型: {}", type);
    switch (type){
      case NOTIFICATION -> out.add(msg.getNotification());
      case LOGIN_REQUEST -> out.add(msg.getLoginRequest());
      case LOGIN_RESPONSE -> out.add(msg.getLoginResponse());
      case MESSAGE_REQUEST -> out.add(msg.getMessageRequest());
      case MESSAGE_RESPONSE -> out.add(msg.getMessageResponse());
      case USER_LIST_REQUEST -> out.add(msg.getUserListRequest());
      case USER_LIST_RESPONSE -> out.add(msg.getUserListResponse());
      case KEEPALIVE_REQUEST -> out.add(msg.getKeepLiveRequest());
      case KEEPALIVE_RESPONSE -> out.add(msg.getKeepLiveResponse());
    }
  }
}
