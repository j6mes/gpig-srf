package co.j6mes.infra.srf.daemon;

import co.j6mes.infra.srf.database.ServiceDatabaseInstance;
import co.j6mes.infra.srf.database.ServiceRegistration;
import co.j6mes.infra.srf.registration.registrationmessage.Register;
import co.j6mes.infra.srf.registration.registrationmessage.Service;
import co.j6mes.infra.srf.registration.registrationmessage.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.*;

/**
 * Created by james on 11/05/2016.
 */
public final class QueryThread implements Runnable {

    private static QueryThread instance;

    private static Logger log = LogManager.getLogger(QueryThread.class);

    private ServiceRegistration database = ServiceDatabaseInstance.getInstance();

    private QueryThread() {

    }

    public static synchronized QueryThread getInstance() {
        if (instance == null) {
            instance = new QueryThread();
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

                log.trace("Packet Received");
                log.trace("Data:" + new String(packet.getData()).trim());

                String message = new String(packet.getData()).trim();

                Register r = null;
                try {
                    log.debug("Service registration received from: "+ packet.getAddress().getHostAddress());
                    r = MessageParser.getInstance().parse(message);
                } catch (JAXBException e) {
                    log.warn("Invalid registrationmessage. Got JAXBException");
                    log.warn(e);
                }


                if(r != null) {

                    for(Service s : r.Service) {
                        for(Topic t : s.Topic) {
                            log.info("Register " + s.Name + "/" + t.Name);
                            database.register(s.Name,t.Name, co.j6mes.infra.srf.database.Service.from(packet.getAddress(),t.ServiceDescription));
                        }

                    }




                    String msg = "";
                    try {
                         msg = MessageParser.getInstance().success();
                    } catch (JAXBException e) {
                        log.error("Could not marshal response message");
                        log.error(e);
                    }


                    if(msg.trim().length()>0) {
                        byte[] sendData = msg.getBytes();

                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                        socket.send(sendPacket);
                        log.debug("Sending ack message to : "+sendPacket.getAddress().getHostAddress());
                        log.trace(msg);
                    }
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
