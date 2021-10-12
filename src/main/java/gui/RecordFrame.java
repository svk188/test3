package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import util.Log;
import core.record.RecordEvent;
import core.record.RecordsManager;

public class RecordFrame extends JFrame {

	private MainPanel mainPanel;
	private JPanel panel;
	private RecordsManager recordsManager;
	private JButton recordButton;
	public JButton playButton;
	private JButton deleteButton;
	private JButton viewButton;
	
	public static JFrame frame;
	private DefaultListModel<String> listModel;
	private JList<String> recordsList;
	private JTextArea logArea;

	public RecordFrame(MainFrame mainFrame){
		super("Recording scripts");
		this.mainPanel = mainFrame.getMainPanel();
		
		this.recordsManager = mainFrame.getGenerator().recordsManager;
		RecordFrame.frame = this;
		initInterface();
		setSize(500, 450);
	}

	private void initInterface() {
		panel = new JPanel();
		
		panel.setLayout(new GridBagLayout());
		
		recordButton = new JButton("Record");
		recordButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!RecordsManager.isRecording)
				{
					log(
							"Recording started!");
					recordsManager.startRecord();
					recordButton.setText("Stop");
				}
				else
				{
					log("The recording is complete!");
					recordsManager.stopRecord();
					recordButton.setText("Record");
					refreshRecordsList();
				}
			}
		});
		
		listModel = new DefaultListModel<String>();
		recordsList = new JList<String>(listModel);
		refreshRecordsList();
		
		JScrollPane recordsScrollPane = new JScrollPane(recordsList);
		
		playButton = new JButton("DOWNLOAD");
		playButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!RecordsManager.isPlaying)
				{
					log("Playback" + recordsList.getSelectedValue() + " started!");
					recordsManager.playRecord(recordsList.getSelectedValue());
					playButton.setText("Stop");
				} 
				else
				{
					log("Play completed!");
					recordsManager.stopPlayingRecord();
					playButton.setText(
							"Download");
				}
			}
		});
		
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to delete the entry?", "Confirmation", JOptionPane.YES_NO_OPTION);
				if(dialogResult == JOptionPane.YES_OPTION){
					recordsManager.deleteRecord(recordsList.getSelectedValue());
					log("Record" + recordsList.getSelectedValue() + " deleted!");
					refreshRecordsList();
				}
			}
		});
		
		viewButton = new JButton("View");
		viewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				recordsManager.readRecordFile(recordsList.getSelectedValue());
				log("=================================");
				log("\n" +
						"Recording " + recordsList.getSelectedValue() + RecordsManager.RECORD_EXTENSION);
				for (RecordEvent ev : recordsManager.playbackEvents)
					log(ev.toString());
				log("=================================");
			}
		});
		
		logArea = new JTextArea();
        logArea.setRows(10);
        logArea.setFont(logArea.getFont().deriveFont(12f));
        
        JScrollPane logScroll = new JScrollPane(logArea);
        DefaultCaret caret = (DefaultCaret)logArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
        JPanel buttonsPan = new JPanel();
        buttonsPan.setLayout(new GridBagLayout());
        buttonsPan.add(playButton, new GBC(0,0,1,1));
        buttonsPan.add(deleteButton, new GBC(0,1,1,1));
        buttonsPan.add(viewButton, new GBC(0,2,1,1));
        
		panel.add(recordButton, new GBC(0,0,2,1,0,0));
		panel.add(recordsScrollPane, new GBC(0,1,1,1,1,0).fill(GridBagConstraints.HORIZONTAL));
		panel.add(buttonsPan, new GBC(1,1,1,1,0,0));
		panel.add(logScroll, new GBC(0,2,2,1,1,1).fill(GridBagConstraints.BOTH));
		setContentPane(panel);
	}
	
	public void refreshRecordsList()
	{
		listModel = new DefaultListModel<String>();
		for (String el : recordsManager.recordsList)
			listModel.addElement(el);
		recordsList.setModel(listModel);
		recordsList.setSelectedIndex(0);
		recordsList.ensureIndexIsVisible(0);
		
	}
	
	public void log(String text)
	{
		logArea.append(text + "\n");
		Log.info("RecordFrame > Log: " + text);
	}
	
}
