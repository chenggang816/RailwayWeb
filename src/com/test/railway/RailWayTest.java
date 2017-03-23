package com.test.railway;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.xml.crypto.Data;

import redis.clients.jedis.JedisCluster;

public class RailWayTest {
	int stopNum = 20; //站点数量
	int seatNum = 1000;
	String resultInfo = "";
	JedisCluster jc = null;
	public String TestRequst(int reqNum) {		
		JedisClusterClient jedisClusterClient = JedisClusterClient.getInstance();
        jedisClusterClient.configRedisCluster();
		jc = jedisClusterClient.jc;		
		
		String resultInfo = Format("---测试开始---<br />");
		long startTime = System.currentTimeMillis();
		
		Random random = new Random(stopNum - 1);
		int successTime = 0; //购票成功的请求次数
		for (int i = 0; i < reqNum; i++) {			
			int start = random.nextInt(stopNum - 1);
			int end =random.nextInt(stopNum - start) + start;	
			Map<String, String> startStopInfo = jc.hgetAll("stop:" + start);
			Map<String, String> endStopInfo = jc.hgetAll("stop:" + end);
			String sectionString = startStopInfo.get("name") + "-" + endStopInfo.get("name");
			Set<String> stops = jc.zrange("number:", start, end);
			boolean hasLeftTickets = true;
			for(String stop:stops){
				Map<String, String> stopInfo = jc.hgetAll(stop);
				if(Integer.valueOf(stopInfo.get("leftTickets")) <= 0){
					hasLeftTickets = false;
					break;
				}
			}
			if(hasLeftTickets){
				for(String stop:stops){
					jc.hincrBy(stop, "leftTickets", -1);
				}
				successTime++;
				if(i < 50){
					resultInfo += Format(sectionString + ":\t购票成功！");
				}
			}
			else {
				if(i < 50){
					resultInfo += Format(sectionString + ":\t购票失败！此区间无余票！");
				}
			}
		}
		
		long endTime = System.currentTimeMillis();
		long costTime = endTime - startTime;
		resultInfo += Format();
		resultInfo += Format("共生成 " + reqNum + " 个随机购票请求");
		resultInfo += Format("购票成功请求数: " + successTime);
		resultInfo += Format("购票失败（余票不足）请求数： " + (reqNum - successTime));
		resultInfo += Format();
		resultInfo += Format("各站点剩余座位数: ");
		for (int i = 0; i < stopNum; i++) {
			Map<String, String> stopMap = jc.hgetAll("stop:" + i);
			resultInfo += ("<span style=\"margin-left:140px\">"
					+ stopMap.get("name") + " ： " + stopMap.get("leftTickets") + "</span><br />");
		}
		resultInfo += Format();
		resultInfo += Format("总共用时：" + String.valueOf(costTime / 1000.0) + " s");
		resultInfo += Format();
		resultInfo += "---测试结束---";
		return resultInfo;
	}
	
	private String Format(String info) {
		if(info == null || info.equals("")){
			return "<br />";
		}
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		String nowString = dateFormat.format(now);
		return nowString + "： "  +  info + "<br />";
	}
	private String Format() {
		return Format("");
	}
}
