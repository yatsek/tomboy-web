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
	private TomboyService tomboyService;

	public TomboyWebApplication() {
		INSTANCE = this;
	}
	
	@Override
	protected void init() {
		super.init();
		String tomboyDirString = (String) getServletContext().getInitParameter("tomboy_dir");
		if (tomboyDirString == null) {
			tomboyDirString = ".tomboy";
		}
        tomboyService = new TomboyService(getRelatedFile(System.getProperty("user.home"), tomboyDirString));
	}

	/**
	 * Returns file if it is absolute, or file related to base if not absolute.
	 * @param base a directory path (absolute or relative)
	 * @param file a file path
	 * @return the result path
	 */
	private File getRelatedFile(String base, String file) {
		File path = new File(file);
		if (path.isAbsolute()) {
			return path;
		}
		return new File(base, path.toString());
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
