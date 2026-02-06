package com.main.common;

import com.main.MsgProtos;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtobufEncoder extends MessageToByteEncoder<MsgProtos.Message> {

  @Override
  protected void encode(ChannelHandlerContext ctx, MsgProtos.Message msg, ByteBuf out) throws Exception {
    //转成字节数组
    byte[] bytes = msg.toByteArray();
    //记录数据长度
    int len = bytes.length;
    //写入数据长度
    out.writeShort(len);
    //写入数据
    out.writeBytes(msg.toByteArray());
  }
}
