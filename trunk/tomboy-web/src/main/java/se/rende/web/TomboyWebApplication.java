package se.rende.web;

import java.io.File;

import org.apache.wicket.protocol.http.WebApplication;

import se.rende.tomboy.TomboyService;


/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see se.rende.Start#main(String[])
 */
public class TomboyWebApplication extends WebApplication {
	private static TomboyWebApplication INSTANCE;
	private final TomboyService tomboyService;

	public TomboyWebApplication() {
		INSTANCE = this;
        tomboyService = new TomboyService(new File(System.getProperty("user.home"), ".tomboy"));
	}

	public Class<NoteListPage> getHomePage() {
		return NoteListPage.class;
	}

	public static TomboyWebApplication getInstance() {
		return INSTANCE;
	}

	public TomboyService getTomboyService() {
		return tomboyService;
	}

}
