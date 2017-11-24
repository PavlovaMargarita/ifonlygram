package ifonlygram.service;

import ifonlygram.dto.Profile;
import ifonlygram.dto.RequestInfo;

public interface ProfileGenerateService {
    Profile generateProfile(RequestInfo requestInfo);
}
