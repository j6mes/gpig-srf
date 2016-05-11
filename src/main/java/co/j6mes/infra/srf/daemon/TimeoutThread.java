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
import java.util.Set;

/**
 * Created by james on 11/05/2016.
 */
public final class TimeoutThread implements Runnable {

    private final long timeOut = 60 * 1000;

    private static TimeoutThread instance;

    private static Logger log = LogManager.getLogger(TimeoutThread.class);

    private ServiceRegistration database = ServiceDatabaseInstance.getInstance();

    private TimeoutThread() {

    }

    public static synchronized TimeoutThread getInstance() {
        if (instance == null) {
            instance = new TimeoutThread();
        }
        return instance;
    }


    @Override
    public void run() {

        while(true) {

            try {
                Thread.sleep(timeOut);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Set<String> services = database.list();

            for(String service: services) {
                co.j6mes.infra.srf.database.Service sr = database.query(service);
                if(sr != null) {
                    if(sr.getRegistrationDate() < System.currentTimeMillis() - timeOut) {
                        log.warn("Deregistering timed out service: " + service);
                        database.deregister(service);
                    }
                }
            }
        }
    }



}
