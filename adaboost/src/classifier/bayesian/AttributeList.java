package classifier.bayesian;

import java.util.List;

public class AttributeList {
	private final List<Attribute> attributes;
	
	public AttributeList(List<Attribute> attributes){
		this.attributes = attributes;
	}
	
	public List<Attribute> getAttributes(){
		return attributes;
	}
	
	public int size(){
		return attributes.size();
	}
}
