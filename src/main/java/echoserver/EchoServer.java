package echoserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;

public class EchoServer {

  private final int port;

  public EchoServer(int port) {
    this.port = port;
  }

  public static void main(String[] args) throws Exception {
//    if (args.length != 1) {
//      System.err.println("Usage " + EchoServer.class.getSimpleName() + " <port>");
//      return;
//    }
//    int port = Integer.parseInt(args[0]);
    int port = 1234;
    new EchoServer(port).start();
  }

  public void start() throws Exception {
    final EchoServerHandler serverHandler = new EchoServerHandler();
    EventLoopGroup group = new NioEventLoopGroup();

    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(group)
          .channel(NioServerSocketChannel.class)
//      Specifies the use of an NIO transport Channel
          .localAddress(new InetSocketAddress(port))
//      Sets the socket address using the specified port
          .childHandler(new ChannelInitializer<SocketChannel>() {
            //            Adds an EchoServerHandler to the Channel’s ChannelPipeline
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
              socketChannel.pipeline().addLast(serverHandler);
            }
          });
      ChannelFuture f = b.bind().sync();
//      Binds the server asynchronously; sync() waits for the bind to complete.
      f.channel().closeFuture().sync();
//      Gets the CloseFuture of the Channel and blocks the current thread until it’s complete
    } finally {
      group.shutdownGracefully().sync();
//      Shuts down the EventLoopGroup, releasing all resources
    }

  }
}
