package com.immune.joinsearcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.immune.joinsearcher.factory.IndexFactory;
import com.immune.joinsearcher.joinsearchers.JoinSearcher;
import com.immune.joinsearcher.joinsearchers.JoinSearcherWithDictionary2;
import com.immune.joinsearcher.joinsearchers.JoinSearcherWithTempIndex;
import com.immune.joinsearcher.joinsearchers.JoinSearcherWithoutDictionary;
import com.immune.joinsearcher.models.JoinCriteria;
import com.immune.joinsearcher.models.JoinField;
import com.immune.joinsearcher.models.constants.IndexTables;
import com.immune.joinsearcher.models.constants.JoinOperators;

public class AppJoinSearcher {

	public static void main(String[] args) throws IOException, ParseException {

		Directory mainIndexDir = FSDirectory.open(new File(IndexFactory.mainIndexLocation));

		IndexSearcher mainIndexSearcher = new IndexSearcher(mainIndexDir);
		
		
		JoinSearcher joinSearcher = new JoinSearcherWithDictionary2(mainIndexSearcher);
		//runJoinSearcher(joinSearcher);
		joinSearcher = new JoinSearcherWithoutDictionary(mainIndexSearcher);
		runJoinSearcher(joinSearcher);
		joinSearcher = new JoinSearcherWithTempIndex(mainIndexSearcher);
		runJoinSearcher(joinSearcher);

	}

	public static void runJoinSearcher(JoinSearcher joinSearcher) throws ParseException, IOException {

		//String[] fromFields = {"authorId", "authorName"};
		//String[] toFields = {"id2", "name2"};
		//String[] ratingFromFields = {"reviewer", "rating"};
		//String[] ratingToFields = {"reviewer", "rating"};

		List<JoinCriteria> joinCriteria = new ArrayList<JoinCriteria>();
		
		List<JoinField> authorsJoinFields = new ArrayList<JoinField>();
		authorsJoinFields.add(new JoinField("authorId", "id2", JoinOperators.LESSER));
		authorsJoinFields.add(new JoinField("authorName", "name2", JoinOperators.GREATER));
		
		List<JoinField> ratingsJoinFields = new ArrayList<JoinField>();
		ratingsJoinFields.add(new JoinField("reviewer", "reviewer", JoinOperators.LESSER));
		ratingsJoinFields.add(new JoinField("rating", "rating",  JoinOperators.LESSER));
		
		joinCriteria.add(new JoinCriteria(IndexTables.AUTHORS, authorsJoinFields));
		joinCriteria.add(new JoinCriteria(IndexTables.RATING, ratingsJoinFields));
		
		Query booksQuery = new QueryParser(Version.LUCENE_30, "content",
				new StandardAnalyzer(Version.LUCENE_30)).parse("written");

		System.out.println("\n\n************");
		long before = System.currentTimeMillis();
		System.out.println("Total Documents Collected: "
				+ joinSearcher.searchAndJoin(joinCriteria,
						booksQuery).get(54).keySet().size());
		System.out.println("Time taken at first time: "
				+ (System.currentTimeMillis() - before));

		before = System.currentTimeMillis();
		joinSearcher.searchAndJoin(joinCriteria, booksQuery);
		
		System.out.println("Time taken at second time: "
				+ (System.currentTimeMillis() - before));

		before = System.currentTimeMillis();
		joinSearcher
				.searchAndJoin(joinCriteria, booksQuery);
		System.out.println("Time taken at third time: "
				+ (System.currentTimeMillis() - before));

		before = System.currentTimeMillis();
		joinSearcher.searchAndJoin(joinCriteria, booksQuery);
		System.out.println("Time taken at fourth time: " + (System.currentTimeMillis() - before));

		before = System.currentTimeMillis();
		joinSearcher.searchAndJoin(joinCriteria,
				booksQuery);
		System.out.println("Time taken at fifth time: "
				+ (System.currentTimeMillis() - before));

		before = System.currentTimeMillis();
		joinSearcher.searchAndJoin(joinCriteria,
				booksQuery);
		System.out.println("Time taken at sixth time: "
				+ (System.currentTimeMillis() - before));

		before = System.currentTimeMillis();
		joinSearcher.searchAndJoin(joinCriteria,
				booksQuery);
		System.out.println("Time taken at seventh time: "
				+ (System.currentTimeMillis() - before));
	}

}
