package book.searcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class BookSearcher extends Application
{
    public static Application app;

    public BookSearcher()
    {
        BookSearcher.app = this;
    }
    static Stage mainStage;
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        mainStage = primaryStage;
        primaryStage.setTitle("책 검색 프로그램");
        primaryStage.getIcons().add(new Image("/icon.png"));
        primaryStage.setResizable(false);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("book_searcher.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
