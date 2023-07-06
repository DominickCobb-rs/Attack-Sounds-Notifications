/*
 * Copyright (c) 2018, Kamiel, <https://github.com/Kamielvf>
 * Copyright (c) 2018, Psikoi <https://github.com/psikoi>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
// Chatbox Item Search - From runelite/client/plugins/banktags/tabs/TabInterface
/*
 * Copyright (c) 2018, Tomas Slusny <slusnucky@gmail.com>
 * Copyright (c) 2018, Ron Young <https://github.com/raiyni>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.AttackSoundNotifications.ui;

import com.AttackSoundNotifications.AttackSoundNotificationsPlugin;
import com.AttackSoundNotifications.ui.AttackSoundNotificationsPanel.Condition;
import com.AttackSoundNotifications.ui.AttackSoundNotificationsPanel.SoundOption;

import net.runelite.api.GameState;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.FlatTextField;
import net.runelite.client.util.ImageUtil;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntryPanel extends JPanel
{
	private JPanel mainPanel;
	private Integer weaponId = -1;
	private ImageIcon weaponIcon;
	private final AttackSoundNotificationsPanel pluginPanel;
	private final AttackSoundNotificationsPlugin plugin;
	private final JPanel weaponIconPanel = new JPanel(new BorderLayout()){
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			ImageIcon image = new ImageIcon(ImageUtil.loadImageResource(AttackSoundNotificationsPlugin.class, "/icons/weapon_back.png"));
			int x = (getWidth() - image.getIconWidth()) / 2;
			int y = (getHeight() - image.getIconHeight()) / 2;
			g.drawImage(image.getImage(), x, y, null);
		}
	};
	private final JPanel chooserPanel = new JPanel(new BorderLayout());
	private final JPanel removePanel = new JPanel(new BorderLayout());
	private final JPanel topPanel = new JPanel(new BorderLayout());
	private final JPanel iconPanel = new JPanel(new BorderLayout());
	private final JPanel textEntry = new JPanel(new BorderLayout());
	private final JPanel buttons = new JPanel(new BorderLayout());

	// Data fields //
	private JLabel weaponLabel = new JLabel();
	private JLabel removeEntry = new JLabel();
	private JLabel audible = new JLabel();
	private JTextField customSoundTextField = new JTextField();
	private JLabel selectFile = new JLabel();
	private JButton testSound = new JButton("Play the sound");
	private JComboBox<Condition> replacing = new JComboBox<>(new DefaultComboBoxModel<>(Condition.values()));
	private JComboBox<SoundOption> playing = new JComboBox<>(new DefaultComboBoxModel<>(SoundOption.values()));
	/////////////////

	// Not my stuff - From the ScreenMarkerPluginPanel //
	private final JPanel bottomContainer = new JPanel(new BorderLayout());
	private final FlatTextField nameInput = new FlatTextField();
	private final JLabel rename = new JLabel("Rename");
	private final JLabel save = new JLabel("Save");
	private final JLabel cancel = new JLabel("Cancel");
	private static final Border NAME_BOTTOM_BORDER = new CompoundBorder(
		BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.DARK_GRAY_COLOR), BorderFactory.createLineBorder(ColorScheme.DARKER_GRAY_COLOR));

	// Icons //
	public static final ImageIcon WEAPON_BACKGROUND;
	public static final BufferedImage DEFAULT_WEAPON_ICON;
	public static final ImageIcon DEFAULT_WEAPON_HOVER_ICON;
	public static final ImageIcon AUDIBLE_ICON;
	public static final ImageIcon AUDIBLE_HOVER_ICON;
	public static final ImageIcon INAUDIBLE_ICON;
	public static final ImageIcon INAUDIBLE_HOVER_ICON;
	public static final ImageIcon REMOVE_ICON;
	public static final ImageIcon REMOVE_HOVER_ICON;
	public static final ImageIcon OPEN_ICON;
	public static final ImageIcon OPEN_HOVER_ICON;

	static
	{
		final BufferedImage audibleImg = ImageUtil.loadImageResource(AttackSoundNotificationsPlugin.class, "/icons/on.png");
		AUDIBLE_ICON = new ImageIcon(audibleImg);
		AUDIBLE_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(audibleImg, -100));

		final BufferedImage inaudibleImg = ImageUtil.loadImageResource(AttackSoundNotificationsPlugin.class, "/icons/off.png");
		INAUDIBLE_ICON = new ImageIcon(inaudibleImg);
		INAUDIBLE_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(inaudibleImg, -100));

		final BufferedImage removeImg = ImageUtil.loadImageResource(AttackSoundNotificationsPlugin.class, "/icons/delete.png");
		REMOVE_ICON = new ImageIcon(removeImg);
		REMOVE_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(removeImg, -100));

		final BufferedImage openImg = ImageUtil.loadImageResource(AttackSoundNotificationsPlugin.class, "/icons/open_icon.png");
		OPEN_ICON = new ImageIcon(openImg);

		final BufferedImage openBrightImage = ImageUtil.loadImageResource(AttackSoundNotificationsPlugin.class, "/icons/open_bright_icon.png");
		OPEN_HOVER_ICON = new ImageIcon(openBrightImage);

		final BufferedImage weaponBackground = ImageUtil.loadImageResource(AttackSoundNotificationsPlugin.class, "/icons/weapon_back.png");
		WEAPON_BACKGROUND = new ImageIcon(weaponBackground);

		DEFAULT_WEAPON_ICON = ImageUtil.loadImageResource(AttackSoundNotificationsPlugin.class, "/icons/panelIcon.png");
		final BufferedImage searchIcon = ImageUtil.loadImageResource(AttackSoundNotificationsPlugin.class, "/icons/search.png");
		DEFAULT_WEAPON_HOVER_ICON = new ImageIcon(searchIcon);
	}


	public EntryPanel(AttackSoundNotificationsPanel passedPanel, AttackSoundNotificationsPlugin passedPlugin)
	{
		pluginPanel = passedPanel;
		plugin = passedPlugin;
		this.setName("Custom Sound " + (pluginPanel.entryPanel.getComponentCount() + 1));

		// Make default icons
		setWeaponIcons(DEFAULT_WEAPON_ICON);

		customSoundTextField.setFocusable(true);

		replacing.setFocusable(false);
		replacing.setToolTipText("When to play the sound");

		playing.setFocusable(false);
		playing.setToolTipText("What sound to play");

		testSound.setFocusable(false);
		testSound.setToolTipText("Play the selected sound, if it exists");

		JPanel nameWrapper = new JPanel(new BorderLayout());
		nameWrapper.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		nameWrapper.setBorder(NAME_BOTTOM_BORDER);

		JPanel nameActions = new JPanel(new BorderLayout(3, 0));
		nameActions.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel resetOptions = new JPanel(new BorderLayout());
		resetOptions.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JLabel reset = new JLabel("Reset");
		reset.setFont(FontManager.getRunescapeSmallFont());
		reset.setForeground(ColorScheme.LIGHT_GRAY_COLOR.darker());
		reset.setHorizontalAlignment(SwingConstants.CENTER);
		reset.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				int confirm = -1;
				boolean shiftHeld = mouseEvent.isShiftDown();
				if (!shiftHeld)
				{
					confirm = JOptionPane.showConfirmDialog(EntryPanel.this, "Reset this sound swap?", "Confirm", JOptionPane.YES_NO_OPTION);
				}
				else
				{
					confirm = 0;
				}
				if (confirm == 0)
				{
					EntryPanel.this.setName("Custom Sound " + pluginPanel.entryPanel.getComponentCount());
					nameInput.setText("Custom Sound " + pluginPanel.entryPanel.getComponentCount());
					audible.setIcon(AUDIBLE_ICON);
					weaponLabel.setIcon(weaponIcon);
					weaponId = -1;
					replacing.setSelectedIndex(0);
					playing.setSelectedIndex(0);
					textEntry.setVisible(false);
					pluginPanel.save();
				}
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				reset.setForeground(ColorScheme.PROGRESS_ERROR_COLOR);
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				reset.setForeground(ColorScheme.LIGHT_GRAY_COLOR.darker());
			}
		});

		save.setVisible(false);
		save.setFont(FontManager.getRunescapeSmallFont());
		save.setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR);
		save.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				save();
				mainPanel.requestFocusInWindow();
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				save.setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR.darker());
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				save.setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR);
			}
		});

		cancel.setVisible(false);
		cancel.setFont(FontManager.getRunescapeSmallFont());
		cancel.setForeground(ColorScheme.PROGRESS_ERROR_COLOR);
		cancel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				cancel();
				mainPanel.requestFocusInWindow();
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				cancel.setForeground(ColorScheme.PROGRESS_ERROR_COLOR.darker());
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				cancel.setForeground(ColorScheme.PROGRESS_ERROR_COLOR);
			}
		});

		rename.setFont(FontManager.getRunescapeSmallFont());
		rename.setForeground(ColorScheme.LIGHT_GRAY_COLOR.darker());
		rename.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				nameInput.setEditable(true);
				updateNameActions(true);
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				rename.setForeground(ColorScheme.LIGHT_GRAY_COLOR.darker().darker());
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				rename.setForeground(ColorScheme.LIGHT_GRAY_COLOR.darker());
			}
		});

		nameActions.add(save, BorderLayout.EAST);
		nameActions.add(cancel, BorderLayout.WEST);
		nameActions.add(rename, BorderLayout.CENTER);

		nameInput.setText(this.getName());
		nameInput.setBorder(null);
		nameInput.setEditable(false);
		nameInput.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		nameInput.getTextField().setForeground(Color.WHITE);
		nameInput.getTextField().setHorizontalAlignment(JTextField.CENTER);
		nameInput.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					save();
					mainPanel.requestFocusInWindow();
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					cancel();
					mainPanel.requestFocusInWindow();
				}
			}
		});

		nameWrapper.add(nameInput, BorderLayout.CENTER);
		nameWrapper.setPreferredSize(new Dimension(200, 20));

		resetOptions.add(reset, BorderLayout.CENTER);

		removeEntry.setIcon(new ImageIcon(getClass().getResource("/icons/delete.png")));
		removeEntry.setToolTipText("Remove this sound");

		removeEntry.setBorder(new EmptyBorder(2, 2, 2, 2));

		removePanel.add(removeEntry, BorderLayout.EAST);
		removePanel.add(nameActions, BorderLayout.WEST);
		removePanel.add(resetOptions, BorderLayout.CENTER);


		weaponLabel.setHorizontalAlignment(SwingConstants.CENTER);
		weaponLabel.setBorder(new EmptyBorder(2, 2, 2, 2));
		weaponIconPanel.add(weaponLabel,BorderLayout.CENTER);
		weaponIconPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		weaponIconPanel.setMinimumSize(new Dimension(32, 32));
		weaponIconPanel.setPreferredSize(new Dimension(32, 32));
		weaponIconPanel.setMaximumSize(new Dimension(32, 32));
		chooserPanel.add(replacing, BorderLayout.EAST);
		chooserPanel.add(weaponIconPanel, BorderLayout.WEST);
		chooserPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		topPanel.add(removePanel, BorderLayout.NORTH);
		topPanel.add(chooserPanel, BorderLayout.CENTER);
		topPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		topPanel.setBorder(new EmptyBorder(4, 4, 4, 4));

		audible.setHorizontalAlignment(SwingConstants.CENTER);
		audible.setToolTipText("Enable/Disable");
		iconPanel.add(audible, BorderLayout.WEST);
		iconPanel.add(playing, BorderLayout.EAST);
		iconPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		iconPanel.setBorder(new EmptyBorder(4, 4, 4, 4));

		selectFile.setIcon(OPEN_ICON);
		selectFile.setVisible(false);
		selectFile.setToolTipText("Find the file in on your computer");

		textEntry.setVisible(false);
		textEntry.add(new JLabel("Enter your custom sound here:"), BorderLayout.NORTH);
		textEntry.add(customSoundTextField, BorderLayout.CENTER);
		textEntry.add(selectFile, BorderLayout.EAST);
		textEntry.setBorder(new EmptyBorder(5, 8, 5, 8));
		textEntry.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		buttons.add(textEntry, BorderLayout.NORTH);
		buttons.add(testSound, BorderLayout.CENTER);
		buttons.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		bottomContainer.add(topPanel, BorderLayout.NORTH);
		bottomContainer.add(iconPanel, BorderLayout.CENTER);
		bottomContainer.add(buttons, BorderLayout.SOUTH);
		bottomContainer.setBorder(new EmptyBorder(8, 0, 8, 0));
		bottomContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setFocusable(true);
		mainPanel.add(nameWrapper, BorderLayout.NORTH);
		mainPanel.add(bottomContainer, BorderLayout.SOUTH);
		Border emptyBorder = new EmptyBorder(0, 0, 0, 0);
		MatteBorder topRightBorder = new MatteBorder(3, 0, 0, 3, ColorScheme.DARKER_GRAY_COLOR.brighter());
		MatteBorder bottomLeftBorder = new MatteBorder(0, 3, 3, 0, ColorScheme.DARKER_GRAY_COLOR.darker());
		CompoundBorder firstBorder = new CompoundBorder(topRightBorder, emptyBorder);
		CompoundBorder semiFinalBorder = new CompoundBorder(bottomLeftBorder, firstBorder);
		CompoundBorder finalBorder = new CompoundBorder(new EmptyBorder(1, 0, 1, 0), semiFinalBorder);

		mainPanel.setBorder(finalBorder);
		playing.setPreferredSize(new Dimension(170, 32));
		replacing.setPreferredSize(playing.getPreferredSize());

		audible.setIcon(AUDIBLE_ICON);
		audible.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		// Set default weapon icon //
		weaponLabel.setIcon(weaponIcon);
		weaponIconPanel.setToolTipText("Choose the weapon to play the sound for");

		// ACTIONS //
		weaponIconPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		weaponIconPanel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				// Ripped directly from weapon-animation-replacer
				if (plugin.client.getGameState() != GameState.LOGGED_IN)
				{
					JOptionPane.showMessageDialog(pluginPanel,
							"This plugin uses the in-game item search panel; you must be logged in to use this.",
							"Log in to choose items",
							JOptionPane.ERROR_MESSAGE);
					weaponLabel.setIcon(weaponIcon);
					return;
				}
				plugin.searchProvider.tooltipText("Choose this weapon").onItemSelected((itemId) -> {
					if (itemId == null)
					{
						log.warn("Failed to get itemId");
					}
					else
					{
						weaponId = itemId;
						log.debug("Found itemId: " + weaponId);
						setWeaponIcons(plugin.itemManager.getImage(weaponId, 0, false));
						pluginPanel.save();
					}
				}).build();
				plugin.chatboxPanelManager.openInput(plugin.searchProvider);
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				weaponLabel.setIcon(DEFAULT_WEAPON_HOVER_ICON);
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				weaponLabel.setIcon(weaponIcon);
			}

		});

		audible.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				if (audible.getIcon() == AUDIBLE_HOVER_ICON)
				{
					audible.setIcon(INAUDIBLE_HOVER_ICON);
				}
				else
				{
					audible.setIcon(AUDIBLE_HOVER_ICON);
				}
				pluginPanel.save();
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				if (audible.getIcon() == AUDIBLE_ICON)
				{
					audible.setIcon(AUDIBLE_HOVER_ICON);
				}
				else
				{
					audible.setIcon(INAUDIBLE_HOVER_ICON);
				}
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				if (audible.getIcon() == AUDIBLE_HOVER_ICON)
				{
					audible.setIcon(AUDIBLE_ICON);
				}
				else
				{
					audible.setIcon(INAUDIBLE_ICON);
				}
			}
		});

		removeEntry.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				boolean shiftHeld = mouseEvent.isShiftDown();
				if (!shiftHeld)
				{
					int confirm = JOptionPane.showConfirmDialog(EntryPanel.this, "Delete this sound swap?", "Confirm", JOptionPane.YES_NO_OPTION);
					if (confirm != 0)
					{
						return; // If user doesn't confirm, we do nothing more
					}
				}
				EntryPanel.this.removeSelf();
				pluginPanel.entryPanel.remove(mainPanel);
				pluginPanel.reloadPanels();
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				removeEntry.setIcon(REMOVE_HOVER_ICON);
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				removeEntry.setIcon(REMOVE_ICON);
			}
		});

		replacing.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				pluginPanel.save();
			}
		});

		playing.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Default sound option selected
				if (playing.getSelectedItem().equals(SoundOption.CUSTOM_SOUND))
				{
					textEntry.setVisible(true);
					selectFile.setVisible(true);
				}
				else
				{
					textEntry.setVisible(false);
					selectFile.setVisible(false);
				}
				pluginPanel.save();
			}
		});

		customSoundTextField.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (customSoundTextField.getText().endsWith(".wav"))
				{
					pluginPanel.requestFocusInWindow();
					pluginPanel.save();
				}
				else
				{
					JOptionPane.showMessageDialog(EntryPanel.this, "Acceptable file types: .wav", "Bad file", JOptionPane.ERROR_MESSAGE);
					customSoundTextField.setText("");
				}
			}
		});

		selectFile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		selectFile.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"))); // start at application
				fileChooser.setFileFilter(new FileNameExtensionFilter(".wav sound files", "wav"));
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION)
				{
					String filePath = fileChooser.getSelectedFile().getPath();
					if (filePath.endsWith(".wav"))
					{
						customSoundTextField.setText(filePath);
						pluginPanel.save();
					}
					else
					{
						JOptionPane.showMessageDialog(EntryPanel.this, "Acceptable file types: .wav", "Bad file", JOptionPane.ERROR_MESSAGE);
					}
				}
				pluginPanel.reloadPanels();
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				selectFile.setIcon(OPEN_HOVER_ICON);
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				selectFile.setIcon(OPEN_ICON);
			}
		});

		testSound.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (getPlaying() == SoundOption.CUSTOM_SOUND)
				{
					pluginPanel.findCustomSound(getCustomSoundPath());
				}
				else
				{
					pluginPanel.playDefaultSound(pluginPanel.getDefaultSoundChoice(getPlaying()));
				}
			}
		});

		log.debug("Created new entry panel");
	}

	// ALSO NOT MINE //
	// From the Screen Markers plugin //
	private void save()
	{
		// plugin.updateConfig();
		nameInput.setEditable(false);
		updateNameActions(false);
		setName(nameInput.getText());
		pluginPanel.save();
	}

	private void cancel()
	{
		nameInput.setEditable(false);
		nameInput.setText(this.getName());
		updateNameActions(false);
	}

	private void updateNameActions(boolean saveAndCancel)
	{
		save.setVisible(saveAndCancel);
		cancel.setVisible(saveAndCancel);
		rename.setVisible(!saveAndCancel);

		if (saveAndCancel)
		{
			nameInput.getTextField().requestFocusInWindow();
			nameInput.getTextField().selectAll();
		}
	}
	/////////////////////////

	// Get the panel to add to the pluginPanel //
	public JPanel getMainPanel()
	{
		return mainPanel;
	}

	// Remove the panel from everything
	public void removeSelf()
	{
		pluginPanel.removeEntryPanel(this);
	}

	// Get the panel data, for sound selection purposes //
	public int getWeaponId()
	{
		return weaponId;
	}

	public boolean getAudible()
	{
		if (audible.getIcon() == AUDIBLE_ICON || audible.getIcon() == AUDIBLE_HOVER_ICON)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public Condition getReplacing()
	{
		return (Condition) replacing.getSelectedItem();
	}

	public SoundOption getPlaying()
	{
		return (SoundOption) playing.getSelectedItem();
	}

	// Get the panel data in string form for simple storage
	public String getWeaponIdString()
	{
		return Integer.toString(weaponId);
	}

	public String getAudibleString()
	{
		if (audible.getIcon() == AUDIBLE_ICON || audible.getIcon() == AUDIBLE_HOVER_ICON)
		{
			return "true";
		}
		else
		{
			return "false";
		}
	}

	public String getCustomSoundPath()
	{
		return customSoundTextField.getText();
	}

	public String getReplacingString()
	{
		return replacing.getSelectedItem().toString();
	}

	public String getPlayingString()
	{
		return playing.getSelectedItem().toString();
	}

	private void setWeaponIcons(BufferedImage image)
	{
		weaponIcon = new ImageIcon(image);
		weaponLabel.setIcon(weaponIcon);
	}

	// Set the panel data from the stored config
	public void setPanelName(String name)
	{
		setName(name);
		nameInput.setText(name);
	}

	public void setWeaponId(String id)
	{
		weaponId = (Integer.parseInt(id));
		if (weaponId == -1)
		{
			setWeaponIcons(DEFAULT_WEAPON_ICON);
		}
		else
		{
			setWeaponIcons(plugin.itemManager.getImage(weaponId, 0, false));
		}
	}

	public void setAudible(String audibleStatus)
	{
		if (audibleStatus.equals("true"))
		{
			audible.setIcon(AUDIBLE_ICON);
		}
		else
		{
			audible.setIcon(INAUDIBLE_ICON);
		}
	}

	public void setReplacing(Condition replacingValue)
	{
		replacing.setSelectedItem(replacingValue);
	}

	public void setPlaying(SoundOption playingValue)
	{
		playing.setSelectedItem(playingValue);
	}

	public void makeSoundInputVisible()
	{
		textEntry.setVisible(true);
		selectFile.setVisible(true);
	}

	public void setCustomSoundPath(String customSoundPath)
	{
		customSoundTextField.setText(customSoundPath);
	}
}