package edu.yu.cs.com1320.project.stage1.impl.DocumentImpl;

import java.io.File;

import java.io.FileInputStream;

import java.io.IOException;
import java.net.URI;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import edu.yu.cs.com1320.project.stage1.Document;

public class DocumentImpl implements Document {
	private URI uri;
	private String text;
	private int hash;
	private byte[] pdf;
	
	public DocumentImpl(URI uri, String txt, int txtHash){
		this.uri=uri;
		this.text=txt;
		this.hash=txtHash;
	}
	public DocumentImpl(URI uri, String txt, int txtHash, byte[] pdfBytes){
		this.uri=uri;
		this.text=txt;
		this.hash=txtHash;
		this.pdf=pdfBytes;
	}

	public byte[] getDocumentAsPdf() {
		if(this.pdf!=null) {
			return this.pdf;
		}
		String name = uri.toString();
		PDDocument doc = new PDDocument();
        try 
        {
            PDPage page = new PDPage();
            doc.addPage(page);
            
            PDFont font = PDType1Font.HELVETICA_BOLD;

            try (PDPageContentStream contents = new PDPageContentStream(doc, page))
            {
                contents.beginText();
                contents.setFont(font, 12);
                contents.newLineAtOffset(100, 700);
                contents.showText(this.text);
                contents.endText();
            }
            
            doc.save(name);
            
        }catch(Exception e){
        	e.printStackTrace();
        }
        File file = new File(name);
        
        byte[] bytesArray = new byte[(int) file.length()]; 

        FileInputStream fis;
		try {
			fis = new FileInputStream(file);
	        fis.read(bytesArray); //read file into bytes[]
	        fis.close();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bytesArray;
		
	}

	public String getDocumentAsTxt() {
		// TODO Auto-generated method stub
		//System.out.println(text);
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
