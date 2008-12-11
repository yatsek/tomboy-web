package se.rende.tomboy;
import java.io.File;
import java.io.Serializable;
import java.util.Date;


public class NoteRef implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String title;
	private final Date changeDate;
	private final File file;

	public NoteRef(String title, Date changeDate, File file) {
		this.title = title;
		this.changeDate = changeDate;
		this.file = file;
	}

	public String getTitle() {
		return title;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public File getFile() {
		return file;
	}

}
