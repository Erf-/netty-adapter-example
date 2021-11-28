package fr.roody.example.netty.adapter;

import fr.roody.example.netty.adapter.encoder.DefaultBytesMessageEncoderDecoder;
import fr.roody.example.netty.adapter.handler.ObjectEchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.util.Arrays;

public class Main {

    static final boolean SSL = System.getProperty("ssl") != null;
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    public static void main(String[] args) throws Exception {

        final var encoderDecoder = new DefaultBytesMessageEncoderDecoder();
        var msg = "";

        if (args != null && args.length != 0) {
            msg = Arrays.stream(args).reduce("", (strA, strB) -> strA + " " + strB);
        }

        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            String finalMsg = msg;
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc(), HOST, PORT));
                            }
                            p.addLast(
                                    new ObjectEncoder(),
                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                    new ObjectEchoClientHandler(finalMsg, encoderDecoder::encode, encoderDecoder::decode));
                                    // ici 2 choix possibles
                                    // 1- utilisation des classes anonymes (lambda) grace a la definition des interfaces fonctionnelles Encoder et Decoder
                                    // 2- passage en parametre de la variable encoderDecoder qui implemente encoder et decoder
                                    // new ObjectEchoClientHandler(finalMsg, encoderDecoder, encoderDecoder));
                        }
                    });

            // Start the connection attempt.
            b.connect(HOST, PORT).sync().channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
