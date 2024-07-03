/**
 * 
 */
package com.kyj.fx.nightmare.comm;

/**
 * 
 */
@FunctionalInterface
public interface FourThFunction<T, U, V, W, R> {
	public R apply(T t, U u, V v, W w);
}
