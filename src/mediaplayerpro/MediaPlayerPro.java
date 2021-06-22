package mediaplayerpro;

import java.io.PrintWriter;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MediaPlayerPro extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        scene.getStylesheets().add(getClass().getResource("MediaStyleSheet.css").toExternalForm());
        
        stage.setTitle("Media Player");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("IconFolder/icon1.png")));
        stage.show();
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                try{
                    PrintWriter pw = new PrintWriter("playlist.txt");
                    pw.append("");
                    pw.close();
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        });
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
