package com.main.client.command;

import com.main.MsgProtos;
import com.main.client.converter.BaseConverter;
import com.main.client.session.SessionManager;
import com.main.server.session.ServerSessionManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

@Data
@Slf4j
public class ChatConsoleCommand implements BaseCommand{

  private String content;
  private String toId;
  @Override
  public String getKey() {
    return UUID.randomUUID().toString();
  }

  @Override
  public String getTip() {
    return "chat";
  }

  @Override
  public void exec(Scanner scanner) {
    Set<String> onLiveUserID = ServerSessionManager.getOnLiveUserID();
    log.info("当前在线的用户：{}", onLiveUserID);
    while (true){
      log.info("请输入要发送的用户:");
      String userId = scanner.nextLine();
//      if(!onLiveUserID.contains(userId)){
//        log.error("选择的用户不存在！");
//        continue;
//      }
      this.toId = userId;
      break;
    }
    while (true){
      log.info("请输入要发送的信息:");
      String input = scanner.nextLine();
      if(!input.isEmpty()){
          this.content = input;
          break;
      }
    }
  }
}
