//Philip Murray and Patrick Barry

package sl.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FileService {
	public static ArrayList<Song> read() {
		ArrayList<Song> SongArray = new ArrayList<Song>();
		FileInputStream fis;
		try {
			fis = new FileInputStream("savefile.txt");
			Scanner in = new Scanner(fis);
			
			while(in.hasNext()) {
				
				boolean infield = false;
				String[] fields = {"","","",""};
				
				String songString = in.nextLine();
				
				int stack = 0;
				int field = 0;
				for(int i=0; i<songString.length(); i++) {
					if(songString.substring(i,i+1).equals("'")) {
						if(!infield) {
							infield = true;
							stack = 1;
						} else {
							stack--;
							if(stack == 0) {
								infield = false;
								field++;
							}
						}
					} else if(infield) {
						fields[field] = fields[field] + songString.substring(i,i+1);
					}
				}
				SongArray.add(new Song(fields[0], fields[1], fields[2], fields[3]));
			}
		} catch (FileNotFoundException e) {
			System.out.println("There is no file");
		}
		return SongArray;
	}
	
	public static void write(ArrayList<Song> SongArray) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("savefile.txt");
			PrintWriter pw = new PrintWriter(fos);
			for(Song song : SongArray) {
				pw.println("<Song name='" + song.Name + "' artist='" + song.Artist + "' album='" + song.Album + "' Year='" + song.Year + "' />");
			}
			pw.close();
		} catch (FileNotFoundException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("FILE NOT FOUND EXCEPTION");
		}
	}
}
