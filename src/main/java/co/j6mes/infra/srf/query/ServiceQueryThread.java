package co.j6mes.infra.srf.query;

import co.j6mes.infra.srf.registration.Comms;
import co.j6mes.infra.srf.registration.SimpleServiceRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

/**
 * Created by james on 11/05/2016.
 */
public class ServiceQueryThread implements Runnable {

    private static Logger log = LogManager.getLogger(ServiceQueryThread.class);
    private final String service;
    private final String topic;
    private final QueryResponse qr;
    public int retry;

    private boolean stop = false;
    public ServiceQueryThread(QueryResponse qr, String service, String topic, int retry) {
        this.retry = retry;
        this.service = service;
        this.topic = topic;
        this.qr = qr;

        log.debug("Init SQ thread");
    }

    @Override
    public void run() {

        log.debug("Starting query thread");

        String response = "";
        while(true) {



            if(stop) {
                break;
            }


            DatagramSocket c = null;
            try {
                c = new DatagramSocket();
                c.setBroadcast(true);

                Query q = new Query();
                q.Service = service;
                q.Topic = topic;

                byte[] sendData = Comms.serializeQuery(q).getBytes();

                try {
                    log.debug("Looking for service");
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

                if(new String(receivePacket.getData()).trim().contains("ServiceNotFound")) {
                    log.warn("Service Not Found");
                } else {
                    log.info("Found Service");
                    response = new String(receivePacket.getData()).trim();
                    break;
                }
            }
            catch (SocketTimeoutException e) {
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

            try {
                Thread.sleep(retry);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


        if(response.trim().length() >0 ){
            try {
                QueryResponse qr2 = QueryParser.getInstance().parseResponse(response);

                qr.IP = qr2.IP;
                qr.Port = qr2.Port;
                qr.Path = qr2.Path;
                qr.Protocol = qr2.Protocol;

                qr.complete = true;

                synchronized (qr) {
                    qr.notify();
                }


            } catch (JAXBException e) {
                log.error(e);
            }
        }

    }


    public void stop() {
        this.stop = true;
    }
}
