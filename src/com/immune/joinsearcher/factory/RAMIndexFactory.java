package com.immune.joinsearcher.factory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.immune.joinsearcher.models.constants.IndexTables;

public class RAMIndexFactory {
	
	public static IndexSearcher mainIndexSearcher;
	public static IndexSearcher authorsIndexSearcher;
	public static IndexSearcher ratingIndexSearcher;

	public static Map<String, IndexSearcher> ramIndexSearchers = new HashMap<String, IndexSearcher>();
	
	public static void init() throws IOException, ParseException{
		getRAMIndexSearcher(IndexTables.AUTHORS);
		getRAMIndexSearcher(IndexTables.RATING);
	}
	
	public static IndexSearcher getRAMIndexSearcher(IndexTables lookupTable) throws IOException, ParseException{
		if(ramIndexSearchers.get(lookupTable.name()) != null) {
			return ramIndexSearchers.get(lookupTable.name());
		}else{
			addIndexSearcher(lookupTable.name());
		}
		
		return ramIndexSearchers.get(lookupTable.name());
	}
	
		
	public static void addIndexSearcher(String lookupTableName) throws IOException, ParseException{
		IndexSearcher indexSearcher = getIndexSearcher(IndexTables.valueOf(lookupTableName));
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
		Query lookupQuery = new QueryParser(Version.LUCENE_30, "*", analyzer).parse("*");
		TopDocs lookupResults = indexSearcher.search(lookupQuery, Integer.MAX_VALUE);
		
		Directory ramDir = new RAMDirectory();
		IndexWriter ramIndexWriter = new IndexWriter(ramDir, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
		
		for (ScoreDoc scoreDoc : lookupResults.scoreDocs) {
			Document doc = indexSearcher.doc(scoreDoc.doc);
			ramIndexWriter.addDocument(doc);
		}
		
		ramIndexSearchers.put(lookupTableName, new IndexSearcher(ramIndexWriter.getReader()));
		
		indexSearcher.close();
		ramIndexWriter.close();
		ramDir.close();
	
	}
	
	private static IndexSearcher getIndexSearcher(IndexTables lookupTable) throws IOException{
		Directory dir;
		
		switch (lookupTable) {
		case MAIN:
			if(mainIndexSearcher == null){
				dir = NIOFSDirectory.open(new File(IndexFactory.mainIndexLocation));
				mainIndexSearcher = new IndexSearcher(dir);
			}
			return mainIndexSearcher;
			
		case AUTHORS:
			if(authorsIndexSearcher == null){
				dir = NIOFSDirectory.open(new File(IndexFactory.authorsIndexLocation));
				authorsIndexSearcher = new IndexSearcher(dir);
			}
			return authorsIndexSearcher;
		
		default:
			if(ratingIndexSearcher == null){
				dir = NIOFSDirectory.open(new File(IndexFactory.ratingIndexLocation));
				ratingIndexSearcher = new IndexSearcher(dir);
			}
			return ratingIndexSearcher;
		}
		
	}

}
