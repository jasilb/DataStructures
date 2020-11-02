package edu.yu.cs.com1320.project.stage5.impl;

import static org.junit.Assert.*;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Before;
import org.junit.Test;

import edu.yu.cs.com1320.project.Utils;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import edu.yu.cs.com1320.project.stage5.DocumentStore.DocumentFormat;

public class Stage5Test {
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
	public void putdoc1() throws IOException {
		//tests if make custom file location
		//test to see if it can read from json file and delete the empty file
		File x =new File("home/jake/Downloads/stage5/test1");
		DocumentStoreImpl IHopeThisWorks = new DocumentStoreImpl(x);
		IHopeThisWorks.setMaxDocumentCount(2);
		String filename1 = "test1";
        String message1 = "making a pdf1";
        String filename2 = "test2";
        String message2 = "making a text";
        String filename3 = "test3";
        String message3 = "making a pdf2";
        String filename4 = "test4";
        String message4 = "making a pdf3";
        InputStream pdf1 = MakePdf(filename1, message1);
        InputStream newPdf =MakePdf(filename4, message4);
        InputStream pdf2 = MakePdf(filename3, message3);
        InputStream txt1 =MakeText (filename2,message2);
        InputStream fake;
        URI a = URI.create(filename1);
        URI b = URI.create(filename2);
        URI c = URI.create(filename3);
        URI d = URI.create("fake");
        //adding documents
        System.out.println("put pdf1");
        IHopeThisWorks.putDocument(pdf1, a, DocumentFormat.PDF);
        System.out.println("put pdf2");
        IHopeThisWorks.putDocument(pdf2, c, DocumentFormat.PDF);
        System.out.println("put txt1");
        IHopeThisWorks.putDocument(txt1, b, DocumentFormat.TXT);
        
        pdf1.close();
        
        txt1.close();
        System.out.println(" ");
        System.out.println("get pdf1");
        
        //adding new document with same uri
        System.out.println(" ");
        System.out.println("replace pdf1");
        //IHopeThisWorks.putDocument(newPdf, a, DocumentFormat.PDF);
        newPdf.close();
        //getting documents
        System.out.println(" ");
        System.out.println("get new pdf1");
      // assertEquals(message4, IHopeThisWorks.getDocumentAsTxt(a));
       System.out.println(" ");
       System.out.println("put fake");
       assertEquals(null, IHopeThisWorks.getDocumentAsTxt(d));
       assertEquals(null, IHopeThisWorks.getDocumentAsPdf(d));
       assertEquals(false, IHopeThisWorks.deleteDocument(d));
       System.out.println(" ");
       System.out.println("get txt1");
       assertEquals(message2, IHopeThisWorks.getDocumentAsTxt(b));
       System.out.println(" ");
       System.out.println("get pdf2");
       assertEquals(message3, IHopeThisWorks.getDocumentAsTxt(c));
       System.out.println(" ");
       System.out.println("get txt1");
       assertEquals(IHopeThisWorks.getDocumentAsTxt(b), PdfToString(IHopeThisWorks.getDocumentAsPdf(b)));	
       // deleting documents

    
       System.out.println(" ");
       System.out.println("delete txt1");
        IHopeThisWorks.putDocument(null, b, DocumentFormat.PDF);
       System.out.println(" ");
       System.out.println("check if delete txt1 worked");
       assertEquals(null, IHopeThisWorks.getDocumentAsTxt(b));
       System.out.println(" ");
       System.out.println("put txt1 with pdf2 ");
       assertEquals(0,IHopeThisWorks.putDocument(pdf2, b, DocumentFormat.TXT));
       pdf2.close();
       System.out.println(" ");
       System.out.println("get txt1/pdf2 ");
       assertEquals(IHopeThisWorks.getDocumentAsTxt(b), PdfToString(IHopeThisWorks.getDocumentAsPdf(b)));
       System.out.println(" ");
       System.out.println("get pdf1");
       assertEquals(message1, IHopeThisWorks.getDocumentAsTxt(a));
	}
	@Test
	public void putdoc2() throws IOException {
		DocumentStoreImpl IHopeThisWorks = new DocumentStoreImpl();
		IHopeThisWorks.setMaxDocumentCount(2);
		String filename1 = "test1";
        String message1 = "making a pdf1";
        String filename2 = "test2";
        String message2 = "making a text";
        String filename3 = "test3";
        String message3 = "making a pdf2";
        String filename4 = "test4";
        String message4 = "making a pdf3";
        InputStream pdf1 = MakePdf(filename1, message1);
        InputStream newPdf =MakePdf(filename4, message4);
        InputStream pdf2 = MakePdf(filename3, message3);
        InputStream txt1 =MakeText (filename2,message2);
        InputStream fake;
        URI a = URI.create(filename1);
        URI b = URI.create(filename2);
        URI c = URI.create(filename3);
        URI d = URI.create("fake");
        //adding documents
        System.out.println("put pdf1");
        IHopeThisWorks.putDocument(pdf1, a, DocumentFormat.PDF);
        System.out.println("put pdf2");
        IHopeThisWorks.putDocument(pdf2, c, DocumentFormat.PDF);
        System.out.println("put txt1");
        IHopeThisWorks.putDocument(txt1, b, DocumentFormat.TXT);
        
        pdf1.close();
        
        txt1.close();

        
        //adding new document with same uri
        System.out.println(" ");
        System.out.println("find pdf1");
        List<String> doc = new ArrayList<String>();
        doc.addAll(IHopeThisWorks.search("pdf1"));
        List<String> list = new ArrayList<String>();
        list.add(IHopeThisWorks.getDocument(a).getDocumentAsTxt());
        assertEquals(doc,list);
        newPdf.close();
        //getting documents
        System.out.println(" ");
        System.out.println("get new pdf1");
       //assertEquals(message4, IHopeThisWorks.getDocumentAsTxt(a));
       System.out.println(" ");
       System.out.println("put fake");
       assertEquals(null, IHopeThisWorks.getDocumentAsTxt(d));
       assertEquals(null, IHopeThisWorks.getDocumentAsPdf(d));
       assertEquals(false, IHopeThisWorks.deleteDocument(d));
       System.out.println(" ");
       System.out.println("get txt1");
       assertEquals(message2, IHopeThisWorks.getDocumentAsTxt(b));
       System.out.println(" ");
       System.out.println("get pdf2");
       assertEquals(message3, IHopeThisWorks.getDocumentAsTxt(c));
       System.out.println(" ");
       System.out.println("get txt1");
       assertEquals(IHopeThisWorks.getDocumentAsTxt(b), PdfToString(IHopeThisWorks.getDocumentAsPdf(b)));	
       // deleting documents

    
       System.out.println(" ");
       System.out.println("delete txt1");
        IHopeThisWorks.putDocument(null, b, DocumentFormat.PDF);
       System.out.println(" ");
       System.out.println("check if delete txt1 worked");
       assertEquals(null, IHopeThisWorks.getDocumentAsTxt(b));
       System.out.println(" ");
       System.out.println("put txt1 with pdf2 ");
       assertEquals(0,IHopeThisWorks.putDocument(pdf2, b, DocumentFormat.TXT));
       pdf2.close();
       System.out.println(" ");
       System.out.println("get txt1/pdf2 ");
       assertEquals(IHopeThisWorks.getDocumentAsTxt(b), PdfToString(IHopeThisWorks.getDocumentAsPdf(b)));
       System.out.println(" ");
       System.out.println("get pdf1");
       //assertEquals(message1, IHopeThisWorks.getDocumentAsTxt(a));
	}
	@Test
	public void putdoc3() throws IOException {
		DocumentStoreImpl IHopeThisWorks = new DocumentStoreImpl();
		IHopeThisWorks.setMaxDocumentCount(2);
		String filename1 = "test1";
        String message1 = "making a pdf1";
        String filename2 = "test2";
        String message2 = "making a text";
        String filename3 = "test3";
        String message3 = "making a pdf2";
        String filename4 = "test4";
        String message4 = "making a pdf3";
        InputStream pdf1 = MakePdf(filename1, message1);
        InputStream newPdf =MakePdf(filename4, message4);
        InputStream pdf2 = MakePdf(filename3, message3);
        InputStream txt1 =MakeText (filename2,message2);
        InputStream fake;
        URI a = URI.create(filename1);
        URI b = URI.create(filename2);
        URI c = URI.create(filename3);
        URI d = URI.create("fake");
        //adding documents
        System.out.println("put pdf1");
        IHopeThisWorks.putDocument(pdf1, a, DocumentFormat.PDF);
        System.out.println("put pdf2");
        IHopeThisWorks.putDocument(pdf2, c, DocumentFormat.PDF);
        System.out.println("put txt1");
        IHopeThisWorks.putDocument(txt1, b, DocumentFormat.TXT);
        
        pdf1.close();
        
        txt1.close();

        
       assertEquals(message1, IHopeThisWorks.getDocumentAsTxt(a));
       
	}
	



