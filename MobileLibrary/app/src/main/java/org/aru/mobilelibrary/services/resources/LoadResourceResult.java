package org.aru.mobilelibrary.services.resources;

import org.aru.mobilelibrary.services.ServiceResult;

/**
 * Created by mmichalek on 10/21/15.
 */
public class LoadResourceResult extends ServiceResult {

    private ResourceModel resourceModel;

    public ResourceModel getResourceModel() {
            return resourceModel;
    }

    public void setResourceModel(ResourceModel resourceModel) {
        this.resourceModel = resourceModel;
    }
}
