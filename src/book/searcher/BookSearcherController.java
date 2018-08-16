package book.searcher;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BookSearcherController
{
    @FXML
    public ListView historyListView;
    @FXML
    public TextField queryTextField;
    @FXML
    public StackPane bookStackPane;
    @FXML
    public ImageView bookImageView;
    @FXML
    public Text titleText, authorsText, translatorsText, publisherText, categoryText, priceText, statusText, contentText;

    private ObservableList<Book> history = FXCollections.observableArrayList();

    @FXML
    public void initialize()
    {
        this.bookStackPane.setVisible(false);

        this.historyListView.setItems(this.history);
        this.historyListView.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
            this.setBook((Book) n);
        });
    }

    private void setBook(Book book)
    {
        if (book == null)
        {
            this.bookStackPane.setVisible(false);
            return;
        }

        this.titleText.setText(book.title);
        this.authorsText.setText(book.authors);
        this.translatorsText.setText(book.translators);
        this.publisherText.setText(book.publisher);
        this.categoryText.setText(book.category);
        this.priceText.setText(book.price);
        this.statusText.setText(book.status);
        this.contentText.setText(book.content);

        if (book.thumbnail != null)
        {
            Image image = new Image(book.thumbnail);
            this.bookImageView.setImage(image);
        }
        else
        {
            this.bookImageView.setImage(null);
        }

        this.bookStackPane.setVisible(true);
    }

    @FXML
    public void onSearchButtonClicked()
    {
        String query = queryTextField.getText().trim();
        if (query.length() == 0) return;

        try { query = URLEncoder.encode(query, "utf-8"); }
        catch (UnsupportedEncodingException e) { }

        BookResult result = KakaoAPI.book(query);
        if (result.error)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(result.reason);
            alert.show();
            return;
        }

        if (this.history.contains(result.book))
        {
            this.history.remove(result.book);
        }
        this.history.add(0, result.book);
        this.historyListView.scrollTo(0);
        this.historyListView.getSelectionModel().select(0);
        this.setBook(result.book);
        this.queryTextField.clear();
    }

    @FXML
    public void onSearchKeyPressed(KeyEvent event)
    {
        if (event.getCode() == KeyCode.ENTER)
        {
            this.onSearchButtonClicked();
        }
    }

    @FXML
    public void onPrintButtonClicked()
    {
        new Alert(Alert.AlertType.INFORMATION, "지이이잉").show();
    }
}
