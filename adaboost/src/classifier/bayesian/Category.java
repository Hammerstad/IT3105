package classifier.bayesian;

import java.util.List;

public class Category {
	private final int categoryValue;
	private final AttributeList attributes;
	
	public Category(int categoryValue, AttributeList attributes){
		this.categoryValue = categoryValue;
		this.attributes = attributes;
	}
	
	public int getCategoryValue(){
		return categoryValue;
	}
	
	public Attribute getAttribute(int pos){
		return attributes.getAttributes().get(pos);
	}

	public List<Attribute> getAttributes(){
		return attributes.getAttributes();
	}
	
	public int size(){
		return attributes.size();
	}
}
