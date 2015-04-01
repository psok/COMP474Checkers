

package com.checkers.kingme.comp474checkers.network.kmp;

import com.checkers.kingme.comp474checkers.Player;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by alshaymaaalhazzaa on 3/30/15.
 */

    public class KMPChallenge extends BaseKMPPacket {

        public static final byte OPCODE = CHGop;
        private Player player = null;
        private DatagramSocket sock;

        public KMPChallenge(int msgid, Player player, InetAddress localWiFiAddr) throws SocketException {
            super(OPCODE, msgid);
            this.player = player;
            this.sock = new DatagramSocket(DEFAULTPORT, localWiFiAddr);
        }

        // check player accept request or not from incoming from the network (such as KMPResponse)
        public boolean isPlayerAccepted(byte[] buffer) throws IOException {
            byte bufopcode;
            short bufmsgid;
            short bufaccept;

            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            DataInputStream dis = new DataInputStream(bais);
            bufopcode = dis.readByte();
            bufmsgid = dis.readShort();
            bufaccept = dis.readShort();
            if (bufopcode != RSPop) {
                throw new IOException("Wrong opcode!");
            }

            return (bufaccept == 1) ? true : false;
        }

        public byte[] write() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(size());
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeByte(this.opcode);
            dos.writeShort(this.msgid);

            byte[] playerNameInBytes = this.player.getName().getBytes();
            dos.write(playerNameInBytes);

            return baos.toByteArray();
        }

        public String getIpAddress(){
            DatagramPacket msgDG = new DatagramPacket(new byte[size()], size());
            String addr;

            while(true) { // read loop
                try {
                    msgDG.setLength(size());
                    sock.receive(msgDG);
                } catch (InterruptedIOException iioe) {
                    System.err.println("Response timed out!");
                    continue;
                } catch (IOException ioe) {
                    System.err.println("Bad receive!");
                    continue;
                }
                addr = msgDG.getAddress().getHostAddress();
                return addr;
            }
        }
    }


