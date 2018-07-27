package org.aru.mobilelibrary.services;

import org.aru.mobilelibrary.services.login.DemoLoginService;
import org.aru.mobilelibrary.services.login.KohaHTMLLoginService;
import org.aru.mobilelibrary.services.login.LoginService;
import org.aru.mobilelibrary.services.resources.KohaRSSResourceService;
import org.aru.mobilelibrary.services.resources.ResourceService;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by mmichalek on 10/21/15.
 */
public class ServiceFactory {

    private Map<String, Object> services = new HashMap<String, Object>();

    public <T> T getService(Class<?> serviceInterface) {
        String name = serviceInterface.getName();
        T service = (T)services.get(name);
        if (service != null) {
            return service;
        }

        if (serviceInterface.equals(LoginService.class)) {
          //  service = (T)new DemoLoginService();
            service = (T)new KohaHTMLLoginService();
            services.put(name, service);
        }
        else if (serviceInterface.equals(ResourceService.class)) {
            service = (T)new KohaRSSResourceService();
            services.put(name, service);
        }

        return service;
    }

    // Singleton pattern.
    private static ServiceFactory instance = new ServiceFactory();

    public static ServiceFactory instance() {
        return instance;
    }

    private ServiceFactory() {
    }
}
