package co.j6mes.infra.srf.database;

import java.util.Set;

/**
 * Created by james on 11/05/2016.
 */
public interface ServiceRegistration {
    void register(String serviceName, String topicName, Service registration);
    Service query(String serviceName, String topicName);

    Set<String> list();
    void deregister(String serviceName, String topicName);
}
