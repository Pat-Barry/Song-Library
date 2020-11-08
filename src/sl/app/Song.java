//Philip Murray and Patrick Barry

package sl.app;

public class Song {
	public String Name;
	public String Artist;
	public String Album;
	public String Year;
	public Song(String Name, String Artist, String Album, String Year) {
		if(Album == null) {
			Album = "";
		}
		if(Year == null) {
			Year = "";
		}
		this.Name = Name;
		this.Artist = Artist;
		this.Album = Album;
		this.Year = Year;
	}	
}
