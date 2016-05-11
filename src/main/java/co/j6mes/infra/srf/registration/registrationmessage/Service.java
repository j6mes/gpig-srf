package co.j6mes.infra.srf.registration.registrationmessage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by james on 11/05/2016.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Service")
public class Service {
    @XmlElement
    public List<Topic> Topic;

    @XmlElement
    public String Name;
}
