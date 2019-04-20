package echoserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

@Sharable
//Indicates that a ChannelHandler can be safely shared by multiple channels
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    ByteBuf in = (ByteBuf) msg;
    System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
    ctx.write(in);
//    Writes the received message to the sender without flushing the outbound messages
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
        .addListener(ChannelFutureListener.CLOSE);
//    Flushes pending messages to the remote peer and closes the channel
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
