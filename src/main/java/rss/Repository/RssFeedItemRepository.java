package rss.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rss.model.RssFeedItem;
import rss.model.projection.TopFeedItemPicks;

@Repository
public interface RssFeedItemRepository extends JpaRepository<RssFeedItem, Long> {
	
	@Query(value = "select fi.* FROM rss_feed_items_words  iw \r\n"
			+ "LEFT JOIN rss_feed_items fi\r\n"
			+ "ON iw.rss_feed_item_id = fi.id\r\n"
			+ "LEFT JOIN rss_feed rf\r\n"
			+ "ON fi.rss_feed_id = rf.id\r\n"
			+ "where rf.request_id = ? \r\n"
			+ "and iw.name = ? \r\n"
			+ "", nativeQuery = true)
	public List<RssFeedItem> findAllFeedItemsByWordAndRequestId(String requestId, String name);
	
}