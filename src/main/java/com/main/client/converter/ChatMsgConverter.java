package com.main.client.converter;

import com.main.MsgProtos;
import com.main.client.session.SessionManager;
import com.main.common.entty.User;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class ChatMsgConverter extends BaseConverter{

  public ChatMsgConverter(SessionManager session) {
    super(MsgProtos.HeadType.MESSAGE_REQUEST, session);
  }

  public MsgProtos.Message _build(String content, String toId, String fromId){
    MsgProtos.Message.Builder outerBuilder = getOuterBuilder(-1);
    MsgProtos.MessageRequest.Builder ld = MsgProtos.MessageRequest.newBuilder()
        .setContent(content)
        .setMsgId(UUID.randomUUID().toString())
        .setTimestamp(System.currentTimeMillis())
        .setMsgType(1)
        .setFromId(fromId)
        .setToId(toId);
    return outerBuilder.setMessageRequest(ld).build();
  }

  public static MsgProtos.Message build(String content, String toId, SessionManager sessionManager) {
    ChatMsgConverter converter =  new ChatMsgConverter(sessionManager);
    return converter._build(content, toId, sessionManager.getUserId());

  }
}


