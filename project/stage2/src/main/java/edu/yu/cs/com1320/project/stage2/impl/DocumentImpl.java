package edu.yu.cs.com1320.project.stage2.impl;

import java.io.ByteArrayOutputStream;


import java.io.IOException;
import java.net.URI;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import edu.yu.cs.com1320.project.stage2.Document;

public class DocumentImpl implements Document {
	private URI uri;
	private String text;
	private int hash;
	private byte[] pdf;

	public DocumentImpl(URI uri, String txt, int txtHash) {
		this.uri = uri;
		this.text = txt;
		this.hash = txtHash;
	}

	public DocumentImpl(URI uri, String txt, int txtHash, byte[] pdfBytes) {
		this.uri = uri;
		this.text = txt;
		this.hash = txtHash;
		this.pdf = pdfBytes;
	}

	public byte[] getDocumentAsPdf() {
		if (this.pdf != null) {
			return this.pdf;
		}
		PDDocument doc = new PDDocument();
		try {
			PDPage page = new PDPage();
			doc.addPage(page);

			PDFont font = PDType1Font.HELVETICA_BOLD;

			PDPageContentStream contents = new PDPageContentStream(doc, page);

			contents.beginText();
			contents.setFont(font, 12);
			contents.newLineAtOffset(100, 700);
			contents.showText(this.text);
			contents.endText();
			contents.close();
			ByteArrayOutputStream returns = new ByteArrayOutputStream();
			doc.save(returns);
			doc.close();
			return returns.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public String getDocumentAsTxt() {
		// TODO Auto-generated method stub
		// System.out.println(text);
		return text;
	}

	public int getDocumentTextHashCode() {
		// TODO Auto-generated method stub
		return hash;
	}

	public URI getKey() {
		// TODO Auto-generated method stub
		return uri;
	}

}
