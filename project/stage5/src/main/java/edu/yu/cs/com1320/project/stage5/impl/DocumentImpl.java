package edu.yu.cs.com1320.project.stage5.impl;

import java.io.ByteArrayOutputStream;




import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import edu.yu.cs.com1320.project.stage5.Document;
//import edu.yu.cs.com1320.project.stage5.impl.DocumentStoreImpl.MinFile;

public class DocumentImpl implements Document {
	private URI uri;
	private String text;
	private int hash;
	private byte[] pdf;
	private Map<String,Integer> theWords;
	private long time;
	private DocumentStoreImpl.MinFile minfile;



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
		long now = System.nanoTime();
		this.time = now;
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
		long now = System.nanoTime();
		this.time = now;
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

	public DocumentImpl(String txt, String newuri, int newhash, HashMap<String, Integer> map) {
		// TODO Auto-generated constructor stub
		this.text=txt;
		try {
			this.uri= new URI(newuri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.hash=newhash;
		this.theWords=map;
		long now = System.nanoTime();
		this.time = now;
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

	@Override
	public long getLastUseTime() {
		// TODO Auto-generated method stub
		return this.time;
	}

	@Override
	public void setLastUseTime(long timeInNanoseconds) {
		// TODO Auto-generated method stub
		this.time = timeInNanoseconds;
	}

	@Override
	public int compareTo(Document doc2) {
		// TODO Auto-generated method stub
		System.out.println("doc time: " + getLastUseTime() + ", doc2 time: " +doc2.getLastUseTime());
		if(time > doc2.getLastUseTime()) {
			return 1;
		}
		if(time < doc2.getLastUseTime()) {
			return -1;
		}
		return 0;
	}

	@Override
	public Map<String, Integer> getWordMap() {
		// TODO Auto-generated method stub
		return theWords;
	}

	@Override
	public void setWordMap(Map<String, Integer> wordMap) {
		// TODO Auto-generated method stub
		this.theWords=wordMap;
	}
	
	protected DocumentStoreImpl.MinFile getMinfile() {
		return minfile;
	}

	protected void setMinfile(DocumentStoreImpl.MinFile min) {
		this.minfile = min;
	}

}
