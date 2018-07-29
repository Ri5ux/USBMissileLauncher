package net.usbmissilelauncher;

import org.hid4java.HidDevice;

public class Console
{
    @SuppressWarnings(value = { "all" })
    public static void write(String in, String...args)
    {
        System.out.println(String.format(in, args));
    }
    
    public static void emptyLine()
    {
        System.out.println();
    }
    
    public static void writeDeviceDetails(HidDevice device)
    {
        write( "Path: " + device.getPath()
            + "\nVendorId: 0x" + Integer.toHexString(device.getVendorId())
            + "\nProductId: 0x" + Integer.toHexString(device.getProductId())
            + "\nSerialNumber: " + device.getSerialNumber()
            + "\nReleaseNumber: 0x" + Integer.toHexString(device.getReleaseNumber())
            + "\nManufacturer: " + device.getManufacturer()
            + "\nProduct: " + device.getProduct()
            + "\nUsagePage: 0x" + Integer.toHexString(device.getUsagePage())
            + "\nUsage: 0x" + Integer.toHexString(device.getUsage())
            + "\nInterfaceNumber: " + device.getInterfaceNumber());
    }
}
