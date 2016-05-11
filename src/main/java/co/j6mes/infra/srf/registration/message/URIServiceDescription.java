package co.j6mes.infra.srf.registration.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by james on 11/05/2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="URIServiceDescription")
public class URIServiceDescription extends ServiceDescription {

    @XmlElement(name="Path")
    public String Path;

}
