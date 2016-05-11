package co.j6mes.infra.srf.query;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by james on 12/05/2016.
 */

@XmlRootElement(name="ServiceRegistrationFrameworkQuery")
public class Query {

    @XmlElement(name="Service")
    public String Service;

    @XmlElement(name="Topic")
    public String Topic;
}
