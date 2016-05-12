package co.j6mes.infra.srf.daemon;

import co.j6mes.infra.srf.database.ServiceDatabaseInstance;
import co.j6mes.infra.srf.database.ServiceRegistration;
import co.j6mes.infra.srf.query.Query;
import co.j6mes.infra.srf.query.QueryParser;
import co.j6mes.infra.srf.registration.registrationmessage.Register;
import co.j6mes.infra.srf.registration.registrationmessage.Service;
import co.j6mes.infra.srf.registration.registrationmessage.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.*;

/**
 * Created by james on 11/05/2016.
 */
public final class RegistrationThread implements Runnable {

    private static RegistrationThread instance;

    private static Logger log = LogManager.getLogger(RegistrationThread.class);

    private ServiceRegistration database = ServiceDatabaseInstance.getInstance();

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

                log.trace("Packet Received");
                log.trace("Data:" + new String(packet.getData()).trim());

                String message = new String(packet.getData()).trim();

                Register r = null;
                try {
                    r = MessageParser.getInstance().parse(message);
                    log.debug("Service registration received from: "+ packet.getAddress().getHostAddress());
                } catch (JAXBException e) {
                    //log.warn("Invalid registrationmessage. Got JAXBException");
                    //log.warn(e);
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
                    continue;
                }


                Query q = null;
                try {
                    q = QueryParser.getInstance().parse(message);
                    log.debug("Service query received from: "+ packet.getAddress().getHostAddress());
                } catch (JAXBException e) {
                    //log.warn("Invalid registrationmessage. Got JAXBException");
                    //log.warn(e);
                }

                if(q != null) {
                    log.info("Received Service Query for "+ q.Service +"/" + q.Topic);
                    co.j6mes.infra.srf.database.Service reg = database.query(q.Service,q.Topic);

                    if(reg == null) {
                        log.warn("Not found");


                        String msg = "";
                        try {
                            msg = QueryParser.getInstance().fail();
                        } catch (JAXBException e) {
                            log.error("Could not marshal response message");
                            log.error(e);
                        }

                        if(msg.trim().length()>0) {
                            byte[] sendData = msg.getBytes();

                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                            socket.send(sendPacket);
                            log.debug("Sending not found message to : "+sendPacket.getAddress().getHostAddress());
                            log.debug(msg);
                        }

                        continue;
                    }

                    String msg = "";
                    try {
                        msg = QueryParser.getInstance().response(reg);
                    } catch (JAXBException e) {
                        log.error("Could not marshal response message");
                        log.error(e);
                    }

                    if(msg.trim().length()>0) {
                        byte[] sendData = msg.getBytes();

                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                        socket.send(sendPacket);
                        log.debug("Sending response message to : "+sendPacket.getAddress().getHostAddress());
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
