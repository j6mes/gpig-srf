package co.j6mes.infra.srf.registration.statusmessage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by james on 11/05/2016.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Success")
public class SuccessMessage {
    @XmlElement(name="Result")
    public Boolean result;
}
