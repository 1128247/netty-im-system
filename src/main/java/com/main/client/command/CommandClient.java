package com.main.client.command;

import com.google.protobuf.ByteString;
import com.main.MsgProtos;
import com.main.client.handler.*;
import com.main.client.sender.ChatSender;
import com.main.client.sender.LoginSender;
import com.main.client.session.SessionManager;
import com.main.common.PacketDispatcher;
import com.main.common.ProtobufDecoder;
import com.main.common.ProtobufEncoder;
import com.main.common.entty.User;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.*;

@Slf4j
public class CommandClient {

  private final LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();
  private final ChatConsoleCommand chatConsoleCommand = new ChatConsoleCommand();
  private final LoginSender sender = new LoginSender();
  private final ChatSender chatSender = new ChatSender();
  public static final CountDownLatch LOGIN_LATCH = new CountDownLatch(1);
  public static CompletableFuture<Set<String>> userListFuture;
  public static Set<String> userListOnLine;
  public static volatile String failReason = "";         // 失败原因

  public void connect() throws InterruptedException, ExecutionException, TimeoutException {
    EventLoopGroup eventExecutors = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
    Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(eventExecutors)
        .channel(NioSocketChannel.class)
        .remoteAddress("localhost", 8080)
        .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder())
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                .addLast(new IdleStateHandler(0, 30,0))
                .addLast(new PacketDispatcher())
                .addLast(new LoginResponseHandler())
                .addLast(new MessageResponseHandler())
                .addLast(new UserListResponseHandler())
                .addLast(new MessageReceiveHandler())
                .addLast(new NotificationHandler())
                .addLast(new HeartBeatArtisanClientHandler());
          }
        });
    //获取连接
    ChannelFuture future = bootstrap.connect();
    //阻塞式等待连接
    future.sync();
    log.info("连接成功");
    SessionManager.getInstance().setConnect(true);
    SessionManager.getInstance().bindSession(future.channel());
    startCommandThread();
    future.channel().closeFuture().sync();

  }

  public void startCommandThread() throws InterruptedException, ExecutionException, TimeoutException {
    userListFuture = new CompletableFuture<>();
    Thread.currentThread().setName("主线程");
    log.info("发送登录请求");
    Scanner scanner = new Scanner(System.in);
    if (!SessionManager.INSTANCE.isLogin()){
      loginConsoleCommand.exec(scanner);
      startLogin(loginConsoleCommand);
      LOGIN_LATCH.await();
      if(SessionManager.INSTANCE.isLogin()){
        log.info("[Client]--开始发送消息");
//        while (true){
          chatConsoleCommand.exec(scanner);
//          if ("quit".equals(chatConsoleCommand.getContent())) {
//            break;
//          }
          startChat(chatConsoleCommand);
//        }

      }
    }
  }
  public void startLogin(LoginConsoleCommand loginConsoleCommand){
    User user = new User();
    user.setUid(loginConsoleCommand.getUsername());
    user.setToken(loginConsoleCommand.getPassword());
    user.setDevId("111");
    sender.setUser(user);
    sender.setSession(SessionManager.getInstance());
    sender.sendLoginMsg();
    MsgProtos.Message build = MsgProtos.Message.newBuilder().setType(MsgProtos.HeadType.USER_LIST_REQUEST).build();
    sender.sendMsg(build);
  }

  public void startChat(ChatConsoleCommand chatConsoleCommand){
    String content = chatConsoleCommand.getContent();
    String toId = chatConsoleCommand.getToId();
    chatSender.setSession(SessionManager.getInstance());
    chatSender.sendMessage(content, toId);
  }


  public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
    new CommandClient().connect();
  }
}
