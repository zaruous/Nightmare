/**
 * 
 */
package com.kyj.fx.nightmare.actions.comm.ai;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;

/**
 * 
 */
public abstract class AbstractCodeRunner implements Runnable {

	private String codeType;

	public AbstractCodeRunner(String codeType) {
		this.codeType = codeType;
	}

	public AbstractCodeRunner codeType(String codeType) {
		this.codeType = codeType;
		return this;
	}

	public ProcessExecutor createDefaultProcessExecutor()
			throws InvalidExitValueException, IOException, InterruptedException, TimeoutException {
		return new ProcessExecutor();
	}
}
