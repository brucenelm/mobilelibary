package org.aru.mobilelibrary.services.resources;


import org.aru.mobilelibrary.services.HttpUtils;
import org.aru.mobilelibrary.services.login.UserModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.PriorityQueue;

 import javax.xml.parsers.DocumentBuilder;
 import javax.xml.parsers.DocumentBuilderFactory;
 import javax.xml.xpath.XPath;
 import javax.xml.xpath.XPathConstants;
 import javax.xml.xpath.XPathFactory;

 /**
 * Created by mmichalek on 10/21/15.
 */
public class KohaRSSResourceService implements ResourceService {

    @Override
    public ResourceSearchResult searchResources(String searchTerm, String title, String authorSearch, String isbn) {

        try {
           String host = "http://41.210.169.134:4141/";
//            String host = "http://model.bywatersolutions.com/";

            String searchType = "kw";
            String searchValue = searchTerm;

            if (searchTerm != null && searchTerm.length() > 0) {
                searchType = "kw";
                searchValue = searchTerm;
            } else if (title != null && title.length() > 0) {
                searchType = "ti";
                searchValue = title;
            } else if (authorSearch != null && authorSearch.length() > 0) {
                searchType = "au";
                searchValue = authorSearch;
            } else if (isbn != null && isbn.length() > 0) {
                searchType = "nb";
                searchValue = isbn;
            }
            String url =
                    String.format("%s/cgi-bin/koha/opac-search.pl?idx=%s&q=%s&count=50&sort_by=acqdate_dsc&format=rss2", host, searchType, URLEncoder.encode(searchValue, "UTF-8"));

         String rssXml = HttpUtils.httpGet(url);

            InputStream stream = new ByteArrayInputStream(rssXml.getBytes("UTF-8"));

            ResourceSearchResult result = new ResourceSearchResult();

            XPath xpath = XPathFactory.newInstance().newXPath();
            String expression = "myNode";
            //NodeList nodes = (NodeList) xpath.evaluate("//*[local-name()='totalResults']", new InputSource(stream), XPathConstants.NODESET);
            NodeList titles = (NodeList) xpath.evaluate("//item", new InputSource(stream), XPathConstants.NODESET);

            for (int i = 0; i < titles.getLength(); i++) {
                Node titleNode = titles.item(i);

                ResourceSearchResultModel resource = new ResourceSearchResultModel();

                for (int j = 0; j < titleNode.getChildNodes().getLength(); j++) {

                    Node childNode = titleNode.getChildNodes().item(j);
                    if ("title".equals(childNode.getLocalName())) {
                        resource.setTitle(childNode.getTextContent());
                    }
                    else if ("guid".equals(childNode.getLocalName())) {
                        resource.setId(childNode.getTextContent());
                    }
                    else if ("description".equals(childNode.getLocalName())) {
                        org.jsoup.nodes.Document doc = Jsoup.parse(childNode.getTextContent());

                        System.out.println(childNode.getTextContent());
                        Element author = doc.select("p").first();

                        if (author != null) {
                            resource.setDescription(author.text());
                        }
                    }
                    else {
                        if (childNode != null && childNode.getLocalName() != null) {
                            System.out.println(childNode.getLocalName());
                        }
                    }

                }
                result.getSearchResults().add(resource);

            }

            return result;

        }
        catch (Exception ex) {
            ex.printStackTrace();
            ResourceSearchResult result = new ResourceSearchResult();
            result.setMessage("Failed to search resources. " + ex.getMessage());
            return result;
        }
    }

    @Override
    public LoadResourceResult loadResource(String resourceURL) {

        System.out.println("Received URL " + resourceURL);

        try {
            org.jsoup.nodes.Document doc = Jsoup.connect(resourceURL).timeout(60 * 1000).get();
            Element image = doc.select("div#bookcover > a > img").first();

            ResourceModel resource = new ResourceModel();
            resource.setId(resourceURL);
            if (image != null) {
                resource.setImageURL(image.absUrl("src"));
            }

            Element title = doc.select("h1.title").first();
            resource.setTitle(title.text());

            Element author = doc.select("h5.author > a > span > span > span").first();
            if (author != null) {
                resource.setAuthor(author.text());
            }
            else {
                author = doc.select("h5.author > a").first();
                if (author != null) {
                    resource.setAuthor(author.text());
                }
            }

            Elements publisher = doc.select("span.results_summary.publisher");

            String publisherString = "";
            for (int i = 0; i < publisher.size(); i++) {
                Element e = publisher.get(i);
                //if (i > 0) { // skip the first one as it's just a label "Publisher:"
                    publisherString +=  e.text();
                //}
            }

            resource.setPublisher(publisherString);

            String isbnString = "";
            Elements isbn = doc.select("span.results_summary.isbn");
            for (int i = 0; i < isbn.size(); i++) {
                Element e = isbn.get(i);
                    isbnString +=  e.text();
            }

            resource.setIsbn(isbnString);

            String locationString = "";
//            Elements location = doc.select("td.location > span:nth-child(3) > span");

            Elements libraryLocation = doc.select("td.location > div > a");
            for (int i = 0; i < libraryLocation.size(); i++) {
                Element e = libraryLocation.get(i);
                locationString +=  e.text() + " - ";
            }

            Elements shelfLocation = doc.select("td.location > span");
            for (int i = 0; i < shelfLocation.size(); i++) {
                Element e = shelfLocation.get(i);
                locationString +=  e.text();
            }
            resource.setLocation(locationString);

            String callNumberString = "";
            Elements callNumber = doc.select("td.call_no");
            for (int i = 0; i < callNumber.size(); i++) {
                Element e = callNumber.get(i);
                callNumberString +=  e.text();
            }
            resource.setCallNumber(callNumberString);

            String statusString = "";
            Elements status = doc.select("td.status");
            for (int i = 0; i < status.size(); i++) {
                Element e = status.get(i);
                statusString +=  e.text();
            }
            resource.setStatus(statusString);

            LoadResourceResult result = new LoadResourceResult();
            result.setResourceModel(resource);
            return result;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            LoadResourceResult result = new LoadResourceResult();
            result.setMessage("Failed to load resource. " + ex.getMessage());
            return result;
        }
    }
}
