package com.immune.joinsearcher;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;

public class JoinSearchTestUtil {

	public static void addBooksDocuments(IndexWriter booksIndexWriter) throws CorruptIndexException, IOException {
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
		// doc.add(new Field("authorName", "author5", Field.Store.YES,
		// Field.Index.NOT_ANALYZED));
		doc.add(new Field("reviewer", "reviewer" + 1, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("rating", "" + 1, Field.Store.YES, Field.Index.NOT_ANALYZED));
		booksIndexWriter.addDocument(doc);

		// 8
		doc = new Document();
		doc.add(new Field("id", "9", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("name", "name9", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("content", "this book is written by author6", Field.Store.NO, Field.Index.ANALYZED));
		doc.add(new Field("authorId", "4", Field.Store.YES, Field.Index.NOT_ANALYZED));
		// doc.add(new Field("authorName", "author5", Field.Store.YES,
		// Field.Index.NOT_ANALYZED));
		doc.add(new Field("reviewer", "reviewer" + 1, Field.Store.YES, Field.Index.NOT_ANALYZED));
		// doc.add(new Field("rating", "" + 1, Field.Store.YES,
		// Field.Index.NOT_ANALYZED));
		booksIndexWriter.addDocument(doc);

	}

	public static void addAuthorsDocuments(IndexWriter authorssIndexWriter) throws CorruptIndexException, IOException {
		Document doc = new Document();
		doc.add(new Field("id2", "1", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("name2", "author1", Field.Store.YES, Field.Index.NOT_ANALYZED));
		authorssIndexWriter.addDocument(doc);

		doc = new Document();
		doc.add(new Field("id2", "2", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("name2", "author2", Field.Store.YES, Field.Index.NOT_ANALYZED));
		authorssIndexWriter.addDocument(doc);

		doc = new Document();
		doc.add(new Field("id2", "3", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("name2", "author3", Field.Store.YES, Field.Index.NOT_ANALYZED));
		authorssIndexWriter.addDocument(doc);

		doc = new Document();
		doc.add(new Field("id2", "4", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("name2", "author4", Field.Store.YES, Field.Index.NOT_ANALYZED));
		authorssIndexWriter.addDocument(doc);
	}

	public static void addRatingsDocuments(IndexWriter ratingsIndexWriter) throws CorruptIndexException, IOException {
		// Ratings Indices
		Document doc = new Document();
		doc.add(new Field("rating_id", 1 + "", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("reviewer", "reviewer" + 1, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("rating", String.valueOf(1), Field.Store.YES, Field.Index.NOT_ANALYZED));
		ratingsIndexWriter.addDocument(doc);

		doc = new Document();
		doc.add(new Field("rating_id", 2 + "", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("reviewer", "reviewer" + 2, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("rating", String.valueOf(2), Field.Store.YES, Field.Index.NOT_ANALYZED));
		ratingsIndexWriter.addDocument(doc);

		doc = new Document();
		doc.add(new Field("rating_id", 3 + "", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("reviewer", "reviewer" + 3, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("rating", String.valueOf(3), Field.Store.YES, Field.Index.NOT_ANALYZED));
		ratingsIndexWriter.addDocument(doc);

		doc = new Document();
		doc.add(new Field("rating_id", 4 + "", Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("reviewer", "reviewer" + 4, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("rating", String.valueOf(4), Field.Store.YES, Field.Index.NOT_ANALYZED));
		ratingsIndexWriter.addDocument(doc);
	}

}
