//Philip Murray and Patrick Barry

package sl.view;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sl.app.FileService;
import sl.app.Song;
import sl.app.popup;

public class SLController {

	private int selectIndex;
	
	private int mode = 0;
	private String[] modes = {"","Add","Edit","Delete"};
	
	private ArrayList<Song> SongArray;
	
	@FXML
    private ListView<String> SongList;
    
	
    @FXML private Text nameDetail;
    @FXML private Text artistDetail;
    @FXML private Text albumDetail;
    @FXML private Text yearDetail;
    
    @FXML private TextField Name;
    @FXML private TextField Artist;
    @FXML private TextField Album;
    @FXML private TextField Year;
	
    @FXML private Button AddButton;
    @FXML private Button EditButton;
    @FXML private Button DeleteButton;
    
    @FXML private Button ConfirmButton;
    @FXML private Button CancelButton;
    
    private ObservableList<String> observableList;
    
    public void start(Stage mainStage) {
    	SongArray = FileService.read();
    	observableList = FXCollections.observableArrayList();
    	for(Song song : SongArray) {
    		observableList.add(song.Name + " by " + song.Artist);
    	}
    	SongList.setItems(observableList);	
    	SongList.getSelectionModel().select(0);
    	SongList.getSelectionModel().selectedIndexProperty().addListener( (obs, oldVal, newVal) -> showItem(mainStage));
    	 
    	adjustLayoutForMode(0);
    	
    	setDetails();
    	
    }
    void setDetails() {
    	if(SongArray.size() == 0) { 
    		nameDetail.setText("");
        	artistDetail.setText("");
        	albumDetail.setText("");
        	yearDetail.setText("");
    		return; 
    	}
    	try {
	    	Song selectedSong = SongArray.get(SongList.getSelectionModel().getSelectedIndex());
	    	nameDetail.setText(selectedSong.Name);
	    	artistDetail.setText(selectedSong.Artist);
	    	albumDetail.setText(selectedSong.Album);
	    	yearDetail.setText(selectedSong.Year);
    	} catch(Exception e) {
    		//No songs in the list;
    	}
    }
    void wipeText() {
    	Name.setText(null); Artist.setText(null); Album.setText(null); Year.setText(null);
    }
    void setCheckText(int mode) {
    	ConfirmButton.setText("Confirm "+modes[mode]);
    	CancelButton.setText("Cancel "+modes[mode]);
    }
    void usingText(boolean b) {
    	Name.setDisable(!b); Artist.setDisable(!b); Album.setDisable(!b); Year.setDisable(!b);
    } 
    void usingModeButtons(boolean b) {
    	AddButton.setDisable(!b); EditButton.setDisable(!b || (observableList.size() == 0)); DeleteButton.setDisable(!b || (observableList.size() == 0));
    }
    void usingChecks(boolean b) {
    	ConfirmButton.setDisable(!b); CancelButton.setDisable(!b);
    }
  
