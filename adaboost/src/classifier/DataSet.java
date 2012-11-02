package classifier;

import java.util.ArrayList;

public class DataSet {

	private Instance[] instances;

	public DataSet(ArrayList<double[]> list) {
		instances = new Instance[list.size()];
		for (int i = 0; i < list.size(); i++) {
			instances[i] = new Instance(list.get(i), 1.0/list.size());
		}
	}
	
	public DataSet(Instance[] instances){
		this.instances = instances;
	}
	
	public int length(){
		return instances.length;
	}
	
	public Instance[] instances(){
		return instances;
	}
	
	public Instance get(int i){
		return instances[i];
	}
	
	public DataSet subset(int from, int to){
		Instance[] instances = new Instance[to-from];
		System.arraycopy(this.instances, from, instances, 0, to-from);
		return new DataSet(instances);
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(Instance element: instances){
			sb.append(element).append('\n');
		}
		return sb.toString();
	}
}
