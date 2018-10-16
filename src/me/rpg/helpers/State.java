package me.rpg.helpers;

/**
 * An enum for different results of methods.
 * Is used to find out how the method execution turned out.
 * @author kleinesfilmröllchen
 * @since 0.0.0004
 * @version 0.0.0004
 */
public enum State {
	/**The finished state. Methods return this to indicate a completely normal execution.*/
	finished,
	/**The failed state. Methods return this when something goes wrong but an exception throw is not necessary.*/
	failed,
	/**The not allowed state. Methods return this to indicate that the execution was not allowed due to gameplay rules.<br>A message should be supplied to inform the user why the execution wasn't allowed.*/
	notAllowed,
	/**The early exit state. Methods return this to indicate an early exit out of the method. Sort of a minor fail.*/
	earlyExit,
}
