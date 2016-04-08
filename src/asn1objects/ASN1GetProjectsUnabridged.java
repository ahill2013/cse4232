package asn1objects;

import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.ASNObj;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

/**
 * Empty request for all projects in database with their tasks. Basically
 * outputs entire database.
 *
 * The response to this is a ProjectsAnswer object
 */
public class ASN1GetProjectsUnabridged extends ASNObj {
    //Unique tag value for object (for project's scope)
    public static final int TAGVALUE = 9;

    /**
     * Encodes a request for database contents
     * @return encoded contents
     */
    @Override
    public Encoder getEncoder() {
        Encoder enc = new Encoder();
        return enc.setASN1Type(Encoder.CLASS_PRIVATE, Encoder.PC_PRIMITIVE, (byte) (Encoder.TAG_SEQUENCE + TAGVALUE));
    }

    /**
     * NEVER CALL THIS METHOD EVER!
     * Decodes request for contents of database (not really necessary)
     *
     * @param dec already created decoder
     * @return failure integer for request because this should never be called
     * @throws ASN1DecoderFail required for all inherited objects
     */
    @Override
    public Integer decode(Decoder dec) throws ASN1DecoderFail {
        return -1;
    }
}