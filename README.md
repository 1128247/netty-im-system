# IM-System (Netty Based Instant Messaging System)

这是一个基于 Java Netty 和 Google Protobuf 开发的高性能即时通讯系统。采用 Client-Server 架构，支持用户登录、私聊消息、用户列表获取等核心功能。

## 🛠️ 技术栈 (Tech Stack)

- **核心框架**: [Netty 4.2](https://netty.io/) - 高性能异步事件驱动的网络应用框架
- **序列化**: [Google Protobuf](https://protobuf.dev/) - 高效的二进制序列化协议
- **构建工具**: Gradle
- **JSON处理**: FastJSON2 / Gson
- **日志**: Logback

## ✨ 功能特性 (Features)

- **自定义协议**: 基于 Protobuf 定义的高效通信协议 (`message.proto`)
- **连接管理**: 支持 TCP 长连接，内置心跳保活机制
- **消息处理**:
  - 用户登录/鉴权
  - 点对点文本消息传输
  - 消息回执机制 (Ack)
  - 用户在线列表查询
- **客户端**: 包含一个基于命令行的交互式客户端 (`CommandClient`)

## 📂 项目结构 (Project Structure)

```
src/main/java/com/main
├── server/       # 服务端核心逻辑 (端口: 8080)
│   ├── handler/  # 业务逻辑处理器 (登录, 消息, 用户列表)
│   └── session/  # 服务端会话管理
├── client/       # 客户端核心逻辑
│   ├── command/  # 命令行交互指令 (Login, Chat)
│   ├── sender/   # 消息发送器
│   └── handler/  # 客户端响应处理
├── common/       # 公共模块 (编码解码器, 实体)
└── proto/        # Protobuf 协议定义文件
```

## 🚀 快速开始 (Getting Started)

### 1. 环境要求
- JDK 17+
- Gradle 8.x

### 2. 编译项目
使用 Gradle 构建项目并生成 Protobuf 代码：

```bash
./gradlew clean build
```

### 3. 运行服务端
找到 `src/main/java/com/main/server/IMServer.java` 并运行 `main` 方法。
服务端将启动在 `8080` 端口。

### 4. 运行客户端
找到 `src/main/java/com/main/client/command/CommandClient.java` 并运行 `main` 方法。
根据控制台提示输入指令进行操作。

## 📝 协议说明 (Protocol)

通信协议定义在 `src/main/proto/message.proto` 中，所有消息均通过 `Message` 外层包装，使用 `oneof` 字段区分具体的消息体类型（如 `LoginRequest`, `MessageRequest` 等）。

```protobuf
message Message {
  HeadType type = 1;      // 消息类型
  uint64 sequence = 2;    // 序列号
  oneof body {
    LoginRequest loginRequest = 4;
    MessageRequest messageRequest = 6;
    // ...
  }
}
```

## 📄 License

MIT License
