package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.stage5.Document;

import edu.yu.cs.com1320.project.stage5.PersistenceManager;
import edu.yu.cs.com1320.project.stage5.impl.DocumentPersistenceManager.DocumentSerializer;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * created by the document store and given to the BTree via a call to BTree.setPersistenceManager
 */
public class DocumentPersistenceManager implements PersistenceManager<URI, Document> {

	File file;

	public DocumentPersistenceManager(File baseDir){
		file = baseDir;
    }

    @Override
    public void serialize(URI uri, Document val) throws IOException {
   
    	Gson gson = new GsonBuilder().registerTypeAdapter(Document.class, new DocumentSerializer()).setPrettyPrinting().create();
    	Type type = new TypeToken<URI>() {
		}.getType();
    	String uri2 = uri.getRawSchemeSpecificPart().toString();
    	String uri3 = file.toString()+File.separatorChar + uri2;
    	
    	System.out.println(uri.getRawSchemeSpecificPart());
    		System.out.println(uri3);
    		
		//URI newuri = new URI(file.getPath(),uri.getHost(),uri.getPath(),".json");
    		File fullfile=new File(uri3);
    		System.out.println("fullFile " +fullfile);
		File newfile = new File(fullfile.toString().substring(0, fullfile.toString().lastIndexOf(File.separatorChar))); 
		System.out.println(" ");
		System.out.println("new File " +newfile);
		Files.createDirectories(newfile.toPath());
		FileWriter filewriter = new FileWriter(uri3+ ".json");
		System.out.println("json file " +filewriter);
		Type Type = new TypeToken<Document>() {}.getType();
		gson.toJson(val, Type, filewriter);
		filewriter.close();
		//FileWriter file3 =new FileWriter("file.json");
		//BufferedWriter writer = new BufferedWriter(new FileWriter(newfile.toString() +".json"));
		//file3.write(gson.toJson(val));
		//file3.flush();
		//writer.close();
		//System.out.println(gson.toJson(val));
    }
    class DocumentSerializer implements JsonSerializer<Document>  {

		@Override
		public JsonElement serialize(Document src, Type typeOfSrc, JsonSerializationContext context) {
			// TODO Auto-generated method stub
			//put in info that will be serialized
			JsonObject jObject = new JsonObject();
			String text = "text";
			String uri = "uri"; 
			String hash = "hash";
			String theWords = "theWords";
			Gson gson2 = new Gson();
			JsonElement jsonHashMap = gson2.toJsonTree(src.getWordMap());
			jObject.addProperty(text, src.getDocumentAsTxt());
			jObject.addProperty(uri, src.getKey().toString());
			jObject.addProperty(hash, src.getDocumentTextHashCode());
			jObject.add(theWords, jsonHashMap);
			return jObject;
		}

   	}
    @Override
    public Document deserialize(URI uri) throws IOException {
    	System.out.println("uri " +uri);
    	String uri2 = uri.getRawSchemeSpecificPart().toString();
    	String uri3 = file.toString()+File.separatorChar  + uri2+".json";
    	 Path path = Paths.get(uri3);
    	System.out.println("path " +path);
    	String file; 
    	try {
    		 file = new String(Files.readAllBytes(path));
    	}catch (NoSuchFileException e) {
    		System.out.println("file is noth there " +path);
    		return null;
    	}
    	Gson gson = new GsonBuilder().registerTypeAdapter(Document.class, new DocumentDeserialiser()).create();
    	Type Type = new TypeToken<Document>() {}.getType();
    	Document NewDoc = gson.fromJson(file, Type);
    	deletefile(path);
        return NewDoc;
    }
    
    private void deletefile(Path path) {
    	File file = path.toFile();
    	System.out.println(path);
		Boolean b=true;
		try {
			Files.delete(path);
			path=path.getParent();
			file = path.toFile();
			System.out.println("list "+ file.list().length);
			
		while(b==true){
				if(file.list().length==0) {
					System.out.println("path to delete " +path);
				Files.delete(path);
				System.out.println(b);
				//System.out.println("\\");
				System.out.println("new path " +path);
				path=path.getParent();
				System.out.println("new path " +path);
				if (path== null) {
					break;
				}
				file = path.toFile();
				System.out.println("new new path " +file);
				}
				else {
					b=false;
					System.out.println("not empty");
				}
		}
				
			} catch (DirectoryNotEmptyException e) {
				// TODO Auto-generated catch block
				b=false;
				System.out.println(b);
				System.out.println("not empty");
				//e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	public class DocumentDeserialiser implements JsonDeserializer<Document> {
    	@Override
    	public Document deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    		JsonObject name = json.getAsJsonObject();
    		JsonObject map =json.getAsJsonObject().getAsJsonObject("theWords");
            
    		Gson gson = new Gson();
    		 Type type = new TypeToken<HashMap<String, Integer>>(){}.getType();
    		HashMap<String, Integer> clonedMap = gson.fromJson(map, type); 
    		Document Doc = new DocumentImpl(
                    name.get("text").getAsString(),
                    name.get("uri").getAsString(),
                    name.get("hash").getAsInt(),
                    clonedMap
            );

    		return Doc;
    	}
   
 
     
    }
}