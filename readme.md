##Overview

This project was created as a Proof of Concept for using Spark Cluster with Kafka for typical backend data processing and storage. The following major modules to note:

* *kafka-producer*. The producer reads files from the "data" directory and pushes each file's content to the Kafka topic named "spipe1". A message per file. Dependent on Kafka and Zookeeper.

* *kafka-processor-1*. This Spark Streaming module emulates pipeline for input data processing. It reads data from the "spipe1" Kafka topic as DStream, then invokes one by one the distributed tasks (just placeholders in this example) on the DStreams to

1. convert string to object
2. filter out bad records
3. translate to different objects
4. send to chained "spipe2" Kafka topic (kafka-processor-2 is listening on this topic and suppose to finish processing)

The module is dependent on Kafka, Zookeeper, Spark Cluster, and Spark Streaming

* *kafka-processor-2*. This Spark Streaming module emulates pipeline for data storage. It reads data from the "spipe2" Kafka topic as DStream, then emulates storing of each record in the database (not really!)

The module is dependent on Kafka, Zookeeper, Spark Cluster, and Spark Streaming


##Prerequisites
**Kafka**
1. Ensure you have Java 8 installed
2. Download and install Kafka http://kafka.apache.org/downloads.html. You can find good Kafka overview here: http://kafka.apache.org/documentation.html
3. Download and install Zookeeper here: http://zookeeper.apache.org/releases.html.
4. Unpack and start zookeeper as ./bin/zkServer.sh start
5. Unpack and start Kafka as ./bin/kafka-server-start.sh config/server.properties

**Spark**
1. Download Spark Standalone cluster here: http://spark.apache.org/downloads.html. I used "Prebuilt for Hadoop 2.6 or Later" option.
2. Unpack.
3. Start Master as sbin/start-master.sh
4. Open the Spark UI in Web Browser as http://localhost:8080/
5. Copy the Master internal URL from the page to the clipboard. It would look something like "spark://My-MacBook-Pro.local:7077"
6. Start Slave as ./sbin/start-slave.sh <master URL from step 5>. For instance: ./sbin/start-slave.sh spark://My-MacBook-Pro.local:7077


##Publish spark-processor-1 to Spark Cluster

* Package the apps as all-inclusive jar file for deployment to Spark Cluster

		cd spark-processor-1
		./package.sh

To publish to Spark Cluster:

		./sbin/spark-submit --class org.aap.kafka.spark.example.KaProcessor1 \
								--deploy-mode cluster \
								--supervise \
								--master <your spark cluster master URL such as spark://My-MacBook-Pro.local:7077> \
								<path to your spark-processor-1 project>/target/spark-kafka-ex-p1-1.0-jar-with-dependencies.jar

To run locally (without publishing to cluster):

		mvn exec:java -Dexec.mainClass="org.aap.kafka.spark.example.p1.KaProcessor1"

##Publish spark-processor-2 to Spark Cluster

* Package the apps as all-inclusive jar file for deployment to Spark Cluster

		cd spark-processor-2
		./package.sh

To publish to Spark Cluster:

		./sbin/spark-submit --class org.aap.kafka.spark.example.KaProcessor2 \
								--deploy-mode cluster \
								--supervise \
								--master <your spark cluster master URL such as spark://My-MacBook-Pro.local:7077> \
								<path to your spark-processor-2 project>/target/spark-kafka-ex-p2-1.0-jar-with-dependencies.jar

To run locally (without publishing to cluster):

		mvn exec:java -Dexec.mainClass="org.aap.kafka.spark.example.p2.KaProcessor2"

##Execute Producer
cd kafka-producer
mvn exec:java -Dexec.mainClass="org.aap.kafka.spark.example.producer.KaProducer"



## See Results
* Go to Spark UI http://localhost:8080/
* Click on worker - here you can check stderr/stdout for both modules.

Have fun!

* P.S. I used Mac/Eclipse with Maven project type to run everything.
* P.P.S. Spark Modules do not have to be deployed to cluster to test - you can also run them as a regular Java Application from your IDE.
