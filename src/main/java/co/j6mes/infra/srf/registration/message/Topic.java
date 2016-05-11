package co.j6mes.infra.srf.registration.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Topic")
public class Topic {
    @XmlElement
    public ServiceDescription ServiceDescription;

    @XmlElement
    public String Name;
}
