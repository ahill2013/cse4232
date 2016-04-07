package client;

import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

public class EncoderDecoder {

    public static byte[] encodeProject(String command) {
        return new Encoder(command).getBytes();
    }

    public static String decodeProject(byte[] response) {
        return new Decoder(response).toString();
    }

}