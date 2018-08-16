package book.searcher;

public class BookResult
{
    public boolean error = false;
    public String reason;
    public Book book;

    public BookResult(String reason)
    {
        this.error = true;
        this.reason = reason;
    }

    public BookResult(Book book)
    {
        if (book == null)
        {
            this.error = true;
            this.reason = "책 생성에 오류 발생";
            return;
        }
        this.book = book;
    }
}
