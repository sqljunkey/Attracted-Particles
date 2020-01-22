package Attracted.Particles;

public class ParticleWeight {
	
	int index; 
	
	Double weight;
	
	public ParticleWeight( int systemIndex , Double weight) {
		
		this.index=systemIndex;
		this.weight = weight;
	}
	
	void setWeight(Double weight) {this.weight = weight;}
	Double getWeight() {return weight; }
	int getIndex() {return index; }

}
