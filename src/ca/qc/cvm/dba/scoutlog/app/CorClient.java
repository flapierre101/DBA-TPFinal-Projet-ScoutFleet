package ca.qc.cvm.dba.scoutlog.app;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import ca.qc.cvm.dba.correctionserver.lib.BaseCorClient;
import ca.qc.cvm.dba.scoutlog.dao.LogDAO;
import ca.qc.cvm.dba.scoutlog.entity.LogEntry;

public class CorClient extends BaseCorClient  {
	
	public CorClient() {
	}
	
	@Override
	protected void executeTests(final List<String> res, List<String> info) {
		
		long count = -1;
		try {
			count = LogDAO.getNumberOfEntries();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("Nb fiches : " + count);
				
		boolean s1 = false;
		try {
			s1 = LogDAO.deleteAll();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("Suppression totale : " + s1);
				
		for (String line : info) {
			if (line.startsWith("RUN;")) {
				line = line.replace("RUN;", "");
				
				StringTokenizer tokenizer = new StringTokenizer(line, ":");
				String p1 = tokenizer.nextElement().toString();
				String p2 = tokenizer.nextElement().toString();
				String p3 = tokenizer.nextElement().toString();
				String p4 = tokenizer.nextElement().toString();
				String p5 = tokenizer.nextElement().toString();
				String p6 = tokenizer.nextElement().toString();
				String p7 = tokenizer.nextElement().toString();
				String p8 = tokenizer.nextElement().toString();
				
				if (p1.equals("-")) p1 = "";
				if (p2.equals("-")) p2 = "";
				if (p3.equals("-")) p3 = "";
				if (p4.equals("-")) p4 = "";
				if (p5.equals("-")) p5 = "";
				if (p6.equals("-")) p6 = "";
				if (p7.equals("-")) p7 = "";
				if (p8.equals("-")) p8 = "false";

				byte[] imageInByte = null;
				
				if (p7.length() > 0) {
					BufferedImage image;
					try {
						File f = new File(p7);
						image = ImageIO.read(f);
						ByteArrayOutputStream b =new ByteArrayOutputStream();
						ImageIO.write(image, "jpg", b );
						imageInByte = b.toByteArray();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				List<String> p9 = new ArrayList<String>();
				while (tokenizer.hasMoreElements()) {
					p9.add(tokenizer.nextElement().toString());
				}
				
				LogEntry p = new LogEntry(p1, p2, p3, p4, p9, p5, p6, imageInByte, Boolean.parseBoolean(p8));
				LogDAO.addLog(p);
			}
		}
		
		count = -1;
		try {
			count = LogDAO.getNumberOfEntries();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		res.add("Nb fiches (ap insertion) : " + count);
	}
}
