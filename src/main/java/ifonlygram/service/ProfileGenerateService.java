package ifonlygram.service;

import ifonlygram.dto.BlogCategory;
import ifonlygram.dto.Profile;
import ifonlygram.dto.RequestInfo;

public interface ProfileGenerateService {
    Profile generateProfile(String name, BlogCategory blogCategory);
}
