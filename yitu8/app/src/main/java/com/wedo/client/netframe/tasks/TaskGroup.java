package com.wedo.client.netframe.tasks;

import com.wedo.client.netframe.util.CollectionUtil;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author wxc
 *
 * @date 2013-5-30 上午10:27:09
 */
public class TaskGroup implements Runnable {
	
	private Collection<Future<?>> mustFutures = CollectionUtil.newArrayList();
	
	private Collection<Future<?>> mayFutures = CollectionUtil.newArrayList();
	
	private Collection<Runnable> mustTasks = CollectionUtil.newArrayList();
	
	private Collection<Runnable> mayTasks = CollectionUtil.newArrayList();
	
	private Collection<Callable<?>> mustCallTask = CollectionUtil.newArrayList();
	
	private ExecutorService executorService = Executors.newCachedThreadPool();

	
	public void addMust(Runnable mustTask) {
		mustTasks.add(mustTask);
//		Log.e("LOG_TAG",mustTasks.size()+"");
	}
	
	public void addMay(Runnable mayTask) {
		mayTasks.add(mayTask);
	}
	
	public void addMustFutres(Future<?> future){
		mustFutures.add(future);
	}
	
	public void addMayFutres(Future<?> future){
		mayFutures.add(future);
	}
	
	public void addCallable(Callable<?> call){
		mustCallTask.add(call);
	}
	
	@Override
	public void run() {
		for(Runnable mustTask : mustTasks) {
			mustFutures.add(executorService.submit(mustTask));

		}
		
		for(Runnable mayTask : mayTasks) {
			mayFutures.add(executorService.submit(mayTask));
		}
		
		for(Callable<?> call  :mustCallTask){
			executorService.submit(call);
		}
	}
	
	public void shutdown() {
		// 关闭线程池
		executorService.shutdown();
	}
	

}