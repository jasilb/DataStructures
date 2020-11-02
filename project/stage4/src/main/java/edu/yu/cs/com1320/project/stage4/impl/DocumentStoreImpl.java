package edu.yu.cs.com1320.project.stage4.impl;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
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
//import edu.yu.cs.com1320.project.Command;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage4.Document;
import edu.yu.cs.com1320.project.stage4.DocumentStore;

public class DocumentStoreImpl implements DocumentStore {

	public DocumentStoreImpl() {
	}
	private int DocCount=0;
	private int ByteCount=0;
	private int MaxDocs =-1;
	private int MaxBytes =-1;
	HashTableImpl<URI, DocumentImpl> store = new HashTableImpl<URI, DocumentImpl>();
	StackImpl<Undoable> stack = new StackImpl<Undoable>();
	TrieImpl<DocumentImpl> trie = new TrieImpl<DocumentImpl>();
	MinHeapImpl<DocumentImpl> heap = new MinHeapImpl<DocumentImpl>();
	


	public int putDocument(InputStream input, URI uri, DocumentFormat format) {
		if (uri == null) {
			throw new IllegalArgumentException();
		}
		if (format == null) {
			throw new IllegalArgumentException();
		}
		if (input == null) {
			if (store.get(uri) == null) {
				GenericCommand<URI> command = new GenericCommand<URI>(uri, (URI) -> {
						return true;});
				stack.push(command);
				return 0;
			}
			int oldhash = store.get(uri).getDocumentTextHashCode();
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
		if (store.get(uri) != null && store.get(uri).getDocumentTextHashCode() == paperHashCode) {
			DocumentImpl fixDoc = store.get(uri);
			fixDoc.setLastUseTime(System.nanoTime());
			heap.reHeapify(fixDoc);
			return paperHashCode;
		} else if (store.get(uri) == null) {
			DocumentImpl doc = new DocumentImpl(uri, paper, paperHashCode, b);
			store.put(uri, doc);
			putAll(doc);
			GenericCommand<URI> command = new GenericCommand<URI>(uri, (URI) -> {
				try {
					RemoveFromHeap(doc);
					store.put(uri, null);
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
		} else {
			DocumentImpl doc = new DocumentImpl(uri, paper, paperHashCode, b);
			DocumentImpl doc2 = store.get(uri);
			paperHashCode = doc2.getDocumentTextHashCode();
			GenericCommand<URI> command = new GenericCommand<URI>(uri, (URI) -> {
				try {
					doc2.setLastUseTime(System.nanoTime());
					ReplaceInHeap(doc, doc2);
					store.put(uri, doc2);
					removeAll(doc);
					putAll(doc2);
					return true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					return false;
				}
			});
			stack.push(command);
			store.put(uri, doc);
			removeAll(doc2);
			putAll(doc);
			ReplaceInHeap(doc2, doc);
			return doc2.getDocumentTextHashCode();
		}

	}



	private void ReplaceInHeap(DocumentImpl olddoc, DocumentImpl newdoc) {
		// TODO Auto-generated method stub
		//System.out.println("replace: ");
		int Bytes = olddoc.getDocumentAsPdf().length + olddoc.getDocumentAsTxt().length();
		olddoc.setLastUseTime(Long.MIN_VALUE);
		heap.reHeapify(olddoc);
		heap.removeMin();
		ByteCount-= Bytes;
		DocCount--;
		AddToHeap(newdoc);
	}

	private void AddToHeap(DocumentImpl doc) {
		// TODO Auto-generated method stub
		System.out.println("put: " + doc);
		int Bytes = doc.getDocumentAsPdf().length + doc.getDocumentAsTxt().length();
		System.out.println("max bytes: " + this.MaxBytes);
		System.out.println("new bytes: " + Bytes);
		if((Bytes> MaxBytes) && (MaxBytes != -1)) {
			throw new IllegalArgumentException();
		}
		else if((DocCount+1 <=MaxDocs) || (MaxDocs ==-1)) {
			if (ByteCount+Bytes >MaxBytes && MaxBytes !=-1 ){
				DocumentImpl removed; 
				while (ByteCount+Bytes >MaxBytes) {
					removed = heap.removeMin();
					int RemovedBytes = removed.getDocumentAsPdf().length + removed.getDocumentAsTxt().length();
					ByteCount-= RemovedBytes;
					DocCount--;
					RemoveFromStack(removed);
				}
			}
				heap.insert(doc);
				DocCount++;
				//System.out.println("DocCount In Store: " +DocCount);
				ByteCount+= Bytes;	
				System.out.println("ByteCount In Store: " +ByteCount);
				return;
		}
		 else {
			 //System.out.println("remove");
			 DocumentImpl removed = heap.removeMin();
			 int RemovedBytes = removed.getDocumentAsPdf().length + removed.getDocumentAsTxt().length();
			 ByteCount-= RemovedBytes;
			 DocCount--;
			 RemoveFromStack(removed);
			 if (ByteCount+Bytes >MaxBytes && MaxBytes !=-1 ){

					while (ByteCount+Bytes >MaxBytes) {
						//System.out.println("remove");
						removed = heap.removeMin();
						RemovedBytes = removed.getDocumentAsPdf().length + removed.getDocumentAsTxt().length();
						ByteCount-= RemovedBytes;
						DocCount--;
						RemoveFromStack(removed);
					}
				}
					heap.insert(doc);
					DocCount++;
					//System.out.println("DocCount In Store: " +DocCount);
					ByteCount+= Bytes;
					//System.out.println("ByteCount In Store: " +ByteCount);
					return;
		 }
		 
		
	}

	private void RemoveFromStack(DocumentImpl removed) {
		// TODO Auto-generated method stub
		URI uri = removed.getKey();
		store.put(uri, null);
		if (stack.size() == 0) {
			throw new IllegalStateException();
		}
		StackImpl<Undoable> hold = new StackImpl<Undoable>();
		Undoable command = stack.pop();
		while(true) {
			if (command == null) {
				break;
			}
			 if( command instanceof GenericCommand) {
				GenericCommand<URI>	GC = (GenericCommand<URI>) command;
				if(GC.getTarget().equals(uri)) {
					command = null;
				}
				else {
					hold.push(command);
				}
			}
			 if( command instanceof CommandSet) {
				CommandSet<URI> CS =  (CommandSet<URI>) command;
				if(CS.containsTarget(uri)) {
					//how do i get the command from that set
					Iterator<GenericCommand<URI>> list = CS.iterator();
					GenericCommand<URI> InList = list.next();
					while(list.hasNext()) {
						if(InList.getTarget().equals(uri)) {
							list.remove();
							break;
						}	
						InList= list.next();
					}
					if(CS.size()!=0) {
						hold.push(command);
					}	
				}
				else {
					hold.push(command);
				}
			}
			 command = stack.pop();
		}
		while (hold.peek() != null) {
			command = hold.pop();
			stack.push(command);
		}
	}

	private int addTxt(byte[] b, URI uri) {
		String paper = new String(b);
		int paperHashCode = paper.hashCode();
		////System.out.println("words: " + paper);
		if (store.get(uri) != null && store.get(uri).getDocumentTextHashCode() == paperHashCode) {
			////System.out.println("same text");
			DocumentImpl fixDoc = store.get(uri);
			fixDoc.setLastUseTime(System.nanoTime());
			heap.reHeapify(fixDoc);
			return paperHashCode;
		} else {
			DocumentImpl doc = new DocumentImpl(uri, paper, paperHashCode);
			if (store.get(uri) == null) {
				store.put(uri, doc);
				putAll(doc);
				GenericCommand<URI> command = new GenericCommand<URI>(uri, (URI) -> {
					try {
						RemoveFromHeap(doc);
						store.put(uri, null);
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
			} else {
				DocumentImpl doc2 = store.get(uri);
				GenericCommand<URI> command = new GenericCommand<URI>(uri, (URI) -> {
					try {
						doc2.setLastUseTime(System.nanoTime());
						ReplaceInHeap(doc, doc2);
						store.put(uri, doc2);
						removeAll(doc);
						putAll(doc2);
						return true;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						return false;
					}
				});
				stack.push(command);
				store.put(uri, doc);
				removeAll(doc2);
				putAll(doc);
				ReplaceInHeap(doc2, doc);
				return doc2.getDocumentTextHashCode();
			}
		}
	}

	public byte[] getDocumentAsPdf(URI uri) {
		// TODO Auto-generated method stub
		if (uri == null) {
			throw new IllegalArgumentException();
		}
		DocumentImpl x = store.get(uri);
		if (x == null) {
			return null;
		}
		x.setLastUseTime(System.nanoTime());
		heap.reHeapify(x);
		return x.getDocumentAsPdf();
	}

	public String getDocumentAsTxt(URI uri) {
		// TODO Auto-generated method stub
		if (uri == null) {
			throw new IllegalArgumentException();
		}
		DocumentImpl x = store.get(uri);
		if (x == null) {
			return null;
		}
		x.setLastUseTime(System.nanoTime());
		heap.reHeapify(x);
		return x.getDocumentAsTxt();
	}

	public boolean deleteDocument(URI uri) {
		if (uri == null) {
			throw new IllegalArgumentException();
		}
		if (store.get(uri) == null) {
			GenericCommand<URI> command = new GenericCommand<URI>(uri, (URI) -> {
				return true;});
			return false;
		}
		if (store.get(uri) != null) {
			DocumentImpl doc = store.put(uri, null);
			removeAll(doc);
			RemoveFromHeap(doc);
			GenericCommand<URI> command = new GenericCommand<URI>(uri, (URI) -> {
				doc.setLastUseTime(System.nanoTime());
				AddToHeap(doc);
				store.put(uri, doc);
				putAll(doc);
				return true;
			});
			stack.push(command);
			return true;
		}
		return false;
	} 

	private void RemoveFromHeap(DocumentImpl doc) {
		// TODO Auto-generated method stub
		int Bytes = doc.getDocumentAsPdf().length + doc.getDocumentAsTxt().length();
		doc.setLastUseTime(Long.MIN_VALUE);
		heap.reHeapify(doc);
		heap.removeMin();
		ByteCount-= Bytes;
		DocCount--;
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
		return store.get(uri);
	}


	public List<String> search(String keyword) {
		if(keyword==null) {
			return new ArrayList<String>();
		}
		// TODO Auto-generated method stub
		String words = keyword;
		words =words.toUpperCase();
		Comparator<DocumentImpl> compare = new DocumentComparator<DocumentImpl>(words);
		List<DocumentImpl> docs = trie.getAllSorted(words, compare); 
		List<String> returns = new ArrayList<String>();
		long time = System.nanoTime();
		for(DocumentImpl doc: docs) {
			returns.add(doc.getDocumentAsTxt());
			doc.setLastUseTime(time);
			heap.reHeapify(doc);
		}
		return returns;
	}


	public List<byte[]> searchPDFs(String keyword) {
		if(keyword==null) {
			return new ArrayList<byte[]>();
		}
		String words = keyword;
		words =words.toUpperCase();
		Comparator<DocumentImpl> compare = new DocumentComparator<DocumentImpl>(words);
		List<DocumentImpl> docs = trie.getAllSorted(words, compare); 
		List<byte[]> returns = new ArrayList<byte[]>();
		long time = System.nanoTime();
		for(DocumentImpl doc: docs) {
			returns.add(doc.getDocumentAsPdf());
			doc.setLastUseTime(time);
			heap.reHeapify(doc);
		}
		return returns;
		
	}


	public List<String> searchByPrefix(String prefix) {
		if(prefix==null) {
			return new ArrayList<String>();
		}
		String words = prefix;
		words =words.toUpperCase();
		Comparator<DocumentImpl> compare = new PrefixComparator<DocumentImpl>(words);
		List<DocumentImpl> docs = trie.getAllWithPrefixSorted(words, compare); 
		if(docs.size()==0) {
			return new ArrayList<String>();
		}
		List<String> returns = new ArrayList<String>();
		long time = System.nanoTime();
		for(DocumentImpl doc: docs) {
			returns.add(doc.getDocumentAsTxt());
			doc.setLastUseTime(time);
			heap.reHeapify(doc);
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
		Comparator<DocumentImpl> compare = new PrefixComparator<DocumentImpl>(words);
		List<DocumentImpl> docs = trie.getAllWithPrefixSorted(words, compare); 
		System.out.println(docs.size());
		if(docs.size()==0) {
			return new ArrayList<byte[]>();
		}
		List<byte[]> returns = new ArrayList<byte[]>();
		long time = System.nanoTime();
		for(DocumentImpl doc: docs) {
			returns.add(doc.getDocumentAsPdf());
			doc.setLastUseTime(time);
			heap.reHeapify(doc);
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
		Set<DocumentImpl> docs= trie.deleteAll(words);
		CommandSet<URI> command = new CommandSet<URI>();
		Set<URI> URIs = new HashSet<URI>();
		for(DocumentImpl doc : docs) {
			removeDoc(doc, words);
			URIs.add(doc.getKey());
			store.put(doc.getKey(), null);
			GenericCommand<URI> commands = new GenericCommand<URI>(doc.getKey(), (URI) -> {
				////System.out.println("Text: "+doc.getDocumentAsTxt());
				doc.setLastUseTime(System.nanoTime());
				AddToHeap(doc);
				store.put(doc.getKey(), doc);
				putAll(doc);
				return true;
			});
			command.addCommand(commands);
			RemoveFromHeap(doc);
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
			 
			 trie.delete(inputWord, doc);
			 ////System.out.println("delete");
		 }	
		
	}

	public Set<URI> deleteAllWithPrefix(String prefix) {
		if(prefix==null) {
			return new HashSet<URI>();
		}
		String words = prefix;
		words =words.toUpperCase();
		Set<DocumentImpl> docs= trie.deleteAllWithPrefix(words);
		CommandSet<URI> command = new CommandSet<URI>();
		Set<URI> URIs = new HashSet<URI>();
		for(DocumentImpl doc : docs) {
			removeDocPrefix(doc, words);
			URIs.add(doc.getKey());
			store.put(doc.getKey(), null);
			GenericCommand<URI> commands = new GenericCommand<URI>(doc.getKey(), (URI) -> {
				doc.setLastUseTime(System.nanoTime());
				AddToHeap(doc);
				////System.out.println("Text "+doc.getDocumentAsTxt());
				store.put(doc.getKey(), doc);
				putAll(doc);
				return true;
			});
			command.addCommand(commands);
			RemoveFromHeap(doc);
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
			 
			 trie.delete(inputWord, doc);
			 ////System.out.println("delete");
		 }	
		
	}

	private void removeAll(DocumentImpl doc2) {
		String[] text= findWords(doc2.getDocumentAsTxt());
		 for(String inputWord : text) {
			 //System.out.println("d " +inputWord);
			 trie.delete(inputWord, doc2);
		 }	
	}

	private void putAll(DocumentImpl doc) {
		String[] text= findWords(doc.getDocumentAsTxt());
		 for(String inputWord : text) {
			 
			 //System.out.println("p " +inputWord +", " + doc.getDocumentAsTxt());
			 trie.put(inputWord, doc);
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
	
	class DocumentComparator<Value> implements Comparator<DocumentImpl> {
		private String keyword;

		public DocumentComparator(String keyword) {
			this.keyword = keyword.toUpperCase();
		}

		@Override
		public int compare(DocumentImpl doc1, DocumentImpl doc2) {
			int doc1Count = doc1.wordCount(keyword);
			int doc2Count = doc2.wordCount(keyword);
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
	class PrefixComparator<Value> implements Comparator<DocumentImpl> {
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
		public int compare(DocumentImpl doc1, DocumentImpl doc2) {
			String[] doc1Words = findWords(doc1.getDocumentAsTxt());
			String[] doc2Words = findWords(doc2.getDocumentAsTxt());
			
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
		System.out.println(limit);
		while(MaxDocs<DocCount) {
			DocumentImpl remove = heap.removeMin();
			int Bytes = remove.getDocumentAsPdf().length + remove.getDocumentAsTxt().length();
			ByteCount-= Bytes;
			DocCount--;
			RemoveFromStack(remove);
		}
		
		// TODO Auto-generated method stub
		
	}



	@Override
	public void setMaxDocumentBytes(int limit) {
		// TODO Auto-generated method stub
		MaxBytes = limit;
		System.out.println(limit);
		while(MaxBytes<ByteCount) {
			DocumentImpl remove = heap.removeMin();
			int Bytes = remove.getDocumentAsPdf().length + remove.getDocumentAsTxt().length();
			ByteCount-= Bytes;
			DocCount--;
			RemoveFromStack(remove);
		}
		
		
	}

}


