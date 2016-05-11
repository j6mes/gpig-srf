package co.j6mes.infra.srf.registration;

import co.j6mes.infra.srf.Location;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by james on 11/05/2016.
 */
public final class LocationFactory implements LocationProvider{

    private LocationFactory() {

    }

    private static LocationFactory instance;

    public static synchronized LocationFactory getInstance() {
        if(instance == null) {
            instance = new LocationFactory();
        }

        return instance;
    }


    private List<String> getPublicIpAddresses() {
        List<String> found = new ArrayList<String>();

        Enumeration e = null;
        try {
            e = NetworkInterface.getNetworkInterfaces();
            while(e.hasMoreElements())
            {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements())
                {
                    InetAddress i = (InetAddress) ee.nextElement();
                    System.out.println(i.getHostAddress());

                    found.add(i.getHostAddress());
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }


        return found;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public Location getLocation(int port) {
        return null;
    }
}
