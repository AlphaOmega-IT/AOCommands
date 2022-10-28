package de.alphaomega.it.cmdHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
	
	String name();
	
	String permission() default "";

	String noPerm() default "You do not have the permissions to execute this command";

	String[] aliases() default {};
	
	String description() default "";
	
	String usage() default "";
	
	boolean inGameOnly() default true;
	
}