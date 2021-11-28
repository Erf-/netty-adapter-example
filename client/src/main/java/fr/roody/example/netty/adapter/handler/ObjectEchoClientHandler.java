package fr.roody.example.netty.adapter.handler;

import fr.roody.example.netty.adapter.encoder.BytesMessageDecoder;
import fr.roody.example.netty.adapter.encoder.BytesMessageEncoder;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static io.netty.channel.ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE;

public final class ObjectEchoClientHandler extends ChannelInboundHandlerAdapter {

    private final String message;
    private final BytesMessageEncoder encoder;
    private final BytesMessageDecoder decoder;

    /**
     * Creates a client-side handler.
     */
    // Un constructeur ne sert qu'a faire de l'injection de dependance !
    // S'il y a des calculs supplementaires a faire pour instancier un objet alors :
    // contructeur prive + builder
    public ObjectEchoClientHandler(String message, BytesMessageEncoder encoder, BytesMessageDecoder decoder) {
        this.message = message;
        this.encoder = encoder;
        this.decoder = decoder;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // Send the first message if this handler is a client-side handler.
        ChannelFuture future = ctx.writeAndFlush(this.encoder.encode(message));
        future.addListener(FIRE_EXCEPTION_ON_FAILURE); // Let object serialisation exceptions propagate.
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // Print decoded message to the console output
        System.out.println(this.decoder.decode((byte[]) msg));
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
