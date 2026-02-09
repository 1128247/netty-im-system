package com.main.client.command;

import com.google.protobuf.ByteString;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.main.client.command.CommandClient.userListFuture;
import static com.main.client.command.CommandClient.userListOnLine;

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
  public void exec(Scanner scanner) throws ExecutionException, InterruptedException, TimeoutException {
    Set<String> onLineUserList = userListFuture.get(3, TimeUnit.SECONDS);
    userListOnLine = onLineUserList;
    log.info("当前在线的用户：{}", onLineUserList);
    while (true){
      log.info("请输入要发送的用户:");
      String userId = scanner.nextLine();
      if(!onLineUserList.contains(userId)){
        log.error("选择的用户不存在！");
        continue;
      }
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
