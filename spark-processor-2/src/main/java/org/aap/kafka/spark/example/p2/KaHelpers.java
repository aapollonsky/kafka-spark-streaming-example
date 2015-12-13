package org.aap.kafka.spark.example.p2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import kafka.serializer.StringDecoder;

public class KaHelpers {
	//
	// Init Spark streaming
	//
	static JavaStreamingContext initStreams (String appName) {
		
		// Init Spark config
		SparkConf sparkConf = new SparkConf().setAppName("KaProcessor1");
		sparkConf.setMaster("local[*]");
		sparkConf.setSparkHome("/Users/aapollonsky/Tools/spark-1.5.2");

		// Create context with a 2 seconds batch interval
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(2));
		return jssc;
	}

	//
	// Start waiting for streams
	//
	static void waitStreams (JavaStreamingContext jssc) {	
		jssc.start();
		jssc.awaitTermination();
	}

	
	//
	// Read messages from Kafka queue
	//
	static JavaPairInputDStream<String, String> readKafkaMessages (JavaStreamingContext jssc, String topics) {
		String brokers = "localhost:9092";

		HashSet<String> topicsSet = new HashSet<String>(Arrays.asList(topics.split(",")));
		HashMap<String, String> kafkaParams = new HashMap<String, String>();
		kafkaParams.put("metadata.broker.list", brokers);

		// Create direct kafka stream with brokers and topics
		return KafkaUtils.createDirectStream(jssc, String.class, String.class,
				StringDecoder.class, StringDecoder.class, kafkaParams, topicsSet);
	}
	
}
