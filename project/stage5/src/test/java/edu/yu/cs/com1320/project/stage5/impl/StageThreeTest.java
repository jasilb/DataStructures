package edu.yu.cs.com1320.project.stage5.impl;
import org.junit.Test;




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
import java.util.*;

import org.junit.Before;
	import org.junit.Test;
public class StageThreeTest extends StageFiveTestBtree {
	
	
	protected void makestore(DocumentStoreImpl store1) throws IOException {
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
        store1.putDocument(pdf2, b, DocumentFormat.PDF);
        pdf2.close();
        store1.putDocument(txt1, c, DocumentFormat.TXT);
        txt1.close();
        store1.putDocument(pdf1, a, DocumentFormat.PDF);
        pdf1.close();
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
	}

	
	@Test
	public void SearchTest1() throws IOException {
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
        //adding documents
        store1.putDocument(pdf2, b, DocumentFormat.PDF);
        pdf2.close();
        store1.putDocument(txt1, c, DocumentFormat.TXT);
        txt1.close();
        store1.putDocument(pdf1, a, DocumentFormat.PDF);
        pdf1.close();
        store1.putDocument(txt2, d, DocumentFormat.TXT);
        txt2.close();
        store1.putDocument(txt3, e, DocumentFormat.TXT);
        txt3.close();
        List<String> list = store1.search("a");
        assertEquals(4,list.size());
        for(String words: list) {
        	System.out.println(words);
        }
	}
	
	@Test
	public void PrefixSearchTest1() throws IOException{
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
        store1.putDocument(pdf2, b, DocumentFormat.PDF);
        pdf2.close();
        store1.putDocument(txt1, c, DocumentFormat.TXT);
        txt1.close();
        store1.putDocument(pdf1, a, DocumentFormat.PDF);
        pdf1.close();
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
        List<String> list = store1.searchByPrefix("a");
        assertEquals(8,list.size());
        System.out.println("List:");
        for(String words: list) {
        	System.out.println(words);
        }
	}
	
	@Test
	public void DeleteTest1() throws IOException{
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
        store1.putDocument(pdf2, b, DocumentFormat.PDF);
        pdf2.close();
        store1.putDocument(txt1, c, DocumentFormat.TXT);
        txt1.close();
        store1.putDocument(pdf1, a, DocumentFormat.PDF);
        pdf1.close();
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
        System.out.println(" ");
        System.out.println("delete all ");
        Set<URI> list = store1.deleteAll("a"); 
        //assertEquals(8,list.size());
        System.out.println("Set URI1:");
        for(URI words: list) { 
        	System.out.println(words);
        }
	}

	@Test
	public void DeletePrefixTest1() throws IOException{
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
        store1.putDocument(pdf2, b, DocumentFormat.PDF);
        pdf2.close();
        store1.putDocument(txt1, c, DocumentFormat.TXT);
        txt1.close();
        store1.putDocument(pdf1, a, DocumentFormat.PDF);
        pdf1.close();
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
        Set<URI> list = store1.deleteAllWithPrefix("ab");
        Set<URI> test = new HashSet<URI>();
        test.add(f);
        test.add(g);
        test.add(i);
        assertEquals(test,list);
        
        assertEquals(3,list.size());
        System.out.println("Set URI2:");
        for(URI words: list) {
        	System.out.println(words);
        }   
	}
	
	@Test
	public void UndoTest1() throws IOException{
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
        store1.putDocument(pdf2, b, DocumentFormat.PDF);
        pdf2.close();
        store1.putDocument(txt1, c, DocumentFormat.TXT);
        txt1.close();
        store1.putDocument(pdf1, a, DocumentFormat.PDF);
        pdf1.close();
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
		store1.deleteAll("aa");
		Set<URI> list=store1.deleteAllWithPrefix("a");
		System.out.println("undo");
		store1.undo();
		List<String> returns= store1.searchByPrefix("ab");
		for(String words: returns) {
			System.out.println(words);
		}

	}
	@Test
	public void UndoTest2() throws IOException{
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
        store1.putDocument(pdf2, b, DocumentFormat.PDF);
        pdf2.close();
        store1.putDocument(txt1, c, DocumentFormat.TXT);
        txt1.close();
        store1.putDocument(pdf1, a, DocumentFormat.PDF);
        pdf1.close();
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
        store1.deleteAll("a");
        store1.undo(b);
        List<byte[]> find= store1.searchPDFs("a");
        List<byte[]> test= new ArrayList<byte[]>();
        test.add(store1.getDocumentAsPdf(b));
        assertEquals(test,find);
        
	}
	
	
	
	
	
	
	
}
