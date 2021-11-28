package adapter.encoder;

import java.nio.charset.StandardCharsets;

public record DefaultBytesMessageEncoderDecoder() implements BytesMessageEncoder, BytesMessageDecoder {

    @Override
    public byte[] encode(String msg) {
        return msg.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String decode(byte[] bytesMsg) {
        return new String(bytesMsg, StandardCharsets.UTF_8);
    }
}
