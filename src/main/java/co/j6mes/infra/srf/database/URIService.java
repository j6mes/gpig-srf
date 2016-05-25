package co.j6mes.infra.srf.database;

import co.j6mes.infra.srf.registration.registrationmessage.ServiceDescription;
import co.j6mes.infra.srf.registration.registrationmessage.URIServiceDescription;

import java.net.InetAddress;

/**
 * Created by james on 11/05/2016.
 */
public class URIService extends Service {
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    protected String path;


}
