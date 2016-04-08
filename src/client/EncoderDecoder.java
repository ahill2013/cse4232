package client;

import asn1objects.ASN1GetProjects;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

public class EncoderDecoder {

    public static byte[] encodeProject(String command) {
        return new Encoder(command).getBytes();
    }

    public static String decodeProject(byte[] response) {
        return new Decoder(response).toString();
    }

    public static byte[] encodeProjects(String command) {
        ASN1GetProjects getProjects = new ASN1GetProjects();
        Encoder enc = getProjects.getEncoder();
        return enc.getBytes();
    }

    public static String decodeProjects(byte[] response) {

    }
}