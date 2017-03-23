package com.test.railway;


import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.JedisCluster;

public class WriteData {
	private static JedisCluster jc = null;
	private static int stopNum = 20; 
	private static int seatNum = 1000;
//	public static void main(String[] args) {
//		
//	}
	public static void Write() {
		System.out.println("写入初始铁路数据到Redis集群");
		JedisClusterClient jcClient = JedisClusterClient.getInstance();
		jcClient.configRedisCluster();
		jc = jcClient.jc;
		writeStopInfo();
		writeNumberInfo();
		System.out.println("数据写入完毕！");
	}
	
	private static void writeStopInfo() {
		for (int i = 0; i < stopNum; i++) {
			String stop = "stop:" + i;
			jc.hset(stop, "name", "S" + i);
			jc.hset(stop, "number", String.valueOf(i));
			jc.hset(stop, "leftTickets", String.valueOf(seatNum));
		}
	}
	
	private static void writeNumberInfo() {
		Map<String, Double> scoreMembers = new HashMap<String, Double>(){{
			for(int i = 0;i < stopNum;i++){
				String stop = "stop:" + i;
				put(stop, Double.valueOf(i));
			}
		}};
		jc.zadd("number:", scoreMembers);
	}
}
