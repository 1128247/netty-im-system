package com.main.common.entty;

import com.main.MsgProtos;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Slf4j
public class User {
  private static final AtomicInteger NO = new AtomicInteger(1);
  private String uid = String.valueOf(NO.getAndIncrement());
  private String devId = UUID.randomUUID().toString();
  private String token = UUID.randomUUID().toString();
  private String nickName = "nickName";
  private String sessionId;

  // 使用 Protobuf 生成的枚举作为成员变量类型，避免二次定义
  private MsgProtos.Platform platform = MsgProtos.Platform.WINDOWS;

  /**
   * 修复方法：直接接受 Protobuf 的枚举类型
   */
  public void setPlatform(MsgProtos.Platform platform) {
    this.platform = platform;
  }

  /**
   * 如果你确实需要通过 int 设置（比如从数据库读取）
   */
  public void setPlatform(int platformValue) {
    // forNumber 是 Protobuf 自动生成的静态方法，比循环遍历更高效
    MsgProtos.Platform p = MsgProtos.Platform.forNumber(platformValue);
    this.platform = (p != null) ? p : MsgProtos.Platform.UNKNOWN;
  }

  public static User fromMsg(MsgProtos.LoginRequest info) {
    User user = new User();
    // 修正点 1：info.getUid() 已经是 String，不需要 new String()
    user.uid = info.getUid();
    user.devId = info.getDeviceId();
    user.token = info.getToken();

    // 修正点 2：传入具体的枚举对象
    user.setPlatform(info.getPlatform());

    log.info("登录中: {}", user.toString());
    return user;
  }
}