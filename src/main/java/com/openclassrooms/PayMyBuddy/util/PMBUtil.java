package com.openclassrooms.PayMyBuddy.util;

import java.nio.charset.Charset;

import org.springframework.http.MediaType;

/**
 * PMBUtil is an entity that handles constants used throughout the App
 */
public class PMBUtil {
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	public static final double MinimumAmountTransaction = 5;
}
