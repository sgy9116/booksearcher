package book.searcher;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public final class KakaoAPI
{
    private static final String KEY = Constants.KAKAO_API_KEY;

    private static final Map<String, Book> cache = new HashMap<>();

    public static BookResult book(String query)
    {
        if (KakaoAPI.cache.containsKey(query))
        {
            Book book = KakaoAPI.cache.get(query);
            return new BookResult(book);
        }

        try
        {
            URL url = new URL("https://dapi.kakao.com/v2/search/book?size=1&query="+query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "KakaoAK " + KEY);

            if (conn.getResponseCode() != 200)
            {
                conn.disconnect();
                return new BookResult("검색 오류");
            }

            try (InputStream is = conn.getInputStream())
            {
                InputStreamReader isr = new InputStreamReader(is);
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(isr);
                JSONArray documents = (JSONArray) json.get("documents");
                if (documents.size() == 0)
                {
                    return new BookResult("검색 결과가 없습니다.");
                }

                JSONObject bookJson = (JSONObject) documents.get(0);
                Book book = Book.makeBookFromJsonString(bookJson);
                KakaoAPI.cache.put(query, book);
                return new BookResult(book);
            }
        }
        catch (Exception e)
        {
            return new BookResult(e.getMessage());
        }
    }
}
