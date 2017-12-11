/**
 * 
 */
package top.lmoon.baiducloud.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author LMoon
 * @date 2017年10月9日
 * 
 */
public class ThreadPool {

	public static ExecutorService commonThreadPool = Executors.newFixedThreadPool(10);
	
	public static ExecutorService downloadThreadPool = Executors.newFixedThreadPool(3);

}
