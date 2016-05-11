package co.j6mes.infra.srf.registration;

import co.j6mes.infra.srf.registration.registrationmessage.Register;

/**
 * Created by james on 11/05/2016.
 */
public interface ServiceRegistry {

    void register(String topicName, int i);
    void register(String topicName, String schema, int port);
    void register(String topicName, String schema, String uri, int port);


    void deregister(String topicName);

    void up();
    void down();

    boolean isUp();
    boolean isGood();

    void setGood(boolean good);

    Register getService();
}
