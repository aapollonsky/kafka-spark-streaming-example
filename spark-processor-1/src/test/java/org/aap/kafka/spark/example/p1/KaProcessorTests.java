package org.aap.kafka.spark.example.p1;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.junit.Assert;
import scala.tools.nsc.matching.ParallelMatching.MatchMatrix.Row;

public class KaProcessorTests {
	  private transient JavaSparkContext javaCtx;

	  @Before
	  public void setUp() {
	    SparkContext context = new SparkContext("local[*]", "testing");
	    javaCtx = new JavaSparkContext(context);
	  }

	  @After
	  public void tearDown() {
	    javaCtx = null;
	  }
	  
	@Test
	public void test() {
		List<InRecord> inlist = new ArrayList<>(2);
	    InRecord in1 = new InRecord("1", "11");
	    InRecord in2 = new InRecord("2", "22");
	    inlist.add(in1);
	    inlist.add(in2);

	    JavaRDD<OutRecord> recStream = javaCtx.parallelize(inlist).map(inrecord -> {
			System.out.println("-- transformed " + inrecord.field2);
			return new OutRecord(inrecord.field1, inrecord.field2);
		});
	    Assert.assertTrue(recStream.count() == 2);
	    
	    recStream.foreach(rec -> {
	    	// put your individual checks here
	    });
	}

}
