package edu.yu.cs.com1320.project.stage2.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URI;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import edu.yu.cs.com1320.project.Command;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.stage2.Document;
import edu.yu.cs.com1320.project.stage2.DocumentStore;

public class DocumentStoreImpl implements DocumentStore {

	public DocumentStoreImpl() {
	}

	HashTableImpl<URI, DocumentImpl> store = new HashTableImpl<URI, DocumentImpl>();
	StackImpl<Command> stack = new StackImpl<Command>();

	public int putDocument(InputStream input, URI uri, DocumentFormat format) {
		if (uri == null) {
			throw new IllegalArgumentException();
		}
		if (format == null) {
			throw new IllegalArgumentException();
		}
		if (input == null) {
			if (store.get(uri) == null) {
				Command command = new Command(uri, (URI) -> {
						return true;});
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
			return paperHashCode;
		} else if (store.get(uri) == null) {
			DocumentImpl doc = new DocumentImpl(uri, paper, paperHashCode, b);
			store.put(uri, doc);
			Command command = new Command(uri, (URI) -> {
				try {
					store.put(uri, null);
					return true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					return false;
				}
			});
			stack.push(command);
			return 0;
		} else {
			DocumentImpl doc = new DocumentImpl(uri, paper, paperHashCode, b);
			DocumentImpl doc2 = store.get(uri);
			paperHashCode = doc2.getDocumentTextHashCode();
			Command command = new Command(uri, (URI) -> {
				try {
					store.put(uri, doc2);
					return true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					return false;
				}
			});
			stack.push(command);
			store.put(uri, doc);
			return doc2.getDocumentTextHashCode();
		}

	}

	private int addTxt(byte[] b, URI uri) {
		String paper = new String(b);
		int paperHashCode = paper.hashCode();
		// System.out.println("words: " + paper);
		if (store.get(uri) != null && store.get(uri).getDocumentTextHashCode() == paperHashCode) {
			// System.out.println("same text");
			return paperHashCode;
		} else {
			DocumentImpl doc = new DocumentImpl(uri, paper, paperHashCode);
			if (store.get(uri) == null) {
				store.put(uri, doc);
				Command command = new Command(uri, (URI) -> {
					try {
						store.put(uri, null);
						return true;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						return false;
					}
				});
				stack.push(command);
				return 0;
			} else {
				DocumentImpl doc2 = store.get(uri);
				Command command = new Command(uri, (URI) -> {
					try {
						store.put(uri, doc2);
						return true;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						return false;
					}
				});
				stack.push(command);
				store.put(uri, doc);

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
		return x.getDocumentAsTxt();
	}

	public boolean deleteDocument(URI uri) {
		if (uri == null) {
			throw new IllegalArgumentException();
		}
		if (store.get(uri) == null) {
			Command command = new Command(uri, (URI) -> {
				return true;});
			return false;
		}
		if (store.get(uri) != null) {
			DocumentImpl doc = store.put(uri, null);
			Command command = new Command(uri, (URI) -> {
				store.put(uri, doc);
				return true;
			});
			stack.push(command);
			return true;
		}
		return false;
	}

	/**
	 * undo the last put or delete command
	 * 
	 * @throws IllegalStateException if there are no actions to be undone, i.e. the
	 *                               command stack is empty
	 */
	public void undo() throws IllegalStateException {
		if (stack.size() == 0) {
			throw new IllegalStateException();
		}
		Command LastIn = stack.pop();
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
		StackImpl<Command> hold = new StackImpl<Command>();
		Command command = stack.pop();
		while (!(command.getUri().equals(uri))) {

			hold.push(command);
			command = stack.pop();
			if (command == null) {
				throw new IllegalStateException();
			}
		}
		command.undo();
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
}
