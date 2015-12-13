package org.aap.kafka.spark.example.p1;

import java.io.Serializable;

import org.apache.hadoop.util.Time;

class OutRecord implements Serializable {
	static final long serialVersionUID = 10L;
	Long  	timeCreated;
	String 	outfield1;
	String 	outfield2;

	public OutRecord(String outfield1, String outfield2) {
		this.outfield1 = outfield1;
		this.outfield2 = outfield2;
		timeCreated = Time.now();
	}
	
    public String toString() { 
        return "OutRecord " + this.outfield1 + ":" + this.outfield2 + ":" + this.timeCreated.toString();
    } 

}