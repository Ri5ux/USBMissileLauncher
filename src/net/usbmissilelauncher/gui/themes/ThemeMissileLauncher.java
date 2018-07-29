package net.usbmissilelauncher.gui.themes;

import org.asx.glx.gui.themes.Theme;
import org.newdawn.slick.Color;

public class ThemeMissileLauncher extends Theme
{
	public ThemeMissileLauncher()
	{
		this.highlightColor = new Color(0xFFFF0000);
		this.borderColor = new Color(0x22AAAAAA);
		this.backgroundColor = new Color(0x66FF0000);
		this.textFieldColor = new Color(0, 0, 0F, 0.4F);
		this.textFieldHoveredColor = new Color(0, 0, 0F, 0.3F);
		this.alternativeTextColor = new Color(1F, 1F, 1F, 1F);
		this.fadeSpeed = 60;
	}
}
