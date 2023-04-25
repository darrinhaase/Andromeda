package com.andromeda.main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.andromeda.desc.DescriptionBar;
import com.andromeda.desc.DrawingText;
import com.andromeda.vgraph.GraphGUI;
import com.andromeda.vgraph.Instruction;
import com.andromeda.vgraph.PlotPoint;
import com.formdev.flatlaf.FlatIntelliJLaf;

public class Main {
	
	public static final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	private static GraphGUI graph;
	private static GraphGUI currentGraph;
	private static JTabbedPane tabbedPane;
	private static DescriptionBar descBar = new DescriptionBar(screen);
	public static JFrame frame = null;
	
	public static void main(String[] args) throws Exception {

			frame = trusty.frame("Jesuit Geometry - Andromeda", screen.width/2, screen.height, null, false);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setVisible(true);
			frame.addKeyListener(new KeyListener() {
				public void keyTyped(KeyEvent e) {}
				public void keyReleased(KeyEvent e) {}
				public void keyPressed(KeyEvent e) {
					if (e.isMetaDown()) {
						if (e.getKeyCode() == 78) {
							try {
								tabbedPane.addTab("Untitled", newTab());
								tabbedPane.repaint();
							} catch (Exception e1) {
								e1.printStackTrace();
							}	
						}
						else if (e.getKeyCode() == 79) openFile(frame);
						else if (e.getKeyCode() == 83) {
							if (currentGraph.saveFile == null) {						
								JFileChooser fileChooser = new JFileChooser();
								FileNameExtensionFilter filter = new FileNameExtensionFilter("Andromeda Files (.ad)", "ad");
								fileChooser.setFileFilter(filter);
								if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
									saveToFile(fileChooser.getSelectedFile(), frame);
								}
							} else {
								saveToFile(currentGraph.saveFile, frame);
							}
						}
					} else if (e.isControlDown()) {
						if (e.getKeyCode() == 78) {
							try {
								tabbedPane.addTab("Untitled", newTab());
								tabbedPane.setSelectedIndex(tabbedPane.getComponentCount()-1);
								tabbedPane.repaint();
							} catch (Exception e1) {
								e1.printStackTrace();
							}	
						}
						else if (e.getKeyCode() == 79) openFile(frame);
						else if (e.getKeyCode() == 83) {
							if (currentGraph.saveFile == null) {						
								JFileChooser fileChooser = new JFileChooser();
								FileNameExtensionFilter filter = new FileNameExtensionFilter("Andromeda Files (.ad)", "ad");
								fileChooser.setFileFilter(filter);
								if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
									saveToFile(fileChooser.getSelectedFile(), frame);
								}
							} else {
								saveToFile(currentGraph.saveFile, frame);
							}
						}
					}
				}
			});
			frame.addWindowListener(new WindowListener() {
				public void windowOpened(WindowEvent e) {}
				public void windowIconified(WindowEvent e) {}
				public void windowDeiconified(WindowEvent e) {}
				public void windowDeactivated(WindowEvent e) {	}
				public void windowClosing(WindowEvent e) {
					boolean isNotSaved = false;
					for (Component i : tabbedPane.getComponents()) {
						if (!((GraphGUI) ((JScrollPane) i).getViewport().getView()).saved) {
							isNotSaved = true;
						}
					}
					if (isNotSaved) {
						if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit without saving?", "Confirm Exit", JOptionPane.YES_NO_OPTION) == 0) {
							System.exit(0);
						}
					} else {
						System.exit(0);
					}
				}
				public void windowClosed(WindowEvent e) {
					
				}
				public void windowActivated(WindowEvent e) {}
			});
			
			FlatIntelliJLaf.setup();
			
			HashMap<String, String[]> menuItemNames = new HashMap<>();
			
			if (System.getProperty("os.name").toLowerCase().contains("mac")) {
				menuItemNames.put("file", new String[] {"New (Cmd + N)","Open (Cmd + O)","Save (Cmd + S)","Quit (Cmd + Q)"});
			} else {
				menuItemNames.put("file", new String[] {"New (Ctrl + N)","Open (Ctrl + O)","Save (Ctrl + S)","Quit (Alt + F4)"});
			}
			
			JMenuBar menuBar = new JMenuBar();
				JMenu fileBar = new JMenu("File");
					String[] fileItemNames = menuItemNames.get("file");
					JMenuItem newButton = new JMenuItem(fileItemNames[0]);
						newButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								try {
									tabbedPane.addTab("Untitled", newTab());
									tabbedPane.setSelectedIndex(tabbedPane.getComponentCount()-1);
									tabbedPane.repaint();
								} catch (Exception e1) {
									e1.printStackTrace();
								}	
							}
						});
					JMenuItem openButton = new JMenuItem(fileItemNames[1]);
						openButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								openFile(frame);
							}
						});
					JMenuItem saveButton = new JMenuItem(fileItemNames[2]);
						saveButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if (currentGraph.saveFile == null) {						
									JFileChooser fileChooser = new JFileChooser();
									FileNameExtensionFilter filter = new FileNameExtensionFilter("Andromeda Files (.ad)", "ad");
									fileChooser.setFileFilter(filter);
									if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
										saveToFile(fileChooser.getSelectedFile(), frame);
									}
								} else {
									saveToFile(currentGraph.saveFile, frame);
								}
							}
						});
					JMenuItem saveAsButton = new JMenuItem("Save as..");
						saveAsButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								JFileChooser fileChooser = new JFileChooser();
								FileNameExtensionFilter filter = new FileNameExtensionFilter("Andromeda Files (.ad)", "ad");
								fileChooser.setFileFilter(filter);
								if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
									saveToFile(fileChooser.getSelectedFile(), frame);
								}
							}
						});
					JMenuItem exitButton = new JMenuItem(fileItemNames[3]);
						fileBar.add(newButton);
						fileBar.add(openButton);
						fileBar.addSeparator();
						fileBar.add(saveButton);
						fileBar.add(saveAsButton);
						fileBar.addSeparator();
						fileBar.add(exitButton);
						
						
						exitButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if (!currentGraph.saved) {
									if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit without saving?", "Confirm Exit", JOptionPane.YES_NO_OPTION) == 0) {
										System.out.println("Exiting program...");
										System.exit(0);
									}
								} else {
									System.out.println("Exiting program...");
									System.exit(0);
								}
							}
						});
						
					menuBar.add(fileBar);
				JMenu editBar = new JMenu("Edit");
					editBar.add(new JMenuItem("Erase"));
					menuBar.add(editBar);
			frame.setJMenuBar(menuBar);
			
			tabbedPane = new JTabbedPane();
			
			currentGraph = graph = new GraphGUI(screen.width/5, -screen.height/25, 15*screen.width, 10*screen.height);

	        JScrollPane scrollPane = new JScrollPane(graph);
	        	scrollPane.getVerticalScrollBar().setUnitIncrement(15);
	        	scrollPane.getHorizontalScrollBar().setUnitIncrement(15);
		    	scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		    	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			    scrollPane.setBorder(null);
			    
			tabbedPane.addTab("Untitled", scrollPane);
			
			tabbedPane.addChangeListener((e) -> {
				JScrollPane s = (JScrollPane) tabbedPane.getSelectedComponent();
				JViewport viewport = s.getViewport(); 
				currentGraph = (GraphGUI) viewport.getView();
			});
			
			tabbedPane.setBounds(screen.width/49, screen.height/45, Math.round(frame.getWidth()/1.05f), frame.getHeight()-frame.getHeight()/8);
			frame.getContentPane().add(tabbedPane);
			
			graph.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {}
				@Override
				public void mousePressed(MouseEvent e) {
					currentGraph.saved = false;
					if (!tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()).endsWith(" *")) {
						tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), tabbedPane.getTitleAt(tabbedPane.getSelectedIndex())+" *");
					}
					graph.plot(e.getX(), e.getY());
					graph.repaint();
				}
				@Override
				public void mouseExited(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseClicked(MouseEvent e) {
					
				}
			});	
			graph.addMouseMotionListener(new MouseMotionListener() {
				@Override
				public void mouseMoved(MouseEvent e) {
					for(Instruction j : graph.findObject(e.getX(), e.getY())) {
						
						String definitionText = "";
						
						descBar.renderText(new DrawingText(descBar.getWidth()/24, descBar.getHeight()/20, (int) (screen.getWidth()/50), j.getType(), TextAttribute.WEIGHT_BOLD), true);
						
						switch(j.getType()) {
						
							case "Point":
								definitionText = "A structure on a plane with no location, dimension, or position";
								break;
						
						}
						
						descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), descBar.getHeight()/9, (int) (screen.getWidth()/100), "Definition: "+definitionText, TextAttribute.WEIGHT_SEMIBOLD, true), false);
					
						descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), descBar.getHeight()/5, (int) (screen.getWidth()/100), "X: ", TextAttribute.WEIGHT_SEMIBOLD), false);
						descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), descBar.getHeight()/4, (int) (screen.getWidth()/100), "Y: ", TextAttribute.WEIGHT_SEMIBOLD), false);
						
						JTextField xField = new JTextField();
						xField.setText(trusty.str(j.getX()));
						xField.setBounds((int) descBar.getWidth()/7, (int) Math.round(descBar.getHeight()/5.31), (int) screen.getWidth()/13, (int) screen.getHeight()/36);
						xField.addKeyListener(new KeyListener() {
							public void keyTyped(KeyEvent e) {}
							public void keyReleased(KeyEvent e) {}
							public void keyPressed(KeyEvent e) {
								int oldX = j.getX();
								
								try {
									if (e.getKeyCode() == 8) {
										j.setX(Integer.valueOf(removeLastChar(xField.getText())));
									} else {
										j.setX(Integer.valueOf(xField.getText()+e.getKeyChar()));
									}
								} catch (NumberFormatException e2) {
									if (removeLastChar(xField.getText()).equals("")) {
										j.setX(0);
									}
								} catch (NullPointerException e2) {
									;
								}
								
								if (j.getType().equals("Point")) {
									ArrayList<Instruction> replacementInstructions = new ArrayList<>();
									for (Instruction i : graph.getInstructions()) {
										
										if (i.getType().equals("Line")) {
											if (i.getP1().getX()-(screen.width/180/2) == oldX || i.getP2().getX()-(screen.width/180/2) == oldX && i.getP1().getY()-(screen.width/180/2) == j.getY() || i.getP2().getY()-(screen.width/180/2) == j.getY()) {
												if (i.getP1().getX()-(screen.width/180/2) == oldX) {
													replacementInstructions.add(new Instruction(new PlotPoint(j.getX()+(screen.width/180/2), j.getY()+(screen.width/180/2)), i.getP2(), "Line", i.getName()));
												} else {
													replacementInstructions.add(new Instruction(i.getP1(), new PlotPoint(j.getX()+(screen.width/180/2), j.getY()+(screen.width/180/2)), "Line", i.getName()));
												}
											} else {
												replacementInstructions.add(i);
											}
										} else {
											replacementInstructions.add(i);
										}
									}
									graph.setInstructions(replacementInstructions);
								}
								
								frame.repaint();
								graph.repaint();
								graph.paintComponent(graph.getGraphics());
							}
						});
						descBar.add(xField);
						
						JTextField yField = new JTextField();
						yField.setText(trusty.str(j.getY()));
						yField.setBounds((int) descBar.getWidth()/7, (int) Math.round(descBar.getHeight()/4.2), (int) screen.getWidth()/13, (int) screen.getHeight()/36);
						yField.addKeyListener(new KeyListener() {
							public void keyTyped(KeyEvent e) {}
							public void keyReleased(KeyEvent e) {}
							public void keyPressed(KeyEvent e) {
								int oldY = j.getY();
								try {
									if (e.getKeyCode() == 8) {
										j.setY(Integer.valueOf(removeLastChar(yField.getText())));
									} else {
										j.setY(Integer.valueOf(yField.getText()+e.getKeyChar()));
									}
								} catch (NumberFormatException e2) {
									if (removeLastChar(yField.getText()).equals("")) {
										j.setY(0);
									}
								} catch (NullPointerException e2) {
									;
								}
								
								if (j.getType().equals("Point")) {
									ArrayList<Instruction> replacementInstructions = new ArrayList<>();
									for (Instruction i : graph.getInstructions()) {
										
										if (i.getType().equals("Line")) {
											if (i.getP1().getX()-(screen.width/180/2) == j.getX() || i.getP2().getX()-(screen.width/180/2) == j.getX() && i.getP1().getY()-(screen.width/180/2) == oldY || i.getP2().getY()-(screen.width/180/2) == oldY) {
												if (i.getP1().getX()-(screen.width/180/2) == j.getX()) {
													replacementInstructions.add(new Instruction(new PlotPoint(j.getX()+(screen.width/180/2), j.getY()+(screen.width/180/2)), i.getP2(), "Line", i.getName()));
												} else {
													replacementInstructions.add(new Instruction(i.getP1(), new PlotPoint(j.getX()+(screen.width/180/2), j.getY()+(screen.width/180/2)), "Line", i.getName()));
												}
											} else {
												replacementInstructions.add(i);
											}
										} else {
											replacementInstructions.add(i);
										}
									}
									graph.setInstructions(replacementInstructions);
								}
								
								frame.repaint();
								graph.repaint();
								graph.paintComponent(graph.getGraphics());
							}
						});
						descBar.add(yField);
						
						toggleDescriptionBar(true);
					}
				}
				
				@Override
				public void mouseDragged(MouseEvent e) {}
			});
		
		
			FlatIntelliJLaf.updateUILater();
			
			frame.repaint();		
	}
	
	@SuppressWarnings("unchecked")
	private static void openFile(JFrame frame) {
		System.out.println("Opening file...");
		try {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Andromeda Files (.ad)", "ad");
			fileChooser.setFileFilter(filter);
			if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			  File selectedFile = fileChooser.getSelectedFile();
			  currentGraph.saveFile = selectedFile;
			  String fileName = selectedFile.getName();
			  String titleReplacement = "";
			  int idx = 0;
			  for (char i : fileName.toCharArray()) {
				  if (idx < fileName.length()-3) {
					  titleReplacement+=i;
				  }
				  idx++;
			  }
			  tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), titleReplacement);
			  
			  ObjectInputStream objin = new ObjectInputStream(new FileInputStream(selectedFile));
			  currentGraph.setInstructions((ArrayList<Instruction>) objin.readObject());
			  objin.close();
			  currentGraph.repaint();
			  frame.repaint();
			}
		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	private static void saveToFile(File file, JFrame frame) {
		  String titleReplacement = "";
		  if (file.getName().endsWith(".ad")) {
			  for (int i = 0; i < file.getName().length()-3; i++) {
				  titleReplacement+=file.getName().toCharArray()[i];
			  }
		  } else {
			  titleReplacement = file.getName();
		  }
		  
		  tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), titleReplacement);
		  
		  ObjectOutputStream objout = null;
			 
			try {
				if (file.toString().contains(".ad")) objout = new ObjectOutputStream(new FileOutputStream(file));
				else objout = new ObjectOutputStream(new FileOutputStream(file+".ad"));
				
				objout.writeObject(currentGraph.getInstructions());
				
				objout.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			} 
			
		currentGraph.saveFile = file;
		currentGraph.saved = true;
	}
	
	private static JScrollPane newTab() throws Exception {
		GraphGUI newGraph = new GraphGUI(screen.width/5, -screen.height/25, 15*screen.width, 10*screen.height);
		
		newGraph.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				currentGraph.saved = false;
				if (!tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()).endsWith(" *")) {
					tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), tabbedPane.getTitleAt(tabbedPane.getSelectedIndex())+" *");
				}
				newGraph.plot(e.getX(), e.getY());
				newGraph.repaint();
			}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});	
		newGraph.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				for(Instruction j : newGraph.findObject(e.getX(), e.getY())) {
					
					String definitionText = "";
					
					descBar.renderText(new DrawingText(descBar.getWidth()/24, descBar.getHeight()/20, (int) (screen.getWidth()/50), j.getType(), TextAttribute.WEIGHT_BOLD), true);
					
					switch(j.getType()) {
					
						case "Point":
							definitionText = "A structure on a plane with no location, dimension, or position";
							break;
					
					}
					
					descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), descBar.getHeight()/9, (int) (screen.getWidth()/100), "Definition: "+definitionText, TextAttribute.WEIGHT_SEMIBOLD, true), false);
				
					descBar.renderText(new DrawingText(descBar.getWidth()/24, descBar.getHeight()/2, (int) (screen.getWidth()/100), "testing", TextAttribute.WEIGHT_SEMIBOLD), false);
					
					
					toggleDescriptionBar(true);
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {}
		});
		
		JScrollPane sP = new JScrollPane(newGraph);
			sP.getVerticalScrollBar().setUnitIncrement(15);
			sP.getHorizontalScrollBar().setUnitIncrement(15);
			sP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			sP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			sP.setBorder(null);
		
		return sP;
	}
	
	public static void toggleDescriptionBar(boolean active) {
		if (active) {
			tabbedPane.setBounds(screen.width/5+screen.width/170, screen.height/35, Math.round(frame.getWidth()/1.25f)-frame.getWidth()/50, frame.getHeight()-frame.getHeight()/8);
			tabbedPane.repaint();
			
			frame.getContentPane().add(descBar);
			descBar.paintComponents(descBar.getGraphics());
		} else {
			tabbedPane.setBounds(screen.width/49, screen.height/45, Math.round(frame.getWidth()/1.05f), frame.getHeight()-frame.getHeight()/8);
			tabbedPane.repaint();
			
			frame.getContentPane().remove(descBar);
			frame.repaint();
		}
	}
	
	public static String removeLastChar(String s) {
	    return (s == null || s.length() == 0)
	      ? null 
	      : (s.substring(0, s.length() - 1));
	}
}