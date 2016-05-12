package co.j6mes.infra.srf.query;

import co.j6mes.infra.srf.database.Service;
import co.j6mes.infra.srf.registration.registrationmessage.Register;
import co.j6mes.infra.srf.registration.statusmessage.SuccessMessage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by james on 12/05/2016.
 */
public class QueryParser {

    private static QueryParser instance;

    private JAXBContext context;
    private JAXBContext responseContext;
    private JAXBContext failContext;

    private QueryParser() {
        try {
            context = JAXBContext.newInstance(Query.class);
            responseContext = JAXBContext.newInstance(QueryResponse.class);
            failContext = JAXBContext.newInstance(NotFoundMessage.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static QueryParser getInstance() {
        if(instance == null) {
            instance = new QueryParser();
        }
        return instance;
    }

    public Query parse(String message) throws JAXBException {
        Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
        return (Query) jaxbUnmarshaller.unmarshal(new StringReader(message));
    }


    public String response(Service reg) throws JAXBException {
        Marshaller jaxbMarshaller = responseContext.createMarshaller();
        StringWriter sw = new StringWriter();

        QueryResponse qr = new QueryResponse();
        qr.IP = reg.getIp();
        qr.Port = reg.getPort();



        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(qr, sw);
        return sw.getBuffer().toString();

    }

    public String fail() throws JAXBException {
        Marshaller jaxbMarshaller = failContext.createMarshaller();

        StringWriter sw = new StringWriter();

        NotFoundMessage s = new NotFoundMessage();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(s, sw);

        return sw.getBuffer().toString();
    }

    public QueryResponse parseResponse(String response) throws JAXBException {
        Unmarshaller jaxbUnmarshaller = responseContext.createUnmarshaller();
        return (QueryResponse) jaxbUnmarshaller.unmarshal(new StringReader(response));
    }
}
