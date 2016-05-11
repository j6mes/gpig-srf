package co.j6mes.infra.srf.registration;

/**
 * Created by james on 11/05/2016.
 */
public final class RegistrationFactory {

    private RegistrationFactory() {

    }

    private static RegistrationFactory instance;

    public static synchronized RegistrationFactory getInstance() {
        if(instance == null) {
            instance = new RegistrationFactory();
        }

        return instance;
    }

}
