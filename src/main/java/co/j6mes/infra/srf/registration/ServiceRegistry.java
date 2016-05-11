package co.j6mes.infra.srf.registration;

/**
 * Created by james on 11/05/2016.
 */
public interface ServiceRegistry {

    Registration init(String service);

    Registration register(String topicName);

    Registration register(String topicName, int i);

}
