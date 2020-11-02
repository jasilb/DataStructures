package edu.yu.cs.com1320.project.stage1.impl.DocumentStoreImpl;

import java.io.IOException;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;

//import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.text.PDFTextStripper;

import edu.yu.cs.com1320.project.impl.HashTableImpl.HashTableImpl;
import edu.yu.cs.com1320.project.stage1.DocumentStore;
import edu.yu.cs.com1320.project.stage1.impl.DocumentImpl.DocumentImpl;

public class DocumentStoreImpl implements DocumentStore {
	public DocumentStoreImpl() {
	}

	HashTableImpl<URI, DocumentImpl> store = new HashTableImpl<URI, DocumentImpl>();


	public int putDocument(InputStream input, URI uri, DocumentFormat format) {
		if (uri==null) {
			throw new IllegalArgumentException();
		}
		if (input == null) {
			if (store.get(uri)==null) {
				return 0;
			}
			int oldhash= store.get(uri).getDocumentTextHashCode();
			deleteDocument(uri);
			return oldhash;
		}
		

		ByteArrayOutputStream array = new ByteArrayOutputStream();
		int test = 0;
		try {
			test = input.read();
			while (test != -1) {
				array.write(test);
				test = input.read();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] b = array.toByteArray();

		if (format == DocumentFormat.TXT) {
			return addTxt(b, uri);
		} else {
			return addPdf(b, uri);

		}

	}

	private int addPdf(byte[] b, URI uri) {
		String paper = null;
		int paperHashCode;
		PDDocument document = new PDDocument();
		try {
			document = PDDocument.load(b);
			PDFTextStripper stripper = new PDFTextStripper();
			paper = stripper.getText(document);
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 

		
		paperHashCode =  paper.hashCode();
		if (store.get(uri) != null &&  store.get(uri).getDocumentTextHashCode() == paperHashCode) {
			return paperHashCode;
		} 
		else {
			DocumentImpl doc = new DocumentImpl(uri, paper, paperHashCode, b);
			if (store.get(uri)==null) {
				store.put(uri, doc);
				return 0;
			}
			else {
				paperHashCode=store.put(uri, doc).getDocumentTextHashCode();
				return paperHashCode;
			}
		}	
	}

	private int addTxt(byte[] b, URI uri) {
		String paper = new String(b);
		int paperHashCode = paper.hashCode();
		//System.out.println("words: " + paper);
		if (store.get(uri) != null && store.get(uri).getDocumentTextHashCode() == paperHashCode) {
			//System.out.println("same text");
			return paperHashCode;
		} else {
			DocumentImpl doc = new DocumentImpl(uri, paper, paperHashCode);
			if (store.get(uri)==null) {
				store.put(uri, doc);
				return 0;
			}
			else {
				paperHashCode=store.put(uri, doc).getDocumentTextHashCode();
				return paperHashCode;
			}
		}
	}

	public byte[] getDocumentAsPdf(URI uri) {
		// TODO Auto-generated method stub
		if (uri==null) {
			throw new IllegalArgumentException();
		}
		DocumentImpl x=store.get(uri);
		if(x==null) {
			return null;
		}
		return x.getDocumentAsPdf();
	}

	public String getDocumentAsTxt(URI uri) {
		// TODO Auto-generated method stub
		if (uri==null) {
			throw new IllegalArgumentException();
		}
		DocumentImpl x=store.get(uri);
		if(x==null) {
			return null;
		}
		return x.getDocumentAsTxt().trim();
	}

	public boolean deleteDocument(URI uri) {
		if (uri==null) {
			throw new IllegalArgumentException();
		}
		if(store.get(uri)==null) {
			return false;
		}
		if (store.put(uri, null) != null) {
			return true;
		}
		return false;
	}

}
