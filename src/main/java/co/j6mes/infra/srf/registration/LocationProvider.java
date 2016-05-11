package co.j6mes.infra.srf.registration;

import co.j6mes.infra.srf.Location;

/**
 * Created by james on 11/05/2016.
 */
public interface LocationProvider {

    Location getLocation();

    Location getLocation(int port);

}
