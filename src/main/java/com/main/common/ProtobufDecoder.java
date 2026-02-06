package com.main.common;

import com.main.MsgProtos;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ProtobufDecoder extends ByteToMessageDecoder {
  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    in.markReaderIndex();

    if(in.readableBytes() < 2){
      return;
    }
    int length = in.readShort();
    //不正常的连接
    if(length < 0){
      ctx.close();
    }
    if(length > in.readableBytes()){
      in.resetReaderIndex();
      return;
    }

    byte[] data;
    if(in.hasArray()){
      ByteBuf slice = in.slice();
      data = slice.array();
    }else {
      data = new byte[length];
      in.readBytes(data, 0, length);
    }

    MsgProtos.Message outMsg = MsgProtos.Message.parseFrom(data);
    //转换成功，加入出战容器
    if(outMsg !=null){
      out.add(outMsg);
    }
  }
}
