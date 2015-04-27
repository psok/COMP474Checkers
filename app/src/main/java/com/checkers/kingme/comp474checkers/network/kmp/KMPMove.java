package com.checkers.kingme.comp474checkers.network.kmp;

import java.io.*;
import java.util.Arrays;

/**
 * Created by Carlos
 */
public class KMPMove extends BaseKMPPacket {

    public static final byte OPCODE = MOVop;

    private byte[] move;

    public KMPMove(int msgid, int from, char type, int to)
    {
        super(OPCODE, msgid);

        this.move = new byte[3];

        this.move[0] = (byte)from;
        this.move[1] = (byte)type;
        this.move[2] = (byte)to;
    }

    // constructor to build a packet incoming from the network
    public KMPMove(byte[] buffer, int length) throws IOException
    {
        byte bufopcode;
        byte buffrom;
        byte buftype;
        byte bufto;
        short bufmsgid;

        ByteArrayInputStream bais = new ByteArrayInputStream(Arrays.copyOf(buffer, length));
        DataInputStream dis = new DataInputStream(bais);
        bufopcode = dis.readByte();
        bufmsgid = dis.readShort();
        buffrom = dis.readByte();
        buftype = dis.readByte();
        bufto = dis.readByte();
        if (bufopcode != OPCODE) {
            throw new IOException("Wrong opcode!");
        }

        this.opcode = bufopcode;
        this.msgid = bufmsgid;

        this.move = new byte[3];

        this.move[0] = buffrom;
        this.move[1] = buftype;
        this.move[2] = bufto;
    }

    public byte[] write() throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size());
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeByte(this.opcode);
        dos.writeShort(this.msgid);
        dos.write(this.move, 0, 3);
        return baos.toByteArray();
    }

    public int size()
    {
        return super.size() + 3; // plus three bytes for the move info
    }

    public int from()
    {
        return (int) move[0];
    }

    public char type()
    {
        return (char) move[1];
    }

    public int to()
    {
        return (int) move[2];
    }
}
