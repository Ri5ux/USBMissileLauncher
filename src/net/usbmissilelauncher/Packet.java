package net.usbmissilelauncher;

import org.hid4java.HidDevice;

public enum Packet
{
    CMD(new byte[] { 0, 0, 0, 0, 0, 0, 0, 2 }),
    LED_ON(new byte[] { 3, 1, 0, 0, 0, 0, 0, 0 }),
    LED_OFF(new byte[] { 3, 0, 0, 0, 0, 0, 0, 0 }),
    FIRE(new byte[] { 2, 16, 0, 0, 0, 0, 0, 0, 0 }),
    STOP(new byte[] { 2, 32, 0, 0, 0, 0, 0, 0, 0 }),
    STATUS(new byte[] { 1, 0, 0, 0, 0, 0, 0, 0 }),
    UP(new byte[] { 2, 2, 0, 0, 0, 0, 0, 0, 0 }),
    DOWN(new byte[] { 2, 1, 0, 0, 0, 0, 0, 0, 0 }),
    LEFT(new byte[] { 2, 4, 0, 0, 0, 0, 0, 0, 0 }),
    RIGHT(new byte[] { 2, 8, 0, 0, 0, 0, 0, 0, 0 });

    private byte[] buffer;

    private Packet(byte[] buffer)
    {
        this.buffer = buffer;
    }

    public void send(HidDevice hidDevice)
    {
        if (hidDevice == null)
        {
            Console.write("Error: Null HID Device");
            return;
        }
        
        if (!hidDevice.isOpen())
        {
            hidDevice.open();
        }

        int bytesWritten = hidDevice.write(this.buffer, this.buffer.length, (byte) 0x00);

        if (bytesWritten >= 0)
        {
            System.out.println(String.format("[DEVICE.WRITE][%s] %s byte(s) sent.", this.toString(), bytesWritten));
        }
        else
        {
            System.err.println(hidDevice.getLastErrorMessage());
        }

        boolean listen = true;

        while (listen)
        {
            byte data[] = new byte[this.buffer.length];

            bytesWritten = hidDevice.read(data, 500);

            switch (bytesWritten)
            {
                case -1:
                    System.err.println(hidDevice.getLastErrorMessage());
                    break;
                case 0:
                    listen = false;
                    break;
                default:
                    System.out.print("[DEVICE.READ] [");

                    for (byte b : data)
                    {
                        System.out.printf(" %02x", b);
                    }

                    System.out.println("]");
                    break;
            }
        }
    }
}