package com.immune.joinsearcher.joinsearchers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;

import com.immune.joinsearcher.models.JoinCriteria;

public interface JoinSearcher {
	
	List<Map<String, String>> searchAndJoin(List<JoinCriteria> joinCriteria, Query booksQuery)  throws ParseException, IOException;
}
