package co.j6mes.infra.srf.registration.daemon;

import co.j6mes.infra.srf.registration.Comms;
import co.j6mes.infra.srf.registration.ServiceRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

/**
 * Created by james on 11/05/2016.
 */
public class RegistryThread implements Runnable {

    private static Logger log = LogManager.getLogger(RegistryThread.class);

    public int retry;

    boolean stop = false;
    boolean onGood;

    ServiceRegistry sr;

    public RegistryThread(ServiceRegistry sr, int retry, boolean onGood) {
        this.retry = retry;
        this.sr = sr;
        this.onGood = onGood;
    }

    @Override
    public void run() {

        while(true) {


            if(!sr.isUp() || stop) {
                break;
            }

            //Send registration is we're good to send it
            if(sr.isGood() == onGood) {
                DatagramSocket c = null;

                try {
                    c = new DatagramSocket();
                    c.setBroadcast(true);

                    byte[] sendData = Comms.serializeService(sr.getService()).getBytes();

                    try {
                        log.debug("Attempting to register service");
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 15000);
                        c.send(sendPacket);
                        log.debug("Packet sent");
                    } catch (Exception e) {
                        log.error("Unable to broadcast service");
                        log.error(e);
                    }

                    byte[] recvBuf = new byte[15000];
                    DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                    c.setSoTimeout(1*1000);
                    c.receive(receivePacket);

                    if(new String(receivePacket.getData()).trim().contains("Success")) {
                        log.info("Registered with service registry");
                        sr.setGood(true);
                    } else {
                        log.warn("Received non-success response from service registry");
                        sr.setGood(false);
                    }
                }
                catch (SocketTimeoutException e) {
                    sr.setGood(false);
                    log.warn("Socket timeout. Unable to register with registry. Is the registry up?");
                }
                catch (IOException ex) {
                    log.error(ex);
                }
                finally {
                    if(c!=null){
                        c.close();
                    }
                }



            }

            try {
                Thread.sleep(retry);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }


    public void stop() {
        this.stop = true;
    }
}
