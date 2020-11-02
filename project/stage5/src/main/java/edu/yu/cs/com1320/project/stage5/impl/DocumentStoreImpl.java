package edu.yu.cs.com1320.project.stage5.impl;

import java.io.ByteArrayOutputStream;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.impl.BTreeImpl;
//import edu.yu.cs.com1320.project.Command;
//import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;

public class DocumentStoreImpl implements DocumentStore {
	
	private int DocCount=0;
	private int ByteCount=0;
	private int MaxDocs =-1;
	private int MaxBytes =-1;
	//HashTableImpl<URI, DocumentImpl> store = new HashTableImpl<URI, DocumentImpl>();//change to btree 
	StackImpl<Undoable> stack = new StackImpl<Undoable>();
	TrieImpl<URI> trie = new TrieImpl<URI>();
	MinHeapImpl<MinFile> heap = new MinHeapImpl<MinFile>();
	BTreeImpl<URI, DocumentImpl> MyBtree = new BTreeImpl<URI, DocumentImpl>();
	
	public DocumentStoreImpl() {
		PersistenceManager manager = new DocumentPersistenceManager(new File(System.getProperty("user.dir")));
		MyBtree.setPersistenceManager(manager);
		try {
			MyBtree.put(new URI(""), null);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public DocumentStoreImpl(File baseDir) {
		PersistenceManager manager = new DocumentPersistenceManager(baseDir);
		MyBtree.setPersistenceManager(manager);
		try {
			MyBtree.put(new URI(""), null);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	


	public int putDocument(InputStream input, URI uri, DocumentFormat format) {
		if (uri == null) {
			throw new IllegalArgumentException();
		}
		if (format == null) {
			throw new IllegalArgumentException();
		}
		if (input == null) {
			if (MyBtree.get(uri) == null) {
				GenericCommand<URI> command = new GenericCommand<URI>(uri, (URI) -> {
						return true;});
				stack.push(command);
				return 0;
			}
			int oldhash = MyBtree.get(uri).getDocumentTextHashCode();
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
		try {

			PDFTextStripper stripper = new PDFTextStripper();
			paper = stripper.getText(PDDocument.load(b)).trim();

		} catch (IOException e) {
			e.printStackTrace();
		}

		paperHashCode = paper.hashCode();
		if (MyBtree.get(uri) != null && MyBtree.get(uri).getDocumentTextHashCode() == paperHashCode) {
			DocumentImpl fixDoc = MyBtree.get(uri);
			fixDoc.setLastUseTime(System.nanoTime());
			if(fixDoc.getMinfile()==null) {//add doc to heap if not already in it
				fixDoc.setMinfile(new MinFile(fixDoc.getKey()));
				heap.insert(fixDoc.getMinfile());
			}
			else {
				heap.reHeapify(fixDoc.getMinfile());
			}
			return paperHashCode;
		} else {
			DocumentImpl doc = new DocumentImpl(uri, paper, paperHashCode, b);
			doc.setMinfile(new MinFile(doc.getKey()));
			if (MyBtree.get(uri) == null) {
				return addNewDoc(doc);
			}else {
			return ReplaceDoc(doc);
			}
		}
	}

	private int addTxt(byte[] b, URI uri) {
		String paper = new String(b);
		int paperHashCode = paper.hashCode();
		////System.out.println("words: " + paper);
		if (MyBtree.get(uri) != null && MyBtree.get(uri).getDocumentTextHashCode() == paperHashCode) {
			////System.out.println("same text");
			DocumentImpl fixDoc = MyBtree.get(uri);
			fixDoc.setLastUseTime(System.nanoTime());
			if(fixDoc.getMinfile()==null) {//add doc to heap if not already in it
				fixDoc.setMinfile(new MinFile(fixDoc.getKey()));
				heap.insert(fixDoc.getMinfile());
			}
			return paperHashCode;
		} else {
			DocumentImpl doc = new DocumentImpl(uri, paper, paperHashCode);
			doc.setMinfile(new MinFile(doc.getKey()));
			if (MyBtree.get(uri) == null) {
				return addNewDoc(doc);
			} else {
				return ReplaceDoc(doc);
			}
		}
	}
	
	private int addNewDoc(DocumentImpl doc) {
		MyBtree.put(doc.getKey(), doc);
		putAll(doc);
		GenericCommand<URI> command = new GenericCommand<URI>(doc.getKey(), (URI) -> {
			try {
				RemoveFromHeap(doc);
				MyBtree.put(doc.getKey(), null);
				removeAll(doc);
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return false;
			}
		});
		stack.push(command);
		AddToHeap(doc);
		return 0;
	}
	
	private int ReplaceDoc(DocumentImpl doc) {
		URI uri = doc.getKey();
		DocumentImpl doc2 = MyBtree.get(uri);
		GenericCommand<URI> command = new GenericCommand<URI>(uri, (URI) -> {
			try {
				doc.setLastUseTime(System.nanoTime());
				if(doc.getMinfile()==null) {//add doc to heap if not already in it
					doc.setMinfile(new MinFile(doc.getKey()));
				}
				System.out.println(doc.getDocumentAsTxt());
				RemoveFromHeap(doc);
				removeAll(doc);
				MyBtree.put(uri, doc2);
				AddToHeap(doc2);
				putAll(doc2);
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		});
		stack.push(command);
		RemoveFromHeap(doc2);
		removeAll(doc2);
		MyBtree.put(uri, doc);
		AddToHeap(doc);
		putAll(doc);
		return doc2.getDocumentTextHashCode();
	}
	private void RemoveFromHeap(DocumentImpl doc) {
		// TODO Auto-generated method stub
		if(doc.getMinfile()==null) {
			return;
		}
		int Bytes = doc.getDocumentAsPdf().length + doc.getDocumentAsTxt().length();
		doc.setLastUseTime(Long.MIN_VALUE);
		heap.reHeapify(doc.getMinfile());
		heap.removeMin();
		ByteCount-= Bytes;
		DocCount--;
	}

	private void AddToHeap(DocumentImpl doc) {
		// TODO Auto-generated method stub
		System.out.println("put: ");
		System.out.println(doc.getDocumentAsPdf());
		int Bytes = doc.getDocumentAsPdf().length + doc.getDocumentAsTxt().length();
		if(Bytes> MaxBytes && MaxBytes != -1) {
			throw new IllegalArgumentException();
		}
		else if((DocCount+1 <=MaxDocs || MaxDocs ==-1)) {
			if (ByteCount+Bytes >MaxBytes && MaxBytes !=-1 ){
				DocumentImpl removed; 
				while (ByteCount+Bytes >MaxBytes) {
					removed = heap.removeMin().GetDoc();
					int RemovedBytes = removed.getDocumentAsPdf().length + removed.getDocumentAsTxt().length();
					ByteCount-= RemovedBytes;
					DocCount--;
					try {//tree
						MyBtree.moveToDisk(removed.getKey());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			System.out.println("add min");
				heap.insert(doc.getMinfile());
				DocCount++;
				//System.out.println("DocCount In Store: " +DocCount);
				ByteCount+= Bytes;	
				//System.out.println("ByteCount In Store: " +ByteCount);
				return;
		}
		 else {
			 //System.out.println("remove");
			 DocumentImpl removed = heap.removeMin().GetDoc();
			 int RemovedBytes = removed.getDocumentAsPdf().length + removed.getDocumentAsTxt().length();
			 ByteCount-= RemovedBytes;
			 DocCount--;
				try {//tree
					MyBtree.moveToDisk(removed.getKey());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 if (ByteCount+Bytes >MaxBytes && MaxBytes !=-1 ){

					while (ByteCount+Bytes >MaxBytes) {
						//System.out.println("remove");
						removed = heap.removeMin().GetDoc();
						RemovedBytes = removed.getDocumentAsPdf().length + removed.getDocumentAsTxt().length();
						ByteCount-= RemovedBytes;
						DocCount--;
						try {//tree
							MyBtree.moveToDisk(removed.getKey());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
					heap.insert(doc.getMinfile());
					DocCount++;
					//System.out.println("DocCount In Store: " +DocCount);
					ByteCount+= Bytes;
					//System.out.println("ByteCount In Store: " +ByteCount);
					return;
		 }
		 
		
	}

	
	public byte[] getDocumentAsPdf(URI uri) {
		// TODO Auto-generated method stub
		if (uri == null) {
			throw new IllegalArgumentException();
		}
		DocumentImpl x = MyBtree.get(uri);
		if (x == null) {
			return null;
		}
		x.setLastUseTime(System.nanoTime());
		if(x.getMinfile()==null) {//add doc to heap if not already in it
			x.setMinfile(new MinFile(x.getKey()));
			AddToHeap(x);
		}
		else {
			heap.reHeapify(x.getMinfile());
		}
		return x.getDocumentAsPdf();
	}

	public String getDocumentAsTxt(URI uri) {
		// TODO Auto-generated method stub
		if (uri == null) {
			throw new IllegalArgumentException();
		}
		DocumentImpl x = MyBtree.get(uri);   
		if (x == null) {
			return null;
		}
		 System.out.println("Text: "+x.getDocumentAsTxt());
		x.setLastUseTime(System.nanoTime());
		if(x.getMinfile()==null) {//add doc to heap if not already in it
			System.out.println("null ");
			x.setMinfile(new MinFile(x.getKey()));
			AddToHeap(x);
		}
		else {
			System.out.println(" not null ");
			heap.reHeapify(x.getMinfile());
		}
		return x.getDocumentAsTxt();
	}

	public boolean deleteDocument(URI uri) {
		if (uri == null) {
			throw new IllegalArgumentException();
		}
		DocumentImpl doc = MyBtree.get(uri);
		
		if (doc == null) {
			GenericCommand<URI> command = new GenericCommand<URI>(uri, (URI) -> {
				return true;});
			return false;
		}
		System.out.println("doc to delete " +doc.getDocumentAsTxt());
		if (doc != null) {
			removeAll(doc);
			RemoveFromHeap(doc);
			GenericCommand<URI> command = new GenericCommand<URI>(uri, (URI) -> {
				doc.setLastUseTime(System.nanoTime());
				if(doc.getMinfile()==null) {//add doc to heap if not already in it
					doc.setMinfile(new MinFile(doc.getKey()));
				}
				MyBtree.put(uri, doc);
				AddToHeap(doc);
				putAll(doc);
				return true;
			});
			stack.push(command);
			
				try {
					MyBtree.put(uri, null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace(); 
				}
			return true;
		}
		return false;
	} 



	/**
	 * undo the last put or delete command
	 * 
	 * @throws IllegalStateException if there are no actions to be undone, i.e. the
	 *  command stack is empty
	 */
	public void undo() throws IllegalStateException {
		if (stack.size() == 0) {
			throw new IllegalStateException();
		}
		Undoable LastIn = stack.pop();
		LastIn.undo();
	
	}

	/**
	 * undo the last put or delete that was done with the given URI as its key
	 * 
	 * @param uri
	 * @throws IllegalStateException if there are no actions on the command stack
	 *                               for the given URI
	 */
	public void undo(URI uri) throws IllegalStateException {
		if (stack.size() == 0) {
			throw new IllegalStateException();
		}
		StackImpl<Undoable> hold = new StackImpl<Undoable>();
		Undoable command = stack.pop();
		while(true) {
			
			if( command instanceof GenericCommand) {
				GenericCommand<URI>	GC = (GenericCommand<URI>) command;
				if(GC.getTarget().equals(uri)) {
					command.undo();
					break; 
				}
			}
			if( command instanceof CommandSet) {
				CommandSet<URI> CS =  (CommandSet<URI>) command;
				if(CS.containsTarget(uri)) {
					CS.undo(uri);
					if(CS.size()!=0) {
						hold.push(command);
					}	
					break;
				}
			}
			hold.push(command);
			command = stack.pop();
			if (command == null) {
				throw new IllegalStateException();
			}
		}
		while (hold.peek() != null) {
			command = hold.pop();
			stack.push(command);
		}
	}

	/**
	 * @return the Document object stored at that URI, or null if there is no such
	 *         Document
	 */

	protected Document getDocument(URI uri) {
		//find back door way to get the document without bringing it back to disk
		DocumentImpl doc = MyBtree.get(uri);
		System.out.println("doc "+doc);
		if(doc==null) {
			return null;
		}
		System.out.println("minfile "+doc.getMinfile() );
		if(doc.getMinfile()==null) {
			try {
				MyBtree.moveToDisk(uri);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			return null;
		}		
		return doc;
	}


	public List<String> search(String keyword) {
		if(keyword==null) {
			return new ArrayList<String>();
		}
		// TODO Auto-generated method stub
		String words = keyword;
		words =words.toUpperCase();
		Comparator<URI> compare = new DocumentComparator<URI>(words);
		List<URI> docs = trie.getAllSorted(words, compare); 
		List<String> returns = new ArrayList<String>();
		long time = System.nanoTime();
		for(URI uri: docs) {
			DocumentImpl doc =MyBtree.get(uri);
			returns.add(doc.getDocumentAsTxt());
			doc.setLastUseTime(time);
			if(doc.getMinfile()==null) {//add doc to heap
				doc.setMinfile(new MinFile(doc.getKey()));
				AddToHeap(doc);
			}
			else {
				heap.reHeapify(doc.getMinfile());
			}
			
		}
		return returns;
	}


	public List<byte[]> searchPDFs(String keyword) {
		if(keyword==null) {
			return new ArrayList<byte[]>();
		}
		String words = keyword;
		words =words.toUpperCase();
		Comparator<URI> compare = new DocumentComparator<URI>(words);
		List<URI> docs = trie.getAllSorted(words, compare); 
		List<byte[]> returns = new ArrayList<byte[]>();
		long time = System.nanoTime();
		for(URI uri: docs) {
			DocumentImpl doc =MyBtree.get(uri);
			returns.add(doc.getDocumentAsPdf());
			doc.setLastUseTime(time);
			if(doc.getMinfile()==null) {//add doc to heap
				doc.setMinfile(new MinFile(doc.getKey()));
				AddToHeap(doc);
			
			}
			else {
				heap.reHeapify(doc.getMinfile());
			}
		}
		return returns;
		
	}


	public List<String> searchByPrefix(String prefix) {
		if(prefix==null) {
			return new ArrayList<String>();
		}
		String words = prefix;
		words =words.toUpperCase();
		Comparator<URI> compare = new PrefixComparator<URI>(words);
		List<URI> docs = trie.getAllWithPrefixSorted(words, compare); 
		List<String> returns = new ArrayList<String>();
		long time = System.nanoTime();
		for(URI uri: docs) {
			DocumentImpl doc =MyBtree.get(uri);
			returns.add(doc.getDocumentAsTxt());
			doc.setLastUseTime(time);
			if(doc.getMinfile()==null) {//add doc to heap
				doc.setMinfile(new MinFile(doc.getKey()));
				AddToHeap(doc);
			}
			else {
				heap.reHeapify(doc.getMinfile());
			}
		}
		return returns;
	}


	public List<byte[]> searchPDFsByPrefix(String prefix) {
		if(prefix==null) {
			return new ArrayList<byte[]>();
		}
		// TODO Auto-generated method stub
		String words = prefix;
		words =words.toUpperCase();
		Comparator<URI> compare = new PrefixComparator<URI>(words);
		List<URI> docs = trie.getAllWithPrefixSorted(words, compare); 
		List<byte[]> returns = new ArrayList<byte[]>();
		long time = System.nanoTime();
		for(URI uri: docs) {
			DocumentImpl doc =MyBtree.get(uri);
			returns.add(doc.getDocumentAsPdf());
			doc.setLastUseTime(time);
			if(doc.getMinfile()==null) {//add doc to heap
				doc.setMinfile(new MinFile(doc.getKey()));
				AddToHeap(doc);
			}
			else {
				heap.reHeapify(doc.getMinfile());
			}
		}
		return returns;
	}

	
	public Set<URI> deleteAll(String key) {
		if(key==null) {
			return new HashSet<URI>();
		}
		// TODO Auto-generated method stub
		String words = key;
		words =words.toUpperCase();
		Set<URI> docs= trie.deleteAll(words);
		CommandSet<URI> command = new CommandSet<URI>();
		Set<URI> URIs = new HashSet<URI>();
		for(URI uri : docs) {
			DocumentImpl doc =MyBtree.get(uri);
			System.out.println("docs: " +doc.getDocumentAsTxt());
			removeDoc(doc, words);
			URIs.add(doc.getKey());
			
			GenericCommand<URI> commands = new GenericCommand<URI>(doc.getKey(), (URI) -> {
				////System.out.println("Text: "+doc.getDocumentAsTxt());
				doc.setLastUseTime(System.nanoTime());
				if(doc.getMinfile()==null) {//make minfile
					doc.setMinfile(new MinFile(doc.getKey()));
				}
				MyBtree.put(doc.getKey(), doc);
				AddToHeap(doc);
				
				putAll(doc);
				return true;
			});
			command.addCommand(commands);
			RemoveFromHeap(doc);
			MyBtree.put(doc.getKey(), null);
		}
		stack.push(command);
		return URIs;
	}

	
	private void removeDoc(DocumentImpl doc, String words) { 
		String[] text= findWords(doc.getDocumentAsTxt());
		 for(String inputWord : text) {
			 ////System.out.println("input: " +words +", " +inputWord);
			 if(inputWord.equals(words)) {
				 continue;
			 }
			 
			 trie.delete(inputWord, doc.getKey());
			 ////System.out.println("delete");
		 }	
		
	}

	public Set<URI> deleteAllWithPrefix(String prefix) {
		if(prefix==null) {
			return new HashSet<URI>();
		}
		String words = prefix;
		words =words.toUpperCase();
		Set<URI> docs= trie.deleteAllWithPrefix(words);
		CommandSet<URI> command = new CommandSet<URI>();
		Set<URI> URIs = new HashSet<URI>();
		for(URI uri : docs) {
			DocumentImpl doc =MyBtree.get(uri);
			removeDocPrefix(doc, words);
			URIs.add(doc.getKey());
			
			GenericCommand<URI> commands = new GenericCommand<URI>(doc.getKey(), (URI) -> {
				doc.setLastUseTime(System.nanoTime());
				if(doc.getMinfile()==null) {//make minfile
					doc.setMinfile(new MinFile(doc.getKey()));
				}
				MyBtree.put(doc.getKey(), doc);
				AddToHeap(doc);
				////System.out.println("Text "+doc.getDocumentAsTxt());
				
				putAll(doc);
				return true;
			});
			command.addCommand(commands);
			RemoveFromHeap(doc);
			MyBtree.put(doc.getKey(), null);
		}
		stack.push(command);
		return URIs;
	}
	
	private void removeDocPrefix(DocumentImpl doc, String words) {
		String[] text= findWords(doc.getDocumentAsTxt());
		 for(String inputWord : text) {
			 ////System.out.println("input: " +words +", " +inputWord);
			 if(inputWord.startsWith(words)) {
				 continue;
			 }
			 
			 trie.delete(inputWord, doc.getKey());
			 ////System.out.println("delete");
		 }	
		
	}

	private void removeAll(DocumentImpl doc2) {
		String[] text= findWords(doc2.getDocumentAsTxt());
		 for(String inputWord : text) {
			 //System.out.println("d " +inputWord);
			 trie.delete(inputWord, doc2.getKey());
		 }	
	}

	private void putAll(DocumentImpl doc) {
		String[] text= findWords(doc.getDocumentAsTxt());
		 for(String inputWord : text) {
			 
			 //System.out.println("p " +inputWord +", " + doc.getDocumentAsTxt());
			 trie.put(inputWord, doc.getKey());
		 }
		
	}
	private String[] findWords(String txt) {
		String words = txt;
		words =words.toUpperCase();
		words= words.replaceAll("[^A-Za-z0-9 ]","");
		words= words.replaceAll("  ", " ");
		//System.out.println(words);
		Set<String> allWords = new HashSet<String>();
        String[] inputWords = words.split(" ");
        for(String inputWord : inputWords){
            inputWord = inputWord.trim();
            //System.out.println("word : " +inputWord);
            allWords.add(inputWord);
        }
        return allWords.toArray(new String[0]);
	}
	
	class DocumentComparator<Value> implements Comparator<URI> {
		private String keyword;

		public DocumentComparator(String keyword) {
			this.keyword = keyword.toUpperCase();
		}

		@Override
		public int compare(URI doc1, URI doc2) {
			int doc1Count = MyBtree.get(doc1).wordCount(keyword);
			int doc2Count = MyBtree.get(doc2).wordCount(keyword);
			//System.out.println("Doc1 " +doc1Count);
			//System.out.println("Doc2 " +doc2Count);
			if (doc1Count < doc2Count) {
				return 1;
			}
			if (doc1Count == doc2Count) {
				return 0;
			}
			return -1;
		}
	}
	
	class PrefixComparator<Value> implements Comparator<URI> {
		private String keyword;

		public PrefixComparator(String keyword) {
			this.keyword = keyword;
		}
		private String[] findWords(String txt) {
			String words = txt;
			words =words.toUpperCase();
			words= words.replaceAll("[^A-Za-z0-9 ]","");
			words= words.replaceAll("  ", " ");
			//System.out.println(words);
			
	        String[] inputWords = words.split(" ");
	       
	        
	        return inputWords;
		}

		@Override
		public int compare(URI uri1, URI uri2) {
			String[] doc1Words = findWords((MyBtree.get(uri1)).getDocumentAsTxt());
			String[] doc2Words = findWords(( MyBtree.get(uri2)).getDocumentAsTxt());
			
			int doc1Count = prefixCount(doc1Words);
			int doc2Count = prefixCount(doc2Words);
			//System.out.println("compare");
			//System.out.println(doc1.getDocumentAsTxt() +doc1Count);
			//System.out.println(doc2.getDocumentAsTxt() +doc2Count);
			if (doc1Count < doc2Count) {
				return 1;
			}
			if (doc1Count == doc2Count) {
				return 0;
			}
			return -1;
		}
		private int prefixCount(String[] docWords) {
			int count =0;
			for(String word : docWords) {
				if(word.startsWith(keyword)) {
					count++;
				}
			}
			return count;
		}

	}
	@Override
	public void setMaxDocumentCount(int limit) {
		MaxDocs = limit;
		
		while(MaxDocs<DocCount) {
			MinFile minfile = heap.removeMin();
			DocumentImpl remove = minfile.GetDoc();
			int Bytes = remove.getDocumentAsPdf().length + remove.getDocumentAsTxt().length();
			ByteCount-= Bytes;
			DocCount--;
			try {//tree
				MyBtree.moveToDisk(remove.getKey());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// TODO Auto-generated method stub
		
	}



	@Override
	public void setMaxDocumentBytes(int limit) {
		// TODO Auto-generated method stub
		MaxBytes = limit;
		while(MaxBytes<ByteCount) {
			MinFile minfile = heap.removeMin();
			DocumentImpl remove = minfile.GetDoc();
			int Bytes = remove.getDocumentAsPdf().length + remove.getDocumentAsTxt().length();
			ByteCount-= Bytes;
			DocCount--;
			try {//tree
				MyBtree.moveToDisk(remove.getKey());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	
	protected class MinFile implements Comparable<MinFile> {
		private URI uri;
		protected MinFile(URI u){
			this.uri=u;
		}
		
		URI GetURI() {
			return uri;

		}
		DocumentImpl GetDoc() {
			return MyBtree.get(uri);
			
		}

		@Override
		public int compareTo(MinFile File2) {
			// TODO Auto-generated method stub
			DocumentImpl d1=this.GetDoc();
			System.out.println("d1= " +d1);
			DocumentImpl d2=File2.GetDoc();
			System.out.println("d2= " +d2);
			System.out.println("doc uri from minfile: " + uri + ", doc2 uri: " +File2.GetURI());
			System.out.println("doc text from minfile: " + d1.getDocumentAsTxt() + ", doc2 text: " +d2.getDocumentAsTxt());
			System.out.println("doc time from minfile: " + d1.getLastUseTime() + ", doc2 time: " +d2.getLastUseTime());
			int x =d1.compareTo(d2);
			System.out.println("x= " +x);
			
			return x;
		}
	}
	
}


