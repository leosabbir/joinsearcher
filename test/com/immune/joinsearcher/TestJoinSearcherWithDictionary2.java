package com.immune.joinsearcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.immune.joinsearcher.factory.IndexFactory;
import com.immune.joinsearcher.joinsearchers.JoinSearcherWithDictionary2;
import com.immune.joinsearcher.models.JoinCriteria;
import com.immune.joinsearcher.models.JoinField;
import com.immune.joinsearcher.models.constants.IndexTables;
import com.immune.joinsearcher.utils.Utils;

public class TestJoinSearcherWithDictionary2 extends TestCase {
	Directory booksDir;
	Directory authorsDir;
	Directory ratingsDir;
	IndexSearcher booksIndexSearcher;
	IndexSearcher authorsIndexSearcher;
	IndexSearcher ratingsIndexSearcher;
	
	public TestJoinSearcherWithDictionary2() throws IOException {

	    booksDir = new RAMDirectory();
	    
	    IndexWriter booksIndexWriter = new IndexWriter( booksDir, new StandardAnalyzer(Version.LUCENE_30), true, IndexWriter.MaxFieldLength.UNLIMITED);
	    
	    authorsDir = new RAMDirectory();
	    
	    IndexWriter authorssIndexWriter = new IndexWriter(authorsDir, new StandardAnalyzer(Version.LUCENE_30), true, IndexWriter.MaxFieldLength.UNLIMITED);
	    
	    ratingsDir = new RAMDirectory();
	    
	    IndexWriter ratingsIndexWriter = new IndexWriter(ratingsDir, new StandardAnalyzer(Version.LUCENE_30), true, IndexWriter.MaxFieldLength.UNLIMITED);
	    
	    JoinSearchTestUtil.addBooksDocuments(booksIndexWriter);
	    JoinSearchTestUtil.addAuthorsDocuments(authorssIndexWriter);
	    JoinSearchTestUtil.addRatingsDocuments(ratingsIndexWriter);
	    
	    this.booksIndexSearcher = new IndexSearcher(booksIndexWriter.getReader());
	    this.authorsIndexSearcher = new IndexSearcher(authorssIndexWriter.getReader());
	    this.ratingsIndexSearcher = new IndexSearcher(ratingsIndexWriter.getReader());
	    
	    IndexFactory.addIndexSearcher(IndexTables.AUTHORS.name(), authorsIndexSearcher);
	    IndexFactory.addIndexSearcher(IndexTables.RATING.name(), this.ratingsIndexSearcher);
	    
	    
	    booksIndexWriter.close();
	    booksDir.close();
	    authorssIndexWriter.close();
	    authorsDir.close();
	    ratingsIndexWriter.close();
	    ratingsDir.close();
	}
	
