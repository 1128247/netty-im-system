package com.main.server.handler;

import com.main.MsgProtos;
import com.main.server.session.ServerSessionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class UserListRequestHandler extends SimpleChannelInboundHandler<MsgProtos.UserListRequest> {
  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MsgProtos.UserListRequest userListRequest) throws Exception {
    Set<String> onLiveUserID = ServerSessionManager.getOnLiveUserID();

    MsgProtos.UserListResponse response = MsgProtos.UserListResponse.newBuilder().addAllUserIds(onLiveUserID).setTotalCount(onLiveUserID.size()).build();
    MsgProtos.Message message = MsgProtos.Message.newBuilder()
        .setType(MsgProtos.HeadType.USER_LIST_RESPONSE)
        .setUserListResponse(response).build();
    channelHandlerContext.writeAndFlush(message);

  }
}
