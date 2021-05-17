package rss.model.projection;

import org.springframework.beans.factory.annotation.Value;

public interface TopFeedItemPicks {
	
	@Value("#{target.name}")
	String getName();
	
	@Value("#{target.counter}")
	String getCounter();

}