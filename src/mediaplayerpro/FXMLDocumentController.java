package mediaplayerpro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 *
 * @author Tehgan
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Slider seek,volSlider;
    
    @FXML
    private Button play,stop,previous,next;
    
    @FXML
    private ToggleButton repeatB,shuffle;
    
    @FXML
    private Label maxTime,currentTime,queueNameLabel;
    
    @FXML
    private MenuItem increaseVolItem,decreaseVolItem,muteItem;
    
    @FXML
    private MenuItem playItem,nextItem,previousItem,stopItem;
    
    @FXML
    private MenuItem queue;
    
    private MenuItem recentMenuItem[];
    
    @FXML
    private Menu recentMenu;
    
    @FXML
    private ImageView volControlImage,imageViewMedia;
    
    private Stage stg;
    
    @FXML
    private BorderPane bsContainer,root,centerPane;
    
    @FXML
    private VBox queuePane;
    
    @FXML
    private ListView<String> queueListView;
    
    @FXML 
    Pane pane,mediaPane;
    
    private Media media;
    
    private MediaPlayer mediaPlayer;
    
    @FXML
    private MediaView mediaView;
    
    private File recentFile,playlistFile,playlistDirect;
    
    private List<String> list;
    
    private int playlistControl;
    
    private ImageView playImgV,pauseImgV, oggImage,mp3Image,wavImage,animatedImage;        
    
    private boolean playingFromPlaylist,queueOpen=false;
    
    private String queueName="Queue - No Name";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        recentFile = new File("recent.txt");
        playlistDirect = new File("playlists");
        
        animatedImage = new ImageView(new Image(getClass().getResourceAsStream("IconFolder/animated.gif")));
        oggImage = new ImageView(new Image(getClass().getResourceAsStream("IconFolder/ogg.png")));
        mp3Image = new ImageView(new Image(getClass().getResourceAsStream("IconFolder/mp3.png")));
        wavImage = new ImageView(new Image(getClass().getResourceAsStream("IconFolder/wav.png")));
        
        centerPane.setCenter(animatedImage);
        
        playingFromPlaylist = false;
        queueOpen = false;
        queue.setDisable(true);
        
        root.setRight(null);
        
        try{
            recentFile.createNewFile();
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        recentMenuItem = new MenuItem[10];
        
        queueListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        playingFromPlaylist = false;
        
        pauseImgV = new ImageView(new Image(getClass().getResourceAsStream("IconFolder/pause-button.png")));
        pauseImgV.setFitHeight(13);
        pauseImgV.setFitWidth(13);
        
        playImgV = new ImageView(new Image(getClass().getResourceAsStream("IconFolder/play-arrow.png")));
        playImgV.setFitHeight(13);
        playImgV.setFitWidth(13);
        
        list = new ArrayList<>();
        
        for(int i=0;i<10;i++){
            recentMenuItem[i] = new MenuItem("No Data");
            recentMenu.getItems().add(recentMenuItem[i]);
        }
        
        recentMenuItem[0].setAccelerator(new KeyCodeCombination(KeyCode.DIGIT0,KeyCombination.CONTROL_DOWN));
        recentMenuItem[1].setAccelerator(new KeyCodeCombination(KeyCode.DIGIT1,KeyCombination.CONTROL_DOWN));
        recentMenuItem[2].setAccelerator(new KeyCodeCombination(KeyCode.DIGIT2,KeyCombination.CONTROL_DOWN));
        recentMenuItem[3].setAccelerator(new KeyCodeCombination(KeyCode.DIGIT3,KeyCombination.CONTROL_DOWN));
        recentMenuItem[4].setAccelerator(new KeyCodeCombination(KeyCode.DIGIT4,KeyCombination.CONTROL_DOWN));
        recentMenuItem[5].setAccelerator(new KeyCodeCombination(KeyCode.DIGIT5,KeyCombination.CONTROL_DOWN));
        recentMenuItem[6].setAccelerator(new KeyCodeCombination(KeyCode.DIGIT6,KeyCombination.CONTROL_DOWN));
        recentMenuItem[7].setAccelerator(new KeyCodeCombination(KeyCode.DIGIT7,KeyCombination.CONTROL_DOWN));
        recentMenuItem[8].setAccelerator(new KeyCodeCombination(KeyCode.DIGIT8,KeyCombination.CONTROL_DOWN));
        recentMenuItem[9].setAccelerator(new KeyCodeCombination(KeyCode.DIGIT9,KeyCombination.CONTROL_DOWN));
        
        recentMediaLoader();
        
        recentMenuItem[0].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                openOnMenuItemPressed(0);
            }
        });
        
        recentMenuItem[1].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                openOnMenuItemPressed(1);
            }
        });
        
        recentMenuItem[2].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                openOnMenuItemPressed(2);
            }
        });
        
        recentMenuItem[3].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                openOnMenuItemPressed(3);
            }
        });
        
        recentMenuItem[4].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                openOnMenuItemPressed(4);
            }
        });
        
        recentMenuItem[5].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                openOnMenuItemPressed(5);
            }
        });
        
        recentMenuItem[6].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                openOnMenuItemPressed(6);
            }
        });
        
        recentMenuItem[7].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                openOnMenuItemPressed(7);
            }
        });
        
        recentMenuItem[8].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                openOnMenuItemPressed(8);
            }
        });
        
        recentMenuItem[9].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                openOnMenuItemPressed(9);
            }
        });
        
        centerPane.setOnDragOver(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != centerPane && event.getDragboard().hasFiles()){
                    event.acceptTransferModes(TransferMode.COPY);
                }
                event.consume();
            }
        });
        centerPane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()){
                    list.clear();
                    for(File file : db.getFiles()){
                        list.add(file.getAbsolutePath());
                    }
                    if(list.size()!=1){
                        queueName = "Queue - No Name";
                        setQueue();
                    }
                    else{
                        if(mediaPlayer==null){
                        }else{
                            clearMedia();
                        }

                        playingFromPlaylist = false;
                        queueOpen = false;
                        queue.setDisable(true);

                        root.setRight(null);

                        MediaData.MEDIAPATH = new File(list.get(0));
                        list.clear();
                        openMedia();
                    }
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
        
        increaseVolItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if(mediaPlayer != null)
                    if(mediaPlayer.getVolume()*100 != 0)
                        mediaPlayer.setVolume((5+volSlider.getValue()) / 100);
                volSlider.setValue(volSlider.getValue()+5);
                volIconSetting();
            }
        });
        decreaseVolItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if(mediaPlayer != null)
                    if(mediaPlayer.getVolume()*100 != 0)
                        mediaPlayer.setVolume((5-volSlider.getValue()) / 100);
                volSlider.setValue(volSlider.getValue()-5);
                volIconSetting();
            }
        });
        
        muteItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if(mediaPlayer!=null){
                    if(!mediaPlayer.isMute()){
                        mediaPlayer.setMute(true);
                        volControlImage.setImage(new Image(getClass().getResourceAsStream("IconFolder/muted.png")));
                    }else{
                        mediaPlayer.setMute(false);
                        volIconSetting();
                    }
                }
            }
        });
        
        seek.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.pause();
                if(!seek.isValueChanging()){
                    mediaPlayer.seek(Duration.seconds(seek.getValue()));
                    mediaPlayer.play();
                }else{
                    mediaPlayer.pause();
                }
            }
        });
        
        seek.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.seek(Duration.seconds(seek.getValue()));
                mediaPlayer.play();
            }
        });
        
        volSlider.valueProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(mediaPlayer != null)
                    mediaPlayer.setVolume(volSlider.getValue() / 100);
                volIconSetting();
            }
        });
        
        
        queueListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    int i = queueListView.getSelectionModel().getSelectedIndex();
                    if(i!=-1){
                        clearMedia();
                        MediaData.MEDIAPATH = new File(list.get(i));
                        playlistControl = i;
                        openMedia();
                    }
                }
            }
        });
    }
    
    @FXML
    private void openMenuItem(ActionEvent event){
        if(mediaPlayer == null){
            
        }else{
            clearMedia();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        playingFromPlaylist = false;
        queueOpen = false;
        queue.setDisable(true);
        
        root.setRight(null);
        
        //fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Audio Files","*wav","*mp3","*ogg"));
        MediaData.MEDIAPATH = fileChooser.showOpenDialog(bsContainer.getScene().getWindow());
        playingFromPlaylist = false;
        openMedia();
    }
    
    @FXML
    private void openDirMenuItem(ActionEvent event){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDir = directoryChooser.showDialog((Stage) seek.getScene().getWindow());
        
        try{
            if((selectedDir.list().length)>0){
                File[] files = selectedDir.listFiles();
                for(File file: files){
                    String s = getFileExt(file.getName());
                    System.out.println(s);
                    if(s.equals("wav") || s.equals("ogg") || s.equals("mp3") || s.equals("mp4"))
                        list.add(file.getAbsolutePath());
                }
                if(!list.isEmpty()){
                    queueName = selectedDir.getName();
                    File savePlaylistFile = new File(playlistDirect+"\\"+queueName+".txt");
                    if(savePlaylistFile.createNewFile()){
                        PrintWriter writer= new PrintWriter(new FileWriter(savePlaylistFile));
                        for(int x=0;x<list.size();x++){
                            if(x==0)
                                writer.append(list.get(x));
                            else
                                writer.append("\n"+list.get(x));
                        }
                        writer.close();
                        
                        FileWriter playlistF = new FileWriter("playlist.txt");
                        playlistF.write(savePlaylistFile.getAbsolutePath());
                    }
                    setQueue();
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    @FXML
    private void exitMenuItem(ActionEvent event){
        try{
            PrintWriter pw = new PrintWriter("playlist.txt");
            pw.append("");
            pw.close();
        }catch(Exception e){
            System.out.println(e);
        }
        Platform.exit();
    }
    
    @FXML
    private void PlaylistListner(ActionEvent event) throws IOException{
        Parent root1 = FXMLLoader.load(getClass().getResource("MediaPlaylist.fxml"));
        Scene scene = new Scene(root1, 400,300);
        
        Stage stage = new Stage();
        stage.setTitle("Playlist");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("IconFolder/playlist.png")));
        //stage.initOwner((Stage)seek.getScene().getWindow());
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                try{
                    File file = new File("playlist.txt");
                    FileReader fr = new FileReader(file.getAbsolutePath());
                    BufferedReader br = new BufferedReader(fr);
                    String s;
                    if((s=br.readLine())!=null){
                        queueName = getFileName(removeExt(s));
                        playlistFile = new File(s);
                        BufferedReader br1 = new BufferedReader(new FileReader(playlistFile));
                        String a;
                        queueListView.getItems().clear();
                        list.clear();
                        while((a=br1.readLine())!=null){
                            list.add(a);
                        }
                        
                        if(mediaPlayer!=null){
                            clearMedia();
                        }
                        
                        br1.close();
                        
                        setQueue();
                    }
                    br.close();
                }
                catch(Exception e){
                    System.out.println(e);
                }
            }
        });
    }
    
    @FXML
    private void jumpForward(ActionEvent event){
        if(mediaPlayer!=null){
            double l = seek.getValue();
            if(15+l>seek.getMax()){
                if(repeatB.isSelected()){
                    mediaPlayer.seek(Duration.ZERO);
                }else{
                    clearMedia();
                }
            }else{
                mediaPlayer.seek(Duration.seconds(seek.getValue()+15));
            }
        }
    }
    
    @FXML
    private void jumpBackward(ActionEvent event){
        if(mediaPlayer!=null){
            if((seek.getValue()-15)<=0){
                mediaPlayer.seek(Duration.ZERO);
            }else{
                mediaPlayer.seek(Duration.seconds(seek.getValue()-15));
            }
        }
    }
    
    String timeConversion(int time){
        if(time>3600){
            int hours = time / 3600;
            int minutes = (time % 3600) / 60;
            int seconds = time % 60;
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        else if(time>60){
            int minutes = (time % 3600) / 60;
            int seconds = time % 60;
            return String.format("%02d:%02d", minutes, seconds);
        }else{
            int minutes = 0;
            int seconds = time % 60;
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
    
    @FXML
    private void setPlayPause(){
        if(!mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){
            play.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("IconFolder/pause-button.png"))));
            playItem.setGraphic(pauseImgV);
            playItem.setText("  Pause");
            mediaPlayer.play();
        }else{
            play.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("IconFolder/play-arrow.png"))));
            playItem.setGraphic(playImgV);
            playItem.setText("  Play");
            mediaPlayer.pause();
        }
    }
    
    @FXML
    private void stopAction(){
        if(playingFromPlaylist){
            try{
                PrintWriter pw = new PrintWriter("playlist.txt");
                pw.append("");
                pw.close();
            }catch(Exception e){
                System.out.println(e);
            }
            list.clear();
            playingFromPlaylist=false;
            playlistControl=0;
            
            queueListView.getItems().clear();
            
            root.setRight(null);
            queueOpen = false;
            queue.setDisable(true);
        }
        clearMedia();
    }
    
    @FXML
    private void nextAction(){
        if(repeatB.isSelected()){
            mediaPlayer.seek(Duration.ZERO);
        }else{
            if(playingFromPlaylist){
                if(shuffle.isSelected()){
                    clearMedia();
                    Random r = new Random();
                    playlistControl = r.nextInt(list.size());
                    MediaData.MEDIAPATH = new File(list.get(playlistControl));
                    openMedia();
                }else{
                    if(playlistControl==(list.size()-1)){
                        clearMedia();
                        playlistControl =  0;
                        MediaData.MEDIAPATH = new File(list.get(playlistControl));
                        System.out.println("full: "+list.get(playlistControl));
                        openMedia();
                    }else{
                        clearMedia();
                        playlistControl++;
                        MediaData.MEDIAPATH = new File(list.get(playlistControl));
                        System.out.println("Not full: "+list.get(playlistControl));
                        openMedia();
                    }
                }
            }
        }
    }
    
    @FXML
    private void previousAction(){
        if(repeatB.isSelected()){
            mediaPlayer.seek(Duration.ZERO);
        }else{
            if(mediaPlayer!=null){
                if(playingFromPlaylist){
                    if(shuffle.isSelected()){
                        clearMedia();
                        Random r = new Random();
                        playlistControl = r.nextInt(list.size());
                        MediaData.MEDIAPATH = new File(list.get(playlistControl));
                        openMedia();
                    }else{
                        if(playlistControl==0){
                            clearMedia();
                            playlistControl =  list.size()-1;
                            MediaData.MEDIAPATH = new File(list.get(playlistControl));
                            openMedia();
                        }else{
                            clearMedia();
                            playlistControl--;
                            MediaData.MEDIAPATH = new File(list.get(playlistControl));
                            openMedia();
                        }
                    }
                }
            }
        }
    }
    
    @FXML
    private void queueListener(){
        if(queueOpen){
            System.out.println("Hello");
            root.setRight(null);
            queueOpen = false;
        }else{
            System.out.println("Hiii");
            root.setRight(queuePane);
            queueOpen = true;
        }
    }
    
    private void clearMedia(){
        
        mediaPlayer.stop();
        mediaPlayer = null;
        media=null;
        
        centerPane.setCenter(animatedImage);
        
        play.setDisable(true);
        stop.setDisable(true);
        previous.setDisable(true);
        next.setDisable(true);
        
        playItem.setDisable(true);
        stopItem.setDisable(true);
        previousItem.setDisable(true);
        nextItem.setDisable(true);
        
        seek.setDisable(true);
        currentTime.setText("00:00");
        maxTime.setText("00:00");
        stg.setTitle("Media Player");
    }
    
    private void openMedia(){
        if(MediaData.MEDIAPATH!=null){
            if(mediaPlayer!=null){
                mediaPlayer.stop();
                mediaPlayer = null;
                media=null;
            }
            System.out.println(playlistControl);
            addFile(MediaData.MEDIAPATH.getAbsolutePath());
            MediaData.MEDIANAME = getFileName(MediaData.MEDIAPATH.getAbsolutePath());
            MediaData.MEDIAEXT = getFileExt(MediaData.MEDIANAME);
            
            stg = (Stage) seek.getScene().getWindow();
            stg.setTitle(MediaData.MEDIANAME);
            media = new Media(MediaData.MEDIAPATH.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            switch (MediaData.MEDIAEXT) {
                case "wav":
                    centerPane.setCenter(wavImage);
                    centerPane.setStyle("-fx-background-color: #FF2400;");
                    break;
                case "mp3":
                    centerPane.setCenter(mp3Image);
                    centerPane.setStyle("-fx-background-color: #FF2400;");
                    break;
                case "ogg":
                    centerPane.setCenter(oggImage);
                    centerPane.setStyle("-fx-background-color: #FF2400;");
                    break;
                case "mp4":
                    mediaView.setMediaPlayer(mediaPlayer);
                    centerPane.setCenter(mediaView);
                    centerPane.setStyle("-fx-background-color: #000;");
                    break;
                default:
                    centerPane.setCenter(animatedImage);
                    centerPane.setStyle("-fx-background-color: #FF2400;");
                    break;
            }
            
            System.out.println("Before Runnable");
            mediaPlayer.setOnReady(new Runnable(){

                @Override
                public void run() {
                    System.out.println("Media Is Ready To Play");
                    MediaData.MEDIALENGHT = mediaPlayer.getTotalDuration().toSeconds();

                    play.setDisable(false);
                    previous.setDisable(false);
                    next.setDisable(false);
                    stop.setDisable(false);
                    
                    playItem.setDisable(false);
                    previousItem.setDisable(false);
                    nextItem.setDisable(false);
                    stopItem.setDisable(false);

                    seek.setDisable(false);
                    seek.setMin(0);
                    seek.setValue(0);
                    seek.setShowTickMarks(false);
                    seek.setShowTickLabels(false);
                    seek.setMax(MediaData.MEDIALENGHT);
                    
                    mediaPlayer.setVolume(volSlider.getValue() / 100);

                    maxTime.setText(timeConversion((int)MediaData.MEDIALENGHT));

                    mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>(){

                        @Override
                        public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                            seek.setValue(newValue.toSeconds());
                            currentTime.setText(""+timeConversion((int)newValue.toSeconds()));
                        }
                    });
                }
            });
            
            mediaPlayer.play();
            
            setPlayPause();
            
            
            mediaPlayer.setOnEndOfMedia(new Runnable() {

                @Override
                public void run() {
                    if(repeatB.isSelected()){
                        mediaPlayer.seek(Duration.ZERO);
                    }else{
                        if(playingFromPlaylist){
                            if(shuffle.isSelected()){
                                clearMedia();
                                Random r = new Random();
                                playlistControl = r.nextInt(list.size());
                                MediaData.MEDIAPATH = new File(list.get(playlistControl));
                                openMedia();
                            }else{
                                if(playlistControl==(list.size()-1)){
                                    clearMedia();
                                    playlistControl=0;
                                    MediaData.MEDIAPATH = new File(list.get(playlistControl));
                                    openMedia();
                                    
                                }else{
                                    clearMedia();
                                    playlistControl++;
                                    MediaData.MEDIAPATH = new File(list.get(playlistControl));
                                    openMedia();
                                }
                            }
                        }else{
                            clearMedia();
                        }
                    }
                }
            });
        }
    }
    
    private void addFile(String path){
        try{
            FileReader fr = new FileReader(recentFile.getAbsolutePath());
            BufferedReader br = new BufferedReader(fr);
           
            StringBuffer a = new StringBuffer("");
            String s;
            int i = 0;
           
            while((s=br.readLine())!=null){
                i++;
                a.append(s+"\n");
            }
            
            br.close();
           
            FileWriter fw = new FileWriter(recentFile.getAbsolutePath());
            PrintWriter pw  = new PrintWriter(fw);
           
            pw.append(path+"\n"+a.toString());
            
            pw.close();
            
            dup(path);
            
            recentMediaLoader();
            
        }
        catch(Exception e){
           
            System.out.println(e);
           
        }
    }
    
    
    private void recentMediaLoader(){
        
        try{
            FileReader fr = new FileReader(recentFile.getAbsolutePath());
            BufferedReader br = new BufferedReader(fr);
            
            String a;
            StringBuffer sb = new StringBuffer();
            int i = 0;
            
            while((a=br.readLine())!=null){
                if(i<10){
                    sb.append(a+"\n");
                    recentMenuItem[i].setText(a);
                    i++;
                }
            }
            
            if(i==0){
                recentMenu.setDisable(true);
            }else{
                recentMenu.setDisable(false);
            }
            
            br.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
        
    }
    
    private void dup(String lineToRemove){
        try{
            
            BufferedReader reader = new BufferedReader(new FileReader(recentFile.getAbsolutePath()));
            
            String currentLine;
            
            int i=0;
            
            StringBuffer sb = new StringBuffer();

            while((currentLine = reader.readLine()) != null) {
                if(currentLine.equals(lineToRemove) && i!=0){
                    
                }else{
                    if(i!=0){
                        sb.append("\n"+currentLine);
                    }else{
                        sb.append(currentLine);
                    }
                } 
                i++;
            }
            
            reader.close();
            
            PrintWriter writer = new PrintWriter(new FileWriter(recentFile.getAbsolutePath()));
            
            writer.append(sb);
            
            writer.close();
            
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    private void openOnMenuItemPressed(int index){
        if(recentMenuItem[index].getText().equals("No Data")){}
        else{
            if(mediaPlayer==null){
                
            }else{
                clearMedia();
            }
            
            playingFromPlaylist = false;
            queueOpen = false;
            queue.setDisable(true);
            
            root.setRight(null);
            
            MediaData.MEDIAPATH = new File(recentMenuItem[index].getText());
            openMedia();
        }
    }
    
    private String getFileExt(String s){
        if(s.lastIndexOf(".") != -1 && s.lastIndexOf(".") != 0)
            s = s.substring(s.lastIndexOf(".")+1);
        else  
            s = "";
        return s;
    }
    
    private String getFileName(String s){
        if(s.lastIndexOf("\\") != -1 && s.lastIndexOf("\\") != 0)
            s = s.substring(s.lastIndexOf("\\")+1);
        else  
            s = "No Name";
        return s;
    }
    
    private String removeExt(String s){
        if(s.lastIndexOf(".") != -1 && s.lastIndexOf(".") != 0)
            s = s.substring(0,s.lastIndexOf("."));
        return s;
    }
    
    private void volIconSetting(){
        double vol = volSlider.getValue();
        if(vol>66.6 && vol<=100)
            volControlImage.setImage(new Image(getClass().getResourceAsStream("IconFolder/volume.png")));
        else if(vol>33.3 && vol<66.6)
            volControlImage.setImage(new Image(getClass().getResourceAsStream("IconFolder/low-volume.png")));
        else if(vol>1 && vol<33.3)
            volControlImage.setImage(new Image(getClass().getResourceAsStream("IconFolder/no-vol.png")));
        else
            volControlImage.setImage(new Image(getClass().getResourceAsStream("IconFolder/muted.png")));
    }
    
    
    private void setQueue(){
        for(String s : list){
            s = getFileName(s);
            queueListView.getItems().add(s);
        }
        queueNameLabel.setText(queueName);
        playlistControl = 0;
        MediaData.MEDIAPATH = new File(list.get(0));

        root.setRight(queuePane);

        playingFromPlaylist = true;
        queue.setDisable(false);
        queueOpen = true;

        openMedia();
    }
}