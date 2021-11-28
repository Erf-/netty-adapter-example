package adapter.encoder;

public interface BytesMessageEncoder {

    byte[] encode(String msg);
}
