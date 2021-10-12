package gui;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jm.music.data.Tempo;
import core.data.DataStorage;
import core.data.Settings;

public class HeaderPanel extends JPanel {

	private MainPanel parent;
	private JLabel tonalityLabel;
	public JComboBox tonalityKey;
	public LinkedList<JRadioButton> tonalityTypes;
	//private JLabel tempoLabel;
	public JSlider tempoSlider;
	
	
	public JCheckBox melismaCheckBox;
	public JPanel melismaPanel;
	
	public JButton addInstrumentButton;
	private JButton resetButton;
	private JButton recordButton;
	
	public String[] keys;
	private String[] tonalityTypesString;
	public JComboBox<Integer> melismaComboBox;
	private JLayeredPane tonalityPanel;
	private JLayeredPane generationPanel;
	public JSlider volumeSlider;
	private JPanel buttonsPanel;	
	public JCheckBox muteAllCheckBox;
	
	public HeaderPanel(MainPanel _parent)
	{
		this.parent = _parent;
		
	    setLayout(new GridBagLayout());
	    
		//=============================================
	    //Initialize elements
		//=============================================
	    
	    initTonalityPanel();
	    initGenerationPanel();
	    initButtonsPanel();
	    
        
        //=============================================
        //Add elements to panel
		//=============================================

	    add(tonalityPanel, new GBC(0,0,1,1).fill(GBC.HORIZONTAL));
	    add(generationPanel, new GBC(1,0,1,1).fill(GBC.HORIZONTAL));
	    //add(tempoPanel, new GBC(0,1,1,1).fill(GBC.HORIZONTAL));
	    add(buttonsPanel, new GBC(2,0,1,1).fill(GBC.HORIZONTAL));
	}

