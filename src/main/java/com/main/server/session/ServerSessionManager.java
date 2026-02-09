package com.main.server.session;

import com.main.MsgProtos;
import io.netty.channel.Channel;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServerSessionManager {
  private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();
  private static final Map<Channel, String> channelUserIdMap = new ConcurrentHashMap<>();

  public static void bindSession(String userId, Channel channel) {
    userIdChannelMap.put(userId, channel);
    channelUserIdMap.put(channel, userId);

    channelUserIdMap.keySet().forEach(ch->{
      MsgProtos.Message message = MsgProtos.Message.newBuilder().setType(MsgProtos.HeadType.NOTIFICATION)
          .setNotification(
              MsgProtos.MessageNotification.newBuilder()
                  .setContent(userId )
                  .setNotifyType(1)
                  .setTimestamp(new Date().getTime())
                  .build()
          ).build();
      ch.writeAndFlush(message);
    });
  }

  public static Channel getChannelByUserID(String id){
    return userIdChannelMap.get(id);
  }

  public static void unbindSession(Channel channel) {
    String userID = channelUserIdMap.remove(channel);
    if (userID != null) {
      userIdChannelMap.remove(userID);
    }
  }

  public static Set<String>  getOnLiveUserID(){
    return userIdChannelMap.keySet();
  }
}
