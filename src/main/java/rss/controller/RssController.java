package rss.controller;

import java.util.List;

import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rss.dto.ApiResponse;
import rss.dto.ElementResponse;
import rss.dto.IdentifierResponse;
import rss.dto.RssFeedRequest;
import rss.service.RssService;


@RestController
public class RssController {
	
	final static Logger logger = LoggerFactory.getLogger(RssController.class);
	
	@Autowired
	RssService rssService;
	
	@RequestMapping(path = "/analyse/new", method = RequestMethod.POST, produces = "application/json")
	public ApiResponse<?> analyzeRss(@RequestBody RssFeedRequest rssUrls){
                IdentifierResponse requestIdentifier = new IdentifierResponse();
		requestIdentifier.setRequestIdentifier(rssService.analyzeRssFeeds(rssUrls.getRssUrls()));
		
		if(!(requestIdentifier.getRequestIdentifier() == null)) {
			return new ApiResponse<>(requestIdentifier, HttpStatus.OK);
		} else {
			return new ApiResponse<>(requestIdentifier, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(path = "/frequency/{id}", method = RequestMethod.GET, produces = "application/json")
	public ApiResponse<?> analyzeRss(@PathVariable   String id){

	    if(id.length() == 36) {
                List<ElementResponse> response = rssService.getAnalysys(id);
		
		if(!(response == null)) {
			return new ApiResponse<>(response, HttpStatus.OK);
		} else {
			return new ApiResponse<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    } else {
		return new ApiResponse<>("Request_Id not valid", HttpStatus.BAD_REQUEST);
	    }
		
	}
	
}