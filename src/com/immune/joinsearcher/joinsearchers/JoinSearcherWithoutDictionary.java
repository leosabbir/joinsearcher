package com.immune.joinsearcher.joinsearchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;

import com.immune.joinsearcher.factory.IndexFactory;
import com.immune.joinsearcher.models.JoinCriteria;
import com.immune.joinsearcher.models.constants.IndexTables;

public class JoinSearcherWithoutDictionary implements JoinSearcher{

	private IndexSearcher booksSearcher;

	private StandardAnalyzer analyzer;

	public JoinSearcherWithoutDictionary(IndexSearcher booksIndexSearcher) throws IOException,
			ParseException {
		analyzer = new StandardAnalyzer(Version.LUCENE_30);
		this.booksSearcher = booksIndexSearcher;
		//this.authorsSearcher = authorsIndexSearcher;
		
		IndexFactory.getIndexSearcher(IndexTables.AUTHORS);
		IndexFactory.getIndexSearcher(IndexTables.RATING);
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
			BooleanQuery query;
			for (JoinCriteria joinCriterium: joinCriteria) {
				query = new BooleanQuery();
				int sizeLimit = joinCriterium.getFromFields().length;
				for (int i = 0; i < sizeLimit; i++) {
					String fromfield = joinCriterium.getFromFields()[i];
					String toField = joinCriterium.getToFields()[i];
					if(doc.get(fromfield) != null){
						query.add(new TermQuery(new Term(toField, doc.get(fromfield))), BooleanClause.Occur.MUST);
					}else{
						QueryParser queryParser= new QueryParser(Version.LUCENE_30, toField, new StandardAnalyzer(Version.LUCENE_30));
						queryParser.setAllowLeadingWildcard(true);
						Query q = queryParser.parse("*");
						query.add(q, BooleanClause.Occur.MUST_NOT);
					}
				}
				
				IndexSearcher lookupIndexSearcher = IndexFactory.getIndexSearcher(joinCriterium.getLookupTable());
				TopDocs topDocs = lookupIndexSearcher.search(query, 1);
				
				Map<String, String> keyValues = new HashMap<String, String>();
				
				if(topDocs.scoreDocs.length > 0){
					int docId = topDocs.scoreDocs[0].doc;
					Document lookupDoc = lookupIndexSearcher.doc(docId);
					
					for(Fieldable field : lookupDoc.getFields()){
						keyValues.put(field.name(), field.stringValue());
					}
				}
				
				if(keyValues.size() > 0){
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
