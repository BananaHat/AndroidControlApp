package com.electrocolorlabs.leds.ledcontroller.app.androiddependant

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * Created by gabriel on 12/26/17.
 */

class NetManager(context: Context) {

    // we're going to hash these strings to try and obfuscate our network activity a bit
    private val TAG = this.javaClass.simpleName
    private val BROADCASTMSG = "Electro Color Labs controller broadcast".hashCode().toString()
    private val RESPONSEMSG = "Electro Color Labs device register response".hashCode().toString()
    private val BROADCASTPORT = Integer.parseInt(BROADCASTMSG.subSequence(0, 5).toString())
    private val LISTEN_PORT = Integer.parseInt(RESPONSEMSG.subSequence(0, 5).toString())

    private val context: Context = context

    fun startBroadcastReciver(broadcastListener: Any) {
        Thread(Runnable {
            sendBroadcast()
            var timeout = System.currentTimeMillis() + 1000*10
            while (System.currentTimeMillis() < timeout) {
                val socket = DatagramSocket(LISTEN_PORT)
                val recvBuf = ByteArray(15000)
                val packet = DatagramPacket(recvBuf, recvBuf.size)
                socket.receive(packet)
                Log.i(TAG, "Packet received from: " + packet.address.hostAddress)
                val data = String(packet.data).trim { it <= ' ' }
                Log.i(TAG, "Packet received; data: " + data)
            }
        })
    }

    fun sendBroadcast() {
        val socket = DatagramSocket()
        socket.broadcast = true
        val contents = (BROADCASTMSG + " " + getBroadcastAddress().toString()).toByteArray()
        val packet = DatagramPacket(contents, contents.size, getBroadcastAddress(), BROADCASTPORT)
        socket.send(packet)

    }

    fun getBroadcastAddress(): InetAddress {
        val dhcpInfo = (context.getSystemService(Context.WIFI_SERVICE) as WifiManager).dhcpInfo
        val broadcast = dhcpInfo.ipAddress and dhcpInfo.netmask or dhcpInfo.netmask.inv()
        val quads = ByteArray(4)
        for (k in 0..3)
            quads[k] = (broadcast shr k * 8 and 0xFF).toByte()
        return InetAddress.getByAddress(quads)
    }
}
