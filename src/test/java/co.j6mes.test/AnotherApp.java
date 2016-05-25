package co.j6mes.test;

import co.j6mes.infra.srf.query.QueryResponse;
import co.j6mes.infra.srf.query.ServiceQuery;
import co.j6mes.infra.srf.query.SimpleServiceQuery;
import co.j6mes.infra.srf.registration.ServiceRegistry;
import co.j6mes.infra.srf.registration.SimpleServiceRegistry;

import java.io.IOException;

/**
 * Created by james on 11/05/2016.
 */
public class AnotherApp {
    public static void main(String[] args) throws IOException {

        ServiceQuery sq = new SimpleServiceQuery();

        QueryResponse qr = sq.query("c2","maps");



        System.out.println("Found Service " + qr.IP + ":" +qr.Port );

        if(qr.Path !=null) {
            System.out.println("Found Service " + qr.IP + ":" +qr.Port + " / " + qr.Path);
        }




    }
}
