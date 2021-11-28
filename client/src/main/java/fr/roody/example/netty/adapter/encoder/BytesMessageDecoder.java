package fr.roody.example.netty.adapter.encoder;

public interface BytesMessageDecoder {

    String decode(byte[] bytesMsg);
}
