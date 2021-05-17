package rss.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import rss.model.RssFeedItem;
import rss.model.RssFeedItemsWords;
import rss.model.projection.TopFeedItemPicks;
import rss.repository.RssFeedItemRepository;
import rss.repository.RssFeedItemsWordsRepository;
import rss.repository.RssFeedRepository;
import rss.util.WordExtractor;
import rss.dto.ElementResponse;
import rss.dto.FeedItemResponse;
import rss.model.RssFeed;

@Service
public class RssService {
	
        final static Logger logger = LoggerFactory.getLogger(RssService.class);
        
        @Value("${app.topicNumber}")
        int numOfTopics;
        
        @Autowired
        WordExtractor wordExtractor;
        
        @Autowired
        RssFeedRepository rssFeedRepository;
        
        @Autowired
        RssFeedItemRepository rssFeedItemRepository;
        
        @Autowired
        RssFeedItemsWordsRepository rssFeedItemsWordsRepository;
        
        
        public String analyzeRssFeeds(List<String> rssFeedUrls){
	 
           //Generate random requestID 
           String requestId = UUID.randomUUID().toString();
           
           
           //Iterate through url list and save data from RssFeed to DB
           RssFeed rssFeed = new RssFeed();
	  try {
		for(String url: rssFeedUrls) {
			
			SyndFeed syndFeed = new SyndFeedInput().build(new XmlReader(new URL(url)));
			
			//RssFeed_TABLE
			rssFeed.setRequestId(requestId);
			rssFeed.setTitle(syndFeed.getTitle());
			rssFeed.setLink(syndFeed.getLink());
			
			//Get data for RssFeedItem_TABLE
			List<SyndEntry> feedItems = syndFeed.getEntries();
			List<RssFeedItem> rssFeedItems = new ArrayList<>();
			for (SyndEntry item : feedItems) {
				RssFeedItem feedItem = new RssFeedItem();
				feedItem.setTitle(item.getTitle());
				feedItem.setLink(item.getLink());
				
				//Get words from titles for RssFeedItemsWords_TABLE
				List<RssFeedItemsWords> wordsList = new ArrayList<>();
				List<String> words = wordExtractor.extractWordsFromRssFeed(item.getTitle());
				for (String name : words) {
					RssFeedItemsWords word = new RssFeedItemsWords();
					word.setName(name);
					wordsList.add(word);
				}
				feedItem.setWords(wordsList);
				rssFeedItems.add(feedItem);
			}
			rssFeed.setItems(rssFeedItems);
			
			rssFeedRepository.save(rssFeed);
			
		}

		return requestId;
			
	  } catch (Exception e) {
		  logger.error(e.getMessage());
		  return null;
	  }
      }
	
        public List<ElementResponse> getAnalysys(String requestId){
		
		List<ElementResponse> response =  new ArrayList<>();
		
           try {
	   
                //Get most common words across all news in order for unique requestId
    	        List<TopFeedItemPicks> topRssFeedItems = rssFeedItemsWordsRepository.getMostFrequentFeedItems(requestId);
   		
   		    for(int i = 0;i < numOfTopics; i++) {
   			ElementResponse element = new ElementResponse();
   			
   			element.setWord(topRssFeedItems.get(i).getName());
   			
   			//Get a list of 'RssFeedItem' in which word occurs
   			List<RssFeedItem> feedItems = rssFeedItemRepository.findAllFeedItemsByWordAndRequestId( requestId, topRssFeedItems.get(i).getName());
   			
   		        //Extract title and item and add to response
   			List<FeedItemResponse> feedItemResponseList = new ArrayList<>();;
   			for(RssFeedItem feedItem: feedItems) {
   				
    				FeedItemResponse feedItemResponse = new FeedItemResponse();
    				feedItemResponse.setTitle(feedItem.getTitle());
    				feedItemResponse.setLink(feedItem.getLink());
    				feedItemResponseList.add(feedItemResponse);
   			    }
   			
   			element.setFeed(feedItemResponseList);	
   			response.add(element);
   			}
   		
   		return response;
   		
       } catch (Exception e) {
    	   logger.error(e.getMessage());
    	   return null;
       }
		
    }
	
}