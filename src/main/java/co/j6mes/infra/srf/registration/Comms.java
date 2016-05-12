package co.j6mes.infra.srf.registration;

import co.j6mes.infra.srf.query.Query;
import co.j6mes.infra.srf.registration.registrationmessage.Register;
import co.j6mes.infra.srf.registration.registrationmessage.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by james on 11/05/2016.
 */
public class Comms {


    public static String serializeService (Register service) {
        //1 marshall request
        StringWriter sw = new StringWriter();
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

        return sw.getBuffer().toString();
    }


    public static String serializeQuery(Query q) {
        //1 marshall request
        StringWriter sw = new StringWriter();
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(Query.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(q, sw);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return sw.getBuffer().toString();
    }
}