	    //variables to hold possible values for doc1
	    private URI uri1;
	    private String txt1;
	    private byte[] pdfData1;
	    private String pdfTxt1;

	    //variables to hold possible values for doc2
	    private URI uri2;
	    private String txt2;
	    private byte[] pdfData2;
	    private String pdfTxt2;

	    //variables to hold possible values for doc3
	    private URI uri3;
	    private String txt3;
	    private byte[] pdfData3;
	    private String pdfTxt3;

	    //variables to hold possible values for doc4
	    private URI uri4;
	    private String txt4;
	    private byte[] pdfData4;
	    private String pdfTxt4;

	    private int bytes1;
	    private int bytes2;
	    private int bytes3;
	    private int bytes4;

	    @Before
	    public void init() throws Exception {
	        //init possible values for doc1
	        this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
	        this.txt1 = "This is the text of doc1, in plain text. No fancy file format - just plain old String. Computer. Headphones.";
	        this.pdfTxt1 = "This is some PDF text for doc1, hat tip to Adobe.";
	        this.pdfData1 = Utils.textToPdfData(this.pdfTxt1);

	        //init possible values for doc2
	        this.uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
	        this.txt2 = "Text for doc2. A plain old String.";
	        this.pdfTxt2 = "PDF content for doc2: PDF format was opened in 2008.";
	        this.pdfData2 = Utils.textToPdfData(this.pdfTxt2);

	        //init possible values for doc3
	        this.uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
	        this.txt3 = "This is the text of doc3";
	        this.pdfTxt3 = "This is some PDF text for doc3, hat tip to Adobe.";
	        this.pdfData3 = Utils.textToPdfData(this.pdfTxt3);

	        //init possible values for doc4
	        this.uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
	        this.txt4 = "This is the text of doc4";
	        this.pdfTxt4 = "This is some PDF text for doc4, which is open source.";
	        this.pdfData4 = Utils.textToPdfData(this.pdfTxt4);

	        this.bytes1 = this.pdfTxt1.getBytes().length + this.pdfData1.length;
	        this.bytes2 = this.pdfTxt2.getBytes().length + this.pdfData2.length;
	        this.bytes3 = this.pdfTxt3.getBytes().length + this.pdfData3.length;
	        this.bytes4 = this.pdfTxt4.getBytes().length + this.pdfData4.length;
	    }

