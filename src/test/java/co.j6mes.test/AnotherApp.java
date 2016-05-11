package co.j6mes.test;

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

        sq.query("ckan","api");

        System.in.read();




    }
}