	private void initTonalityPanel() {
		
		tonalityPanel = new JLayeredPane();
	    tonalityPanel.setPreferredSize(new Dimension(150, 90));
	    tonalityPanel.setBorder(BorderFactory.createTitledBorder(
                "tonality"));
	    tonalityPanel.setLayout(new GridBagLayout());
	    
	    
	    tonalityLabel = new JLabel("sound  ");
	    tonalityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
	    
	    keys = new String[] { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
	    
	    tonalityKey = new JComboBox(keys);
	    tonalityKey.setSelectedIndex(0);
	    tonalityKey.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Settings.setTonalityPitch(tonalityKey.getSelectedIndex());
				parent.generator.recordsManager.recordTonalityKey();
			}
		});
	    
	    
	    //Initialize key types elements
        JPanel tonalityTypePanel = new JPanel();
        tonalityTypePanel.setLayout(new GridBagLayout());
        
        tonalityTypesString = new String[] {"Major", "Minor"};
        tonalityTypes = new LinkedList<JRadioButton>();
        ButtonGroup typeGroup = new ButtonGroup();
        for (int i=0; i< tonalityTypesString.length; i++)
        {
        	JRadioButton jrb = new JRadioButton(tonalityTypesString[i]);
        	tonalityTypes.add(jrb);
        	typeGroup.add(jrb);
        	jrb.setActionCommand(tonalityTypesString[i]);
        	jrb.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Settings.setTonalityType(DataStorage.getTonalityTypeByName(e.getActionCommand()));
					parent.generator.recordsManager.recordTonalityType();
				}
			});
        }
        tonalityTypes.getFirst().setSelected(true);
        
        tonalityPanel.add(tonalityLabel, new GBC(0,0,1,1).fill(GBC.HORIZONTAL));
        tonalityPanel.add(tonalityKey, new GBC(1,0,1,1).fill(GBC.HORIZONTAL));
	    
	    for(JRadioButton jrb : tonalityTypes) {
	    	tonalityTypePanel.add(jrb, new GBC(tonalityTypes.indexOf(jrb), 0, 1, 1).fill(GBC.HORIZONTAL));
	    }
	    
	    tonalityPanel.add(tonalityTypePanel, new GBC(0,1,2,1).fill(GBC.HORIZONTAL));
	}

	private void initGenerationPanel() {
		
		generationPanel = new JLayeredPane();
        generationPanel.setPreferredSize(new Dimension(450, 90));
        generationPanel.setBorder(BorderFactory.createTitledBorder(
                "Generation"));
        generationPanel.setLayout(new GridBagLayout());
 
	    tempoSlider = new JSlider(JSlider.HORIZONTAL, 50, 150, (int)Tempo.DEFAULT_TEMPO);
	    tempoSlider.setBorder(BorderFactory.createTitledBorder("Pace"));
	    tempoSlider.setMajorTickSpacing(50);
	    tempoSlider.setMinorTickSpacing(10);
	    tempoSlider.setPaintTicks(true);
        tempoSlider.setPaintLabels(true);
        
        tempoSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				JSlider source = (JSlider) arg0.getSource();
		        if (!source.getValueIsAdjusting()) {
					Settings.TEMPO.setTempo(tempoSlider.getValue());
					parent.generator.recordsManager.recordTempo();
		        }
			}
		});

        
        melismaCheckBox = new JCheckBox("Melisma");
        melismaCheckBox.setSelected(false);
        melismaCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Settings.USE_MELISMAS = melismaCheckBox.isSelected();
				melismaComboBox.setEnabled(Settings.USE_MELISMAS);
				parent.generator.recordsManager.recordSetMelismas();
			}
		});
        
        JLabel melismaFreqLabel = new JLabel("Melisma frequency ");
        melismaFreqLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        Integer[] melismaFreqs = new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	    
	    melismaComboBox = new JComboBox<Integer>(melismaFreqs);
	    melismaComboBox.setSelectedIndex(4);
	    melismaComboBox.setEnabled(false);
	    melismaComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.MELISMAS_CHANCE = melismaComboBox.getSelectedIndex() + 1;
				parent.generator.recordsManager.recordSetMelismasChance();
			}
		});
	    
	    melismaPanel = new JPanel();
        melismaPanel.setLayout(new GridBagLayout());
        melismaPanel.add(melismaFreqLabel, new GBC(0,0,1,1,1,1,GridBagConstraints.WEST, GridBagConstraints.NONE));
        melismaPanel.add(melismaComboBox, new GBC(1,0,1,1,1,1,GridBagConstraints.WEST, GridBagConstraints.NONE));
        
	    JLabel volumeLabel = new JLabel("Volume");
	    volumeLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
	    volumeSlider.setBorder(BorderFactory.createTitledBorder("Volume %"));
	    volumeSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				JSlider source = (JSlider) arg0.getSource();
		        if (!source.getValueIsAdjusting()) {
					Settings.OVERALL_VOLUME = volumeSlider.getValue();
					parent.generator.recordsManager.recordVolume();
		        }
			}
		});
	    
	    volumeSlider.setMajorTickSpacing(50);
	    volumeSlider.setMinorTickSpacing(10);
	    volumeSlider.setPaintTicks(true);
	    volumeSlider.setPaintLabels(true);

	    generationPanel.add(melismaCheckBox, new GBC(0,0,1,1,0.1,0.1,GridBagConstraints.WEST, GridBagConstraints.NONE));
	    generationPanel.add(melismaPanel, new GBC(0,1,1,1,0.1,0.1,GridBagConstraints.WEST, GridBagConstraints.NONE));
	    generationPanel.add(tempoSlider, new GBC(1,0,1,2,1,1,GridBagConstraints.WEST, GridBagConstraints.NONE).fill(GBC.HORIZONTAL));
	    generationPanel.add(volumeSlider, new GBC(2,0,1,2,1,1,GridBagConstraints.WEST, GridBagConstraints.NONE).fill(GBC.HORIZONTAL));
	}

	private void initButtonsPanel() {
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        
        addInstrumentButton = new JButton("Add");
        addInstrumentButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				parent.addInstrument();
			}
		});
        
        recordButton = new JButton("Recording");
        recordButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				parent.showRecorder();
			}
		});
        
        resetButton = new JButton("Clear");
        resetButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				parent.removeAllInstruments();
			}
		});
        
        muteAllCheckBox = new JCheckBox("Off all");
        muteAllCheckBox.setSelected(true);
        muteAllCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (InstrumentPanel ip : parent.instrumentPanels)
				{
					ip.muteCheckBox.setSelected(muteAllCheckBox.isSelected());
					ip.setMuted(muteAllCheckBox.isSelected());
					parent.generator.recordsManager.recordInstrMute(ip);
				}
			}
		});

	    buttonsPanel.add(addInstrumentButton, new GBC(0,0,1,1).fill(GBC.HORIZONTAL));
	    buttonsPanel.add(resetButton, new GBC(0,1,1,1).fill(GBC.HORIZONTAL));	
	    buttonsPanel.add(muteAllCheckBox, new GBC(0,2,1,1).fill(GBC.HORIZONTAL));
	    buttonsPanel.add(recordButton, new GBC(0,3,1,1).fill(GBC.HORIZONTAL));
	}
}
