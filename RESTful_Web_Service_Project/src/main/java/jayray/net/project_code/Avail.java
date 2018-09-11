package jayray.net.assignment2;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//import jayray.net.orders.Address;
//import jayray.net.orders.Namespaces;

@XmlRootElement(namespace = "http://jayray.net/orders")
@XmlAccessorType(XmlAccessType.FIELD)
public class Avail {
	public boolean Available = true;

	
	
	public boolean getAvailData() {
		return Available;
	}

	public void setAvailData(boolean Available) {
		this.Available = Available;
	}

}
