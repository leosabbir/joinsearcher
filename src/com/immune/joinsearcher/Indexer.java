package com.immune.joinsearcher;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.immune.joinsearcher.factory.IndexFactory;

public class Indexer {
	
	public static final int NO_BOOKS = 100000;
	public static final int NO_AUTHORS = 10000*1;
	public static final int NO_RATINGS = 10000*1;
	public static final int NO_REVIEWERS = 10000;
	public static final int MAX_RATING = 10000;
	
	private IndexWriter bookIndexWriter;
	private IndexWriter authorIndexWriter;
	private IndexWriter ratingIndexWriter;
	
	public Indexer() throws IOException{
		
		Directory mainIndexDir = FSDirectory.open(new File(IndexFactory.mainIndexLocation));
		Directory createrIndexDir = FSDirectory.open(new File(IndexFactory.authorsIndexLocation));
		Directory ratingIndexDir = FSDirectory.open(new File(IndexFactory.ratingIndexLocation));
		
		bookIndexWriter = new IndexWriter(mainIndexDir, new StandardAnalyzer(Version.LUCENE_30), true, IndexWriter.MaxFieldLength.UNLIMITED);
		authorIndexWriter = new IndexWriter(createrIndexDir, new StandardAnalyzer(Version.LUCENE_30), true, IndexWriter.MaxFieldLength.UNLIMITED);
		ratingIndexWriter = new IndexWriter(ratingIndexDir, new StandardAnalyzer(Version.LUCENE_30), true, IndexWriter.MaxFieldLength.UNLIMITED);
		
	}
	
	public void createBooksIndex() throws CorruptIndexException, IOException{
		int id = 1;
		
		for(int i = 0; i < NO_BOOKS; i++){
			int rn = (int) (1 + Math.random() * NO_AUTHORS);
			int rating = (int) (1 + Math.random() * MAX_RATING);
			
			Document doc = new Document();
			
			doc.add(new Field("id", "" + id, Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("name", "name" + id++, Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("content", "this book is written by author" + rn, Field.Store.NO, Field.Index.ANALYZED));
			doc.add(new Field("authorId", "" + rn, Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("authorName", "author" + rn, Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("reviewer", "reviewer" + rn, Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("rating", "" + rating, Field.Store.YES, Field.Index.NOT_ANALYZED));
			//doc.add(new Field("price", "" + rating * 100, Field.Store.YES, Field.Index.NOT_ANALYZED));
			
			this.bookIndexWriter.addDocument(doc);
			
			
		}
	}
	
	public void createAuthorIndex() throws CorruptIndexException, IOException{
		
		for(int i = 1; i <= NO_AUTHORS; i++){
			Document doc = new Document();
			
			doc.add(new Field("id2", i + "", Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("name2", "author" + i, Field.Store.YES, Field.Index.NOT_ANALYZED));
			
			this.authorIndexWriter.addDocument(doc);
		}
	}
	
	public void createRatingIndex() throws CorruptIndexException, IOException{
		
		for(int i = 0; i < NO_RATINGS; i++){
			int rating = (int) (1 + Math.random() * MAX_RATING);
			int random_reviewer = (int) (1 + Math.random() * NO_REVIEWERS);
			
			Document doc = new Document();
		
			doc.add(new Field("rating_id", i + "", Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("reviewer", "reviewer" + i, Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("rating", String.valueOf(rating), Field.Store.YES, Field.Index.NOT_ANALYZED));
			
			this.ratingIndexWriter.addDocument(doc);
		}
		
	}
	
	public void close() throws CorruptIndexException, IOException{
		this.bookIndexWriter.close();
		this.authorIndexWriter.close();
		this.ratingIndexWriter.close();
	}
	
	public void confirmCreation(){
		System.out.println(this.bookIndexWriter.maxDoc());
		System.out.println(this.authorIndexWriter.maxDoc());
		System.out.println(this.ratingIndexWriter.maxDoc());
	}
	
	public static void main(String[] args) throws IOException {
		Indexer bookAuthor = new Indexer();
		bookAuthor.createBooksIndex();
		bookAuthor.createAuthorIndex();
		bookAuthor.createRatingIndex();
		
		bookAuthor.confirmCreation();
		bookAuthor.close();
	}

}
