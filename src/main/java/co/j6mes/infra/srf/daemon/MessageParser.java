package co.j6mes.infra.srf.daemon;

import co.j6mes.infra.srf.registration.message.Register;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

/**
 * Created by james on 11/05/2016.
 */
public class MessageParser {
    static MessageParser instance;

    static JAXBContext context;
    private MessageParser() {
        
    }

    public static MessageParser getInstance() {
        if (instance == null) {
            instance = new MessageParser();
            try {
                context = JAXBContext.newInstance(Register.class);
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
}
