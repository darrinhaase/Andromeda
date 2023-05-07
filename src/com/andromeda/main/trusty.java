package com.andromeda.main;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.Timer;

public class trusty implements ActionListener {
	
	
	//Variables
	private static JFrame frame;
	private static String response;
	private static JButton button;
	private static HashMap<String, methodAddition> buttonNamesAndValues = new HashMap<>();

	
	//Frame Method
	public static JFrame frame(String name, int sizeW, int sizeL, String imagepath, boolean exitOnClose)  {
		
		frame = new JFrame(name);
		frame.setName(name);
		frame.setSize(sizeW, sizeL);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(Color.white);
		frame.setBackground(Color.white);
		
		
		if (exitOnClose) {
			
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		}
		
		else {
			
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			
		}
		
		if (imagepath == null) {
			
		frame.setIconImage(null);
		
		}
		
		else {
			
			frame.setIconImage(new ImageIcon(imagepath).getImage());
			
		}
		
		return frame;
		
	}
	
	
	//Label Method
	public static JLabel label(String name, String words, int x, int y, int width, int height, Font font) {

		JLabel label = new JLabel(words);
		label.setName(name);
		label.setFont(font);
		label.setBounds(x, y, width, height);
		frame.getContentPane().add(label);
		
		return label;
	}
	
	
	//Entry Method
	public static JTextField entryField(String name, int x, int y, int width, int height) {
		JTextField textField = new JTextField();
		textField.setName(name);
		textField.setBounds(x, y, width, height);
		frame.getContentPane().add(textField);		
		return textField;
	}
	
	
	//Entry Method
	public static JEditorPane entry(String name, int x, int y, int width, int height) {
		
		JEditorPane pane = new JEditorPane();
		pane.setName(name);
		pane.setBounds(x, y, width, height);
		frame.getContentPane().add(pane);
		
		return pane;
	}
	
	public static BufferedImage resizeImage(String path, int targetWidth, int targetHeight) throws IOException {
	    BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D graphics2D = resizedImage.createGraphics();
	    graphics2D.drawImage(ImageIO.read(new File(path)), 0, 0, targetWidth, targetHeight, null);
	    graphics2D.dispose();
	    return resizedImage;
	}
	
	
	//Picture Label Method
	public static JLabel piclabel(String name, String words, int x, int y, int width, int height, String imagepath) {
		
		JLabel piclabel = new JLabel(words);
		piclabel.setName(name);
		piclabel.setBounds(x, y, width, height);
		piclabel.setIcon(new ImageIcon(imagepath));
				
		return piclabel;
	}
	
	public static JLabel piclabel(String name, String words, int x, int y, int width, int height, BufferedImage img) {
		
		JLabel piclabel = new JLabel(words);
		piclabel.setName(name);
		piclabel.setBounds(x, y, width, height);
		piclabel.setIcon(new ImageIcon(img));
				
		return piclabel;
	}
	
	//Button Method
	public static JButton button(String name, String words, int x, int y, int width, int height, methodAddition ma) {
		
		button = new JButton();
		button.setName(name);
		button.setText(words);
		button.setBounds(x, y, width, height);
		frame.getContentPane().add(button);
		trusty trusty = new trusty();
		trusty.addListenerToButton();
		buttonNamesAndValues.put(name, ma);
	
		
		return button;
	}
	
	
	//Button functions
	private void addListenerToButton() {
		button.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		for (String i : buttonNamesAndValues.keySet()) {			
			JButton castedButtonToGetName = (JButton) e.getSource();
			
			if (castedButtonToGetName.getName() == i) {
				buttonNamesAndValues.get(i).actionEventer("NULL");
			}
		}
		
	}
	
