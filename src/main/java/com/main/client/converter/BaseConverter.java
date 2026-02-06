package com.main.client.converter;

import com.main.MsgProtos;
import com.main.client.session.SessionManager;

public class BaseConverter {

  protected MsgProtos.HeadType type;
  private long seqId;
  private SessionManager session;

  public BaseConverter(MsgProtos.HeadType headType, SessionManager session){
    this.type = headType;
    this.session = session;
  }

  public MsgProtos.Message buildOuter(long seqId){
    return this.getOuterBuilder(seqId).buildPartial();
  }

  public MsgProtos.Message.Builder getOuterBuilder(long seqId){
    this.seqId = seqId;
    MsgProtos.Message.Builder mb = MsgProtos.Message.newBuilder().setType(this.type).setSessionId(session.getUserId()).setSequence(seqId);
    return mb;
  }
  public MsgProtos.Message.Builder baseBuilder(long seqId){
    this.seqId = seqId;
    MsgProtos.Message.Builder mb =
        MsgProtos.Message
            .newBuilder()
            .setType(type)
            .setSessionId(session.getUserId())
            .setSequence(seqId);
    return mb;
  }
}
