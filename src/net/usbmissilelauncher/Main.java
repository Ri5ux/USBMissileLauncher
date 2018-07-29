package net.usbmissilelauncher;

import java.io.File;

import org.hid4java.HidException;

public class Main
{
    public static void main(String[] args) throws HidException
    {
        String libraryPath = getLibraryPath(args);
        new Main().start(libraryPath);
    }
    
    public void start(String libraryPath)
    {
        System.setProperty("org.lwjgl.librarypath", libraryPath);
        new Interface();
    }
    
    private static String getLibraryPath(String[] args)
    {
        if ((args.length % 2) != 0)
        {
            throw new RuntimeException("Invalid amount of arguments. Must be divisble by 2.");
        }

        String librarypath = null;

        for (int i = 0; i < args.length; i++)
        {
            if (args[i].equalsIgnoreCase("--natives"))
            {
                librarypath = args[i + 1];
            }
        }

        if (librarypath == null)
        {
            throw new RuntimeException("Missing argument: --natives");
        }

        File libraryFile = new File(librarypath);
        librarypath = libraryFile.getAbsolutePath();
        
        return librarypath;
    }
}