	@FunctionalInterface
	protected interface methodAddition {
		void actionEventer(String method);
	}
	
	
	//Panel Method
	public static JPanel panel(String name, int x, int y, int width, int height, Color color) {
		
		JPanel panel = new JPanel();
		panel.setName(name);
		panel.setBackground(color);
		panel.setBounds(x, y, width, height);
		frame.getContentPane().add(panel);
		
		return panel;
	}
	
	
	//Input Method
	public static String input(String prompt) {
		System.out.println(prompt);
		Scanner scanner = new Scanner(System.in);
		response = scanner.nextLine();
		scanner.close();
		return response;
	}
	
	
	//Print Method
	public static void print(String statement) {
		System.out.println(statement);
	}
	
	
	//Int Casting Method
	public static int Int(String inputwords) {
		int i = Integer.parseInt(inputwords);
		return i;
	}
	public static int Int(char[] inputwords) {
		int i = Integer.parseInt(inputwords.toString());
		return i;
	}
	
	
	//String casting method
	public static String str(int inputNumber) {
		String casted = Integer.toString(inputNumber);
		return casted;
	}
	public static String str(double inputNumber) {
		String casted = Double.toString(inputNumber);
		return casted;
	}
	public static String str(float inputNumber) {
		String casted = Float.toString(inputNumber);
		return casted;
	}
	public static String str(char[] inputNumber) {
		String casted = inputNumber.toString();
		return casted;
	}
	public static String str(long inputNumber) {
		String casted = Long.toString(inputNumber);
		return casted;
	}
	
	
	//Formatting String Method
	public static String format(String inputToBeFormatted, String inputReplacingBraces) {

		char[] inputCharArray = inputToBeFormatted.toCharArray();
		String result = "";
		
		for (int i = 0; i < inputCharArray.length; i++) {
			if (inputCharArray[i]=='{' && inputCharArray[i+1]=='}') {
				i+=1;
				result+=inputReplacingBraces;
			}
			else {
				result+=inputCharArray[i];
			}
		}
		
		return result;
	}
	
	
	//Yes or No response
	public static boolean ynAsk(String prompt) {
		Scanner scanner = new Scanner(System.in);
		
		while (true) {
			System.out.println(prompt);
			String response = scanner.nextLine().toLowerCase();
			
			if (response.equals("y") || response.equals("yes")) {
				scanner.close();
				return true;
			}
			else if (response.equals("n") || response.equals("no")) {
				scanner.close();
				return false;
			}
			else {
				System.out.println("This is a yes or no question please respond accordingly.\n");
			}
		}
	}
	
	
	//Copy Function
	public static void copy(String inputCopy) {
		StringSelection selection = new StringSelection(inputCopy);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
	}
	
	
	
	//Class to allow for ease of use 2d physics
	
	/*
	 * !!!!!!!
	 * UNFINISHED
	 * !!!!!!!
	 */
	
	public static class physics {
		
