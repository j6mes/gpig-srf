package co.j6mes.infra.srf.registration.message;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by james on 11/05/2016.
 */

@XmlRootElement(name="ServiceRegistrationFramework")
public class Register {

    @XmlElement
    public List<Service> Service;


}
