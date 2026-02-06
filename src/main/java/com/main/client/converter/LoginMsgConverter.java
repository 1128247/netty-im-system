package com.main.client.converter;

import com.main.MsgProtos;
import com.main.client.session.SessionManager;
import com.main.common.entty.User;

public class LoginMsgConverter extends BaseConverter {
  private final User user;

  public LoginMsgConverter(User user, SessionManager session) {
    super(MsgProtos.HeadType.LOGIN_REQUEST, session);
    this.user = user;
  }

  public MsgProtos.Message build() {

    MsgProtos.Message.Builder outerBuilder = getOuterBuilder(-1);


    MsgProtos.LoginRequest.Builder lb =
        MsgProtos.LoginRequest.newBuilder()
            .setDeviceId(user.getDevId())
            .setPlatform(user.getPlatform())
            .setToken(user.getToken())
            .setUid(user.getUid());

    MsgProtos.Message requestMsg = outerBuilder.setLoginRequest(lb).build();

    return requestMsg;
  }


  public static MsgProtos.Message build(User user, SessionManager session) {
    user.setSessionId(session.getUserId());
    session.setUserId(user.getUid());
    LoginMsgConverter converter =  new LoginMsgConverter(user, session);
    return converter.build();

  }


}