		@SuppressWarnings("unused")
		private static double WIDTH, HEIGHT, FRICTION, SPEED;
		private static HashMap<String, ArrayList<String>> objects = new HashMap<>();
		private static HashMap<String, ArrayList<String>> barriers = new HashMap<>();
		private static ArrayList<String> currentData = new ArrayList<>();
		
		
		public static void createPhysics(int windowWidth, int windowHeight, double frictionOnSurface) {
			WIDTH = windowWidth;
			HEIGHT = windowHeight;
			FRICTION = frictionOnSurface;
		}
		
		
		
		
		private static double currentObjectMass = 0;
		private static int currentObjectWidth = 0;
		private static int currentObjectHeight = 0;
		private static double currentXVel = 0;
		private static double currentYVel = 0;
		private static double currentObjectFriction = 0;
		private static Component currentObject;
		private static boolean xHit = false;
		private static boolean yHit = false;
		
		
		public static void startComponentMotion(Component object, double dx, double dy, int speedControlX, int speedControlY) {
			ArrayList<String> objectDetails = objects.get(object.getName());
			
			currentObject = object;
			
			currentObjectMass = Double.parseDouble(objectDetails.get(0));
			currentObjectWidth = Int(objectDetails.get(1));
			currentObjectHeight = Int(objectDetails.get(2));
			double currentNormalForce = currentObjectMass*9.8;
			currentObjectFriction = currentNormalForce * FRICTION * 0.03;
			
			
			
			Timer timer = new Timer(30, e -> {		
				
				
				if (currentObject.getX() <= 0 || currentObject.getX()+currentObjectWidth >= WIDTH) {
					if (!xHit) {
						xHit = true;
					}
					else {
						xHit = false;
					}
				}
				
				if (currentObject.getY() <= 0 || currentObject.getY()+currentObjectHeight >= HEIGHT) {
					if (!yHit) {
						yHit = true;
					}
					else {
						yHit = false;
					}
				}
				
				
				
				
				if (xHit) {
					
					currentXVel = dx/currentObjectFriction*-1;
					if (Math.abs(currentXVel) >= speedControlX) {
						if (currentXVel < 0) {
							currentXVel = speedControlX*-1;
						}
						else {
							currentXVel = speedControlX;
						}
					}
					
				}
				else {
					
					currentXVel = dx/currentObjectFriction;
					if (Math.abs(currentXVel) >= speedControlX) {
						if (currentXVel < 0) {
							currentXVel = speedControlX*-1;
						}
						else {
							currentXVel = speedControlX;
						}
					}
					
				}
				
				
				if (yHit) {
					
					currentYVel = dy/currentObjectFriction*-1;
					if (Math.abs(currentYVel) >= speedControlY) {
						if (currentYVel < 0) {
							currentXVel = speedControlY*-1;
						}
						else {
							currentXVel = speedControlY;
						}
					}
					
				}
				else {
					
					currentYVel = dy/currentObjectFriction;
					if (Math.abs(currentYVel) >= speedControlY) {
						if (currentYVel < 0) {
							currentXVel = speedControlY*-1;
						}
						else {
							currentXVel = speedControlY;
						}
					}
					
				}
				
				
				
				if (dy == 0) {
					currentYVel = 0;
				}
				if (dx == 0) {
					currentXVel = 0;
				}
				
				
				
				currentObjectFriction += (currentObjectFriction*0.035);
				
				currentObject.setBounds((int) Math.round(currentObject.getX()+currentXVel), (int) Math.round(currentObject.getY()+currentYVel), currentObjectWidth, currentObjectHeight);
				
			});
				
			timer.start();
			
		}
		
		
		
		public static void addPhysics(Component object, double mass) {
			
			if (currentData.size() != 0) {	
				currentData.remove(currentData.size()-1);
			}
			
			currentData.add(str(mass));
			currentData.add(str(object.getWidth()));
			currentData.add(str(object.getHeight()));
			
			objects.put(object.getName(), currentData);		
		}
		
		
		
		public static void addBarrierObject(Component barrier, boolean movable, int mass) {
			if (currentData.size() != 0) {
				currentData.remove(currentData.size()-1);
			}
			
			currentData.add(Integer.toString(barrier.getWidth()));
			currentData.add(Integer.toString(barrier.getHeight()));
			currentData.add(Integer.toString(mass));
			
			barriers.put(barrier.getName(), currentData);
		}
		
		
		public static void testForCollisions() {
			
			
			
		}
		
		
		
		public static boolean intersects(Component object1, Component object2) {
			
			int object1Width = object1.getWidth();
	        int object1Height = object1.getHeight();
	        int object2Width = object2.getWidth();
	        int object2Height = object2.getHeight();
	        
	        if (object2Width <= 0 || object2Height <= 0 || object1Width <= 0 || object1Height <= 0) {
	            return false;
	        }
	        
	        int object1X = object1.getX();
	        int object1Y = object1.getY();
	        int object2X = object2.getX();
	        int object2Y = object2.getY();
	        object2Width += object2X;
	        object2Height += object2Y;
	        object1Width += object1X;
	        object1Height += object1Y;

	        
	        return ((object2Width < object2X || object2Width > object1X) &&
	                (object2Height < object2Y || object2Height > object1Y) &&
	                (object1Width < object1X || object1Width > object2X) &&
	                (object1Height < object1Y || object1Height > object2Y));
	        
		}
		
		
		
	}
	
	
	
	//Testing Class
	public static class TestAssist {
		
