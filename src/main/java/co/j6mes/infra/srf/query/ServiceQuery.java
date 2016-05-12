package co.j6mes.infra.srf.query;

/**
 * Created by james on 12/05/2016.
 */
public interface ServiceQuery {
    QueryResponse query(String ckan, String api);
    void post(QueryResponse qr);
}
