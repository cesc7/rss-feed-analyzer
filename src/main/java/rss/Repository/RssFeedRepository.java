package rss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import rss.model.RssFeed;

@Repository
public interface RssFeedRepository extends JpaRepository<RssFeed, Long> {
	

}
