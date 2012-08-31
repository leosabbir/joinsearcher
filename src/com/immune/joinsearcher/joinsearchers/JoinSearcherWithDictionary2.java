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
import com.immune.joinsearcher.factory.IndexFactory;
import com.immune.joinsearcher.models.FieldNamesMap;
import com.immune.joinsearcher.models.FieldValuesMap;
import com.immune.joinsearcher.models.JoinCriteria;
import com.immune.joinsearcher.models.TableMap;
import com.immune.joinsearcher.models.constants.IndexTables;

public class JoinSearcherWithDictionary2 implements JoinSearcher {
	
	public static final String KEY_TOKENIZER = ";";
	
	//lookuptablename ==> id;name ==> 2;author1 ==> keys/values
	private TableMap authorsDictionaries;
	
	private IndexSearcher booksSearcher;
	
	StandardAnalyzer analyzer;
	
	public JoinSearcherWithDictionary2(IndexSearcher booksIndexSearcher) throws IOException, ParseException{
		authorsDictionaries = new TableMap();
		
		analyzer = new StandardAnalyzer(Version.LUCENE_30);
		
		this.booksSearcher = booksIndexSearcher;
	}
	
	public FieldValuesMap getDictionary(IndexTables lookupTable, String fields) throws ParseException, IOException{
		
		String lookupTableName = lookupTable.name();
		
		String[] fieldsArrays = fields.split(KEY_TOKENIZER);
		
		String fieldNamesAsKey = Utils.tokenize(Arrays.asList(fieldsArrays), KEY_TOKENIZER);
		
		if(this.authorsDictionaries.getFieldNamesMapForTable(lookupTableName) == null){
			this.authorsDictionaries.addFieldNamesMap(lookupTableName, new FieldNamesMap());
		}
		
		
		if(authorsDictionaries.getFieldNamesMapForTable(lookupTableName).getFieldValuesMap(fieldNamesAsKey) == null){
			Query authorsQuery = new QueryParser(Version.LUCENE_30, "*", analyzer).parse("*");
			//TopDocs authorsResults = this.authorsSearcher.search(authorsQuery, Integer.MAX_VALUE);
			IndexSearcher lookupIndexSearcher = IndexFactory.getIndexSearcher(lookupTable);
			TopDocs authorsResults = lookupIndexSearcher.search(authorsQuery, Integer.MAX_VALUE);
			
			FieldValuesMap fieldValuesMap = new FieldValuesMap();
			for (ScoreDoc scoreDoc : authorsResults.scoreDocs) {
				Map<String, String> keyValues = new HashMap<String, String>();
				
				Document doc = lookupIndexSearcher.doc(scoreDoc.doc);
				List<String> values = new ArrayList<String>();
				boolean fieldExists = false;
				for (String fieldName : fieldsArrays) {
					fieldExists = false;
					if(doc.get(fieldName) != null){
						fieldExists = true;
						values.add(doc.get(fieldName));
					}else{
						values.add("NONE");
					}
				}
				for (Fieldable field : doc.getFields()) {
					keyValues.put(field.name(), field.stringValue());
				}
				if(fieldExists){
					//fieldValuesMap.put(Utils.sortTokenize((values), KEY_TOKENIZER), keyValues);
					fieldValuesMap.put(Utils.tokenize((values), KEY_TOKENIZER), keyValues);
				}
			}
			this.authorsDictionaries.getFieldNamesMapForTable(lookupTableName).addFieldValuesMap(fieldNamesAsKey, fieldValuesMap);
		}

		return this.authorsDictionaries.getFieldNamesMapForTable(lookupTableName).getFieldValuesMap(fieldNamesAsKey);
		
	}
	
	public List<Map<String, String>> searchAndJoin(List<JoinCriteria> joinCriteria, Query booksQuery) throws ParseException, IOException{
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		Map<String, String> keyValuesAll = new HashMap<String, String>();
		
		TopDocs booksResults = this.booksSearcher.search(booksQuery, Integer.MAX_VALUE);
		
		List<Map<String, String>> listOfKeyValues  = new ArrayList<Map<String,String>>();
		
		for (ScoreDoc scoreDoc : booksResults.scoreDocs) {
			Document doc = this.booksSearcher.doc(scoreDoc.doc);
			listOfKeyValues.clear();
			
			/**Loop here append from each criteria to listOfKeyValues**/
			for (JoinCriteria joinCriterium: joinCriteria) {
				List<String> joinValues = new ArrayList<String>();
				
				for (String fromField : joinCriterium.getFromFields()) {
					if(doc.get(fromField) != null){
						joinValues.add(doc.get(fromField));
					}else{
						joinValues.add("NONE");
					}
				}
				
				Map<String, Map<String, String>> authorsDictionary =  getDictionary(joinCriterium.getLookupTable(), Utils.tokenize(Arrays.asList(joinCriterium.getToFields()), KEY_TOKENIZER));
				Map<String, String> keyValues = authorsDictionary.get(Utils.tokenize(joinValues, KEY_TOKENIZER));
				
				if(keyValues != null){
					listOfKeyValues.add(keyValues);
				}
			}
			
			/**loop ends*/
			
			
			keyValuesAll = new HashMap<String, String>();
			for (Fieldable field : doc.getFields()) {
				keyValuesAll.put(field.name(), field.stringValue());
			}
			//loop listOfKeyValues to append all fields
			//System.out.println(listOfKeyValues.size());
			for (Map<String, String> keyValues : listOfKeyValues) {
				for (String key : keyValues.keySet()) {
					keyValuesAll.put(key, keyValues.get(key));
				}
			}
			
			result.add(keyValuesAll);
		}
		
		return result;
	}

}
