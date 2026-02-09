package com.main.client.handler;

import com.main.MsgProtos;
import com.main.client.command.CommandClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


@Slf4j
public class UserListResponseHandler extends SimpleChannelInboundHandler<MsgProtos.UserListResponse> {
  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MsgProtos.UserListResponse userListResponse) throws Exception {
    Set<String> userListOnLine = new CopyOnWriteArraySet<>(userListResponse.getUserIdsList());
    CommandClient.userListFuture.complete(userListOnLine);
  }
}
