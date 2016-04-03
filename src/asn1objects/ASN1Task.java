package asn1objects;



import datatypes.Task;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.ASNObj;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by armin1215 on 3/31/16.
 */
public class ASN1Task extends ASNObj {

    private Task task;
    private SimpleDateFormat _sdf;

    public ASN1Task() {
        this(" ", "1970-01-01:01h01m01s001Z", "1980-01-01:01h01m01s001Z", "localhost", 0, false);
    }


    public ASN1Task(String name, String start, String end, String ip, int port, boolean status) {
        try {
            _sdf = new SimpleDateFormat("yyyy-MM-dd:hh'h'mm'm'ss's'SSS'Z'");
            task = new Task(name, _sdf.parse(start), _sdf.parse(end), ip, port, status);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public ASN1Task(Task t) {
        task = t;
    }


    @Override
    public String toString() {
        return task.toString();
    }


    @Override
    public Encoder getEncoder() {
        final String startTime = Encoder.getGeneralizedTime(task.getStartTime().getTime());
        final String endTime = Encoder.getGeneralizedTime(task.getEndTime().getTime());
        Encoder enc = new Encoder().initSequence();
        enc.addToSequence(new Encoder(task.getName()).setASN1Type(Encoder.TAG_UTF8String));
        enc.addToSequence(new Encoder(Long.toString(task.getStartTime().getTime())).setASN1Type(Encoder.TAG_GeneralizedTime));
        enc.addToSequence(new Encoder(Long.toString(task.getEndTime().getTime())).setASN1Type(Encoder.TAG_GeneralizedTime));
        enc.addToSequence(new Encoder(task.getIP()).setASN1Type(Encoder.TAG_UTF8String));
        enc.addToSequence(new Encoder(task.getPort()).setASN1Type(Encoder.TAG_INTEGER));
        enc.addToSequence(new Encoder(task.getStatus()).setASN1Type(Encoder.TAG_BOOLEAN));
        return enc.setASN1Type(Encoder.CLASS_APPLICATION,Encoder.PC_CONSTRUCTED,Encoder.TAG_SEQUENCE);

    }

    @Override
    public Task decode(Decoder decoder) throws ASN1DecoderFail {
        final Decoder dec = decoder.getContent();

        String name = dec.getFirstObject(true).getString(Encoder.TAG_UTF8String);
        Date start = new Date(Long.parseLong(dec.getFirstObject(true).getGeneralizedTime(Encoder.TAG_GeneralizedTime)));
        Date end = new Date(Long.parseLong(dec.getFirstObject(true).getGeneralizedTime(Encoder.TAG_GeneralizedTime)));
        String ip = dec.getFirstObject(true).getString(Encoder.TAG_UTF8String);
        int port = dec.getFirstObject(true).getInteger(Encoder.TAG_INTEGER).intValue();
        boolean status = dec.getFirstObject(true).getBoolean(Encoder.TAG_BOOLEAN);

        return new Task(name, start, end, ip, port, status);
    }
}
