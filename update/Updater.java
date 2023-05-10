import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Scanner;
import com.andromeda.main.trusty;
import com.google.gson.Gson;

public class Updater {
	
	private static void write(String url, String fileName) throws MalformedURLException, IOException {
		try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
				  FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
				    byte dataBuffer[] = new byte[1024];
				    int bytesRead;
				    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
				        fileOutputStream.write(dataBuffer, 0, bytesRead);
				    }
				    
				   
				} catch (IOException e) {
				    e.printStackTrace();
				}
		 InputStream in = new URL(url).openStream();
		 Files.copy(in, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
	}
	
	    private static void update(String version) {
		try {
			FileWriter writer = new FileWriter(new File("assets/version.dat"));
				writer.write("{\"version\":\""+version+"\"}");
				writer.close();
				
			write("https://credo-downloads.s3.us-east-2.amazonaws.com/public/v1.011/Credo+Calculator+Installer.exe", "Andromeda.exe");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public static void main(String[] args) {
		Thread httpUpdate = new Thread(() -> {
			String rawData = trusty.Requests.get("https://f.techman.dev/d/andromeda/version.json");
			
			Gson gson = new Gson();
			
			Map<?, ?> jsonInCloud = gson.fromJson(rawData, Map.class);
				String release = (String) ((Map<?,?>) jsonInCloud.get("latest")).get("release");
				
			Scanner fileReader = null;
			StringBuilder rawLocal = null;
			
			try {
				fileReader = new Scanner(new FileReader(new File("assets/version.dat")));
			
				rawLocal = new StringBuilder();
					
				while (fileReader.hasNextLine()) {
					rawLocal.append(fileReader.nextLine());
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			fileReader.close();
			
			Map<?, ?> jsonLocal = gson.fromJson(rawLocal.toString(), Map.class);
				String version = (String) jsonLocal.get("version");
				
			if (version.equals(release)) {
				if (System.getProperty("os.name").toLowerCase().contains("mac")) {
					Desktop desktop = Desktop.getDesktop();

				    try {
						desktop.open(new File("./Andromeda"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					Desktop desktop = Desktop.getDesktop();

				    try {
						desktop.open(new File("Andromeda.exe"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				if (System.getProperty("os.name").toLowerCase().contains("mac")) {
					try {
						Desktop.getDesktop().browse(new URI("https://andromeda.techman.dev/update-assistant"));
					} catch (IOException e) {
						e.printStackTrace();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				} else {
					update(release);
					Desktop desktop = Desktop.getDesktop();

				    try {
						desktop.open(new File("Andromeda.exe"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		httpUpdate.start();
	}
}