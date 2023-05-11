package com.andromeda.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
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
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.*;
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
	public static GraphGUI currentGraph;
	private static JTabbedPane tabbedPane;
	private static DescriptionBar descBar = new DescriptionBar(screen);
	public static JFrame frame = null;
	private static final HashMap<String, Double> conversions = new HashMap<>();

	public static String selectedTool = "point";
	public static String[] availableTools = {"point", "segment", "quadrilateral", "triangle", "circle", "textbox"};
	
    public static String selectedUnit = "px";
    public static String[] availableUnits = {"px", "cm", "in"};

	public static void rebuildMenu(JMenu thisMenu, String[] itemsList, String selected, String typeView) {

		/*	responsible for building, rebuilding, and event handling for "Tools" menu bar
		* 	directly controls selectedTool and availableTools above */

		// clean out existing JMenuItems
		thisMenu.removeAll();

		// dynamically define tools based on above array (availableTools)
		for (int i = 0; i < itemsList.length; i++) {
			//original string name in array for tool, used in plot();
			String _tempToolName = itemsList[i];
			String _tempToolDisplayName = "";

			// build the display text shown in menu
			if (Arrays.asList(availableTools).contains(_tempToolName)) {
				_tempToolDisplayName = _tempToolName.substring(0, 1).toUpperCase() + _tempToolName.substring(1) + " Tool";
				if (_tempToolName == selected) {
					_tempToolDisplayName += " (selected)";
				}
			} else {
				_tempToolDisplayName = _tempToolName.substring(0, 1).toUpperCase() + _tempToolName.substring(1) + " Unit";
				if (_tempToolName == selected) {
					_tempToolDisplayName += " (selected)";
				}
			}

			// add item to JMenu
			JMenuItem _tempToolMenuBarItem = thisMenu.add(new JMenuItem(_tempToolDisplayName));
			_tempToolMenuBarItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					if (currentGraph != null) {
                         if (typeView.equals("tools") && currentGraph.currentlyDrawing) {
                                JOptionPane.showMessageDialog(frame, "You're currently drawing a shape. Please finish drawing to switch tools.");
                                return;
                        }
                     }
					if (Arrays.asList(availableTools).contains(_tempToolName)) {
						// when clicked, set the global selectedTool to the selected menu item
						selectedTool = _tempToolName;
						// run this function again on click to update (selected) indicator
						rebuildMenu(thisMenu, itemsList, selectedTool, typeView);
					} else {
						selectedUnit = _tempToolName;
						rebuildMenu(thisMenu, itemsList, selectedUnit, typeView);
					}
					// reset plotnum count in current GraphUI
					currentGraph.plotnum = 1;
				}
			});
		}
	}
	
	public static void main(String[] args) throws Exception {
		
			if (System.getProperty("os.name").toLowerCase().equals("mac")) {
				Desktop.getDesktop().browse(new URI("https://andromeda.jesuitnotes.com"));
			}
		
			conversions.put("px", 1d);
			conversions.put("cm", 0.0264583333);
			conversions.put("in", 0.010416666666666666);

			frame = trusty.frame("Jesuit Geometry - Andromeda", screen.width/2, screen.height, "assets/logo.png", false);
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
					
				JMenu toolsBar = new JMenu("Tools");
                    rebuildMenu(toolsBar, availableTools, selectedTool, "tools");
                    menuBar.add(toolsBar);
                  
             
                JMenu viewBar = new JMenu("View");
                    rebuildMenu(viewBar, availableUnits, selectedUnit, "units");
                    menuBar.add(viewBar);
                        
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
				toggleDescriptionBar(false);
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
					descriptionBarRender(graph, e.getX(), e.getY());
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
		toggleDescriptionBar(false);
		
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
				descriptionBarRender(newGraph, e.getX(), e.getY());
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
			
			frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			frame.getContentPane().remove(descBar);
			frame.repaint();
		}
	}
	
	public static String removeLastChar(String s) {
	    return (s == null || s.length() == 0)
	      ? null 
	      : (s.substring(0, s.length() - 1));
	}
	
	private static void descriptionBarRender(GraphGUI g, int x, int y) {
		for(Instruction j : g.findObject(x, y)) {
			
			String definitionText = "";
			
			descBar.renderText(new DrawingText(descBar.getWidth()/24, descBar.getHeight()/20, (int) (screen.getWidth()/50), j.getType(), TextAttribute.WEIGHT_BOLD), true);
			
			switch(j.getType()) {
			
				case "Point":
					definitionText = "A named coordinate on a plane";
					break;
				case "Segment":
					definitionText = "One dimensional structure between two points";
					break;
                case "Circle":
                	definitionText = "A collection of all the points equidistant from a point on a plane";
                	break;
                case "Text":
                	definitionText = "A way to label your sketches";
                	break;
                case "Triangle":
                	definitionText = "A plane figure with three straight sides and three angles";
                	break;
                case "Quadrilateral":
                	definitionText = "A four-sided figure on a plane";
                	break;
			}
			
			descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), descBar.getHeight()/9, (int) (screen.getWidth()/100), "Definition: "+definitionText, TextAttribute.WEIGHT_SEMIBOLD, true), false);
		
			switch(j.getType()) {
			
			case "Point":
				addXandY(j, g);
				break;
			case "Segment":
				float[] lineData = j.getLineData();
				double scale = Math.pow(10, 3);
				
				descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), descBar.getHeight()/5, (int) (screen.getWidth()/100), "Slope-Intercept Form: ", TextAttribute.WEIGHT_BOLD), false);
				
				if (lineData == null) {
					descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), (int) Math.round(descBar.getHeight()/4.3), (int) (screen.getWidth()/100), "Slope cannot be calculated because the X values are identical.", TextAttribute.WEIGHT_SEMIBOLD), false);
				} else {
					descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), (int) Math.round(descBar.getHeight()/4.3), (int) (screen.getWidth()/100), "y = "+trusty.str(Math.round(lineData[0]*scale) / scale)+"x + "+trusty.str(Math.round(lineData[1]*scale) / scale), TextAttribute.WEIGHT_SEMIBOLD), false);
					descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), (int) Math.round(descBar.getHeight()/3.3), (int) (screen.getWidth()/100), "Exact Slope: ", TextAttribute.WEIGHT_BOLD), false);
					descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), (int) Math.round(descBar.getHeight()/2.95), (int) (screen.getWidth()/100), trusty.str(Math.round(lineData[2]))+" / "+trusty.str(Math.round(lineData[3])), TextAttribute.WEIGHT_SEMIBOLD), false);		
				}
				
				descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), (int) Math.round(descBar.getHeight()/2.45), (int) (screen.getWidth()/100), "Options:", TextAttribute.WEIGHT_BOLD), false);		
				
				JButton midpointBtn = new JButton();
				midpointBtn.setText("Toggle Midpoint");
				midpointBtn.addMouseListener(new MouseListener() {
					public void mouseReleased(MouseEvent e) {}
					public void mousePressed(MouseEvent e) {
						ArrayList<Instruction> replacementList = currentGraph.getInstructions();
						if (j.midpt.equals("")) {
								Instruction m = new Instruction((j.getP2().getX()+j.getP1().getX())/2-(Main.screen.width/180/2), (j.getP1().getY()+j.getP2().getY())/2-(Main.screen.width/180/2), "Point", "midpoint"+j.getName());
									m.setDiameter(screen.width/150);
									m.setFilled(true);
									m.setColor(new Color(255,0,0));
								replacementList.add(m);
							currentGraph.setInstructions(replacementList);
							j.midpt = "midpoint"+j.getName();
						} else {
							for (int o = 0; o < replacementList.size(); o++) {
								if (replacementList.get(o).getName().equals("midpoint"+j.getName())) {
									replacementList.remove(o);
								}
							}
							currentGraph.setInstructions(replacementList);
							j.midpt = "";
						}
						currentGraph.repaint();
						currentGraph.paintComponent(currentGraph.getGraphics());
					}
					public void mouseExited(MouseEvent e) {}
					public void mouseEntered(MouseEvent e) {}
					public void mouseClicked(MouseEvent e) {}
				});
				midpointBtn.setBounds((int) Math.round(descBar.getWidth()/21.5), (int) Math.round(descBar.getHeight()/2.35), screen.width/11, screen.height/30);
				descBar.add(midpointBtn);
				
				descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), (int) Math.round(descBar.getHeight()/1.9), (int) (screen.getWidth()/100), "Segment Length: ", TextAttribute.WEIGHT_BOLD), false);
				descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), (int) Math.round(descBar.getHeight()/1.78), (int) (screen.getWidth()/100), Math.round(GraphGUI.calculateDistance(j.getP1().getX(), j.getP1().getY(), j.getP2().getX(), j.getP2().getY())*scale)/scale*conversions.get(selectedUnit) + " " + selectedUnit, TextAttribute.WEIGHT_SEMIBOLD), false);
				
				break;
				
				
            case "Text":
                descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), (int) Math.round(descBar.getHeight()/4.8), (int) (screen.getWidth()/100), "X: ", TextAttribute.WEIGHT_SEMIBOLD), false);
                descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), (int) Math.round(descBar.getHeight()/3.8), (int) (screen.getWidth()/100), "Y: ", TextAttribute.WEIGHT_SEMIBOLD), false);
                
                Instruction textPoint = null;
                
                for (Instruction l : currentGraph.getInstructions()) {
                	if (l.getSuperObject().equals(j.getName())) {
                		textPoint = l;
                	}
                }
                
                JTextField xField = new JTextField();
                xField.setText(trusty.str(textPoint.getX()));
                
                xField.setBounds((int) descBar.getWidth()/7, (int) Math.round(descBar.getHeight()/5.31), (int) screen.getWidth()/13, (int) screen.getHeight()/36);
                xField.addKeyListener(new KeyListener() {
                    public void keyTyped(KeyEvent e) {}
                    public void keyReleased(KeyEvent e) {
                    	
                    	for (Instruction l : currentGraph.getInstructions()) {
                        	if (l.getSuperObject().equals(j.getName())) {
                        		try {
                                    if (!xField.getText().equals("")) {
                                    	l.setX(trusty.Int(xField.getText()));
                                    	j.setX(trusty.Int(xField.getText()));
                                    }
                                    else {
                                    	l.setX(0);
                                    	j.setX(0);
                                    }
                                } catch(NumberFormatException e1) {
                                    ;
                                }
                                
                                frame.repaint();
                                g.repaint();
                                g.paintComponent(g.getGraphics());
                        	}
                        }
                        
                        
                    }
                    public void keyPressed(KeyEvent e) {}
                });
                descBar.add(xField);
                
                JTextField yField = new JTextField();
                yField.setText(trusty.str(j.getY()));
                yField.setBounds((int) descBar.getWidth()/7, (int) Math.round(descBar.getHeight()/4.2), (int) screen.getWidth()/13, (int) screen.getHeight()/36);
                yField.addKeyListener(new KeyListener() {
                    public void keyTyped(KeyEvent e) {}
                    public void keyReleased(KeyEvent e) {
                        for (Instruction l : currentGraph.getInstructions()) {
                        	 if (l.getSuperObject().equals(j.getName())) {
                        		 try {
                                     if (!yField.getText().equals("")) {
                                    	 l.setY(trusty.Int(yField.getText()));
                                    	 j.setY(trusty.Int(yField.getText()));
                                     }
                                     else {
                                    	 l.setY(0);
                                    	 j.setY(0);
                                     }
                                 } catch(NumberFormatException e1) {
                                     ;
                                 }
                           
                                 frame.repaint();
                                 g.repaint();
                                 g.paintComponent(g.getGraphics());
                        	 }
                        }
                    }
                    public void keyPressed(KeyEvent e) {}
                });
                descBar.add(yField);
                addTextEntry(j, g);
                
                break;
		
            case "Circle":
            	addXandY(j, g);
            	descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), (int) Math.round(descBar.getHeight()/2.97), (int) (screen.getWidth()/100), "Radius: ", TextAttribute.WEIGHT_SEMIBOLD), false);        
                
                JTextField radiusBox = new JTextField();
                radiusBox.setText(trusty.str(j.getDiameter()/2));
                radiusBox.setBounds((int) Math.round(descBar.getWidth()/3.85), (int) Math.round(descBar.getHeight()/3.17), (int) screen.getWidth()/13, (int) screen.getHeight()/36);
                
                radiusBox.addKeyListener(new KeyListener() {
                    public void keyTyped(KeyEvent e) {}
                    public void keyReleased(KeyEvent e) {
                    	
                    	Instruction outerPoint = g.getInstructions().stream().filter(l -> l.getSuperObject().equals(j.getName())).findFirst().get();
                    	Instruction middlePoint = g.getInstructions().stream().filter(l -> l.getSuperObject().equals("mid"+j.getName())).findFirst().get();	
                    	
                        try {					
        						if (!radiusBox.getText().equals("")) {
        							j.setX((middlePoint.getX()+(Main.screen.width/180/2))-trusty.Int(radiusBox.getText()));
        							j.setY(middlePoint.getY()+(Main.screen.width/180/2)-(trusty.Int(radiusBox.getText())));
        							j.setDiameter(trusty.Int(radiusBox.getText())*2);
        							outerPoint.setX(middlePoint.getX());
        							outerPoint.setY(middlePoint.getY()+j.getDiameter()/2);
        						}
        						else {
        							j.setDiameter(0);
        							outerPoint.setY(g.getInstructions().stream().filter(l -> l.getSuperObject().equals("mid"+j.getName())).findFirst().get().getY()+j.getDiameter()/2);
        							outerPoint.setX(g.getInstructions().stream().filter(l -> l.getSuperObject().equals("mid"+j.getName())).findFirst().get().getX());
        						}
        				} catch(NumberFormatException e1) {
        					;
        				}
                        
                        frame.repaint();
                        g.repaint();
                        g.paintComponent(g.getGraphics());
                    }
                    public void keyPressed(KeyEvent e) {}
                    });
                
                descBar.add(radiusBox);
                
                descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), (int) Math.round(descBar.getHeight()/2.45), (int) (screen.getWidth()/100), "Area: ", TextAttribute.WEIGHT_BOLD), false);        
                descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), (int) Math.round(descBar.getHeight()/2.28), (int) (screen.getWidth()/100), trusty.str(Instruction.calculateCircleArea(j.getDiameter()/2)*conversions.get(selectedUnit))+ " " +selectedUnit, TextAttribute.WEIGHT_SEMIBOLD), false);        
                
            	break;
			}
			
			JButton deleteButton = new JButton();
				deleteButton.setText("Delete Element");
				deleteButton.setForeground(new Color(248,81,73));
				deleteButton.addMouseListener(new MouseListener() {
					public void mouseReleased(MouseEvent e) {}
					public void mousePressed(MouseEvent e) {
						descBar.removeElement(j, currentGraph);
					}
					public void mouseExited(MouseEvent e) {
						frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						deleteButton.setForeground(new Color(248,81,73));
						deleteButton.setBackground(new Color(255,255,255));
					}
					public void mouseEntered(MouseEvent e) {
						frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
						deleteButton.setBackground(new Color(248,81,73));
						deleteButton.setForeground(new Color(255,255,255));
					}
					public void mouseClicked(MouseEvent e) {}
				});
				
				if (System.getProperty("os.name").toLowerCase().contains("windows")) deleteButton.setBounds((int) Math.round(descBar.getWidth()/13), (int) Math.round(descBar.getHeight()/1.18), screen.width/6, screen.height/30);
				else deleteButton.setBounds((int) Math.round(descBar.getWidth()/13), (int) Math.round(descBar.getHeight()/1.22), screen.width/6, screen.height/30);
				
				if (j.getSuperObject().equals("")) descBar.add(deleteButton);
			toggleDescriptionBar(true);
			break;
		}
	}
	
	private static void addTextEntry(Instruction j, GraphGUI g) {
		        descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), (int) Math.round(descBar.getHeight()/2.97), (int) (screen.getWidth()/100), "Text: ", TextAttribute.WEIGHT_SEMIBOLD), false);        
        
        JTextField textBoxData = new JTextField();
        textBoxData.setText(j.getText());
        textBoxData.setBounds((int) descBar.getWidth()/5, (int) Math.round(descBar.getHeight()/3.17), (int) screen.getWidth()/13, (int) screen.getHeight()/36);
        
        textBoxData.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyReleased(KeyEvent e) {
                j.setText(textBoxData.getText());
                frame.repaint();
                g.repaint();
                g.paintComponent(g.getGraphics());
            }
            public void keyPressed(KeyEvent e) {}
            });
        
        descBar.add(textBoxData);
    }
	
	private static void addXandY(Instruction j, GraphGUI g) {
		descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), (int) Math.round(descBar.getHeight()/4.8), (int) (screen.getWidth()/100), "X: ", TextAttribute.WEIGHT_SEMIBOLD), false);
		descBar.renderText(new DrawingText((int) Math.round(descBar.getWidth()/21.5), (int) Math.round(descBar.getHeight()/3.8), (int) (screen.getWidth()/100), "Y: ", TextAttribute.WEIGHT_SEMIBOLD), false);
		
		JTextField xField = new JTextField();
		xField.setText(trusty.str(j.getX()));
		
		if (j.getType().equals("Circle")) xField.setText(trusty.str(g.getInstructions().stream().filter(l -> l.getSuperObject().equals("mid"+j.getName())).findFirst().get().getX()));
		
		xField.setBounds((int) descBar.getWidth()/7, (int) Math.round(descBar.getHeight()/5.31), (int) screen.getWidth()/13, (int) screen.getHeight()/36);
		xField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {
				int oldX = j.getX();
				
				try {					
					if (j.getType().equals("Circle")) {
						if (!xField.getText().equals("")) {
							g.getInstructions().stream().filter(l -> l.getSuperObject().equals("mid"+j.getName())).findFirst().get().setX(trusty.Int(xField.getText()));
							j.setX(trusty.Int(xField.getText())-(j.getDiameter()/2)+(Main.screen.width/180/2));
							g.getInstructions().stream().filter(l -> l.getSuperObject().equals(j.getName())).findFirst().get().setY(g.getInstructions().stream().filter(l -> l.getSuperObject().equals("mid"+j.getName())).findFirst().get().getY()+j.getDiameter()/2);
							g.getInstructions().stream().filter(l -> l.getSuperObject().equals(j.getName())).findFirst().get().setX(g.getInstructions().stream().filter(l -> l.getSuperObject().equals("mid"+j.getName())).findFirst().get().getX());
						}
						else {
							g.getInstructions().stream().filter(l -> l.getSuperObject().equals("mid"+j.getName())).findFirst().get().setX(0);
							j.setX(trusty.Int(xField.getText())-(j.getDiameter()/2)+(Main.screen.width/180/2));
							g.getInstructions().stream().filter(l -> l.getSuperObject().equals(j.getName())).findFirst().get().setY(g.getInstructions().stream().filter(l -> l.getSuperObject().equals("mid"+j.getName())).findFirst().get().getY()+j.getDiameter()/2);
							g.getInstructions().stream().filter(l -> l.getSuperObject().equals(j.getName())).findFirst().get().setX(g.getInstructions().stream().filter(l -> l.getSuperObject().equals("mid"+j.getName())).findFirst().get().getX());
						}
					} else {
						if (!xField.getText().equals("")) j.setX(trusty.Int(xField.getText()));
						else j.setX(0);
					}
				} catch(NumberFormatException e1) {
					;
				}
				
				if (j.getType().equals("Point")) {
					ArrayList<Instruction> replacementInstructions = new ArrayList<>();
					for (Instruction i : g.getInstructions()) {
						
						if (i.getType().equals("Segment")) {
							if (i.getP1().getX()-(screen.width/180/2) == oldX || i.getP2().getX()-(screen.width/180/2) == oldX && i.getP1().getY()-(screen.width/180/2) == j.getY() || i.getP2().getY()-(screen.width/180/2) == j.getY()) {
								if (i.getP1().getX()-(screen.width/180/2) == oldX) {
									replacementInstructions.add(new Instruction(new PlotPoint(j.getX()+(screen.width/180/2), j.getY()+(screen.width/180/2)), i.getP2(), "Segment", i.getName()));
								} else {
									replacementInstructions.add(new Instruction(i.getP1(), new PlotPoint(j.getX()+(screen.width/180/2), j.getY()+(screen.width/180/2)), "Segment", i.getName()));
								}
							} else {
								replacementInstructions.add(i);
							}
						} else {
							replacementInstructions.add(i);
						}
					}
					g.setInstructions(replacementInstructions);
				}
				
				frame.repaint();
				g.repaint();
				g.paintComponent(g.getGraphics());
			}
			public void keyPressed(KeyEvent e) {}
		});
		descBar.add(xField);
		
		JTextField yField = new JTextField();
		yField.setText(trusty.str(j.getY()));
		
		if (j.getType().equals("Circle")) yField.setText(trusty.str(g.getInstructions().stream().filter(l -> l.getSuperObject().equals("mid"+j.getName())).findFirst().get().getY()));
		
		yField.setBounds((int) descBar.getWidth()/7, (int) Math.round(descBar.getHeight()/4.2), (int) screen.getWidth()/13, (int) screen.getHeight()/36);
		yField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {
				int oldY = j.getY();
				
				try {
					if (!j.getType().equals("Circle")) {
						if (!yField.getText().equals("")) j.setY(trusty.Int(yField.getText()));
						else j.setY(0);
					} else {
						if (!yField.getText().equals("")) {
							g.getInstructions().stream().filter(l -> l.getSuperObject().equals("mid"+j.getName())).findFirst().get().setY(trusty.Int(yField.getText()));
							j.setY(trusty.Int(yField.getText())-(j.getDiameter()/2)+(Main.screen.width/180/2));
							g.getInstructions().stream().filter(l -> l.getSuperObject().equals(j.getName())).findFirst().get().setY(g.getInstructions().stream().filter(l -> l.getSuperObject().equals("mid"+j.getName())).findFirst().get().getY()+j.getDiameter()/2);
							g.getInstructions().stream().filter(l -> l.getSuperObject().equals(j.getName())).findFirst().get().setX(g.getInstructions().stream().filter(l -> l.getSuperObject().equals("mid"+j.getName())).findFirst().get().getX());
						}
						else {
							g.getInstructions().stream().filter(l -> l.getSuperObject().equals("mid"+j.getName())).findFirst().get().setY(0);
							j.setY(0-(j.getDiameter()/2)+(Main.screen.width/180/2));
							g.getInstructions().stream().filter(l -> l.getSuperObject().equals(j.getName())).findFirst().get().setY(g.getInstructions().stream().filter(l -> l.getSuperObject().equals("mid"+j.getName())).findFirst().get().getY()+j.getDiameter()/2);
							g.getInstructions().stream().filter(l -> l.getSuperObject().equals(j.getName())).findFirst().get().setX(g.getInstructions().stream().filter(l -> l.getSuperObject().equals("mid"+j.getName())).findFirst().get().getX());
						}
					}
				} catch(NumberFormatException e1) {
					;
				}
				
				if (j.getType().equals("Point")) {
					ArrayList<Instruction> replacementInstructions = new ArrayList<>();
					for (Instruction i : g.getInstructions()) {
						if (i.getType().equals("Segment")) {
							if (i.getP1().getX()-(screen.width/180/2) == j.getX() || i.getP2().getX()-(screen.width/180/2) == j.getX() && i.getP1().getY()-(screen.width/180/2) == oldY || i.getP2().getY()-(screen.width/180/2) == oldY) {
								if (i.getP1().getX()-(screen.width/180/2) == j.getX()) {
									replacementInstructions.add(new Instruction(new PlotPoint(j.getX()+(screen.width/180/2), j.getY()+(screen.width/180/2)), i.getP2(), "Segment", i.getName()));
								} else {
									replacementInstructions.add(new Instruction(i.getP1(), new PlotPoint(j.getX()+(screen.width/180/2), j.getY()+(screen.width/180/2)), "Segment", i.getName()));
								}
							} else {
								replacementInstructions.add(i);
							}
						} else {
							replacementInstructions.add(i);
						}
					}
					g.setInstructions(replacementInstructions);
				}
				
				frame.repaint();
				g.repaint();
				g.paintComponent(g.getGraphics());
			}
			public void keyPressed(KeyEvent e) {}
		});
		descBar.add(yField);
	}
}
