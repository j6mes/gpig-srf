package co.j6mes.infra.srf.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by james on 11/05/2016.
 */
public class ServiceDatabaseInstance implements ServiceRegistration {

    private static Logger log = LogManager.getLogger(ServiceDatabaseInstance.class);
    private static ServiceDatabaseInstance instance;


    private HashMap<String,Service> services = new HashMap<>();

    private ServiceDatabaseInstance () {

    }
    public static synchronized ServiceDatabaseInstance getInstance() {
        
        if(instance == null) {
            instance = new ServiceDatabaseInstance();
        }

        return instance;
    }


    @Override
    public synchronized void register(String serviceName, String topicName, Service registration) {
        String shortname = serviceName + "/" + topicName;
        services.put(shortname,registration);

    }

    @Override
    public synchronized Service query(String serviceName, String topicName) {
        return services.get(serviceName+"/"+topicName);
    }

    @Override
    public Service query(String service) {
        return services.get(service);
    }

    @Override
    public synchronized Set<String> list() {
        return services.keySet();
    }

    @Override
    public synchronized void deregister(String serviceName, String topicName) {
        services.remove(serviceName+"/"+topicName);
    }

    @Override
    public void deregister(String service) {
        services.remove(service);
    }


}
