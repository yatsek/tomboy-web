package se.rende;

import junit.framework.TestCase;
import org.apache.wicket.util.tester.WicketTester;

import se.rende.web.NoteListPage;
import se.rende.web.TomboyWebApplication;

/**
 * Simple test using the WicketTester
 */
public class TestHomePage extends TestCase
{
	private WicketTester tester;

	@Override
	public void setUp()
	{
		tester = new WicketTester(new TomboyWebApplication());
	}

	public void testRenderMyPage()
	{
		//start and render the test page
		tester.startPage(NoteListPage.class);

		//assert rendered page class
		tester.assertRenderedPage(NoteListPage.class);

		//assert rendered label component
		//tester.assertLabel("message", "If you see this message wicket is properly configured and running");
	}
}
