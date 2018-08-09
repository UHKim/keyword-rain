package com.uhkim.acidnews.snow;

import java.io.IOException;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class TopSearchParser {
    public enum SiteName {
        daum, naver
    }

    public static Document doc;
    public static Elements topSearch;

    public static SiteName currentSite;

    public TopSearchParser(SiteName site) {
        try {
            getDocument(site);
            genSnowList();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void getDocument(SiteName site) throws IOException {
        if (site == SiteName.daum) {
            doc = Jsoup.connect("http://www.daum.net/").get();
            topSearch = doc.select("ol.list_hotissue.issue_row");
        } else if (site == SiteName.naver) {
            doc = Jsoup.connect("http://www.naver.com/").get();
            topSearch = doc.select("div.ah_list.PM_CL_realtimeKeyword_list_base>ul.ah_l");
        } else {
            System.out.println("Not Proper Sitename");
            ;
        }
        System.out.println(site);
        System.out.println(topSearch.toString());
        currentSite = site;
    }

    public Vector<TopSearchSnow> genSnowList() {
        Vector<TopSearchSnow> result = new Vector<TopSearchSnow>(0);
        Elements words = topSearch.select("a[href]");

        System.out.println(currentSite);
        System.out.println(words.toString());

        for (Element e : words) {
            TopSearchSnow snow = new TopSearchSnow();
            if (currentSite == SiteName.naver) {
                snow.searchWord = e.select("span.ah_k").text();
                snow.newsURL = e.attr("abs:href");
            } else if (currentSite == SiteName.daum) {
                if ("-1".equals(e.attr("tabindex"))) {
                    snow.searchWord = e.text();
                    snow.newsURL = e.attr("abs:href");
                }
            }
            snow.source = currentSite;
            result.addElement(snow);

        }

        return result;


    }

    public String toString() {
        return null;
    }
}
