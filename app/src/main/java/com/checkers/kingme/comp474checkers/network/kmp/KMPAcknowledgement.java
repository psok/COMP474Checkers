package com.checkers.kingme.comp474checkers.network.kmp;

/**
 * Created by Carlos 
 */
import java.io.*;
import java.util.Arrays;

// this class implements our KMP (King Me Protocol) ACK packet structure
public class KMPAcknowledgement extends BaseKMPPacket {

    public static final byte OPCODE = ACKop;

    public KMPAcknowledgement(int msgid)
    {
        super(OPCODE, msgid);
    }

    // constructor to build a packet incoming from the network
    public KMPAcknowledgement(byte[] buffer, int length) throws IOException
    {
        byte bufopcode;
        short bufmsgid;

        ByteArrayInputStream bais = new ByteArrayInputStream(Arrays.copyOf(buffer, length));
        DataInputStream dis = new DataInputStream(bais);
        bufopcode = dis.readByte();
        bufmsgid = dis.readShort();
        if (bufopcode != OPCODE) {
            throw new IOException("Wrong opcode!");
        }

        this.opcode = bufopcode;
        this.msgid = bufmsgid;
    }

    public byte[] write() throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size());
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeByte(this.opcode);
        dos.writeShort(this.msgid);
        return baos.toByteArray();
    }

}