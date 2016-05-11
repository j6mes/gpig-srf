package co.j6mes.infra.srf.daemon;

import java.io.IOException;

/**
 * Created by james on 11/05/2016.
 */
public class ServiceRegistrationDaemon {
    public static void main(String args[]) throws IOException {
        Thread t = new Thread(RegistrationThread.getInstance());
        t.start();

        System.in.read();

    }
}
