package com.immune.joinsearcher.utils;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;

import com.immune.joinsearcher.models.constants.JoinOperators;

public class QueryBuilder {

	public static Query getQuery(String toField, String value, JoinOperators joinOperator) throws ParseException {
		Query q;
		StringBuilder sb;
		
		switch (joinOperator) {
		case LESSER:
			sb = new StringBuilder();
			sb.append("{* TO ");
			sb.append(value);
			sb.append("}");
			q = new QueryParser(Version.LUCENE_30, toField, new StandardAnalyzer(Version.LUCENE_30)).parse(sb.toString());
			//q = buildFieldCacheQuery("*", value, toField);
			break;

		case GREATER:
			sb = new StringBuilder();
			sb.append("{");
			sb.append(value);
			sb.append(" TO *}");
			q = new QueryParser(Version.LUCENE_30, toField, new StandardAnalyzer(Version.LUCENE_30)).parse(sb.toString());
			//q = buildFieldCacheQuery(value, "*", toField);
			break;
			
		case LESSEREQUAL:
			sb = new StringBuilder();
			sb.append("[* TO ");
			sb.append(value);
			sb.append("]");
			q = new QueryParser(Version.LUCENE_30, toField, new StandardAnalyzer(Version.LUCENE_30)).parse(sb.toString());
			break;
			
		case GREATEREQUAL:
			sb = new StringBuilder();
			sb.append("[");
			sb.append(value);
			sb.append(" TO *]");
			q = new QueryParser(Version.LUCENE_30, toField, new StandardAnalyzer(Version.LUCENE_30)).parse(sb.toString());
			break;

		default:
			q = new TermQuery(new Term(toField, value));
			break;
		}
		
		
		return q;
	}
	
	public static Query buildFieldCacheQuery(final String start, final String end, final String field) {
		Filter f = new Filter() {
			@Override
			public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
				final String[] data = FieldCache.DEFAULT.getStrings(reader, field);
				return new DocIdSet() {

					@Override
					public DocIdSetIterator iterator() throws IOException {
						return new DocIdSetIterator() {
							int docid = -1;

							@Override
							public int advance(int target) throws IOException {
								docid = target - 1;
								return nextDoc();
							}

							@Override
							public int docID() {
								return docid;
							}

							@Override
							public int nextDoc() throws IOException {
								String val;
								docid++;
								while (docid<data.length) {
									val = data[docid];
									if( start.equals("*")){
										if ( val.compareTo(end) < 0)
											return docid;
										else
											docid++;
									} else if ( end.equals("*")){
										if (val.compareTo(start) > 0)
											return docid;
										else
											docid++;
									} else{
										if (val.compareTo(start) > 0 && val.compareTo(end) < 0)
											return docid;
										else
											docid++;
									}
								}
								return DocIdSetIterator.NO_MORE_DOCS;
							}
						};
					}
				};
			}
		};

		return new ConstantScoreQuery(f);
	}

}
