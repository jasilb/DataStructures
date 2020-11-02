package edu.yu.cs.com1320.project.stage4.impl;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Test;

import edu.yu.cs.com1320.project.stage4.DocumentStore.DocumentFormat;

public class Stage4Test {
	protected InputStream MakePdf(String filename, String message)throws IOException {
		
		PDDocument doc = new PDDocument();
        try 
        {
        	
            PDPage page = new PDPage();
            doc.addPage(page);
            
            PDFont font = PDType1Font.HELVETICA_BOLD;

             PDPageContentStream contents = new PDPageContentStream(doc, page);
            
                contents.beginText();
                contents.setFont(font, 12);
                contents.newLineAtOffset(100, 700);
                contents.showText(message);
                contents.endText();
                contents.close();
               
           
           
        }catch(Exception e){
        	e.printStackTrace();
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        doc.save(outputStream);
        doc.close();
        InputStream targetStream = new ByteArrayInputStream(outputStream.toByteArray());
       
 
		return targetStream;
		
	}
	protected InputStream MakeText( String filename, String message)throws IOException {
		
		

	    InputStream targetStream = new ByteArrayInputStream(message.getBytes());
		return targetStream;
		
	}
	
	protected String PdfToString(byte[] pdf) {
		String paper = null;

		PDDocument document = new PDDocument();
		try {
			document.close();
			document = PDDocument.load(pdf);
			PDFTextStripper stripper = new PDFTextStripper();
			paper = stripper.getText(document);
			
			//System.out.println("words: " + paper.trim());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return paper.trim();
		
	}
	@Test
	public void putTest1() throws IOException {
		int docs =0;
		int bytes=0;
		DocumentStoreImpl store1 = new DocumentStoreImpl();
		String filename1 = "test1";
        String message1 = "one a";

        String filename2 = "test2";
        String message2 = "two a a";
        
        String filename3 = "test3";
        String message3 = "three a a a";
        
        String filename4 = "test4";
        String message4 = "four a a a a";
        
        String filename5 = "test5";
        String message5 = "none of those words";
        
        InputStream pdf1 = MakePdf(filename1, message1);
        InputStream pdf2 = MakePdf(filename2, message2);
        InputStream txt1 =MakeText (filename3,message3);
        InputStream txt2 =MakeText (filename4,message4);
        InputStream txt3 =MakeText (filename5,message5);
        URI a = URI.create(filename1);
        URI b = URI.create(filename2);
        URI c = URI.create(filename3);
        URI d = URI.create(filename4);
        URI e = URI.create(filename5);
        System.out.println("adding documents");
        store1.putDocument(pdf1, a, DocumentFormat.PDF);
        pdf1.close();
       // System.out.println("PDF1, "+ ++docs +", " + (bytes+= (store1.getDocumentAsPdf(a).length + store1.getDocumentAsTxt(a).length())));
        store1.putDocument(pdf2, b, DocumentFormat.PDF);
        pdf2.close();
       // System.out.println("PDF2, "+ ++docs +", " + (bytes+= (store1.getDocumentAsPdf(b).length + store1.getDocumentAsTxt(b).length())));
        store1.putDocument(txt1, c, DocumentFormat.TXT);
        txt1.close();
       // System.out.println("TXT1, "+ ++docs +", " + (bytes+= (store1.getDocumentAsPdf(c).length + store1.getDocumentAsTxt(c).length())));
        store1.putDocument(txt2, d, DocumentFormat.TXT);
        txt2.close();
      //  System.out.println("TXT2, "+ ++docs +", " + (bytes+= (store1.getDocumentAsPdf(d).length + store1.getDocumentAsTxt(d).length() )));
        store1.putDocument(txt3, e, DocumentFormat.TXT);
        txt2.close();
       // System.out.println("TXT3, "+ ++docs +", " + (bytes+= (store1.getDocumentAsPdf(e).length + store1.getDocumentAsTxt(e).length() )));
        store1.getDocumentAsTxt(a);
        } 


	@Test
public void maxTest1() throws IOException {
	int docs =0;
	int bytes=0;
	DocumentStoreImpl store1 = new DocumentStoreImpl();
	store1.setMaxDocumentCount(3);
	String filename1 = "test1";
    String message1 = "one a";

    String filename2 = "test2";
    String message2 = "two a a";
    
    String filename3 = "test3";
    String message3 = "three a a a";
    
    String filename4 = "test4";
    String message4 = "four a a a a";
    
    String filename5 = "test5";
    String message5 = "none of those words";
    
    InputStream pdf1 = MakePdf(filename1, message1);
    InputStream pdf2 = MakePdf(filename2, message2);
    InputStream txt1 =MakeText (filename3,message3);
    InputStream txt2 =MakeText (filename4,message4);
    InputStream txt3 =MakeText (filename5,message5);
    URI a = URI.create(filename1);
    URI b = URI.create(filename2);
    URI c = URI.create(filename3);
    URI d = URI.create(filename4);
    URI e = URI.create(filename5);
    System.out.println(" ");
    System.out.println("adding documents2 ");
    System.out.println("pdf1: ");
    store1.putDocument(pdf1, a, DocumentFormat.PDF);
    pdf1.close();
    //System.out.println("PDF1, "+ ++docs +", " + (bytes+= (store1.getDocumentAsPdf(a).length + store1.getDocumentAsTxt(a).length())));
    System.out.println("pdf2: ");
    store1.putDocument(pdf2, b, DocumentFormat.PDF);
    pdf2.close();
    //System.out.println("PDF2, "+ ++docs +", " + (bytes+= (store1.getDocumentAsPdf(b).length + store1.getDocumentAsTxt(b).length())));
    System.out.println("txt1: ");
    store1.putDocument(txt1, c, DocumentFormat.TXT);
    txt1.close();
   // System.out.println("TXT1, "+ ++docs +", " + (bytes+= (store1.getDocumentAsPdf(c).length + store1.getDocumentAsTxt(c).length())));
    System.out.println("txt2: ");
    store1.putDocument(txt2, d, DocumentFormat.TXT);
    txt2.close();
    //System.out.println("TXT2, "+ ++docs +", " + (bytes+= (store1.getDocumentAsPdf(d).length + store1.getDocumentAsTxt(d).length() )));
    System.out.println("txt3: ");
    store1.putDocument(txt3, e, DocumentFormat.TXT);
    txt3.close();
    //System.out.println("TXT3, "+ ++docs +", " + (bytes+= (store1.getDocumentAsPdf(e).length + store1.getDocumentAsTxt(e).length() )));
    //store1.getDocumentAsTxt(a);
    } 
	
	@Test
	public void UndoHeapSearchTest1() throws IOException{
		DocumentStoreImpl store1 = new DocumentStoreImpl();
		String filename1 = "test1";
        String message1 = "one a";

        String filename2 = "test2";
        String message2 = "one a a";
        
        String filename3 = "test3";
        String message3 = "three a a a";
        
        String filename4 = "test4";
        String message4 = "four a a a a";
        
        String filename5 = "test5";
        String message5 = "none of those words";
        
        String filename6 = "test6";
        String message6 = "abc";
        String filename7 = "test7";
        String message7 = "abc abcd";
        String filename8 = "test8";
        String message8 = "aa bb aabb";
        String filename9 = "test9";
        String message9 = "a ab abc abcd abcde";
        
        
        InputStream pdf1 = MakePdf(filename1, message1);
        InputStream pdf2 = MakePdf(filename2, message2);
        InputStream txt1 =MakeText (filename3,message3);
        InputStream txt2 =MakeText (filename4,message4);
        InputStream txt3 =MakeText (filename5,message5);
        InputStream txt4 =MakeText (filename6,message6);
        InputStream txt5 =MakeText (filename7,message7);
        InputStream txt6 =MakeText (filename8,message8);
        InputStream txt7 =MakeText (filename9,message9);
        URI a = URI.create(filename1);
        URI b = URI.create(filename2);
        URI c = URI.create(filename3);
        URI d = URI.create(filename4);
        URI e = URI.create(filename5);
        URI f = URI.create(filename6);
        URI g = URI.create(filename7);
        URI h = URI.create(filename8);
        URI i = URI.create(filename9);
        
        //adding documents
        store1.putDocument(pdf1, a, DocumentFormat.PDF);
        pdf1.close();
        store1.putDocument(pdf2, b, DocumentFormat.PDF);
        pdf2.close();
        List<String> list = store1.search("one");
        store1.putDocument(txt1, c, DocumentFormat.TXT);
        txt1.close();
        store1.putDocument(txt2, d, DocumentFormat.TXT);
        txt2.close();
        store1.putDocument(txt3, e, DocumentFormat.TXT);
        txt3.close();
        store1.putDocument(txt4, f, DocumentFormat.TXT);
        txt4.close();
        store1.putDocument(txt5, g, DocumentFormat.TXT);
        txt5.close();
        store1.putDocument(txt6, h, DocumentFormat.TXT);
        txt6.close();
        store1.putDocument(txt7, i, DocumentFormat.TXT);
        txt7.close();
        assertEquals(2,list.size());
        System.out.println("List:");
        for(String words: list) {
        	System.out.println(words);
        }
        System.out.println("set doc count");
        store1.setMaxDocumentCount(8);
        store1.undo(a);
        
	}
}
