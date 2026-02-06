package com.main.server;

import com.main.client.handler.MessageHandler;
import com.main.common.PacketDispatcher;
import com.main.common.ProtobufDecoder;
import com.main.common.ProtobufEncoder;
import com.main.server.handler.MessageRequestHandler;
import com.main.server.handler.UserListRequestHandler;
import com.main.server.handler.UserLoginRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class IMServer {

  public void runServer() throws InterruptedException {
    EventLoopGroup bossGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
    EventLoopGroup workGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG, 128)
        .childOption(ChannelOption.SO_KEEPALIVE, true)
        .childHandler(new ChannelInitializer<>() {
          @Override
          protected void initChannel(Channel ch) throws Exception {
            ch.pipeline()
                .addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder())
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                .addLast(new PacketDispatcher())
                .addLast(new UserLoginRequestHandler())
                .addLast(new UserListRequestHandler())
                .addLast(new MessageRequestHandler())
                .addLast(new MessageHandler());
          }
        });
    ChannelFuture channelFuture = bootstrap.bind(8080).sync();
    channelFuture.channel().closeFuture().sync();
  }

  public static void main(String[] args) throws InterruptedException {
    new IMServer().runServer();
  }
}
