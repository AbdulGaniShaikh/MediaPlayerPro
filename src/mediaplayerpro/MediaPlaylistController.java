/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaplayerpro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MediaPlaylistController implements Initializable {
    
    @FXML
    Button createButton,addSongButton,searchButton,viewPlaylist;
    
    @FXML
    ListView<String> songListView,playListView;
    
    @FXML
    TextField searchNameField,playlistNameField;
    
    File playlistDirect;
    
    List<String> list,searchList;
    
    String s;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        list = new ArrayList<>();
        searchList = new ArrayList<>();
        
        playlistDirect = new File("playlists");
        
        try{
            playlistDirect.mkdir();
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        
        playListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        fileDataSet();
        
        playListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    openPlaylist();
                }
            }
        });
        
        searchNameField.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (oldValue.length()>newValue.length()) {
                    playListView.getItems().clear();
                    for(String s: list){
                        if(s.contains(newValue))
                            searchList.add(s);
                    }
                    playListView.getItems().clear();
                    playListView.getItems().addAll(searchList);
                    searchList.clear();
                }
                else if(oldValue.length()<newValue.length()){
                    playListView.getItems().clear();
                    for(String s: list){
                        if(s.contains(newValue))
                            searchList.add(s);
                    }
                    playListView.getItems().clear();
                    playListView.getItems().addAll(searchList);
                    searchList.clear();
                }
                else if(newValue.equals("")){
                    playListView.getItems().clear();
                    searchList.clear();
                    fileDataSet();
                }
            }
        });
        
        createButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try{
                    if(playlistNameField.getText().equals("") || songListView.getItems().isEmpty()){
                        
                    }else{
                        File sfile;
                        sfile = new File(playlistDirect+"\\"+playlistNameField.getText()+".txt");
                        if(sfile.createNewFile()){
                            PrintWriter writer= new PrintWriter(new FileWriter(sfile));
                            for(int x=0;x<songListView.getItems().size();x++){
                                if(x==0)
                                    writer.append(songListView.getItems().get(x));
                                else
                                    writer.append("\n"+songListView.getItems().get(x));
                            }
                            writer.close();
                            FileWriter playlistF = new FileWriter("playlist.txt");
                            playlistF.write(sfile.getAbsolutePath());
                            
                            songListView.getItems().clear();
                            playListView.getItems().clear();
                            list.clear();
                            playlistNameField.setText("");
                            fileDataSet();
                        }
                    }
                }
                catch(Exception e){
                    System.out.println(e);
                }
            }
        });
        
        addSongButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Audio Files","*wav","*mp3","*ogg","*mp4"));
                File f = fileChooser.showOpenDialog(addSongButton.getScene().getWindow());
                if(f!=null){
                    songListView.getItems().add(f.getAbsolutePath());
                }
            }
        });
        
        
    }
    
    private void fileDataSet(){
        
        try{
            if((playlistDirect.list().length)>0){
                File[] files = playlistDirect.listFiles();
                for(File file: files){
                    list.add(file.getName());
                }
                playListView.getItems().addAll(list);
            }else{
                playListView.getItems().add("No Playlists Created");
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    @FXML
    private void deletePlaylist(){
        if(playListView.getSelectionModel().getSelectedIndex()<=(-1)){
            
        }else{
            try{
                list.clear();
                String s = playListView.getSelectionModel().getSelectedItem();
                for(File file: playlistDirect.listFiles()){
                    if(file.getName().equals(s)){
                        file.delete();
                    }
                    else{
                        list.add(file.getName());
                    }
                }
                playListView.getItems().clear();
                playListView.getItems().addAll(list);
                
            }catch(Exception e){
                System.out.println(e);
            }
        }
    }
    
    @FXML
    private void deleteSong(){
        if(songListView.getSelectionModel().getSelectedIndex()<=(-1)){
            
        }else{
            int index = songListView.getSelectionModel().getSelectedIndex();
            songListView.getItems().remove(index);
        }
    }
    @FXML
    private void openPlaylist(){
        String s = playListView.getSelectionModel().getSelectedItem();
        if(!s.equals("No Playlists Created")){
            try {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setTitle("You want to play "+ s +" playlist");
                a.setHeaderText("Press OK and close the Playlist Window to play the Playlist");
                Optional<ButtonType> option = a.showAndWait();

                if(option.get()==ButtonType.OK){
                    String fname = new File("src").getAbsolutePath();
                    File playlistFile = new File("playlist.txt");
                    System.out.println("Here");
                    PrintWriter pw = new PrintWriter(playlistFile);
                    pw.append(fname.substring(0,fname.length()-4)+"\\playlists\\"+s);
                    pw.close();
                }else if(option.get()==ButtonType.CANCEL){
                    PrintWriter pw = new PrintWriter("playlist.txt");
                    pw.append("");
                    pw.close();
                }
            }
            catch (FileNotFoundException ex){
                System.out.println(ex);
            }
        }
    }
    @FXML
    private void viewPlaylist(){
        
    }
}