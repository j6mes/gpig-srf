package co.j6mes.infra.srf.database;

import co.j6mes.infra.srf.registration.registrationmessage.ServiceDescription;

import java.net.InetAddress;

/**
 * Created by james on 11/05/2016.
 */
public class Service {
    protected Integer port;
    protected String ip;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public static Service from(InetAddress ip, ServiceDescription serviceDescription) {

        Service s = new Service();
        s.port = serviceDescription.Port;
        s.ip = ip.getHostAddress();

        return s.init(serviceDescription);
    }

    protected Service init(ServiceDescription desc) {
        return this;
    }
}
