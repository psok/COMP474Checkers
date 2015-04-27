package com.checkers.kingme.comp474checkers.network;

import android.app.Activity;

import com.checkers.kingme.comp474checkers.network.kmp.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Carlos
 */

public class NetworkListener
{
    private Activity act;
    private DatagramSocket sock;
    private DatagramPacket receivedDG;
    private BaseKMPPacket pkt;
    private NetworkReturnCallback callback;

    private final NetworkListener myRef = this;
    private Thread listenerThread;

    // expected
    private InetAddress exp_sender;
    private int exp_msgid;
    private int exp_opcode;

    private class AsynchronousNetworkRunner implements Runnable
    {
        public void run ()
        {
            DatagramPacket udpDG = new DatagramPacket(new byte[BaseKMPPacket.MAXMSGSIZE], BaseKMPPacket.MAXMSGSIZE);
            InetAddress senderAddr;

            while(true) { // read loop
                byte[] buffer;

                if (Thread.interrupted()) {
                    return;
                }

                try {
                    udpDG.setLength(BaseKMPPacket.MAXMSGSIZE);
                    sock.receive(udpDG);

                    senderAddr = udpDG.getAddress();
                    buffer = udpDG.getData();

                    if(exp_sender != null && !exp_sender.equals(senderAddr)) {
                        throw new IOException();
                    }
                    if (BaseKMPPacket.opcode(buffer) != exp_opcode) {
                        throw new IOException();
                    }
                    switch(exp_opcode) {
                        case BaseKMPPacket.CHGop:
                            pkt = new KMPChallenge(buffer, udpDG.getLength());
                            break;
                        case BaseKMPPacket.RSPop:
                            pkt = new KMPResponse(buffer, udpDG.getLength());
                            break;
                        case BaseKMPPacket.MOVop:
                            pkt = new KMPMove(buffer, udpDG.getLength());
                            break;
                        case BaseKMPPacket.ACKop:
                            pkt = new KMPAcknowledgement(buffer, udpDG.getLength());
                            break;
                        default:
                            throw new IOException();
                    }
                    if (exp_msgid != 0 && pkt.msgid() != exp_msgid) {
                        throw new IOException();
                    }

                    break;
                } catch (IOException ioe) {
                    System.err.println("Bad receive!");
                    continue;
                }
            }

            receivedDG = udpDG;

            DatagramPacket ackDG;
            try {
                ackDG = new DatagramPacket(new byte[0], 0, senderAddr, udpDG.getPort());

                KMPAcknowledgement ackPKT = new KMPAcknowledgement(pkt.msgid());

                ackDG.setData(ackPKT.write());
                ackDG.setLength(ackPKT.size());

            } catch (IOException ioe) {
                System.err.println("Bad input!");
                return;
            }

            try {
                sock.send(ackDG);
            } catch (IOException ioe) {
                System.err.println("Couldn't send acknowledgement back!");
                return;
            }

            long sendtime = System.currentTimeMillis();

            act.runOnUiThread(new Runnable() {
                public void run() {
                    callback.callback(myRef);
                }
            });

            do { // dallying...
                byte[] buffer;

                try {
                    sock.receive(udpDG);

                    buffer = udpDG.getData();

                    if (BaseKMPPacket.opcode(buffer) != exp_opcode) {
                        throw new IOException();
                    }

                    if (udpDG.getAddress().equals(senderAddr)) {
                        try {
                            sock.send(ackDG);
                        } catch (IOException ioe) {
                            System.err.println("Couldn't resend ack!");
                            return;
                        }

                        break;
                    }
                } catch (IOException ioe) {
                    System.err.println("Bad receive!");
                    continue;
                }
            } while (System.currentTimeMillis() - sendtime < (BaseKMPPacket.INITTIMEOUT));
        }
    }

    public interface NetworkReturnCallback { // interface between view and system
        void callback(NetworkListener origin);
    }

    public NetworkListener(Activity act, DatagramSocket sock, NetworkReturnCallback callback, int opcode)
    {
        this(act, sock, callback, opcode, null, 0);
    }

    public NetworkListener(Activity act, DatagramSocket sock, NetworkReturnCallback callback, int opcode, InetAddress sender, int msgid)
    {
        this.act = act;
        this.sock = sock;
        this.callback = callback;
        this.exp_opcode = opcode;
        this.exp_sender = sender;
        this.exp_msgid = msgid;
    }

    public void listen()
    {
        AsynchronousNetworkRunner anr = new AsynchronousNetworkRunner();
        listenerThread = new Thread(anr);
        listenerThread.start();
    }

    public void giveUp()
    {
        listenerThread.interrupt();
    }

    public DatagramPacket getReceivedDG()
    {
        return this.receivedDG;
    }

    public BaseKMPPacket getKMPPacket()
    {
        return this.pkt;
    }
}
