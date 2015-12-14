package org.aap.kafka.spark.example.p1;

import java.io.Serializable;

import org.apache.hadoop.util.Time;

public class InRecord implements Serializable {
	static final long serialVersionUID = 10L;
	Long  	timeReceived;
	String 	field1;
	String 	field2;

	public InRecord(String field1, String field2) {
		this.field1 = field1;
		this.field2 = field2;
		timeReceived = Time.now();
	}
	
    public String toString() { 
        return "InRecord " + this.field1 + ":" + this.field2 + ":" + this.timeReceived.toString();
    }

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	} 

}