    void adjustLayoutForMode(int newMode) {
    	wipeText();
    	setCheckText(newMode);
    	if(newMode == 0) {
    		SongList.setDisable(false); usingModeButtons(true);  usingText(false); usingChecks(false);
    	}
    	if(newMode == 1 || newMode == 2) {
    		SongList.setDisable(true);  usingModeButtons(false); usingText(true);  usingChecks(true);
    	}
    	if(newMode == 3) {
    		SongList.setDisable(true);  usingModeButtons(false); usingText(false); usingChecks(true);
    	}
    }
    boolean insert(int i, Song inputSong) {
    	SongArray.add(i, inputSong);
    	observableList.add(i, inputSong.Name + " by " + inputSong.Artist);
    	SongList.getSelectionModel().select(i);
    	return true;
    }
    void insert(Song inputSong) {
    	SongArray.add(inputSong);
    	observableList.add(inputSong.Name + " by " + inputSong.Artist);
    	SongList.getSelectionModel().select(SongArray.size()-1);
    }
    void confirm1() {
    	Song inputSong = new Song(Name.getText(), Artist.getText(), Album.getText(), Year.getText());
    	if (inputSong.Name == null || inputSong.Artist == null) {
			popup.display("Error!", "Album and Song name must have at least 0 characters");
			return;
    	}
    	if(inputSong.Year.length() > 0) {
	    	try {
	    		int z = Integer.parseInt(inputSong.Year);
	    		if(z < 0 || 9999 < z) {
	    			popup.display("Error", "Year must be range from 0 to 9999");
	    			return;
	    		}
	    	} catch(NumberFormatException e) {
	    		popup.display("Error", "Year must be a non-decimal number");
	    		return;
	    	}
    	}
    	boolean inserted = false;
    	for(int i= 0; i<SongArray.size(); i++) {
    		int c = SongArray.get(i).Name.compareToIgnoreCase(inputSong.Name);
    		if(c > 0) {
    	    	inserted = insert(i, inputSong);
    	    	break;
    		} else if (c == 0) {
    			int c2 = SongArray.get(i).Artist.compareToIgnoreCase(inputSong.Artist);
    			if(c2 > 0) {
    				inserted = insert(i, inputSong);
    				break;
    			} else if(c2 == 0) {
    				popup.display("Error!","Song/Artist pair already exists");
    				return;
    			}
    		}
    	}
    	if(!inserted) {
    		insert(inputSong);
    	}
    }
    void confirm2() {
    	Song inputSong = new Song(Name.getText(), Artist.getText(), Album.getText(), Year.getText());
    	if(inputSong.Name == null || inputSong.Artist == null) {
    		popup.display("Error!","Album and Song name must have at least 0 characters");
    		return;
    	}
    	if(inputSong.Year.length() > 0) {
	    	try {
	    		int z = Integer.parseInt(inputSong.Year);
	    		if(z < 0 || 9999 < z) {
	    			popup.display("Error!","Cannot make edit. Year must be range from 0 to 9999");
	    			return;
	    		}
	    	} catch(NumberFormatException e) {
	    		popup.display("Error!", "Valid year not entered");
	    		return;
	    	}
    	}
    	for(int i=0; i<SongArray.size(); i++) {
    		if(SongArray.get(i).Name.equals(inputSong.Name) && SongArray.get(i).Artist.equals(inputSong.Artist) && i!=SongList.getSelectionModel().getSelectedIndex()) {
    			popup.display("Error!","Cannot make edit as there is already a song with this name/artist pair");
    			return;
    		}
    	}
    	delete(SongList.getSelectionModel().getSelectedIndex());
    	boolean inserted = false;
    	for(int i= 0; i<SongArray.size(); i++) {
    		int c = SongArray.get(i).Name.compareToIgnoreCase(inputSong.Name);
    		if(c > 0) {
    	    	inserted = insert(i, inputSong);
    	    	break;
    		} else if (c == 0) {
    			int c2 = SongArray.get(i).Artist.compareToIgnoreCase(inputSong.Artist);
    			if(c2 > 0) {
    				inserted = insert(i, inputSong);
    				break;
    			} 
    		}
    	}
    	if(!inserted) {
    		insert(inputSong);
    	}
    }
    void delete(int i) {
        int si = SongList.getSelectionModel().getSelectedIndex();
        observableList.remove(si);
        SongArray.remove(si);
        if(SongArray.size() == 0 || SongArray.size() == si) {
        	return;
        }
        SongList.getSelectionModel().select(si);
    }
    void confirm3() {
    	delete(SongList.getSelectionModel().getSelectedIndex());
    }
 
    public void showItem(Stage mainStage) {
    	setDetails();
    }

    @FXML
    void add(ActionEvent event) {
    	adjustLayoutForMode(1);
    	mode = 1;
    }

    @FXML
    void cancel(ActionEvent event) {
    	adjustLayoutForMode(0);
    	mode = 0;
    }

    @FXML
    void confirm(ActionEvent event) {
    	if(mode == 1) {
    		confirm1();
    	}
    	if(mode == 2) {
    		confirm2();
    	}
    	if(mode == 3) {
    		confirm3();
    	}
    	FileService.write(SongArray);
    	adjustLayoutForMode(0);
    	mode = 0;
    }

    @FXML
    void delete(ActionEvent event) {
    	selectIndex = SongList.getSelectionModel().getSelectedIndex();
    	adjustLayoutForMode(3);	
    	mode = 3;
    }

    @FXML
    void edit(ActionEvent event) {
    	
    	adjustLayoutForMode(2);
    	mode = 2;
    	
    	selectIndex = SongList.getSelectionModel().getSelectedIndex();
    	Name.setText(SongArray.get(selectIndex).Name);
    	Artist.setText(SongArray.get(selectIndex).Artist);
    	Album.setText(SongArray.get(selectIndex).Album);
    	Year.setText(SongArray.get(selectIndex).Year);
    }
}