		public static<T> boolean assertEquals(T expected, T actual) {
			if (expected != actual) { 
				System.out.println("Test failed!\nExpected: "+expected+"\nActual: "+actual);
				return false;
			}
			else { 
				System.out.println("Test Passed!");
				return true;
			}
		}
		
		
		public static HashMap<String, String> assertSetEquals(ArrayList<?> expectedSet, ArrayList<?> actualSet) {
			
			HashMap<String, String> failedTests = new HashMap<>();
			
			for (int i = 0; i < expectedSet.size(); i++) {
				if (expectedSet.toArray()[i] != actualSet.toArray()[i]) {
					failedTests.put(i+";~&"+expectedSet.toArray()[i].toString(), actualSet.toArray()[i].toString());
				}
			}
			System.out.println(failedTests);
			
			
			System.out.println("Failed Tests:\n");
			
			for (String currentTest : failedTests.keySet()) {
				String[] numberAndKey = currentTest.split(";~&");
				System.out.println(str(Int(numberAndKey[0])+1)+". ("+numberAndKey[1]+", "+failedTests.get(currentTest)+")");
			}
			
			
			return failedTests;
		}
		
	}
	
	
	
	
	//Requests Class
	public static class Requests {
		
		public static String get(String url) {
			try {
				HttpRequest getReq = HttpRequest.newBuilder()
						.uri(new URI(url))
						.build();
				HttpClient client = HttpClient.newHttpClient();
				HttpResponse<String> response = client.send(getReq, BodyHandlers.ofString());
				return response.body();
			} catch (URISyntaxException | IOException | InterruptedException exception) {
				exception.printStackTrace();
				return null;
			}
		}
		
		
		public static String post(String url, String json) throws IOException {
			URL uri = new URL(url);
			URLConnection con = uri.openConnection();
			HttpURLConnection http = (HttpURLConnection)con;
			http.setRequestMethod("POST");
			http.setDoOutput(true);
			
			byte[] byteString = json.getBytes(StandardCharsets.UTF_8);
			http.setFixedLengthStreamingMode(byteString.length);
			http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			http.connect();
			try(OutputStream os = http.getOutputStream()) {
			    os.write(byteString);
			}
			
			try(InputStream is = http.getInputStream()) {
				byte[] output = is.readAllBytes();
				return new String(output);
			}
		}
		
	}
	
	
	
	//Socket Server Nested Classes and Methods
	
	public static class socket {
		
		public static class server {
			
			private static ServerSocket socketConnection = null;
			private static Socket acceptClient = null;
			
			
			public static ServerSocket setup(int port) {
				try {
					socketConnection = new ServerSocket(port);
					acceptClient = socketConnection.accept();
				}
				catch (IOException e) {}
				
				return socketConnection;
			}
			
			
			
			public static String getInput() {
				InputStream inputClient;
				String text = null;
				try {
					inputClient = acceptClient.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(inputClient));
					text = br.readLine();
				} catch (IOException e) {}
				
				return text;
			}
			
			
			
			public static void sendOutput(String text) {
				OutputStream output = null;
				try {
					output = acceptClient.getOutputStream();
					PrintStream stream = new PrintStream(output);
					stream.println(text);
				} catch (IOException e) {}
			}
			
			
		}
		
		public static class client {
			
			public static Socket connectToServer;
			
			public static void setup(String ip, int port) {
				try {
					connectToServer = new Socket(ip, port);
				}
				catch (IOException e) {}
			}
			
			public static String getInput() {
				
				String text = null;
				
				try {
					InputStream streamInput = connectToServer.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(streamInput));
					text = reader.readLine();
				} catch (IOException e) {}
				
				return text;
			}
			
			public static void sendOutput(String text) {
				OutputStream streamOut = null;
				try {
					streamOut = connectToServer.getOutputStream();
					PrintStream printer = new PrintStream(streamOut);
					printer.println(text);
				}
				catch(IOException e) {}
			}
			
		}
 	}
	
	public static class EncodeDecoder {
		public static String encode(String text) {
			return Base64.getEncoder().encodeToString(text.getBytes());
		}
		public static String decode(String text) {
			return new String(Base64.getDecoder().decode(text));
		}
	}
}