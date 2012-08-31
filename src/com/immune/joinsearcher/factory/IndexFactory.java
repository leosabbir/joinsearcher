package com.immune.joinsearcher.factory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;

import com.immune.joinsearcher.models.constants.IndexTables;

public class IndexFactory {
	
	public static final String mainIndexLocation = "/Users/sabbirmanandhar/Documents/eclipseWorkspaces/loginspect/booksIndexDir";
	public static final String authorsIndexLocation = "/Users/sabbirmanandhar/Documents/eclipseWorkspaces/loginspect/authorsIndexDir";
	public static final String ratingIndexLocation = "/Users/sabbirmanandhar/Documents/eclipseWorkspaces/loginspect/ratingIndexDir";
	
	public static IndexSearcher mainIndexSearcher;
	public static IndexSearcher authorsIndexSearcher;
	public static IndexSearcher ratingIndexSearcher;
	
	public static Map<String, IndexSearcher> indexSearchers = new HashMap<String, IndexSearcher>();
	
	public static void addIndexSearcher(String lookupTableName, IndexSearcher indexSearcher){
		indexSearchers.put(lookupTableName, indexSearcher);
	}
	
	public static IndexSearcher getIndexSearcher(IndexTables lookupTable) throws IOException{
		if(indexSearchers.get(lookupTable.name()) != null){
			return indexSearchers.get(lookupTable.name());
		}
		
		Directory dir;
		
		switch (lookupTable) {
		case MAIN:
			if(mainIndexSearcher == null){
				dir = NIOFSDirectory.open(new File(mainIndexLocation));
				mainIndexSearcher = new IndexSearcher(dir);
				indexSearchers.put(lookupTable.name(), mainIndexSearcher);
			}
			return mainIndexSearcher;
			
		case AUTHORS:
			if(authorsIndexSearcher == null){
				dir = NIOFSDirectory.open(new File(authorsIndexLocation));
				authorsIndexSearcher = new IndexSearcher(dir);
				indexSearchers.put(lookupTable.name(), authorsIndexSearcher);
			}
			return authorsIndexSearcher;
		
		default:
			if(ratingIndexSearcher == null){
				dir = NIOFSDirectory.open(new File(ratingIndexLocation));
				ratingIndexSearcher = new IndexSearcher(dir);
				indexSearchers.put(lookupTable.name(), ratingIndexSearcher);
			}
			return ratingIndexSearcher;
		}
		
	}
	

}
