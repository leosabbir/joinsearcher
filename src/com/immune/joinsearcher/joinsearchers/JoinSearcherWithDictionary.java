package com.immune.joinsearcher.joinsearchers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.immune.joinsearcher.utils.Utils;

public class JoinSearcherWithDictionary {
	
	private static final String KEY_TOKENIZER = ";";
	
	//lookuptablename ==> id;name ==> 2;author1 ==> keys/values
	Map<String, Map<String, Map<String, String>>> authorsDictionaries;
	
	private IndexSearcher booksSearcher;
	private IndexSearcher authorsSearcher;
	
	StandardAnalyzer analyzer;
	
	public JoinSearcherWithDictionary(IndexSearcher booksIndexSearcher, IndexSearcher authorsIndexSearcher) throws IOException, ParseException{
		authorsDictionaries = new HashMap<String, Map<String,Map<String,String>>>();
		
		analyzer = new StandardAnalyzer(Version.LUCENE_30);
		
		this.booksSearcher = booksIndexSearcher;
		this.authorsSearcher = authorsIndexSearcher;
	}
	
	public Map<String, Map<String, String>> getDictionary(String fields) throws ParseException, IOException{
		String[] fieldsArrays = fields.split(KEY_TOKENIZER);
		
		String key = Utils.sortTokenize(Arrays.asList(fieldsArrays), KEY_TOKENIZER);
		
		if( authorsDictionaries.get(key) == null){
			Query authorsQuery = new QueryParser(Version.LUCENE_30, "*", analyzer).parse("*");
			TopDocs authorsResults = this.authorsSearcher.search(authorsQuery, Integer.MAX_VALUE);
			
			Map<String, Map<String, String>> tokensDictionaries = new HashMap<String, Map<String,String>>();
			for (ScoreDoc scoreDoc : authorsResults.scoreDocs) {
				Map<String, String> keyValues = new HashMap<String, String>();
				
				Document doc = this.authorsSearcher.doc(scoreDoc.doc);
				String[] values = new String[fieldsArrays.length];
				int i = 0;
				for (String fieldName : fieldsArrays) {
					if(doc.get(fieldName) != null){
						values[i++] = doc.get(fieldName);
					}else{
						values[i++] = doc.get("NONE");
					}
				}
				for (Fieldable field : doc.getFields()) {
					keyValues.put(field.name(), field.stringValue());
				}
				tokensDictionaries.put(Utils.sortTokenize(Arrays.asList(values), KEY_TOKENIZER), keyValues);
			}
			this.authorsDictionaries.put(key, tokensDictionaries);
		}

		return this.authorsDictionaries.get(key);
		
	}
	
	public List<Map<String, String>> searchAndJoin(String[] fromFields, String[] toFields, Query booksQuery) throws ParseException, IOException{
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		Map<String, String> keyValuesAll = new HashMap<String, String>();
		
		TopDocs booksResults = this.booksSearcher.search(booksQuery, Integer.MAX_VALUE);
		
		for (ScoreDoc scoreDoc : booksResults.scoreDocs) {
			Document doc = this.booksSearcher.doc(scoreDoc.doc);
			
			String[] joinValues = new String[fromFields.length];
			int i = 0;
			for (String fromField : fromFields) {
				if(doc.get(fromField) != null){
					joinValues[i++] = doc.get(fromField);
				}else{
					joinValues[i++] = "NONE";
				}
			}
			
			Map<String, Map<String, String>> authorsDictionary =  getDictionary(Utils.sortTokenize(Arrays.asList(toFields), KEY_TOKENIZER));
			Map<String, String> keyValues = authorsDictionary.get(Utils.sortTokenize(Arrays.asList(joinValues), KEY_TOKENIZER));
			
			keyValuesAll = new HashMap<String, String>();
			for (Fieldable field : doc.getFields()) {
				keyValuesAll.put(field.name(), field.stringValue());
			}
			if(keyValues != null){
				for (String key : keyValues.keySet()) {
					keyValuesAll.put(key, keyValues.get(key));
				}
			}
			
			result.add(keyValuesAll);
			
		}
		
		return result;
	}
	
	public static void main(String[] args) throws ParseException, IOException {
		
		String mainIndexDirPath = "/Users/sabbirmanandhar/Documents/eclipseWorkspaces/loginspect/booksIndexDir";
		String createrIndexDirPath = "/Users/sabbirmanandhar/Documents/eclipseWorkspaces/loginspect/authorsIndexDir";
		
		Directory mainIndexDir = FSDirectory.open(new File(mainIndexDirPath));
		Directory createrIndexDir = FSDirectory.open(new File(createrIndexDirPath));
		
		JoinSearcherWithDictionary searcher = new JoinSearcherWithDictionary(new IndexSearcher(mainIndexDir), new IndexSearcher(createrIndexDir));
		
		//Constraint: fields must be in order in both i.e corresponding field in same location
		String[] fromFields = {"authorId", "authorName"};
		String[] toFields = {"id2", "name2"};
		
		Query booksQuery = new QueryParser(Version.LUCENE_30, "content", new StandardAnalyzer(Version.LUCENE_30)).parse("written");
		
		long beforeFirst = System.currentTimeMillis();
		System.out.println(searcher.searchAndJoin(fromFields, toFields, booksQuery).size());
		System.out.println(System.currentTimeMillis() - beforeFirst);
		long beforeSecond = System.currentTimeMillis();
		System.out.println(searcher.searchAndJoin(fromFields, toFields, booksQuery).get(0).keySet().size());
		System.out.println(System.currentTimeMillis() - beforeSecond);
		
		beforeSecond = System.currentTimeMillis();
		searcher.searchAndJoin(fromFields, toFields, booksQuery);
		System.out.println(System.currentTimeMillis() - beforeSecond);
	}

}
