package ifonlygram.service;

import ifonlygram.dto.BlogCategory;
import ifonlygram.dto.Profile;

public interface ProfileGenerateService {
    Profile generateProfile(String name, BlogCategory blogCategory);
}
