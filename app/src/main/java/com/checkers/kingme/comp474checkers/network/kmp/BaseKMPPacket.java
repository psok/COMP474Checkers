package com.checkers.kingme.comp474checkers.network.kmp;

/**
 * Created by Carlos 
 */
import java.io.*;

// this class implements our KMP (King Me Protocol) base packet structure
public abstract class BaseKMPPacket {


    // possible packet types specified in the protocol
    public static final byte  CHGop = 1;
    public static final byte  RSPop = 2;
    public static final byte  MOVop = 3;
    public static final byte  ACKop = 4;
    //public static final short ERRORop=5; // will we need this?

    // the default port to be used by the protocol
    public static final short DEFAULTPORT = 8888;

    // some standard parameters
    public static final int   INITTIMEOUT = 3000;      // milliseconds
    public static final int   HARDTIMEOUT = 250;      // milliseconds
    public static final int   SHORTSIZE = 2;           // in bytes
    public static final int   INTSIZE = 4;
    public static final int   BASESIZE = 1 + SHORTSIZE;// opcode (byte) + msgid (short)
    public static final int   MAXMSGSIZE = 24;

    public static final int   HEADERSIZE = BASESIZE;

    /* Not using these at this time
    public static final int   EBADPORT = 1;
    public static final int   EBADOPCODE = 2;
    */


    protected byte  opcode; // this byte tells us what kind of packet this is
    protected short msgid;  // the next two bytes are used for acknowledging messages

    // this static method allows you to identify the packet type of a just received packet
    public static int opcode(byte[] buf) {
        return buf[0];
    }

    protected BaseKMPPacket()
    {
        // The default constructor does nothing.
    }

    protected BaseKMPPacket(int opcode, int msgid)
    {
        this.opcode = (byte) opcode;
        this.msgid = (short) msgid;
    }

    public int size()
    {
        return BASESIZE;
    }

    public int opcode()
    {
        return this.opcode;
    }

    public int msgid()
    {
        return this.msgid;
    }

    // this method shall be used by subclasses to implement externalization
    public abstract byte[] write() throws IOException;

}