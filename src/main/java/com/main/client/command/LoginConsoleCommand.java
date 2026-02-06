package com.main.client.command;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
@Data
@Slf4j
public class LoginConsoleCommand implements BaseCommand{

  private static String KEY = "1";
  private String username;
  private String password;

  @Override
  public String getKey() {
    return KEY;
  }

  @Override
  public String getTip() {
    return "Login";
  }

  @Override
  public void exec(Scanner scanner) {
    log.info("请输入用户信息(id:password) ");
    String[] info = null;
    while (true){
      String input = scanner.nextLine();
      info = input.split(":");
      if(info.length != 2){
        System.out.println("请按照格式输入 (id:password):");
      }else {
        break;
      }
    }
    username = info[0];
    password = info[1];
  }
}
