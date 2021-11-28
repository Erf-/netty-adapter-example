package fr.roody.example.netty.adapter.encoder;

public interface BytesMessageEncoder {

    byte[] encode(String msg);
}
