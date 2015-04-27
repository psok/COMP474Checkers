package com.checkers.kingme.comp474checkers.network.kmp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Richa on 4/7/2015.
 */
public class KMPResponse extends BaseKMPPacket {

    public static final byte OPCODE = RSPop;
    private byte[] playerName;
    private byte response;

    public KMPResponse(int msgid, String playerName, boolean response) {
        super(OPCODE, msgid);
        if(playerName.length() > 20) {
            playerName = playerName.substring(0, 20);
        }
        this.playerName = playerName.getBytes();
        // 1 = accept, 0 = reject
        if(response) this.response = 1;
        else this.response = 0;
    }

    // constructor to build a packet incoming from the network
    public KMPResponse(byte[] buffer, int length) throws IOException {
        byte bufopcode;
        short bufmsgid;
        byte[] bufPNameRsp = new byte[21];
        byte bufResponse;
        int nobytes;

        ByteArrayInputStream bais = new ByteArrayInputStream(Arrays.copyOf(buffer, length));
        DataInputStream dis = new DataInputStream(bais);
        bufopcode = dis.readByte();
        bufmsgid = dis.readShort();

        if (bufopcode != OPCODE) {
            throw new IOException("Wrong opcode!");
        }
        nobytes = dis.read(bufPNameRsp);
        if (nobytes == 0) {
            throw new IOException("No response!");
        } else if (nobytes == 1) {
            throw new IOException("Player name empty!");
        }
        bufResponse = bufPNameRsp[nobytes - 1];

        this.opcode = bufopcode;
        this.msgid = bufmsgid;
        this.playerName = Arrays.copyOf(bufPNameRsp, nobytes - 1);
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
        dos.writeByte(this.response);
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
    public boolean response(){return this.response == 1;}

}
