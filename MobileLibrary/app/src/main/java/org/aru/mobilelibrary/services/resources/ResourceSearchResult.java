package org.aru.mobilelibrary.services.resources;

import org.aru.mobilelibrary.services.ServiceResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mmichalek on 10/21/15.
 */
public class ResourceSearchResult extends ServiceResult {

    private List<ResourceSearchResultModel> searchResults = new ArrayList<ResourceSearchResultModel>();

    public List<ResourceSearchResultModel> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<ResourceSearchResultModel> searchResults) {
        this.searchResults = searchResults;
    }


}
