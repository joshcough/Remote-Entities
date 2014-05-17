package com.joshcough.remoteentities.exceptions;

public class NotRideableException extends RuntimeException
{
	public NotRideableException()
	{
		super("The entity can't be ridden.");
	}
}