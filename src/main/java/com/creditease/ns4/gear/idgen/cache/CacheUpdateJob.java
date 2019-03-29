package com.creditease.ns4.gear.idgen.cache;

import com.creditease.ns4.gear.idgen.common.NsLogger;

import java.util.concurrent.*;

/**
 * 更新缓存任务
 */
public class CacheUpdateJob extends Thread {

	private volatile static CacheUpdateJob job;
	/**
	 * 提交队列
	 */
	private static BlockingQueue<KeyInfo>  queue = new ArrayBlockingQueue<>(200);
	private static ExecutorService pool = Executors.newFixedThreadPool(200);
	/**
	 * 停止标志
	 */
	private volatile static Boolean shutDown = Boolean.FALSE;
	
	private CacheUpdateJob() {
	}
	
	 /**
	  * 获取更新缓存任务的单例
	 * @return jobInstance
	 */
	public static CacheUpdateJob getInstance() {  
		if (job == null) {
			//防止重复初始化
			 synchronized (CacheUpdateJob.class) {  
				 if (job == null) {
					 job = new CacheUpdateJob();
				 }
			 }
		} 
		return job;
	 }

	@Override
	public void run() {
		//循环获取队列中的数据
		while(!shutDown) {
			try {
				pool.submit(queue.take());
			} catch (Exception e) {
				NsLogger.logger.error("更新任务异常", e);
			}
		}
	}
	
	/**
	 * 提交
	 * @param keyInfo keyInfo
	 */
	public void submitJob(KeyInfo keyInfo) {
		NsLogger.logger.info("提交更新任务:{}", keyInfo);
		if (keyInfo != null && !shutDown) {
			try {
				queue.put(keyInfo);
			} catch (Exception e) {
				NsLogger.logger.error("提交更新任务异常", e);
			}
		}
	}
	
	/**
	 * 停止
	 */
	public void shutDown() {
		NsLogger.logger.info("停止任务");
		//设置停止标志
		shutDown = true;
		//终止线程池
		pool.shutdown();
	}
}
