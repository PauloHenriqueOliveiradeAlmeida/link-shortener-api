package com.shortener.link.application.repositories;

import com.shortener.link.domain.entities.Link;

public interface ILinkRepository extends IBaseRepository<Link> {
    Link findByShortUrl(String shortUrl);
}
