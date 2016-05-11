package co.j6mes.infra.srf.daemon;

import co.j6mes.infra.srf.registration.registrationmessage.Register;
import co.j6mes.infra.srf.registration.registrationmessage.Service;
import co.j6mes.infra.srf.registration.registrationmessage.URIServiceDescription;
import co.j6mes.infra.srf.registration.statusmessage.SuccessMessage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by james on 11/05/2016.
 */
public class MessageParser {
    static MessageParser instance;

    static JAXBContext context;
    static JAXBContext statuscontext;

    private MessageParser() {
        
    }

    public static MessageParser getInstance() {
        if (instance == null) {
            instance = new MessageParser();
            try {
                context = JAXBContext.newInstance(Register.class);
                statuscontext = JAXBContext.newInstance(SuccessMessage.class);

            } catch (JAXBException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        return instance;
    }

    public Register parse(String message) throws JAXBException {
        Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
        return (Register) jaxbUnmarshaller.unmarshal(new StringReader(message));
    }

    public String success() throws JAXBException {
        Marshaller jaxbMarshaller = statuscontext.createMarshaller();

        StringWriter sw = new StringWriter();

        SuccessMessage s = new SuccessMessage();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(s, sw);

        return sw.getBuffer().toString();

    }
}
