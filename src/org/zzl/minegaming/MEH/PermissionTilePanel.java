package org.zzl.minegaming.MEH;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.zzl.minegaming.GBAUtils.BitConverter;
import org.zzl.minegaming.MEH.MapElements.MapTile;
import org.zzl.minegaming.MEH.MapElements.Tileset;

import java.awt.event.InputEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

public class PermissionTilePanel extends JPanel
{
	private static final long serialVersionUID = -877213633894324075L;
	public static int baseSelectedTile;	// Called it base in case of multiple tile
	// selection in the future.
	private static int editorWidth = 4; //Editor width in 16x16 tiles
	private Tileset globalTiles;
	private Tileset localTiles;
	private boolean isMouseDown = true;
	private boolean legacyPerms = true;
    private static boolean Redraw = true;
	static Rectangle mouseTracker;
	public static Image imgPermissions;
	public void SetRect(int width, int heigh){

		if(heigh>16) heigh=16;
		if(width>16) width=16;
		mouseTracker.height=heigh;
		mouseTracker.width=width;
	}
	public void SetRect(){
		mouseTracker.height=16;
		mouseTracker.width=16;
	}
	int srcX;
	int srcY;

	public PermissionTilePanel()
	{
		setPermissionMode(legacyPerms);
		mouseTracker=new Rectangle(0,0,16,16);
		try {
			imgPermissions=ImageIO.read(MainGUI.class.getResourceAsStream(legacyPerms ? "/resources/permissionslinear.png" : "/resources/permissions.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//auto generate image on fail.
			e1.printStackTrace();
		}

		this.addMouseMotionListener(new MouseMotionListener()
		{

			@Override
			public void mouseDragged(MouseEvent e)
			{
				int b1 = InputEvent.BUTTON1_DOWN_MASK;
				int b2 = InputEvent.BUTTON2_DOWN_MASK;
				if ((e.getModifiersEx() & (b1 | b2)) != b1) 
				{
					MapEditorPanel.calculateSelectBox(e);
					repaint();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e)
			{
				mouseTracker.x=e.getX();
				mouseTracker.y=e.getY();
				if(mouseTracker.x > editorWidth * 16)
					mouseTracker.x = (int)((editorWidth - 1) * 16);
				if(mouseTracker.y > 16 * 16)
					mouseTracker.y = (int)(16 * 15);
				repaint();

			}

		});

		this.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseClicked(MouseEvent e)
			{
				int x = 0;
				int y = 0;

				x = (e.getX() / 16);
				y = (e.getY() / 16);
				if (e.getClickCount() == 2 && e.getButton()==3){
					SetRect();//Reset tile rectangle
				}
				else{
					srcX=x;
					srcY=y;
					baseSelectedTile = x + (y * editorWidth);
					MapEditorPanel.selectBuffer = new MapTile[1][1];
					MapEditorPanel.selectBuffer[0][0] = new MapTile(TileEditorPanel.baseSelectedTile,PermissionTilePanel.baseSelectedTile); //TODO implement movement perms
					MapEditorPanel.bufferWidth = 1;
					MapEditorPanel.bufferHeight = 1;
					MapEditorPanel.selectBox.width = 16;
					MapEditorPanel.selectBox.height = 16;
					String k = "Current Tile: ";
					k += String.format("0x%8s",
							Integer.toHexString(baseSelectedTile))
							.replace(' ', '0');
					MainGUI.lblTileVal.setText("Current Perm: 0x" + BitConverter.toHexString(PermissionTilePanel.baseSelectedTile));
					repaint();
				}

			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if(e.getButton() == 3)
				{
					MapEditorPanel.selectBox = new Rectangle(e.getX(),e.getY(),0,0);
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{

			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				isMouseDown = true;
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				if(e.getButton() == 3)
				{
					MapEditorPanel.calculateSelectBox(e);

					//Fill the tile buffer
					MapEditorPanel.selectBuffer = new MapTile[MapEditorPanel.selectBox.width / 16][MapEditorPanel.selectBox.height / 16];
					MapEditorPanel.bufferWidth = MapEditorPanel.selectBox.width / 16;
					MapEditorPanel.bufferHeight = MapEditorPanel.selectBox.height / 16;
					for(int x = 0; x < MapEditorPanel.bufferWidth; x++)
						for(int y = 0; y < MapEditorPanel.bufferHeight; y++)
							MapEditorPanel.selectBuffer[x][y] = new MapTile(baseSelectedTile = x + (y * editorWidth), 0xC); //TODO implement movement perms
				}
			}

		});


	}


	public static Graphics gcBuff;
	static Image imgBuffer = null;
	public void DrawTileset()
	{
		Dimension d = new Dimension(editorWidth * 16,(64 / editorWidth) * 16);//4 tiles per level, 16 levels total 

		imgBuffer = new BufferedImage(d.width,d.height,BufferedImage.TYPE_INT_ARGB);
		setSize(d);
		gcBuff=imgBuffer.getGraphics();
		int x=0;
		int y=0;
		int i=0;
					
		for(y=0;y<64 / editorWidth;y++){
			for(x=0;x<editorWidth;x++){
				
				gcBuff.drawImage(((BufferedImage)(PermissionTilePanel.imgPermissions)).getSubimage((x+(y*editorWidth)) * 16, 0, 16, 16), x * 16, y * 16, this);
			      
				
			}
		}
		

		
	}
	
	public void setPermissionMode(boolean legacy)
	{
		if(legacy)
			editorWidth = 1;
		else
			editorWidth = 4;
		
		setPreferredSize(new Dimension(editorWidth * 16,(64 / editorWidth) * 16));
		setSize(new Dimension(editorWidth * 16,(64 / editorWidth) * 16));	
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
			if(PermissionTilePanel.Redraw==true){
				DrawTileset();
				PermissionTilePanel.Redraw=false;
			}
			g.drawImage(imgBuffer, 0, 0, this);
			
			g.setColor(Color.red);
			g.drawRect((baseSelectedTile % editorWidth) * 16, (baseSelectedTile / editorWidth) * 16, 15, 15);
			
			g.setColor(Color.GREEN);
			if( mouseTracker.width <0)
				mouseTracker.x-=Math.abs( mouseTracker.width);
			if( mouseTracker.height <0)
				mouseTracker.y-=Math.abs( mouseTracker.height);
			g.drawRect(((mouseTracker.x / 16) % editorWidth) * 16,(mouseTracker.y / 16) * 16,MapEditorPanel.selectBox.width-1,MapEditorPanel.selectBox.height-1);
		try
		{
			//best error image.
			//I'll always remember you Smeargle <3
			//g.drawImage(ImageIO.read(MainGUI.class.getResourceAsStream("/resources/smeargle.png")), 100, 240,null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}