	@Test
	public void testSearchAndJoin() throws IOException, ParseException{
		JoinSearcherWithDictionary2 searcher = new JoinSearcherWithDictionary2(this.booksIndexSearcher);
		
		List<JoinCriteria> joinCriteria = new ArrayList<JoinCriteria>();
		
		List<JoinField> authorsJoinFields = new ArrayList<JoinField>();
		authorsJoinFields.add(new JoinField("authorId", "id2"));
		authorsJoinFields.add(new JoinField("authorName", "name2"));
		
		List<JoinField> ratingsJoinFields = new ArrayList<JoinField>();
		ratingsJoinFields.add(new JoinField("reviewer", "reviewer"));
		ratingsJoinFields.add(new JoinField("rating", "rating"));
		
		joinCriteria.add(new JoinCriteria(IndexTables.AUTHORS, authorsJoinFields));
		joinCriteria.add(new JoinCriteria(IndexTables.RATING, ratingsJoinFields));
		
		
		Query booksQuery = new QueryParser(Version.LUCENE_30, "content", new StandardAnalyzer(Version.LUCENE_30)).parse("author4");
		
		//long beforeFirst = System.currentTimeMillis();
		List<Map<String, String>> results = searcher.searchAndJoin(joinCriteria, booksQuery);
		
		//System.out.println(System.currentTimeMillis() - beforeFirst);
		//long beforeSecond = System.currentTimeMillis();
		//searcher.searchAndJoin(joinCriteria, booksQuery);
		//System.out.println(System.currentTimeMillis() - beforeSecond);
		
		Assert.assertEquals(3, results.size());
		
		Map<String, String> keyValue = results.get(0);
		Assert.assertEquals(9, keyValue.keySet().size());
		Assert.assertEquals(keyValue.get("id"), "4");
		Assert.assertEquals(keyValue.get("id2"), "4");
		Assert.assertEquals(keyValue.get("name2"), "author4");
		Assert.assertEquals(keyValue.get("rating_id"), "4");
		
		keyValue = results.get(1);
		//System.out.println(keyValue.get("id") + " " + keyValue.get("rating_id"));
		Assert.assertEquals(8, keyValue.keySet().size());
		Assert.assertEquals(keyValue.get("id"), "5");
		Assert.assertEquals(keyValue.get("id2"), "4");
		Assert.assertEquals(keyValue.get("name2"), "author4");
		Assert.assertNull(keyValue.get("rating_id"));
		
		keyValue = results.get(2);
		Assert.assertEquals(7, keyValue.keySet().size());
		Assert.assertEquals(keyValue.get("id"), "6");
		Assert.assertNull(keyValue.get("id2"));
		Assert.assertNull(keyValue.get("name2"));
		Assert.assertEquals(keyValue.get("rating_id"), "1");
		
		booksQuery = new QueryParser(Version.LUCENE_30, "content", new StandardAnalyzer(Version.LUCENE_30)).parse("author5");
		results = searcher.searchAndJoin(joinCriteria, booksQuery);
		
		Assert.assertEquals(1, results.size());
		keyValue = results.get(0);
		Assert.assertEquals(6, keyValue.keySet().size());
		Assert.assertEquals(keyValue.get("id"), "7");
		Assert.assertNull(keyValue.get("id2"));
		Assert.assertNull(keyValue.get("name2"));
		Assert.assertNull(keyValue.get("rating_id"));
		
		//Missing fields in main Index
		booksQuery = new QueryParser(Version.LUCENE_30, "content", new StandardAnalyzer(Version.LUCENE_30)).parse("author6");
		results = searcher.searchAndJoin(joinCriteria, booksQuery);
				
		Assert.assertEquals(2, results.size());
		keyValue = results.get(0);
		Assert.assertEquals(6, keyValue.keySet().size());
		Assert.assertEquals(keyValue.get("id"), "8");
		Assert.assertNull(keyValue.get("id2"));
		Assert.assertNull(keyValue.get("name2"));
		Assert.assertEquals(keyValue.get("rating_id"), "1");
				
		keyValue = results.get(1);
		Assert.assertEquals(4, keyValue.keySet().size());
		Assert.assertEquals(keyValue.get("id"), "9");
		Assert.assertNull(keyValue.get("id2"));
		Assert.assertNull(keyValue.get("name2"));
		Assert.assertNull(keyValue.get("rating_id"));
		
		//WRONG FIELD names will not result into Join
		joinCriteria = new ArrayList<JoinCriteria>();
		
		authorsJoinFields = new ArrayList<JoinField>();
		authorsJoinFields.add(new JoinField("authorId", "id2"));
		authorsJoinFields.add(new JoinField("authorName", "name22"));
		
		ratingsJoinFields = new ArrayList<JoinField>();
		ratingsJoinFields.add(new JoinField("reviewer", "reviewer22"));
		ratingsJoinFields.add(new JoinField("rating", "rating"));
		
		joinCriteria.add(new JoinCriteria(IndexTables.AUTHORS, authorsJoinFields));
		joinCriteria.add(new JoinCriteria(IndexTables.RATING, ratingsJoinFields));
		
		booksQuery = new QueryParser(Version.LUCENE_30, "content", new StandardAnalyzer(Version.LUCENE_30)).parse("author4");
		results = searcher.searchAndJoin(joinCriteria, booksQuery);
		
		Assert.assertEquals(3, results.size());
		
		for (Map<String, String> map : results) {
			Assert.assertEquals(6, map.keySet().size());
			//Assert.assertEquals(map.get("id"), "7");
			Assert.assertNull(map.get("id2"));
			Assert.assertNull(map.get("name2"));
			Assert.assertNull(map.get("rating_id"));			
		}

		
		
		String[] fromFields2 = {"authorId"};
		String[] toFields2 = {"id2"};
		
		authorsJoinFields = new ArrayList<JoinField>();
		authorsJoinFields.add(new JoinField("authorId", "id2"));
		
		searcher.searchAndJoin(Arrays.asList(new JoinCriteria(IndexTables.AUTHORS, authorsJoinFields)), booksQuery);
		Assert.assertNotNull(searcher.getDictionary(IndexTables.AUTHORS, Utils.sortTokenize(Arrays.asList(toFields2), JoinSearcherWithDictionary2.KEY_TOKENIZER)));
	}
	
	@Test
	public void testGetDictionary(){
		
	}

}
