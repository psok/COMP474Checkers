package com.checkers.kingme.comp474checkers.network.kmp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Richa on 4/7/2015.
 */
public class KMPResponse extends BaseKMPPacket {

    public static final byte OPCODE = CHGop;
    private byte[] playerName;
    private byte response;

    public KMPResponse(int msgid, String playerName, int response) {
        super(OPCODE, msgid);
        this.playerName = playerName.substring(0, 20).getBytes();
        // 1 - accept, 0- reject
        this.response = (byte) response;
    }

    // constructor to build a packet incoming from the network
    public KMPResponse(byte[] buffer) throws IOException {
        byte bufopcode;
        short bufmsgid;
        byte[] bufPName = new byte[20];
        byte bufResponse;

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
        bufResponse = dis.readByte();
        this.opcode = bufopcode;
        this.msgid = bufmsgid;
        this.playerName = bufPName;
        this.response = bufResponse;
    }

    /**
     *
     * @return byte[]
     * @throws IOException
     */
    public byte[] write() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size());
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeByte(this.opcode);
        dos.writeShort(this.msgid);
        dos.write(this.playerName);
        dos.write(this.response);
        return baos.toByteArray();
    }

    /**
     *
     * @return size
     */
    public int size() {
        return super.size() + this.playerName.length + 1;
    }

    // to get the player name as a string
    public String playerName() {
        return new String(playerName);
    }

    /**
     *
     * @return response
     */
    public int getResponse(){return response;}

}