	    @Test
	    public void testPutPdfDocumentNoPreviousDocAtURI(){
	        DocumentStore store = new DocumentStoreImpl();
	        int returned = store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
	        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
	        assertTrue(returned == 0 || returned == this.pdfTxt1.hashCode());
	    }

	    @Test
	    public void testPutTxtDocumentNoPreviousDocAtURI(){
	        DocumentStore store = new DocumentStoreImpl();
	        int returned = store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
	        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
	        assertTrue(returned == 0 || returned == this.txt1.hashCode());
	    }

	    @Test
	    public void testPutDocumentWithNullArguments(){
	        DocumentStore store = new DocumentStoreImpl();
	        try {
	            store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()), null, DocumentStore.DocumentFormat.TXT);
	            fail("null URI should've thrown IllegalArgumentException");
	        }catch(IllegalArgumentException e){}
	        try {
	            store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()), this.uri1, null);
	            fail("null format should've thrown IllegalArgumentException");
	        }catch(IllegalArgumentException e){}
	    }

	    @Test
	    public void testPutNewVersionOfDocumentPdf(){
	        //put the first version
	        DocumentStore store = new DocumentStoreImpl();
	        int returned = store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
	        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
	        assertTrue(returned == 0 || returned == this.pdfTxt1.hashCode());
	        assertEquals("failed to return correct pdf text",this.pdfTxt1,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri1)));

	        //put the second version, testing both return value of put and see if it gets the correct text
	        returned = store.putDocument(new ByteArrayInputStream(this.pdfData2),this.uri1, DocumentStore.DocumentFormat.PDF);
	        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
	        assertTrue("should return hashcode of old text",this.pdfTxt1.hashCode() == returned || this.pdfTxt2.hashCode() == returned);
	        assertEquals("failed to return correct pdf text", this.pdfTxt2,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri1)));
	    }

	    @Test
	    public void testPutNewVersionOfDocumentTxt(){
	        //put the first version
	        DocumentStore store = new DocumentStoreImpl();
	        int returned = store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
	        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
	        assertTrue(returned == 0 || returned == this.txt1.hashCode());
	        assertEquals("failed to return correct text",this.txt1,store.getDocumentAsTxt(this.uri1));

	        //put the second version, testing both return value of put and see if it gets the correct text
	        returned = store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
	        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
	        assertTrue("should return hashcode of old text",this.txt1.hashCode() == returned || this.txt2.hashCode() == returned);
	        assertEquals("failed to return correct text",this.txt2,store.getDocumentAsTxt(this.uri1));
	    }

	    @Test
	    public void testGetTxtDocAsPdf(){
	        DocumentStore store = new DocumentStoreImpl();
	        int returned = store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
	        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
	        assertTrue(returned == 0 || returned == this.txt1.hashCode());
	        assertEquals("failed to return correct pdf text",this.txt1,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri1)));
	    }

	    @Test
	    public void testGetTxtDocAsTxt(){
	        DocumentStore store = new DocumentStoreImpl();
	        int returned = store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
	        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
	        assertTrue(returned == 0 || returned == this.txt1.hashCode());
	        assertEquals("failed to return correct text",this.txt1,store.getDocumentAsTxt(this.uri1));
	    }

	    @Test
	    public void testGetPdfDocAsPdf(){
	        DocumentStore store = new DocumentStoreImpl();
	        int returned = store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
	        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
	        assertTrue(returned == 0 || returned == this.pdfTxt1.hashCode());
	        assertEquals("failed to return correct pdf text",this.pdfTxt1,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri1)));
	    }

	    @Test
	    public void testGetPdfDocAsTxt(){
	        DocumentStore store = new DocumentStoreImpl();
	        int returned = store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
	        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
	        assertTrue(returned == 0 || returned == this.pdfTxt1.hashCode());
	        assertEquals("failed to return correct text",this.pdfTxt1,store.getDocumentAsTxt(this.uri1));
	    }

	    @Test
	    public void testDeleteDoc(){
	        DocumentStore store = new DocumentStoreImpl();
	        System.out.println("");
	        System.out.println("find me................................");
	        store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
	        System.out.println("");
	        System.out.println("delete");
	        store.deleteDocument(this.uri1);
	        System.out.println("");
	        System.out.println("find this");
	        assertEquals("calling get on URI from which doc was deleted should've returned null", null, store.getDocumentAsPdf(this.uri1));
	    }
	    
}
