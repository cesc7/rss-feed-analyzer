package rss.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rss.model.RssFeedItem;
import rss.model.RssFeedItemsWords;
import rss.model.projection.TopFeedItemPicks;

@Repository
public interface RssFeedItemsWordsRepository  extends JpaRepository<RssFeedItemsWords, Long>{
	
	
	@Query(value = "SELECT  iw.name, COUNT(iw.name) counter   \r\n"
			+ "FROM rss_feed rf\r\n"
			+ "LEFT JOIN rss_feed_items fi\r\n"
			+ "ON rf.id= fi.rss_feed_id \r\n"
			+ "LEFT JOIN rss_feed_items_words iw \r\n"
			+ "ON fi.id = iw.rss_feed_item_id \r\n"
			+ "WHERE rf.request_id = ?  \r\n"
			+ "GROUP BY iw.name \r\n"
			+ "ORDER BY counter DESC", nativeQuery = true)
	public List<TopFeedItemPicks> getMostFrequentFeedItems(String requestId);
    
}