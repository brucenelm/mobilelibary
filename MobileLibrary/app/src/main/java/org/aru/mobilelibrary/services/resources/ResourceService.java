package org.aru.mobilelibrary.services.resources;

/**
 * Created by mmichalek on 10/21/15.
 */
public interface ResourceService {

    public ResourceSearchResult searchResources(String searchTerm, String title, String author, String isbn);
    public LoadResourceResult loadResource(String id);

}
