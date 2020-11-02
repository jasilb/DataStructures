package edu.yu.cs.com1320.project.stage5.impl;

import static org.junit.Assert.*;


import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.impl.*;

import org.junit.Test;

public class documentPMTest {

	@Test
	public void serialize1() throws Exception {
		URI a = new URI ("https://testnumber2/test3/test4");
		DocumentImpl test = new DocumentImpl(a, "test 1", 15);
		DocumentPersistenceManager DPM = new DocumentPersistenceManager(new File("/home/jake/Documents"));
		DPM.serialize(a, test);
		System.out.println("NewDoc");
		Document test2 =DPM.deserialize(a);
		System.out.println(test2.getDocumentAsTxt());
	}

}
