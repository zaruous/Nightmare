/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2016. 12. 27.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.nightmare;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author KYJ
 *
 */
public class ThreadUtil {

	/**
	 * 스레드 그룹.
	 * 
	 * @최초생성일 2016. 12. 27.
	 */
	private static ThreadGroup threadGroup;

//	static {
//		threadGroup = new ThreadGroup("Gargoyle-ThreadUtil");
//		threadGroup.setDaemon(true);
//	}

	public static void createNewThreadAndRun(Runnable r) {
		createNewThread("Gragoyle-ThreadUtil-Default", r).start();
	}

	public static void createNewThreadAndRun(String name, Runnable r) {
		createNewThread(name, r).start();
	}

	public static Thread createNewThread(String name, Runnable r) {
		Thread thread = new Thread(getGroup(), r, name);
		return thread;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 9. 18. 
	 * @return
	 */
	private static synchronized ThreadGroup getGroup() {
		
		if (threadGroup == null /*|| threadGroup.isDestroyed()*/) {
			threadGroup = new ThreadGroup("Gargoyle-ThreadUtil");
//			threadGroup.setDaemon(true);
		}
		return threadGroup;

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 8.
	 * @return
	 */
	public static String getAllStackTrace() {

		Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
		Optional<String> r = allStackTraces.entrySet().stream().map(ent -> {

			Thread key = ent.getKey();
			StackTraceElement[] value = ent.getValue();

			String threadName = key.getName();
			Optional<String> reduce = Stream.of(value).map(a -> {

				return String.format("%s %s %s", a.getClassName(), a.getMethodName(), a.getLineNumber());

			}).reduce((str1, str2) -> {
				return str1.concat("\n").concat(str2);
			});
			// String.format("Thread Name : %s", args)
			if (reduce.isPresent()) {
				return threadName.concat("############## \n").concat(reduce.get());
			}
			return threadName;
		}).reduce((str1, str2) -> str1.concat("\n").concat(str2));

		if (r.isPresent())
			return r.get();
		return "";
	}

}
