package se.rende.tomboy;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



public class TomboyService {
	private static DateFormat noteDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ");
	private final File tomboyDir;
	
	/**
	 * A service object with the specified tomboy folder (normally ~/.tomboy), or tomboy sync folder.
	 * @param file
	 */
	public TomboyService(File file) {
		this.tomboyDir = file;
	}

	/**
	 * Returns a list of note references in the current tomboy folder.
	 * @return
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public List<NoteRef> getNotes() throws ParserConfigurationException, SAXException, IOException, ParseException {
		List<File> files = getNoteFiles();
		List<NoteRef> notes = new ArrayList<NoteRef>();
		for (File file : files) {
			if (file.getName().endsWith(".note")) {
				SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
				FileInputStream is = new FileInputStream(new File(tomboyDir, file.toString()));
				NoteDocumentHandler noteDocumentHandler = new NoteDocumentHandler();
				parser.parse(is, noteDocumentHandler);
				is.close();
				String dateText = noteDocumentHandler.getChangeDateText();
				String normalizedDateText = dateText.substring(0, dateText.length() - 3) // remove colon in time zone offset
				+ dateText.substring(dateText.length() - 2);
				Date changeDate = noteDateFormat.parse(normalizedDateText);

				notes.add(new NoteRef(noteDocumentHandler.getTitle(), changeDate, file));
			}
		}
		return notes;
	}
	
	/**
	 * Returns a list of note files at tomboyDir, regardless if it is a normal or sync folder.
	 * @return the note files
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private List<File> getNoteFiles() throws ParserConfigurationException, SAXException, IOException {
		// parse top level manifest.xml, determine if normal or sync folder and get note file paths accordingly
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		FileInputStream is = new FileInputStream(new File(tomboyDir, "manifest.xml"));
		ManifestDocumentHandler manifestDocumentHandler = new ManifestDocumentHandler();
		parser.parse(is, manifestDocumentHandler);
		is.close();
		return manifestDocumentHandler.getNoteFiles();
	}

	/**
	 * Format the note file into HTML and write to the output stream.
	 * @param os
	 * @param noteFile
	 * @throws TransformerException
	 */
	public void writeNoteHtml(OutputStream os, File noteFile)
			throws TransformerException {

		TransformerFactory tFactory = TransformerFactory.newInstance();

		Transformer transformer = tFactory.newTransformer(new StreamSource(getClass().getResourceAsStream("ExportToHtml.xsl")));

		transformer.transform(
				new StreamSource(noteFile), 
				new StreamResult(os));

	}
	
	/**
	 * Catch path of all note files relative to tomboyDir from the parsed manifest.xml.
	 * @author dag
	 *
	 */
	private final class ManifestDocumentHandler extends DefaultHandler {
		private boolean foundDocumentElement = false;
		private boolean isSyncDir = false;
		private boolean foundNotesElement = false;
		private boolean foundNotesEndElement = false;
		
		private List<File> noteFiles = new ArrayList<File>();

		@Override
		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException {
			if (!foundNotesEndElement) {
				if (foundDocumentElement) {
					if (foundNotesElement) {
						if (name.equals("note")) {
							if (isSyncDir) {
								noteFiles.add(new File("0/" + attributes.getValue("rev") + "/" + attributes.getValue("id") + ".note"));
							} else {
								noteFiles.add(new File(attributes.getValue("guid") + ".note"));
							}
						}
					} else {
						if (!isSyncDir) {
							if (name.equals("note-revisions")) {
								foundNotesElement = true;
							}
						}
					}
				} else {
					if (name.equals("manifest")) {
						foundDocumentElement = true;
					} else if (name.equals("sync")) {
						isSyncDir = true;
						foundDocumentElement = true;
						foundNotesElement = true;
					}
				}
			}
		}
		
		@Override
		public void endElement(String uri, String localName, String name)
				throws SAXException {
			if (name.equals("note-revisions") || name.equals("sync")) {
				foundNotesEndElement = true;
			}
		}

		public List<File> getNoteFiles() {
			return noteFiles;
		}

	}
	
	/**
	 * Catch the title and last-change-date element contents in the parsed note document.
	 */
	private final class NoteDocumentHandler extends DefaultHandler {
		private String title = null;
		private String changeDateText = null;
		private boolean catchTitle = false;
		private boolean catchChangeDate = false;

		@Override
		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException {
			if (name.equals("title")) {
				catchTitle = true;
			} else if (name.equals("last-change-date")) {
				catchChangeDate = true;
			} else {
				catchTitle = false;
				catchChangeDate = false;
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (catchTitle) {
				title = new String(ch, start, length);
			} else if (catchChangeDate) {
				changeDateText = new String(ch, start, length);
			}
			catchTitle = false;
			catchChangeDate = false;
		}

		public String getTitle() {
			return title;
		}

		public String getChangeDateText() {
			return changeDateText;
		}
	}

	/**
	 * Returns the tomboy folder (ordinary or sync folder).
	 * @return
	 */
	public File getTomboyDir() {
		return tomboyDir;
	}

}
