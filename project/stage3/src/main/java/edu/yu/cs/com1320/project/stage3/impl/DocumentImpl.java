package edu.yu.cs.com1320.project.stage3.impl;

import java.io.ByteArrayOutputStream;



import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import edu.yu.cs.com1320.project.stage3.Document;

public class DocumentImpl implements Document {
	private URI uri;
	private String text;
	private int hash;
	private byte[] pdf;
	private Map<String,Integer> theWords;

	public DocumentImpl(URI uri, String txt, int txtHash) {
		this.uri = uri;
		this.text = txt;
		makeTextMap(txt);
		this.hash = txtHash;
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
			pdf= returns.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void makeTextMap(String txt) {
		String words = txt;
		words =words.toUpperCase();
		words= words.replaceAll("[^A-Za-z0-9 ]", "");
		
		this.theWords = new HashMap<>();
        String[] inputWords = words.split(" ");
        for(String inputWord : inputWords){
            inputWord = inputWord.trim();
            if(this.theWords.containsKey(inputWord)){
                this.theWords.put(inputWord,this.theWords.get(inputWord)+1);
            }else{
                this.theWords.put(inputWord,1);
            }
		
        }
	}



	public DocumentImpl(URI uri, String txt, int txtHash, byte[] pdfBytes) {
		this.uri = uri;
		this.text = txt;
		makeTextMap(txt);
		this.hash = txtHash;
		this.pdf = pdfBytes;
	}

	public byte[] getDocumentAsPdf() {
			return this.pdf;
	 
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


	public int wordCount(String word) {
		word = word.toUpperCase();
        if(this.theWords.containsKey(word)){
            return this.theWords.get(word);
        }
        return 0;
	}

}
