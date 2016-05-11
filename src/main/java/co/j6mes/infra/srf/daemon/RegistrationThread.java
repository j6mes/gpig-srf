package co.j6mes.infra.srf.daemon;

import java.io.IOException;
import java.net.*;

/**
 * Created by james on 11/05/2016.
 */
public class RegistrationThread implements Runnable {

    private static RegistrationThread instance;

    private RegistrationThread() {

    }

    public static synchronized RegistrationThread getInstance() {
        if (instance == null) {
            instance = new RegistrationThread();
        }
        return instance;
    }


    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(15000, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);

            while(true) {
                System.out.println("Ready");

                //Receive a packet
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);

                //Packet received
                System.out.println(getClass().getName() + ">>>Discovery packet received from: " + packet.getAddress().getHostAddress());
                System.out.println(getClass().getName() + ">>>Packet received; data: " + new String(packet.getData()));

                //See if the packet holds the right command (message)
                String message = new String(packet.getData()).trim();
                if (message.equals("DISCOVER_FUIFSERVER_REQUEST")) {
                    byte[] sendData = "DISCOVER_FUIFSERVER_RESPONSE".getBytes();

                    //Send a response
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                    socket.send(sendPacket);

                    System.out.println(getClass().getName() + ">>>Sent packet to: " + sendPacket.getAddress().getHostAddress());
                }

            }


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
