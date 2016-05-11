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
    public static void main(String[] args) throws IOException {

        ServiceRegistry sr = new SimpleServiceRegistry("ckan");
        sr.register("api",5000);
        sr.register("other",123);

        sr.up();


        System.in.read();




    }
}
