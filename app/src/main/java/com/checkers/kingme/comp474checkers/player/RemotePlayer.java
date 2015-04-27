package com.checkers.kingme.comp474checkers.player;

import android.app.Activity;

import com.checkers.kingme.comp474checkers.backend.Color;
import com.checkers.kingme.comp474checkers.model.GameState;
import com.checkers.kingme.comp474checkers.network.NetworkListener;
import com.checkers.kingme.comp474checkers.network.NetworkMessenger;
import com.checkers.kingme.comp474checkers.network.kmp.BaseKMPPacket;
import com.checkers.kingme.comp474checkers.network.kmp.KMPChallenge;
import com.checkers.kingme.comp474checkers.network.kmp.KMPMove;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Richa on 2/21/2015.
 */
public class RemotePlayer extends Player
{
    DatagramSocket rcvsock;
    DatagramSocket sndsock;
    InetAddress ip;
    int id;

    public RemotePlayer(Color color, String name, DatagramSocket rcvsock, DatagramSocket sndsock, InetAddress ip, int initialId)
    {
        super(color, name);
        this.ip = ip;
        this.id = initialId;
        this.rcvsock = rcvsock;
        this.sndsock = sndsock;
    }

    public void onTap(int squareId)
    {
        // DO NOTHING!!!
    }

    public void wake(Activity system)
    {
        new NetworkListener(system, rcvsock, new NetworkListener.NetworkReturnCallback() {
            public void callback(NetworkListener origin) {
                KMPMove kmpm = (KMPMove)origin.getKMPPacket();
                stateHandler.onGetInput(kmpm.from());
                stateHandler.onGetInput(kmpm.to());
            }
        }, BaseKMPPacket.MOVop, ip, id++).listen();
    }

    public void notify(int from, char type, int to)
    {
        new NetworkMessenger(null, sndsock, null, new KMPMove(id++, from, type, to), ip).dispatch();
    }
}
