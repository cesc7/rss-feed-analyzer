package rss.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ElementResponse {
	
	String word;
	List<FeedItemResponse> feed;

}
