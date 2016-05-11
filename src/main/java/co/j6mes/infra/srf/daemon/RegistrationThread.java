package co.j6mes.infra.srf.daemon;

import co.j6mes.infra.srf.registration.Registration;
import co.j6mes.infra.srf.registration.message.Register;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.*;

/**
 * Created by james on 11/05/2016.
 */
public class RegistrationThread implements Runnable {

    private static RegistrationThread instance;

    private static Logger log = LogManager.getLogger(RegistrationThread.class);

    private RegistrationThread() {

    }

    public static synchronized RegistrationThread getInstance() {
        if (instance == null) {
            instance = new RegistrationThread();
        }
        return instance;
    }

    int port = 15000;
    public void setPort(int port) {
        this.port = port;
    }


    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(port, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);


            log.info("Listening on 0.0.0.0 port " + port);

            while(true) {
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);

                log.debug("Packet Received");
                log.debug("Source address: "+ packet.getAddress().getHostAddress());
                log.debug("Data:" + new String(packet.getData()).trim());

                String message = new String(packet.getData()).trim();

                Register r = null;
                try {
                    r = MessageParser.getInstance().parse(message);
                } catch (JAXBException e) {
                    log.warn("Invalid message. Got JAXBException");
                    log.warn(e);
                }


                if(r != null) {
                    log.info("Register " + r.Service.get(0).Name);

                }

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
