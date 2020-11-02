package edu.yu.cs.com1320.project.stage3.impl;

import static org.junit.Assert.assertEquals;





import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.junit.Test;

import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.stage3.DocumentStore.DocumentFormat;
import edu.yu.cs.com1320.project.stage3.impl.DocumentStoreImpl;


public class StageTwoTestTwo extends StageTwoTest {

	@Test
	public void FillTable() {
		HashTableImpl<Integer, String> t = new HashTableImpl<Integer, String>();
		for(int i =0; i<=40 ; i++) {
			int number = i;
			String string = "word" +i;

			t.put(number,string);
		}
		for(int i =0; i<=40 ; i++) {
			String string = "word" +i;
			assertEquals(string,t.get(i));
		}
	}
	
	
	@Test
	public void StackTest() {
		StackImpl<Integer> stack = new StackImpl<Integer>();
		
		stack.push(1);
		assertEquals(1,stack.peek(),0.1);
		assertEquals(1,stack.pop(),0.1);
		assertEquals(null,stack.pop());
		for(int i =0; i<=5 ; i++) {
			stack.push(i);
		}
		assertEquals(6,stack.size());
		assertEquals(5,stack.peek(),0.1);
		assertEquals(5,stack.pop(),0.1);
		assertEquals(4,stack.pop(),0.1);
		assertEquals(3,stack.pop(),0.1);
		assertEquals(2,stack.pop(),0.1);
		assertEquals(1,stack.pop(),0.1);
		assertEquals(0,stack.peek(),0.1);
		assertEquals(0,stack.pop(),0.1);
		assertEquals(null,stack.peek());
		assertEquals(null,stack.pop());
		assertEquals(0,stack.size(),0.1);
	}
	
	
	@Test
	public void DocumentsInStack() throws IOException {
		//System.out.println("ManyDocumentsTest:");
		DocumentStoreImpl store1 = new DocumentStoreImpl();
		String filename1 = "test1";
        String message1 = "making a pdf1";
        String filename2 = "test2";
        String message2 = "making a text";
        String filename3 = "test3";
        String message3 = "making a pdf2";
        InputStream pdf1 = MakePdf(filename1, message1);
        InputStream pdf2 = MakePdf(filename3, message3);
        InputStream txt1 =MakeText (filename2,message2);
        URI a = URI.create(filename1);
        URI b = URI.create(filename2);
        URI c = URI.create(filename3);
        //adding documents
        store1.putDocument(pdf1, a, DocumentFormat.PDF);
        pdf1.close();
        store1.putDocument(pdf2, c, DocumentFormat.PDF);
        pdf2.close();
        store1.putDocument(txt1, b, DocumentFormat.TXT);
        txt1.close();
        //getting documents
       assertEquals(message1, store1.getDocumentAsTxt(a));
       assertEquals(message2, store1.getDocumentAsTxt(b));
       assertEquals(message3, store1.getDocumentAsTxt(c));
       assertEquals(store1.getDocumentAsTxt(b), PdfToString(store1.getDocumentAsPdf(b)));
       store1.undo();
       assertEquals(null,store1.getDocument(b));     
       assertEquals(true, store1.deleteDocument(a));
       store1.undo();
       assertEquals(message1, store1.getDocumentAsTxt(a));
	}
	@Test
	public void Undo() throws IOException {
		for(int z= 0; z<= 10; z++) {
		DocumentStoreImpl store2 = new DocumentStoreImpl();
		for(int i =0; i<40 ; i++) {
			String filename = "test" +i;
			String words = "word" +i;
			URI uri = URI.create(filename);
	        if(Math.random()<0.5) {
	        	InputStream pdf1 = MakePdf(filename, words);
	        	store2.putDocument(pdf1, uri, DocumentFormat.PDF);
	        	pdf1.close();
	        }
	        else {
	        	InputStream txt1 =MakeText (filename,words);
	        	store2.putDocument(txt1, uri, DocumentFormat.TXT);
	            txt1.close();
	        }
	    
		}
		int[] number = new int[5];
		for(int i =0; i<5 ; i++) {
			
			int x = (int) (Math.random()*40);
			number[i]=x;
			System.out.println("delete" +x);
			String filename = "test" +x;
			String words = "word" +x;
			URI uri = URI.create(filename);
			store2.deleteDocument(uri);
        }
		for(int i =0; i<5 ; i++) {
			int x = (int) (Math.random()*40);
			System.out.println("undo" +x);
			String filename = "test" +x;
			String words = "word" +x;
			URI uri = URI.create(filename);
			try {
				store2.undo(uri);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				break;
			}
			int q=0;
			int y;
			for(int s =0; s<5 ; s++) {
				y= number[s];
				if (y==x) {
						number[s]=-1;
						assertEquals(words, store2.getDocumentAsTxt(uri));

					q++;
					break;
				}
			}
			if (q==0) {
				assertEquals(null, store2.getDocumentAsTxt(uri));
			}
			
		}
		}
	}
	
	@Test
	public void Undo2() throws IOException {
		DocumentStoreImpl store1 = new DocumentStoreImpl();
		String filename1 = "test1";
        String message1 = "making a pdf1";

        String filename3 = "test3";
        String message3 = "making a pdf2";
        
        String filename2 = "test2";
        String message2 = "making a text";
        String message4 = "override message 2";
        
        InputStream pdf1 = MakePdf(filename1, message1);
        InputStream pdf2 = MakePdf(filename3, message3);
        InputStream txt1 =MakeText (filename2,message2);
        InputStream txt2 =MakeText (filename2,message4);
        URI a = URI.create(filename1);
        URI b = URI.create(filename2);
        URI c = URI.create(filename3);
        //adding documents
        store1.putDocument(pdf1, a, DocumentFormat.PDF);
        pdf1.close();
        store1.putDocument(pdf2, c, DocumentFormat.PDF);
        pdf2.close();
        store1.putDocument(txt1, b, DocumentFormat.TXT);
        txt1.close();
        store1.putDocument(txt2, b, DocumentFormat.TXT);
        store1.undo();
        assertEquals(message2,store1.getDocumentAsTxt(b));   
       store1.undo();
       assertEquals(null,store1.getDocument(b));     
	}
	@Test (expected = IllegalStateException.class)
	public void Undo3() throws IOException {
		DocumentStoreImpl store1 = new DocumentStoreImpl();
		String filename1 = "test1";
        String message1 = "making a pdf1";

        String filename3 = "test3";
        String message3 = "making a pdf2";
        
        String filename2 = "test2";
        String message2 = "making a text";
        String message4 = "override message 2";
        
        InputStream pdf1 = MakePdf(filename1, message1);
        InputStream pdf2 = MakePdf(filename3, message3);
        InputStream txt1 =MakeText (filename2,message2);
        InputStream txt2 =MakeText (filename2,message4);
        URI a = URI.create(filename1);
        URI b = URI.create(filename2);
        URI c = URI.create(filename3);
        //adding documents
        store1.putDocument(pdf1, a, DocumentFormat.PDF);
        pdf1.close();
        store1.putDocument(pdf2, c, DocumentFormat.PDF);
        pdf2.close();
        store1.putDocument(txt1, b, DocumentFormat.TXT);
        txt1.close();
        store1.putDocument(txt2, b, DocumentFormat.TXT);
        store1.undo();  
       store1.undo(b);
       store1.undo(b);
	}
}
