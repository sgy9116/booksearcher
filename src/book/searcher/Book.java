package book.searcher;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Book
{
    public String title;
    public String authors;
    public String translators;
    public String publisher;
    public String category;
    public String price;
    public String status;
    public String content;
    public String thumbnail;

    public static Book makeBookFromJsonString(JSONObject json)
    {
        try
        {
            Book book = new Book();

            book.title = (String) json.get("title");
            book.publisher = (String) json.get("publisher");
            book.category = (String) json.get("category");
            book.price = json.get("price") + " 원";
            book.status = (String) json.get("status");
            book.content = (String) json.get("contents");

            String thumbnail = ((String) json.get("thumbnail")).trim();
            book.thumbnail = thumbnail.length() > 0 ? thumbnail : null;

            Stream<String> authors =  ((List<String>) json.get("authors")).stream();
            book.authors = authors.collect(Collectors.joining(", "));

            Stream<String> translators =  ((List<String>) json.get("translators")).stream();
            book.translators = translators.collect(Collectors.joining(", "));


            return book;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public String toString()
    {
        return this.title;
    }
}
