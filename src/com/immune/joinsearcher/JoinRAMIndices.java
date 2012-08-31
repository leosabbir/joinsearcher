package com.immune.joinsearcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.immune.joinsearcher.factory.IndexFactory;
import com.immune.joinsearcher.models.constants.IndexTables;

public class JoinRAMIndices {
	
	private static Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
	
	
	public static void main(String[] args) throws ParseException, IOException {
		
		Query booksQuery = new QueryParser(Version.LUCENE_30, "content", analyzer).parse("written");
		
		Query authorsQuery = new QueryParser(Version.LUCENE_30, "*", analyzer).parse("*");
	
		IndexSearcher mainIndexSearcher = IndexFactory.getIndexSearcher(IndexTables.MAIN);
		IndexSearcher ratingIndexSearcher = IndexFactory.getIndexSearcher(IndexTables.RATING);
		
		TopDocs books = mainIndexSearcher.search(booksQuery, 10000);
		TopDocs authors = ratingIndexSearcher.search(authorsQuery, 10000);
		
		IndexSearcher booksRAMIndexSearcher = createRAMIndex(mainIndexSearcher, books);
		IndexSearcher ratingRAMIndexSearcher = createRAMIndex(ratingIndexSearcher, authors);
		
		System.out.println(booksRAMIndexSearcher.maxDoc());
		System.out.println(ratingRAMIndexSearcher.maxDoc());
		
		
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		Map<String, String> keyValuesAll = new HashMap<String, String>();
		List<Map<String, String>> listOfKeyValues  = new ArrayList<Map<String,String>>();
		for (ScoreDoc scoreDoc : books.scoreDocs) {
			Document doc = mainIndexSearcher.doc(scoreDoc.doc);
			
			BooleanQuery query = new BooleanQuery();
			
			String fromField = "reviewer";
			String toField = "reviewer";
			if(doc.get("reviewer") != null){
				query.add(new TermQuery(new Term(toField, doc.get(fromField))), BooleanClause.Occur.MUST);
			}else{
				QueryParser queryParser= new QueryParser(Version.LUCENE_30, toField, new StandardAnalyzer(Version.LUCENE_30));
				queryParser.setAllowLeadingWildcard(true);
				Query q = queryParser.parse("*");
				query.add(q, BooleanClause.Occur.MUST_NOT);
			}
			
			fromField = "rating";
			toField = "rating";
			if(doc.get(fromField) != null){
				Query q = new QueryParser(Version.LUCENE_30, toField, new StandardAnalyzer(Version.LUCENE_30)).parse("[* TO 3]");
				//query.add(new TermQuery(new Term(toField, doc.get(fromField))), BooleanClause.Occur.MUST);
				query.add(q, BooleanClause.Occur.MUST);
			}else{
				QueryParser queryParser= new QueryParser(Version.LUCENE_30, toField, new StandardAnalyzer(Version.LUCENE_30));
				queryParser.setAllowLeadingWildcard(true);
				Query q = queryParser.parse("*");
				query.add(q, BooleanClause.Occur.MUST_NOT);
			}
			
			TopDocs topDocs = ratingRAMIndexSearcher.search(query, 1);
			
			Map<String, String> keyValues = new HashMap<String, String>();
			
			if(topDocs.scoreDocs.length > 0){
				int docId = topDocs.scoreDocs[0].doc;
				Document lookupDoc = ratingRAMIndexSearcher.doc(docId);
				
				for(Fieldable field : lookupDoc.getFields()){
					keyValues.put(field.name(), field.stringValue());
				}
			}
			
			if(keyValues.size() > 0){
				listOfKeyValues.add(keyValues);
			}
			
			keyValuesAll = new HashMap<String, String>();
			for (Fieldable field : doc.getFields()) {
				if(Integer.parseInt(doc.get("rating")) < 4)
					keyValuesAll.put(field.name(), field.stringValue());
			}
			//loop listOfKeyValues to append all fields
			//System.out.println(listOfKeyValues.size());
			for (Map<String, String> keyValue : listOfKeyValues) {
				for (String key : keyValue.keySet()) {
					keyValuesAll.put(key, keyValue.get(key));
				}
			}
			
			if(keyValues.keySet().size() > 0){
				result.add(keyValuesAll);
			}
		}
		
		System.out.println(result.size());
		
		Map<String, String> sample = result.get(0);
		
		for (String key : sample.keySet()) {
			System.out.println(key + ":" + sample.get(key));
		}
		
	}
	
	public static IndexSearcher createRAMIndex(IndexSearcher indexSearcher, TopDocs topDocs) throws CorruptIndexException, LockObtainFailedException, IOException{
		Directory ramDir = new RAMDirectory();
		IndexWriter ramIndexWriter = new IndexWriter(ramDir, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
		
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexSearcher.doc(scoreDoc.doc);
			ramIndexWriter.addDocument(doc);
		}
		
		return new IndexSearcher(ramIndexWriter.getReader());
	}

}
