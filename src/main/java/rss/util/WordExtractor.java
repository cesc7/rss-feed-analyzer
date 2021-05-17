package rss.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rss.model.RssFeedItem;

@Getter
@Setter
@NoArgsConstructor
@Component
public class WordExtractor {

	public List<String> extractWordsFromRssFeed(String title) throws IOException{
		
		List<String> feedItemWords = new ArrayList<>();
		
		//Remove stopwords and generate list of rssFeedItemWords
		List<String> stopwords = Files.readAllLines(Paths.get("english_stopwords.txt"));
		
		 String[] allWords = title.toLowerCase().split(" ");

		    for(String word : allWords) {
		        if(!stopwords.contains(word)) {
		           feedItemWords.add(word);
		        }
		    }
		  
	   return feedItemWords;
	}
	
	
}
