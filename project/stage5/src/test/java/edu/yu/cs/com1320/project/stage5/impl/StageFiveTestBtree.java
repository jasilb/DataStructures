package edu.yu.cs.com1320.project.stage5.impl;

import static org.junit.Assert.*;


import org.junit.Test;

import edu.yu.cs.com1320.project.impl.BTreeImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.stage5.*;
import edu.yu.cs.com1320.project.stage5.DocumentStore.DocumentFormat;
import edu.yu.cs.com1320.project.stage5.impl.DocumentStoreImpl;

import static org.junit.Assert.*;

	import java.io.BufferedReader;
	import java.io.ByteArrayInputStream;
	import java.io.ByteArrayOutputStream;
	import java.io.DataInputStream;
	import java.io.File;
	import java.io.FileInputStream;
	import java.io.FileNotFoundException;
	import java.io.IOException;
	import java.io.InputStream;
	import java.io.InputStreamReader;

	import org.apache.pdfbox.pdmodel.PDDocument;
	import org.apache.pdfbox.pdmodel.PDPage;

	import org.apache.pdfbox.pdmodel.PDPageContentStream;

	import org.apache.pdfbox.pdmodel.font.PDFont;
	import org.apache.pdfbox.pdmodel.font.PDType1Font;
	import org.apache.pdfbox.text.PDFTextStripper;

	import java.net.URI;
	import java.net.URISyntaxException;
	import java.net.URL;

	import org.junit.Before;
	import org.junit.Test;

	public class StageFiveTestBtree {


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
		public void putGenericTets() {
			//System.out.println("putGenericTets:");

			BTreeImpl<Integer, String> t = new BTreeImpl<Integer, String>();
			int zero =0;
			int three= 3;
			int ten= 10;
			int five= 5;
			int two = 2;
			int fifteen = 15;
			String y ="y";
			String a = "a";
			String b= "b";
			String f= "f";
			assertEquals(null, t.put(zero, y));
			assertEquals(y, t.get(zero));
			assertEquals(null,t.put(three, b));
			assertEquals(null, t.put(ten, a));
			assertEquals(a ,t.get(ten));
			assertEquals(null, t.put(five,b));
			assertEquals(b ,t.get(five));
			t.put(five, a);
			assertEquals(a, t.get(five));
			assertEquals(y,t.get(zero));
			assertEquals(b,t.get(three));
			assertEquals(a, t.put(ten,null));//dont have a persistance manager to check if there is a json file
			//t.put(ten, a);
			t.put(fifteen, f);
			assertEquals(f, t.put(fifteen,null));
			
			
			
			
			
		}

		
		@Test
		public void uriTest()  {
			//System.out.println("uriTest:");

			BTreeImpl<URI, URI> t1 = new BTreeImpl<URI, URI>();
			URI a = URI.create("a");
			URI b = URI.create("b");
			URI c = URI.create("c");
			URI d = URI.create("d");
			URI e =URI.create("e");
			URI f =URI.create("f");
			
			URI x =  URI.create("x");
			URI y= URI.create("y");
			URI z= URI.create("z");
			t1.put(a, x);
			assertEquals(null, t1.put(b, b));
			t1.put(e, b);
			t1.put(f, z);
			assertEquals(z,t1.get(f));
			t1.put(c, b);
			assertEquals(null,t1.put(d, x));
			assertEquals(x,t1.get(a));
			assertEquals(b,t1.get(c));
			assertEquals(b,t1.get(e));
			
			t1.put(a,b);
			
			assertEquals(b, t1.put(a,c));
			assertEquals(b,t1.put(c, null));
			
			//assertEquals(z,t1.put(f, null));
			
			
			
		}
		
		@Test
		public void documentStoreOneDocumentTest() throws IOException {
			//System.out.println("documentStoreOneDocumentTest:");
			DocumentStoreImpl help = new DocumentStoreImpl();

			        String filename = "test";
			        String message = "making a pdf";
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
			                contents.showText(message);
			                contents.endText();
			            }
			            
			            doc.save(filename);
			        }catch(Exception e){

			        	e.printStackTrace();
			        }
			        
			        File file = new File(filename);
			       
			        byte[] bytesArray = new byte[(int) file.length()]; 

			        FileInputStream fis = new FileInputStream(file);
			        fis.read(bytesArray); //read file into bytes[]
			        fis.close();
			        InputStream targetStream = new FileInputStream(file);
		
			        URI a = URI.create(filename);
			     
			        //test hashcode
			        assertEquals(0, help.putDocument(targetStream, a, DocumentFormat.PDF));
			        
			    
			        //test text
			       assertEquals(message, help.getDocumentAsTxt(a)); 
			       
			       
			        byte[] test=help.getDocumentAsPdf(a);
			        
			        PDDocument document =  PDDocument.load(test);
			       

			            PDFTextStripper stripper = new PDFTextStripper();

			                String theText = stripper.getText(document);
			                String text1 =theText.trim();
			                //test pdf byte[] creates same text
			                assertEquals(message,text1);
			    }
		
		@Test
		public void ManyDocumentsTest() throws IOException {
			//System.out.println("ManyDocumentsTest:");
			DocumentStoreImpl IHopeThisWorks = new DocumentStoreImpl();
			String filename1 = "test1";
	        String message1 = "making a pdf1";
	        String filename2 = "test2";
	        String message2 = "making a text";
	        String filename3 = "test3";
	        String message3 = "making a pdf2";
	        InputStream pdf1 = MakePdf(filename1, message1);
	        InputStream sameAsPdf1 =MakePdf(filename1, message1);
	        InputStream pdf2 = MakePdf(filename3, message3);
	        InputStream txt1 =MakeText (filename2,message2);
	        URI a = URI.create(filename1);
	        URI b = URI.create(filename2);
	        URI c = URI.create(filename3);
	        //adding documents
	        IHopeThisWorks.putDocument(pdf1, a, DocumentFormat.PDF);
	        pdf1.close();
	        IHopeThisWorks.putDocument(pdf2, c, DocumentFormat.PDF);
	        pdf2.close();
	        IHopeThisWorks.putDocument(txt1, b, DocumentFormat.TXT);
	        txt1.close();
	        //adding same document
	        IHopeThisWorks.putDocument(sameAsPdf1, a, DocumentFormat.PDF);
	        sameAsPdf1.close();
	        //getting documents
	       assertEquals(message1, IHopeThisWorks.getDocumentAsTxt(a));
	       assertEquals(message2, IHopeThisWorks.getDocumentAsTxt(b));
	       assertEquals(message3, IHopeThisWorks.getDocumentAsTxt(c));
	       assertEquals(IHopeThisWorks.getDocumentAsTxt(b), PdfToString(IHopeThisWorks.getDocumentAsPdf(b)));	
		}
		@Test
		public void ManyDocumentsTest2() throws IOException {
			//System.out.println("ManyDocumentsTest2:");
			DocumentStoreImpl IHopeThisWorks = new DocumentStoreImpl();
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
	        assertEquals(message1, IHopeThisWorks.getDocumentAsTxt(a));
	        //adding new document with same uri
	        System.out.println(" ");
	        System.out.println("replace pdf1");
	        IHopeThisWorks.putDocument(newPdf, a, DocumentFormat.PDF);
	        newPdf.close();
	        //getting documents
	        System.out.println(" ");
	        System.out.println("get new pdf1");
	       assertEquals(message4, IHopeThisWorks.getDocumentAsTxt(a));
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
	       System.out.println("delete pdf3");
	       assertEquals(true, IHopeThisWorks.deleteDocument(a));
	       
	       System.out.println(" ");
	       System.out.println("delete nothing");
	       
	       assertEquals(0, IHopeThisWorks.putDocument(null, a, DocumentFormat.PDF));
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
		}

	

	

}
