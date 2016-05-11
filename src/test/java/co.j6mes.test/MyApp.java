package co.j6mes.test;

import co.j6mes.infra.srf.registration.ServiceRegistry;
import co.j6mes.infra.srf.registration.SimpleServiceRegistry;
import co.j6mes.infra.srf.registration.registrationmessage.Register;
import co.j6mes.infra.srf.registration.registrationmessage.Service;
import co.j6mes.infra.srf.registration.registrationmessage.URIServiceDescription;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.net.*;
import java.util.ArrayList;

/**
 * Created by james on 11/05/2016.
 */
public class MyApp {
    public static void main(String[] args) {

        StringWriter sw = new StringWriter();


        Register service = new Register();
        service.Service = new ArrayList<>();


        Service s = new Service();
        s.Name = "CKAN Service";
        s.Topic = new ArrayList<>();

        URIServiceDescription sd = new URIServiceDescription();
        sd.Path = "/api";
        sd.Port = 5000;

        co.j6mes.infra.srf.registration.registrationmessage.Topic t = new co.j6mes.infra.srf.registration.registrationmessage.Topic();
        t.Name = "API";
        t.ServiceDescription = sd;

        s.Topic.add(t);

        service.Service.add(s);
        service.Service.add(s);
        service.Service.add(s);

        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(Register.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(service, sw);
        } catch (JAXBException e) {
            e.printStackTrace();
        }



        ServiceRegistry sr = new SimpleServiceRegistry("ckan");
        sr.register("topic",5000);


        DatagramSocket c;
        // Find the server using UDP broadcast
        try {
            //Open a random port to send the package
            c = new DatagramSocket();
            c.setBroadcast(true);

            byte[] sendData = sw.getBuffer().toString().getBytes();

            //Try the 255.255.255.255 first
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 15000);
                c.send(sendPacket);
                System.out.println(">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
            } catch (Exception e) {
            }


            //Wait for a response
            byte[] recvBuf = new byte[15000];
            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
            c.receive(receivePacket);

            //We have a response
            System.out.println(">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

            //Check if the registrationmessage is correct
            String message = new String(receivePacket.getData()).trim();
            if (message.equals("DISCOVER_FUIFSERVER_RESPONSE")) {
                //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your contro
                System.out.println("Message" + message);
            }

            //Close the port!
            c.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }




    }
}
