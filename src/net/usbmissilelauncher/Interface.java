package net.usbmissilelauncher;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.asx.glx.gui.GuiPanel;
import org.asx.glx.gui.themes.Theme;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.util.ResourceLoader;

import net.usbmissilelauncher.gui.forms.CommandForm;
import net.usbmissilelauncher.gui.themes.ThemeMissileLauncher;

public class Interface
{
    public static Interface instance;
    private GuiPanel        panel;
    private Theme           theme;
    private boolean         shouldTerminate;

    public Interface()
    {
        new USBMissileLauncher();
        Interface.instance = this;
        this.theme = new ThemeMissileLauncher();

        this.init();
        this.start();
    }

    public ByteBuffer loadIcon(String filename, int width, int height) throws IOException
    {
        BufferedImage image = ImageIO.read(new BufferedInputStream(ResourceLoader.getResourceAsStream(filename)));
        byte[] imageBytes = new byte[width * height * 4];

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                int pixel = image.getRGB(j, i);
                for (int k = 0; k < 3; k++)
                {
                    imageBytes[(i * 16 + j) * 4 + k] = (byte) (((pixel >> (2 - k) * 8)) & 255);
                }
                imageBytes[(i * 16 + j) * 4 + 3] = (byte) (((pixel >> (3) * 8)) & 255); // alpha
            }
        }
        return ByteBuffer.wrap(imageBytes);
    }

    public ByteBuffer loadIcon(String url)
    {
        try
        {
            BufferedImage bufferedImage = ImageIO.read(new BufferedInputStream(ResourceLoader.getResourceAsStream(url)));

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

            return ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public void init()
    {
        try
        {
            Display.setTitle("USB Missile Launcher Control Panel");
            Display.setDisplayMode(new DisplayMode(854, 480));
            Display.create();
            Display.setVSyncEnabled(true);
            Display.setResizable(true);
        }
        catch (LWJGLException e)
        {
            e.printStackTrace();
            System.exit(0);
        }

        Keyboard.enableRepeatEvents(true);

        this.panel = new GuiPanel(this.theme);
        new CommandForm(this.panel, null);
    }

    public void start()
    {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearDepth(1.0D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());

        while (!Display.isCloseRequested() && !this.shouldTerminate)
        {
            if (Display.wasResized())
            {
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();
                GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadIdentity();
                GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
            }

            this.render();
            
//            USBMissileLauncher.INSTANCE.onUpdate();

            Display.update();
            Display.sync(50);
        }

        Display.destroy();
        AL.destroy();
        System.exit(0);
    }

    public void render()
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

         GL11.glPushMatrix();
         GL11.glDisable(GL11.GL_TEXTURE_2D);
        
         GL11.glBegin(GL11.GL_QUADS);
         new Color(0, 200, 225, 100).bind();
        
         GL11.glVertex3f(0, 0, 0);
         GL11.glVertex3f(Display.getWidth(), 0, 0);
         GL11.glVertex3f(Display.getWidth(), Display.getHeight(), 0);
         GL11.glVertex3f(0, Display.getHeight(), 0);
         GL11.glEnd();
        
         GL11.glColor4f(1, 1, 1, 1);
         GL11.glEnable(GL11.GL_TEXTURE_2D);
         
         this.panel.render();
        
         GL11.glPopMatrix();
    }

    public void terminate()
    {
        this.shouldTerminate = true;
    }

    public Theme theme()
    {
        return theme;
    }
}
