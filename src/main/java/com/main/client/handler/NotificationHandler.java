package com.main.client.handler;

import com.main.MsgProtos;
import com.main.client.command.CommandClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotificationHandler extends SimpleChannelInboundHandler<MsgProtos.MessageNotification> {
  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MsgProtos.MessageNotification messageNotification) throws Exception {
    //首次登录会手动拉取一次
    if(CommandClient.userListOnLine == null){
      return;
    }
    String content = messageNotification.getContent();
    int notifyType = messageNotification.getNotifyType();

    if(notifyType == 1){
      log.info("{}已上线", content);
      CommandClient.userListOnLine.add(content);
    }else {
      CommandClient.userListOnLine.remove(content);
      log.info("{}已下线", content);
    }

  }
}
