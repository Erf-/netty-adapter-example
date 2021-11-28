package adapter.handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handles both client-side and server-side handler depending on which
 * constructor was called.
 */
public final class NettyObjectEchoServerHandlerAdapter extends ChannelInboundHandlerAdapter {

    // TODO cas a traiter dans l'ordre :
    // 0- handler qui decode et ecrit le message dans la console
    // 1- envoi du message gere ici
    // 2- envoi du message delegue au handler
    // 3- handlers multiples

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // Echo back the received object to the client.
        // ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
