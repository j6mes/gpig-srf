package co.j6mes.infra.srf.registration.registrationmessage;

import javax.xml.bind.annotation.*;

/**
 * Created by james on 11/05/2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="ServiceDescription")
@XmlSeeAlso(URIServiceDescription.class)
public class ServiceDescription {

    @XmlElement(name="ServicePort")
    public Integer Port;

    @XmlElement(name="Schema")
    public String Schema;
}
