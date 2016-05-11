package co.j6mes.infra.srf.registration;

import co.j6mes.infra.srf.Location;
import co.j6mes.infra.srf.Service;
import co.j6mes.infra.srf.Topic;

/**
 * Created by james on 11/05/2016.
 */
public interface Registration {
    Service getService();
    Topic getTopic();
    Location getLocation();

    void up();
}
