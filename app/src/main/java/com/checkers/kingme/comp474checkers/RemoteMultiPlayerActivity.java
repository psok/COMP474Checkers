package com.checkers.kingme.comp474checkers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.checkers.kingme.comp474checkers.backend.Color;
import com.checkers.kingme.comp474checkers.frontend.CheckersSystem;
import com.checkers.kingme.comp474checkers.network.NetworkListener;
import com.checkers.kingme.comp474checkers.network.NetworkMessenger;
import com.checkers.kingme.comp474checkers.network.kmp.BaseKMPPacket;
import com.checkers.kingme.comp474checkers.network.kmp.KMPAcknowledgement;
import com.checkers.kingme.comp474checkers.network.kmp.KMPChallenge;
import com.checkers.kingme.comp474checkers.network.kmp.KMPResponse;
import com.checkers.kingme.comp474checkers.player.LocalPlayer;
import com.checkers.kingme.comp474checkers.player.RemotePlayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;


public class RemoteMultiPlayerActivity extends ActionBarActivity
{

    private EditText dest;
    private DatagramSocket sndsock;
    private DatagramSocket rcvsock;
    private InetAddress destaddr;

    NetworkListener nl;
    NetworkMessenger nm;

    //InetAddress localWiFiAddr;
    public final static String EXTRA_PLAYER1 = "playerName";
    private AlertDialog chgDialogue;

    private String myName;
    private EditText editTextFirstPlayer;// = (EditText) findViewById(R.id.edittext_firstPlayerName);

    private boolean response;
    private int chgid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_remote_multi_player);

        editTextFirstPlayer = (EditText) findViewById(R.id.edittext_firstPlayerName);
        dest = (EditText)findViewById(R.id.editText_secondPlayerAddress);
        TextView muhip = (TextView) findViewById(R.id.textView_firstPlayerAddress);
        WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();

        InetAddress localWiFiAddr;
        try {
            localWiFiAddr = intToInetAddr(dhcp.ipAddress);
        } catch (UnknownHostException e) {
            System.err.println("Couldn't figure out my own IP address!");
            return;
        }

        muhip.setText(localWiFiAddr.toString());

        try {
            sndsock = new DatagramSocket(BaseKMPPacket.EXTRAPORT, localWiFiAddr);
            rcvsock = new DatagramSocket(BaseKMPPacket.DEFAULTPORT, localWiFiAddr);
        }
        catch (SocketException se) {
            System.err.println("No socket available!");
            return;
        }
        try {
            sndsock.setSoTimeout(BaseKMPPacket.HARDTIMEOUT);
            rcvsock.setSoTimeout(BaseKMPPacket.HARDTIMEOUT);        // time in milliseconds
        } catch (SocketException se) {
            System.err.println("Socket exception: timeout not set!");
            return;
        }

        acceptChallenges();
    }

    private void acceptChallenges()
    {
        nl = new NetworkListener(this, rcvsock,new NetworkListener.NetworkReturnCallback() {
            public void callback(NetworkListener origin) {
                receiveChallengeDialogue((KMPChallenge)origin.getKMPPacket(), origin.getReceivedDG().getAddress());
            }
        }, BaseKMPPacket.CHGop);
        nl.listen();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_remote_multi_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendChallenge(View view) {

        try {
            destaddr = InetAddress.getByName(dest.getText().toString());
        } catch (UnknownHostException uhe) {
            System.err.println("Couldn't figure out destination host!");
        }

        myName = editTextFirstPlayer.getText().toString();
        chgid = (int)(Math.random()*4913);

        nl.giveUp();

        nm = new NetworkMessenger(this, sndsock, new NetworkMessenger.NetworkSendCallback() {
            public void callback() {
                chgDialogue.setTitle("Awaiting response...");
                awaitResponse();
            }
        }, new KMPChallenge(chgid, myName), destaddr);

        nm.dispatch();

        sendChallengeDialogue();
    }

    private InetAddress intToInetAddr(int ip) throws UnknownHostException
    {
        byte[] octets = { (byte)(0xff & ip),
                (byte)(0xff & (ip >> 8)),
                (byte)(0xff & (ip >> 16)),
                (byte)(0xff & (ip >> 24)) };

        return InetAddress.getByAddress(octets);
    }

    public ActionBarActivity getActivity(){
        return this;
    }

    public void sendChallengeDialogue(){
        chgDialogue = new AlertDialog.Builder(this)
                .setTitle("Sending the challenge...")
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        nm.giveUp();
                        acceptChallenges();
                    }
                })
                .setIcon(android.R.drawable.ic_menu_info_details)
                .show();
    }

    public void receiveChallengeDialogue(final KMPChallenge kmpc, final InetAddress sender)
    {
        myName = editTextFirstPlayer.getText().toString();
        new AlertDialog.Builder(this)
                .setTitle("You've been challenged by " + kmpc.playerName())
                .setMessage("Would you like to accept the challenge?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        response = true;
                        responded(kmpc, sender);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        response = false;
                        responded(kmpc, sender);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void responded(final KMPChallenge kmpc, final InetAddress sender)
    {
        nm = new NetworkMessenger(getActivity(), sndsock, new NetworkMessenger.NetworkSendCallback() {
            public void callback() {
                if (response) {
                    Intent intent = new Intent(getActivity() , CheckersSystem.class);

                    MainActivity.blackPlayer = new RemotePlayer(Color.BLACK, kmpc.playerName(), rcvsock, sndsock, sender, kmpc.msgid()+2);
                    MainActivity.redPlayer = new LocalPlayer(Color.RED, myName);

                    startActivity(intent);
                } else {
                    acceptChallenges();
                }
            }
        }, new KMPResponse(kmpc.msgid() + 1, myName, response), sender);
        nm.dispatch();
    }

    public void awaitResponse()
    {
        new NetworkListener(this, rcvsock,new NetworkListener.NetworkReturnCallback() {
            public void callback(NetworkListener origin) {
                KMPResponse kmpr = (KMPResponse)origin.getKMPPacket();
                String playerName = kmpr.playerName();
                String resp = kmpr.response() ? "accepted" : "refused";
                chgDialogue.setTitle(playerName + " " + resp + " your challenge");

                // this kills the dialogue :(
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        chgDialogue.dismiss();
                    }
                }, 1000);

                if (kmpr.response()){
                    Intent intent = new Intent(getActivity() , CheckersSystem.class);

                    MainActivity.blackPlayer = new LocalPlayer(Color.BLACK, myName);
                    MainActivity.redPlayer = new RemotePlayer(Color.RED, kmpr.playerName(), rcvsock, sndsock, destaddr, chgid + 2);

                    startActivity(intent);
                } else {
                    acceptChallenges();
                }
            }
        }, BaseKMPPacket.RSPop, destaddr, chgid + 1).listen();
    }
}
