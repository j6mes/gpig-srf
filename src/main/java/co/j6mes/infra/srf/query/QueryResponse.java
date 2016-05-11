package co.j6mes.infra.srf.query;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by james on 12/05/2016.
 */
@XmlRootElement(name="ServiceRegistrationFrameworkResponse")
public class QueryResponse {

    @XmlElement(name="IP")
    public String IP;

    @XmlElement(name="Port")
    public Integer Port;

    @XmlElement(name="Protocol")
    public String Protocol;

    @XmlElement(name="Path")
    public String Path;

}
