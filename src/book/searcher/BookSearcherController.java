package book.searcher;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    private Book book;
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
        this.book = book;
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
        //new Alert(Alert.AlertType.INFORMATION, "지이이잉").show();
        Label titleLabel = new Label("제목: "+this.book.title);
        titleLabel.setWrapText(true);
        Label authorLabel = new Label("저자: "+this.book.authors);
        Label translatorsLabel = new Label("번역: "+ this.book.translators);
        Label publisherLabel = new Label("출판: "+this.book.publisher);
        Label categoryLabel = new Label("카테고리: "+ this.book.category);
        Label priceLabel = new Label ("가격: "+ this.book.price);
        TextArea contentText = new TextArea();
        contentText.setText("내용: \n"+ this.book.content);
        contentText.setPadding(new Insets(0,240,0,0));
        contentText.setWrapText(true);

        VBox vbox = new VBox(10,titleLabel, authorLabel, translatorsLabel, publisherLabel, categoryLabel,priceLabel, contentText);
        vbox.setAlignment(Pos.CENTER_LEFT);
        pageSetup(vbox, BookSearcher.mainStage);
    }
    private void pageSetup(Node node, Stage owner)
    {
        PrinterJob job = PrinterJob.createPrinterJob();
        JobSettings jobSettings = job.getJobSettings();
        Printer printer = job.getPrinter();
        PageLayout pageLayout= printer.createPageLayout(Paper.MONARCH_ENVELOPE, PageOrientation.PORTRAIT,
                1.0,1.0, 1.0,1.0 );
        jobSettings.setPageLayout(pageLayout);

        boolean proceed = job.showPageSetupDialog(owner);
        if (proceed)
        {
            print(job,node);
        }
    }

    private void print(PrinterJob job,Node node)
    {
        boolean printed = job.printPage(node);

        if(printed)
        {
            this.queryTextField.requestFocus();
            job.endJob();
        }
    }
}
