package se.rende.web;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;

import se.rende.tomboy.TomboyService;

public class NoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		ServletOutputStream os = resp.getOutputStream();
		TomboyService tomboyService = TomboyWebApplication.getInstance().getTomboyService();
		try {
			String note = req.getParameter("note");
			if (!note.endsWith(".note") 
					|| note.substring(0, note.length() - 5).contains(".")) {
				throw new ServletException("illegal parameter: note=" + note);
			}
			tomboyService.writeNoteHtml(os, new File(tomboyService.getTomboyDir(), note));
		} catch (TransformerException e) {
			throw new ServletException(e);
		}
		os.close();
	}
}
