package com.test.railway;



import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class JedisClusterClient {
	private static int count = 0;
	 
    private static final JedisClusterClient redisClusterClient = new JedisClusterClient();
 
    public JedisCluster jc = null;
    /**
     * ˽�й��캯��
     */
    private JedisClusterClient() {
    	
    }
 
    public static JedisClusterClient getInstance() {
        return redisClusterClient;
    }
 
    private JedisPoolConfig getPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(1000);
        config.setMaxIdle(100);
        config.setTestOnBorrow(true);
        return config;
    }
 
    public void configRedisCluster() {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("10.129.247.235", 7000));
        jedisClusterNodes.add(new HostAndPort("10.129.247.235", 7001));
        jedisClusterNodes.add(new HostAndPort("10.129.247.235", 7002));
        jedisClusterNodes.add(new HostAndPort("10.129.247.236", 7003));
        jedisClusterNodes.add(new HostAndPort("10.129.247.236", 7004));
        jedisClusterNodes.add(new HostAndPort("10.129.247.236", 7005));
 
        jc = new JedisCluster(jedisClusterNodes,getPoolConfig());
        Date now = new Date(); 
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ");//���Է�����޸����ڸ�ʽ
        String nowStr = dateFormat.format( now ); 
        jc.set("cluster", nowStr + "Redis ��Ⱥ��������");
        String result = jc.get("cluster");
        System.out.println(result);
        jc.del("cluster");
    }
 
    public static void main(String[] args) {
        JedisClusterClient jedisClusterClient = JedisClusterClient.getInstance();
        jedisClusterClient.configRedisCluster();
    }
}
