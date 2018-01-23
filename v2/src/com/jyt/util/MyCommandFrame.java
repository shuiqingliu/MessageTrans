package com.jyt.util;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MyCommandFrame extends JFrame {
	private Map<String, Process> process_map = new HashMap<String, Process>();
	private Map<String, StringBuffer> text_map = new HashMap<String,StringBuffer>();
	private Map<String, StringBuffer> error_text_map = new HashMap<String,StringBuffer>();
	private Map<String,BufferedReader> read_map = new HashMap<String,BufferedReader>();
	private Map<String,BufferedReader> error_read_map = new HashMap<String,BufferedReader>();

	class Communication extends Thread
	{
		public Communication()
		{
			for(String key:process_map.keySet())
			{
				text_map.put(key, new StringBuffer());
			}
		}
		
		public void run()
		{
			try{
				while(true)
				{
					boolean changed = false;
					for(String process_name:process_map.keySet())
					{
						BufferedReader bfr = read_map.get(process_name);
						BufferedReader error_bfr = error_read_map.get(process_name);
						
						if(bfr==null)
						{
							InputStream   in   =   process_map.get(process_name).getInputStream(); 
							bfr   =   new   BufferedReader(new   InputStreamReader(in)); 
							read_map.put(process_name, bfr);					
						}
						if(error_bfr==null)
						{
							InputStream   in   =   process_map.get(process_name).getInputStream(); 
							error_bfr   =   new   BufferedReader(new   InputStreamReader(in)); 
							error_read_map.put(process_name, bfr);					
						}
						
						if(text_map.get(process_name)==null)
						{
							StringBuffer sb = new StringBuffer();
							while(bfr.ready()){
								sb.append(bfr.readLine());
								sb.append("\n");	
								changed = true;
							}
							text_map.put(process_name,sb);
						}
						else{
							while(bfr.ready()){
								text_map.get(process_name).append(bfr.readLine());		
								text_map.get(process_name).append("\n");
								changed = true;
							}
						}					
						
						if(error_text_map.get(process_name)==null)
						{
							StringBuffer sb = new StringBuffer();
							while(error_bfr.ready()){
								sb.append(error_bfr.readLine());
								sb.append("\n");	
								changed = true;
							}
							error_text_map.put(process_name,sb);
						}
						else{
							while(error_bfr.ready()){
								text_map.get(process_name).append(error_bfr.readLine());		
								text_map.get(process_name).append("\n");
								changed = true;
							}
						}		
						
					}
					if(changed==false)
						Thread.sleep(1000);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	class ButtonListenner implements ActionListener {
		public String process_name;

		public ButtonListenner(String process_name) {
			this.process_name = process_name;
		}


		public void actionPerformed(ActionEvent event) {
			try{
				JFrame   frame   =   new   JFrame(); 
				frame.setTitle(process_name);
				Container contentPane = frame.getContentPane();
				
				String s1 = error_text_map.get(process_name).toString();
				String s2 = "============================================";
				String s3 = text_map.get(process_name).toString();
				String s = s1+"\n"+s2+"\n"+s3;

				JTextArea   textArea   =   new   JTextArea(s); 
				JScrollPane scrollPane = new JScrollPane(textArea);
				scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	

				contentPane.add(scrollPane,BorderLayout.CENTER); 
				frame.setBounds(200,   200,   500,   500); 
				frame.setVisible(true); 

				frame.addWindowListener(new WindowAdapter()
				{
					public void windowClosing(WindowEvent e)
					{
						e.getWindow().setVisible(false);
						e.getWindow().dispose();
					}
				});

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	public MyCommandFrame(Map<String, Process> map) {
		this.process_map = map;
		JFrame frame = new JFrame();
		frame.setTitle("½ø³ÌÏÔÊ¾");
		Container content = frame.getContentPane();
		content.setLayout(new BoxLayout(content,BoxLayout.X_AXIS));
		List<String> key_list = new ArrayList<String>();
		for(String key:process_map.keySet())
		{
			key_list.add(key);
		}
		Collections.sort(key_list);
		for (String key : key_list) {
			JButton button = new JButton(key);
			ButtonListenner bl = new ButtonListenner(key);
			button.addActionListener(bl);
			content.add(button);
		}
		Communication com = new Communication();
		com.start();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				for (String key : process_map.keySet()) {
					Process p = process_map.get(key);
					p.destroy();
					try{
						int ev = p.exitValue();
						MyPrint.print(ev+"", new Exception());						
					}
					catch(Exception ex)
					{
						
					}
				}
				System.exit(0);
			}
		});
		frame.pack();
		frame.setVisible(true);
	}

}
