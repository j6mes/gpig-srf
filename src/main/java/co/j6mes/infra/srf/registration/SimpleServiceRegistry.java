package co.j6mes.infra.srf.registration;

import co.j6mes.infra.srf.registration.daemon.RegistryThread;
import co.j6mes.infra.srf.registration.registrationmessage.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 11/05/2016.
 */
public class SimpleServiceRegistry implements ServiceRegistry {

    private Service service;

    private boolean isUp = false;
    private boolean isGood = false;


    List<RegistryThread> threads = new ArrayList<>();

    public SimpleServiceRegistry(String serviceName) {
        this.service = new Service();
        this.service.Name = serviceName;
        this.service.Topic = new ArrayList<>();
    }



    @Override
    public void register(String topicName, int port) {
        Topic t = new Topic();

        t.Name = topicName;
        t.ServiceDescription = new ServiceDescription();
        t.ServiceDescription.Schema = "TCP";
        t.ServiceDescription.Port = port;

        service.Topic.add(t);
    }

    @Override
    public void register(String topicName, String schema, int port) {
        Topic t = new Topic();

        t.Name = topicName;
        t.ServiceDescription = new ServiceDescription();
        t.ServiceDescription.Schema = schema;
        t.ServiceDescription.Port = port;

        service.Topic.add(t);
    }

    @Override
    public void register(String topicName, String schema, String uri, int port) {
        Topic t = new Topic();

        t.Name = topicName;
        t.ServiceDescription = new URIServiceDescription();
        t.ServiceDescription.Schema = schema;
        t.ServiceDescription.Port = port;
        ((URIServiceDescription)t.ServiceDescription).Path = uri;

        service.Topic.add(t);
    }



    @Override
    public void deregister(String topicName) {

    }

    @Override
    public void up() {
        if(isUp) {
            return;
        }

        isUp = true;

        for(RegistryThread thread : threads) {
            thread.stop();
        }

        threads.clear();

        RegistryThread status = new RegistryThread(this, 30*1000, true);
        RegistryThread retry = new RegistryThread(this, 5*1000, false);

        threads.add(status);
        threads.add(retry);

        for(RegistryThread thread : threads) {
            Thread t = new Thread(thread);
            t.start();
        }

    }

    @Override
    public void down() {
        if(!isUp) {
            return;
        }

        isUp = false;

        for(RegistryThread thread : threads) {
            thread.stop();
        }


        threads.clear();
    }

    @Override
    public boolean isUp() {
        return isUp;
    }

    @Override
    public boolean isGood() {
        return isGood;
    }

    @Override
    public void setGood(boolean good) {
        isGood = good;
    }

    @Override
    public Register getService() {
        Register r = new Register();
        r.Service = new ArrayList<>();
        r.Service.add(service);
        return r;
    }
}
