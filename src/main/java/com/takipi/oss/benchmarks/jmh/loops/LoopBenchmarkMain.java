package com.takipi.oss.benchmarks.jmh.loops;

import java.net.URL;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.function.Consumer;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.options.IntegerValueConverter;

@State(Scope.Benchmark)
public class LoopBenchmarkMain {
	final int size = 1000;
	List<Integer> integers = null;
	Map<Integer,Integer> mapA = new HashMap();
//	final String API="http://127.0.0.1:5000/";
	final String API="http://www.thomas-bayer.com/sqlrest/CUSTOMER/";

	public static void main(String[] args) {
		LoopBenchmarkMain benchmark = new LoopBenchmarkMain();
		benchmark.setup();
		

	}
	
	@Setup
	public void setup() {
		integers = new ArrayList<Integer>(size);
		populate(integers);
	}

	public void populate(List<Integer> list) {
		Random random = new Random();
		for (int i = 0; i < size; i++) {
//			list.add(Integer.valueOf(random.nextInt(1000000)));
			mapA.put(i,Integer.valueOf(random.nextInt(1000000)));
		}


	}
	
	public void callRESTUtil() {
		try {

			URL url = new URL(API);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

    //Testing for loops vs parallel stream for a heavy operation like API Call
//    @Benchmark
//	@BenchmarkMode(Mode.AverageTime)
//	@OutputTimeUnit(TimeUnit.MILLISECONDS)
//	@Fork(2)
//	@Measurement(iterations = 5)
//	@Warmup(iterations = 5)
//	public void call_normal() {
//		for(int i=0;i<10;i++){
//			callRESTUtil();
//		}
//	}
//	@Benchmark
//	@BenchmarkMode(Mode.AverageTime)
//	@OutputTimeUnit(TimeUnit.MILLISECONDS)
//	@Fork(2)
//	@Measurement(iterations = 5)
//	@Warmup(iterations = 5)
//	public void call_parallel() {
//		IntStream.range(1, 11).parallel().forEach(i -> callRESTUtil());
//	}



//	@Benchmark
//	@BenchmarkMode(Mode.AverageTime)
//	@OutputTimeUnit(TimeUnit.MILLISECONDS)
//	@Fork(2)
//	@Measurement(iterations = 5)
//	@Warmup(iterations = 5)
//	public void iterator_loop() {
//		//traversing using Iterator
//		Iterator<Integer> it = integers.iterator();
//		while(it.hasNext()){
//			Integer i = it.next();
//			System.out.println("Iterator Value::"+i);
//		}
//	}
//
//	@Benchmark
//	@BenchmarkMode(Mode.AverageTime)
//	@OutputTimeUnit(TimeUnit.MILLISECONDS)
//	@Fork(2)
//	@Measurement(iterations = 5)
//	@Warmup(iterations = 5)
//	public void forEach_loop() {
//		//traversing through forEach method of Iterable with anonymous class
//		integers.forEach(new Consumer<Integer>() {
//
//			public void accept(Integer t) {
//				System.out.println("forEach anonymous class Value::"+t);
//			}
//
//		});
//	}
//    @Benchmark
//    @BenchmarkMode(Mode.AverageTime)
//    @OutputTimeUnit(TimeUnit.MILLISECONDS)
//    @Fork(2)
//    @Measurement(iterations = 5)
//    @Warmup(iterations = 5)
//    public void forEachImplement_loop() {
//        //traversing with Consumer interface implementation
//        MyConsumer action = new MyConsumer();
//        integers.forEach(action);
//    }

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	@Fork(2)
	@Measurement(iterations = 5)
	@Warmup(iterations = 5)
	public void mapIterateNormal() {
		for (Object key : mapA.keySet()) {
			System.out.println(mapA.get(key));
		}
	}
	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	@Fork(2)
	@Measurement(iterations = 5)
	@Warmup(iterations = 5)
	public void mapIterateEntrySet() {
		for (Map.Entry<Integer, Integer> entry : mapA.entrySet()) {
			System.out.println(entry.getValue());
		}
	}

}
class MyConsumer implements Consumer<Integer>{

    public void accept(Integer t) {
        System.out.println("Consumer impl Value::"+t);
    }


}
