package co.j6mes.infra.srf.query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by james on 12/05/2016.
 */
public class SimpleServiceQuery implements ServiceQuery {

    private static Logger log = LogManager.getLogger(SimpleServiceQuery.class);

    @Override
    public QueryResponse query(String service, String topic) {

        final QueryResponse qr = new QueryResponse();
        ServiceQueryThread sq = new ServiceQueryThread(qr,service,topic,2*1000);

        Thread t = new Thread(sq);
        t.start();

        while(!qr.complete) {
            try {
                synchronized (qr) {
                    qr.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        t.stop();


        log.info("Found Service " + qr.IP + ":" +qr.Port );
        return qr;
    }

    @Override
    public void post(QueryResponse qr) {

    }

}
