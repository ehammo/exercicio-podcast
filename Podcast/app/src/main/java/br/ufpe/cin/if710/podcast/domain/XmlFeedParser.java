package br.ufpe.cin.if710.podcast.domain;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.db.entity.ItemFeedEntity;
import br.ufpe.cin.if710.podcast.model.ItemFeed;

public class XmlFeedParser {

    public static List<ItemFeedEntity> parse(String xmlFeed) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(xmlFeed));
        xpp.nextTag();
        return readRss(xpp);
    }

    public static List<ItemFeedEntity> readRss(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        List<ItemFeedEntity> items = new ArrayList<ItemFeedEntity>();
        parser.require(XmlPullParser.START_TAG, null, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("channel")) {
                items.addAll(readChannel(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }

    public static List<ItemFeedEntity> readChannel(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        List<ItemFeedEntity> items = new ArrayList<ItemFeedEntity>();
        parser.require(XmlPullParser.START_TAG, null, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                items.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }

    public static ItemFeedEntity readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        String pubDate = null;
        String description = null;
        String downloadLink = null;
        parser.require(XmlPullParser.START_TAG, null, "item");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readData(parser, "title");
            } else if (name.equals("guid")) {
                link = readData(parser, "guid");
            } else if (name.equals("pubDate")) {
                pubDate = readData(parser, "pubDate");
            } else if (name.equals("description")) {
                description = readData(parser, "description");
            } else if (name.equals("enclosure")) {
                downloadLink = readEnclosure(parser);
                skip(parser);
            } else {
                skip(parser);
            }
        }
        ItemFeedEntity result = new ItemFeedEntity(title, link, pubDate, description, downloadLink);
        return result;
    }

    // Processa tags de forma parametrizada no feed.
    public static String readData(XmlPullParser parser, String tag)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, tag);
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, tag);
        return data;
    }

    public static String readText(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    public static String readAttribute(XmlPullParser parser, String attribute) {
        String result = parser.getAttributeValue(null, attribute);
        if (result != null) {
            return result;
        }
        return "";
    }

    // Processa tags do tipo <enclosure> para obter dados do episodio
    public static String readEnclosure(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        String data = readAttribute(parser, "url");
        //parser.require(XmlPullParser.END_TAG, null, "enclosure");
        return data;
    }


    public static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}