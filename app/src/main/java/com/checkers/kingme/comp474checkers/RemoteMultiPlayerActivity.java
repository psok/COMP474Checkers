package com.checkers.kingme.comp474checkers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.checkers.kingme.comp474checkers.frontend.CheckersSystem;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class RemoteMultiPlayerActivity extends ActionBarActivity {


    private EditText msgtext;
    private EditText dest;
    private DatagramSocket sock;
    private String message;
    private String destaddr;

    final int PORT = 32767;
    final int BUFSIZE = 512;
    final int TIMEOUT = 15000;

    //InetAddress localWiFiAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_multi_player);
        message = "abc";
        dest = (EditText)findViewById(R.id.edittext_firstplayer);
        EditText muhip = (EditText) findViewById(R.id.editText_secondPlayer);
        WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();

        InetAddress localWiFiAddr;
        try {
            localWiFiAddr = intToInetAddr(dhcp.ipAddress);
        } catch (UnknownHostException e) {
            System.err.println("Couldn't figure out my own IP address!");
            return;
        }

        //muhip.append(localWiFiAddr.toString());

        muhip.setText(localWiFiAddr.toString());

        try {
            sock = new DatagramSocket(PORT, localWiFiAddr);
        }
        catch (SocketException se) {
            System.err.println("No socket available!");
            return;
        }
        try {
            sock.setSoTimeout(TIMEOUT);       // time in milliseconds
        } catch (SocketException se) {
            System.err.println("Socket exception: timeout not set!");
            return;
        }

        NetworkListener netl = new NetworkListener();
        Thread listenerThread = new Thread(netl);
        listenerThread.start();

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

    public void sendChallenge(View view){
        //message = msgtext.getText().toString();
        destaddr = dest.getText().toString();

        NetworkMessenger netm = new NetworkMessenger();
        Thread messengerThread = new Thread(netm);
        messengerThread.start();
    }

    public void sendMessage(String msg, String add){
        message = msg;
        destaddr = add;

        NetworkMessenger netm = new NetworkMessenger();
        Thread messengerThread = new Thread(netm);
        messengerThread.start();
    }
    private InetAddress intToInetAddr(int ip) throws UnknownHostException
    {
        byte[] octets = { (byte)(0xff & ip),
                (byte)(0xff & (ip >> 8)),
                (byte)(0xff & (ip >> 16)),
                (byte)(0xff & (ip >> 24)) };

        return InetAddress.getByAddress(octets);
    }
    class NetworkListener implements Runnable
    {

        public void run ()
        {
            DatagramPacket msgDG = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
            String addr;

            while(true) { // read loop
                try {
                    msgDG.setLength(BUFSIZE);
                    sock.receive(msgDG);
                } catch (InterruptedIOException iioe) {
                    System.err.println("Response timed out!");
                    continue;
                } catch (IOException ioe) {
                    System.err.println("Bad receive!");
                    continue;
                }
                addr = msgDG.getAddress().getHostAddress();
                //final String str = new String(msgDG.getData(), 0, msgDG.getLength());
                final String str = new String(msgDG.getData(), 0, msgDG.getLength());
                final String finalAddr = addr;
                runOnUiThread(new Runnable() {
                    public void run() {

                        if(str.equalsIgnoreCase("Accept")){
                            Intent intent = new Intent(getActivity(), CheckersSystem.class);
                            startActivity(intent);
                        }else if (str.equalsIgnoreCase("Reject")){

                        }else {
                            showDialogue(finalAddr);
                        }
                    }
                });
            }
        }
    }
    public class NetworkMessenger implements Runnable {


        public void run()
        {

            DatagramPacket msgDG;
            try {
                msgDG = new DatagramPacket(new byte[0], 0, InetAddress.getByName(destaddr), PORT);
            }
            catch (UnknownHostException uhe) {
                System.err.println("Couldn't figure out destination host!");
                return;
            }

            String buf = message.concat("\n");
            int slen = buf.length();
            /*                        try {
                            KMPChallenge pack = new KMPChallenge(1, new RemotePlayer(), localWiFiAddr);
                        }
                            catch(SocketException exc){

                            }*/

            msgDG.setData(buf.getBytes());
            msgDG.setLength(slen);

            try {
                sock.send(msgDG);
            } catch (IOException ioe) {
                System.err.println("Couldn't send message out!");
            }
        }
    }

    public ActionBarActivity getActivity(){
        return this;
    }

    public void showDialogue(final String add){
        new AlertDialog.Builder(this)
                .setTitle("You are Challenged")
                .setMessage("Are you sure you want accept the challenge")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendMessage("Accept", add);
                        Intent intent = new Intent(getActivity(), CheckersSystem.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendMessage("Reject", add);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
