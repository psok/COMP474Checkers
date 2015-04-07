package com.checkers.kingme.comp474checkers.network.kmp;

import java.io.*;

/**
 * Created by alshaymaaalhazzaa on 3/30/15.
 */

public class KMPChallenge extends BaseKMPPacket
{
    public static final byte OPCODE = CHGop;
    private byte[] playerName;

    public KMPChallenge(int msgid, String playerName)
    {
        super(OPCODE, msgid);
        this.playerName = playerName.substring(0, 20).getBytes();
    }

    // constructor to build a packet incoming from the network
    public KMPChallenge(byte[] buffer) throws IOException
    {
        byte bufopcode;
        short bufmsgid;
        byte[] bufPName = new byte[20];

        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        DataInputStream dis = new DataInputStream(bais);
        bufopcode = dis.readByte();
        bufmsgid = dis.readShort();
        if (bufopcode != OPCODE) {
            throw new IOException("Wrong opcode!");
        }
        if (dis.read(bufPName) == 0) {
            throw new IOException("Player name empty!");
        }

        this.opcode = bufopcode;
        this.msgid = bufmsgid;
        this.playerName = bufPName;
    }

    public byte[] write() throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size());
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeByte(this.opcode);
        dos.writeShort(this.msgid);
        dos.write(this.playerName);

        return baos.toByteArray();
    }

    public int size()
    {
        return super.size() + this.playerName.length;
    }

    public String playerName()
    {
        return new String(playerName);
    }

}


