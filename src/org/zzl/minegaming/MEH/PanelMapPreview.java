package org.zzl.minegaming.MEH;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import org.zzl.minegaming.GBAUtils.ROMManager;

public class PanelMapPreview extends JPanel
{
	private static final long serialVersionUID = 3799250208011044425L;
	
	Image mapBuf;
	private int scale = 4;
	public PanelMapPreview()
	{
		super();
	}
	
	public void setMap(int bank, int map)
	{
		long offset=BankLoader.maps[bank].get(map);
		mapBuf = Map.renderMap(new Map(ROMManager.currentROM,(int)(offset)), true);
		mapBuf = mapBuf.getScaledInstance((int)(mapBuf.getWidth(this) / scale), (int)(mapBuf.getHeight(this) / scale), Image.SCALE_SMOOTH);
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(mapBuf != null)
			g.drawImage(mapBuf,(this.getWidth() / 2) - (mapBuf.getWidth(this) / 2), (this.getHeight() / 2) - (mapBuf.getHeight(this) / 2),this);
	}

}
