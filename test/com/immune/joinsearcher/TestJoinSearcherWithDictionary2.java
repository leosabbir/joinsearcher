package com.immune.joinsearcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
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
	    
	    // 0
	    Document doc = new Document();
		doc.add(new Field("id", "1", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("name", "name1", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("content", "this book is written by author1", Field.Store.NO, Field.Index.ANALYZED));
		doc.add(new Field("authorId", "1", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("authorName", "author1", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("reviewer", "reviewer" + 1, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("rating", "" + 1, Field.Store.YES, Field.Index.NOT_ANALYZED));
		booksIndexWriter.addDocument(doc);

	    // 1
	    doc = new Document();
	    doc.add(new Field("id", "2", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("name", "name2", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("content", "this book is written by author2", Field.Store.NO, Field.Index.ANALYZED));
		doc.add(new Field("authorId", "2", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("authorName", "author2", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("reviewer", "reviewer" + 2, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("rating", "" + 2, Field.Store.YES, Field.Index.NOT_ANALYZED));
		booksIndexWriter.addDocument(doc);

	    // 2
	    doc = new Document();
	    doc.add(new Field("id", "3", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("name", "name3", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("content", "this book is written by author3", Field.Store.NO, Field.Index.ANALYZED));
		doc.add(new Field("authorId", "3", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("authorName", "author3", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("reviewer", "reviewer" + 3, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("rating", "" + 3, Field.Store.YES, Field.Index.NOT_ANALYZED));
		booksIndexWriter.addDocument(doc);

	    // 3
	    doc = new Document();
	    doc.add(new Field("id", "4", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("name", "name4", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("content", "this book is written by author4", Field.Store.NO, Field.Index.ANALYZED));
		doc.add(new Field("authorId", "4", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("authorName", "author4", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("reviewer", "reviewer" + 4, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("rating", "" + 4, Field.Store.YES, Field.Index.NOT_ANALYZED));
		booksIndexWriter.addDocument(doc);
	    booksIndexWriter.commit();

	    // 4
	    doc = new Document();
	    doc.add(new Field("id", "5", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("name", "name5", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("content", "this book is written by author4", Field.Store.NO, Field.Index.ANALYZED));
		doc.add(new Field("authorId", "4", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("authorName", "author4", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("reviewer", "reviewer" + 5, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("rating", "" + 5, Field.Store.YES, Field.Index.NOT_ANALYZED));
		booksIndexWriter.addDocument(doc);

	    // 5
	    doc = new Document();
	    doc.add(new Field("id", "6", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("name", "name6", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("content", "this book is written by author4", Field.Store.NO, Field.Index.ANALYZED));
		doc.add(new Field("authorId", "3", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("authorName", "author4", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("reviewer", "reviewer" + 1, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("rating", "" + 1, Field.Store.YES, Field.Index.NOT_ANALYZED));
		booksIndexWriter.addDocument(doc);
		
		// 6
	    doc = new Document();
	    doc.add(new Field("id", "7", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("name", "name7", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("content", "this book is written by author5", Field.Store.NO, Field.Index.ANALYZED));
		doc.add(new Field("authorId", "4", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("authorName", "author5", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("reviewer", "reviewer" + 1, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("rating", "" + 2, Field.Store.YES, Field.Index.NOT_ANALYZED));
		booksIndexWriter.addDocument(doc);
		
		// 7
	    doc = new Document();
	    doc.add(new Field("id", "8", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("name", "name8", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("content", "this book is written by author6", Field.Store.NO, Field.Index.ANALYZED));
		doc.add(new Field("authorId", "4", Field.Store.YES, Field.Index.NOT_ANALYZED));
		//doc.add(new Field("authorName", "author5", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("reviewer", "reviewer" + 1, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("rating", "" + 1, Field.Store.YES, Field.Index.NOT_ANALYZED));
		booksIndexWriter.addDocument(doc);
		
		// 8
	    doc = new Document();
	    doc.add(new Field("id", "9", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("name", "name9", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("content", "this book is written by author6", Field.Store.NO, Field.Index.ANALYZED));
		doc.add(new Field("authorId", "4", Field.Store.YES, Field.Index.NOT_ANALYZED));
		//doc.add(new Field("authorName", "author5", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("reviewer", "reviewer" + 1, Field.Store.YES, Field.Index.NOT_ANALYZED));
		//doc.add(new Field("rating", "" + 1, Field.Store.YES, Field.Index.NOT_ANALYZED));
		booksIndexWriter.addDocument(doc);
		
		doc = new Document();
		doc.add(new Field("id2", "1", Field.Store.YES, Field.Index.NO));
		doc.add(new Field("name2", "author1", Field.Store.YES, Field.Index.NOT_ANALYZED));
		authorssIndexWriter.addDocument(doc);
		
		doc = new Document();
		doc.add(new Field("id2", "2", Field.Store.YES, Field.Index.NO));
		doc.add(new Field("name2", "author2", Field.Store.YES, Field.Index.NOT_ANALYZED));
		authorssIndexWriter.addDocument(doc);
		
		doc = new Document();
		doc.add(new Field("id2", "3", Field.Store.YES, Field.Index.NO));
		doc.add(new Field("name2", "author3", Field.Store.YES, Field.Index.NOT_ANALYZED));
		authorssIndexWriter.addDocument(doc);
		
		doc = new Document();
		doc.add(new Field("id2", "4", Field.Store.YES, Field.Index.NO));
		doc.add(new Field("name2", "author4", Field.Store.YES, Field.Index.NOT_ANALYZED));
		authorssIndexWriter.addDocument(doc);
		
		//Ratings Indices
		doc = new Document();
		doc.add(new Field("rating_id", 1 + "", Field.Store.YES, Field.Index.NO));
		doc.add(new Field("reviewer", "reviewer" + 1, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("rating", String.valueOf(1), Field.Store.YES, Field.Index.NOT_ANALYZED));
		ratingsIndexWriter.addDocument(doc);
		
		doc = new Document();
		doc.add(new Field("rating_id", 2 + "", Field.Store.YES, Field.Index.NO));
		doc.add(new Field("reviewer", "reviewer" + 2, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("rating", String.valueOf(2), Field.Store.YES, Field.Index.NOT_ANALYZED));
		ratingsIndexWriter.addDocument(doc);
		
		doc = new Document();
		doc.add(new Field("rating_id", 3 + "", Field.Store.YES, Field.Index.NO));
		doc.add(new Field("reviewer", "reviewer" + 3, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("rating", String.valueOf(3), Field.Store.YES, Field.Index.NOT_ANALYZED));
		ratingsIndexWriter.addDocument(doc);
		
		doc = new Document();
		doc.add(new Field("rating_id", 4 + "", Field.Store.YES, Field.Index.NO));
		doc.add(new Field("reviewer", "reviewer" + 4, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("rating", String.valueOf(4), Field.Store.YES, Field.Index.NOT_ANALYZED));
		ratingsIndexWriter.addDocument(doc);

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
		
		String[] fromFields = {"authorId", "authorName"};
		String[] toFields = {"id2", "name2"};
		String[] ratingFromFields = {"reviewer", "rating"};
		String[] ratingToFields = {"reviewer", "rating"};
		
		List<JoinCriteria> joinCriteria = new ArrayList<JoinCriteria>();
		joinCriteria.add(new JoinCriteria(IndexTables.AUTHORS, fromFields, toFields));
		joinCriteria.add(new JoinCriteria(IndexTables.RATING, ratingFromFields, ratingToFields));
		
		
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
		String[] fromFields3 = {"authorId", "authorName"};
		String[] toFields3 = {"id2", "name22"};
		String[] ratingFromFields3 = {"reviewer", "rating"};
		String[] ratingToFields3 = {"reviewer22", "rating"};
		
		joinCriteria = new ArrayList<JoinCriteria>();
		joinCriteria.add(new JoinCriteria(IndexTables.AUTHORS, fromFields3, toFields3));
		joinCriteria.add(new JoinCriteria(IndexTables.RATING, ratingFromFields3, ratingToFields3));
		
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
		
		
		searcher.searchAndJoin(Arrays.asList(new JoinCriteria(IndexTables.AUTHORS, fromFields2, toFields2)), booksQuery);
		Assert.assertNotNull(searcher.getDictionary(IndexTables.AUTHORS, Utils.sortTokenize(Arrays.asList(toFields2), JoinSearcherWithDictionary2.KEY_TOKENIZER)));
	}
	
	@Test
	public void testGetDictionary(){
		
	}

}
