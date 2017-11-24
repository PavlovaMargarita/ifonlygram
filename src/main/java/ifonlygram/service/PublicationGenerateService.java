package ifonlygram.service;

import ifonlygram.dto.Publication;

import java.util.List;

public interface PublicationGenerateService {
    List<Publication> generatePublications(String tag, String name, String infoWiki);
}
