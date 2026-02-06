package com.main.server.session;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServerSessionManager {
  private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();
  private static final Map<Channel, String> channelUserIdMap = new ConcurrentHashMap<>();

  public static void bindSession(String userId, Channel channel) {
    userIdChannelMap.put(userId, channel);
    channelUserIdMap.put(channel, userId);
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
