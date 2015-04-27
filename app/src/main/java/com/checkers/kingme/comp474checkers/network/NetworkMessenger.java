package com.checkers.kingme.comp474checkers.network;

import android.app.Activity;

import com.checkers.kingme.comp474checkers.network.kmp.BaseKMPPacket;
import com.checkers.kingme.comp474checkers.network.kmp.KMPAcknowledgement;
import com.checkers.kingme.comp474checkers.network.kmp.KMPChallenge;
import com.checkers.kingme.comp474checkers.network.kmp.KMPMove;
import com.checkers.kingme.comp474checkers.network.kmp.KMPResponse;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by cfilipe on 26/04/15.
 */
public class NetworkMessenger
{
    private BaseKMPPacket pkt;
    private InetAddress destination;
    private Activity act;
    private DatagramSocket sock;
    private NetworkSendCallback callback;

    //private final NetworkListener myRef = this;
    private Thread messengerThread;

    private class AsynchronousNetworkRunner implements Runnable
    {
        public void run ()
        {
            DatagramPacket msgDG;
            try {
                msgDG = new DatagramPacket(new byte[0], 0, destination, BaseKMPPacket.DEFAULTPORT);
                msgDG.setData(pkt.write());
                msgDG.setLength(pkt.size());

            }
            catch (UnknownHostException uhe) {
                System.err.println("Couldn't figure out destination host!");
                return;
            }
            catch (IOException ioe) {
                System.err.println("Bad input!");
                return;
            }

            try {
                sock.send(msgDG);
            } catch (IOException ioe) {
                System.err.println("Couldn't send message out!");
                return;
            }

            long startTime = System.currentTimeMillis();


            DatagramPacket ackDG = new DatagramPacket(new byte[BaseKMPPacket.MAXMSGSIZE], BaseKMPPacket.MAXMSGSIZE);


            while(true) { // read loop
                if (Thread.interrupted()) {
                    return;
                }


                byte[] buffer;
                KMPAcknowledgement ackPKT;

                if (System.currentTimeMillis() - startTime > BaseKMPPacket.INITTIMEOUT) {
                    try {
                        sock.send(msgDG);
                    } catch (IOException ioe) {
                        System.err.println("Couldn't resend message out!");
                        continue;
                    }
                    startTime = System.currentTimeMillis();
                }

                try {
                    ackDG.setLength(BaseKMPPacket.MAXMSGSIZE);
                    sock.receive(ackDG);

                    buffer = ackDG.getData();

                    if(!ackDG.getAddress().equals(destination)
                            || BaseKMPPacket.opcode(buffer) != BaseKMPPacket.ACKop) {
                        throw new IOException();
                    }

                    ackPKT = new KMPAcknowledgement(buffer, ackDG.getLength());
                    if(ackPKT.msgid() != pkt.msgid()) {
                        throw new IOException();
                    }

                    break;
                } catch (IOException ioe) {
                    System.err.println("Bad receive!");
                    continue;
                }
            }

            if (act != null) {
                act.runOnUiThread(new Runnable() {
                    public void run() {
                        callback.callback();
                    }
                });
            }
        }
    }

    public interface NetworkSendCallback { // interface between view and system
        void callback();
    }

    public NetworkMessenger(Activity act, DatagramSocket sock, NetworkSendCallback callback, BaseKMPPacket payloadPacket, InetAddress destination)
    {
        this.act = act;
        this.sock = sock;
        this.callback = callback;
        this.pkt = payloadPacket;
        this.destination = destination;
    }

    public void dispatch()
    {
        AsynchronousNetworkRunner anr = new AsynchronousNetworkRunner();
        messengerThread = new Thread(anr);
        messengerThread.start();
    }

    public void giveUp()
    {
        messengerThread.interrupt();
    }
}