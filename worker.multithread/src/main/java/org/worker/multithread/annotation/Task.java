package org.worker.multithread.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * @author Anish Singh 
 * 
 * This Annotations will be used for executing task on another thread. 
 * This should have to be used on methods of Objects.
 * 
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Task {

	public enum Type {
		ASYNCHRONOUS, SYNCHRONOUS
	}

	String taskName() default "NULL";

	Type type() default Type.ASYNCHRONOUS;

}
