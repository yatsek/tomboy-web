package se.rende.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.xml.sax.SAXException;

import se.rende.tomboy.NoteRef;
import se.rende.tomboy.TomboyService;


/**
 * List of tomboy notes, clickable to view the note contents.
 */
public class NoteListPage extends WebPage {
	private static final long serialVersionUID = 1L;
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");

    /**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
     * @throws ParseException 
     * @throws IOException 
     * @throws SAXException 
     * @throws ParserConfigurationException 
	 */
    public NoteListPage(final PageParameters parameters) throws ParserConfigurationException, SAXException, IOException, ParseException {
        TomboyService tomboyService = TomboyWebApplication.getInstance().getTomboyService();
		List<NoteRef> notes = new ArrayList<NoteRef>(tomboyService.getNotes());
		Collections.sort(notes, new Comparator<NoteRef>() {
			public int compare(NoteRef o1, NoteRef o2) {
				return o1.getChangeDate().compareTo(o2.getChangeDate()) * -1;
			}	
		});
		final DataView<NoteRef> table = new DataView<NoteRef>("noteTable", new ListDataProvider<NoteRef>(notes)) {
			private static final long serialVersionUID = 1L;

			public void populateItem(final Item<NoteRef> item) {
                item.add(new Label("date", dateFormat.format(item.getModelObject().getChangeDate())));
                ExternalLink link = new ExternalLink("noteLink", "view-note?note=" + item.getModelObject().getFile());
				item.add(link);
                link.add(new Label("title", item.getModelObject().getTitle()));
            }
        };
        add(table);
    }
}
