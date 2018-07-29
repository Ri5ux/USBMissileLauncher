package net.usbmissilelauncher;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.HidServicesSpecification;
import org.hid4java.ScanMode;
import org.hid4java.event.HidServicesEvent;

public class USBMissileLauncher implements HidServicesListener
{
    private static final int         VENDOR_ID     = 0x2123;
    private static final int         PRODUCT_ID    = 0x1010;
    public static final String       SERIAL_NUMBER = null;
    public static USBMissileLauncher INSTANCE;

    private static HidServices       hidServices;
    private boolean                  isRunning;
    private HidDevice                device;
    private long                     ticks;

    public USBMissileLauncher()
    {
        USBMissileLauncher.INSTANCE = this;
        
        HidServicesSpecification hidServicesSpecification = new HidServicesSpecification();
        hidServicesSpecification.setAutoShutdown(true);
        hidServicesSpecification.setScanInterval(1000);
        hidServicesSpecification.setPauseInterval(5000);
        hidServicesSpecification.setScanMode(ScanMode.SCAN_AT_FIXED_INTERVAL_WITH_PAUSE_AFTER_WRITE);

        hidServices = HidManager.getHidServices(hidServicesSpecification);
        hidServices.addHidServicesListener(USBMissileLauncher.INSTANCE);
        System.out.println("Starting Human Interaction Device service...");
        
        hidServices.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run()
            {
                USBMissileLauncher.INSTANCE.onProgramExit();
                hidServices.shutdown();
                System.out.println("Human Interaction Device service shut down successfully.");
            }
        });

        USBMissileLauncher.INSTANCE.onProgramStart(hidServices);
        
        System.gc();
    }

    @Override
    public void hidDeviceAttached(HidServicesEvent event)
    {
        if (event.getHidDevice().isVidPidSerial(VENDOR_ID, PRODUCT_ID, null))
        {
            this.device = event.getHidDevice();

            if (this.device != null)
            {
                this.onDeviceConnection();
            }
        }
    }

    @Override
    public void hidDeviceDetached(HidServicesEvent event)
    {
        System.err.println("Device Disconnected: " + event);
    }

    @Override
    public void hidFailure(HidServicesEvent event)
    {
        System.err.println("Device Error: " + event);
    }

    public void onProgramStart(HidServices hidServices)
    {
        this.isRunning = true;
        Console.write("Searching for human interaction devices matching vendor id 0x%s and product id 0x%s...", Integer.toHexString(VENDOR_ID), Integer.toHexString(PRODUCT_ID));
        Console.emptyLine();

        for (HidDevice hidDevice : hidServices.getAttachedHidDevices())
        {
            System.out.println(hidDevice);
        }

        Console.emptyLine();

        this.device = hidServices.getHidDevice(VENDOR_ID, PRODUCT_ID, SERIAL_NUMBER);

        if (this.device != null)
        {
            this.onDeviceConnection();
        }
    }

    private void onDeviceConnection()
    {
        Console.write("Connected to %s %s successfully. ", this.device.getManufacturer(), this.device.getProduct());
        Console.writeDeviceDetails(this.device);
        Console.emptyLine();
        this.sendPacket(Packet.LED_ON);
    }

    private boolean ledState = false;

    public void onUpdate()
    {
        ticks++;

        if (ticks % 25 == 0)
        {
            if (ledState)
            {
                sendPacket(Packet.LED_OFF);
            }
            else
            {
                sendPacket(Packet.LED_ON);
            }

            ledState = !ledState;
        }
    }

    public void onProgramExit()
    {
        this.sendPacket(Packet.STOP);
        Interface.instance.terminate();
    }

    private void sendPacket(Packet packet)
    {
        packet.send(this.getDevice());
    }

    public HidDevice getDevice()
    {
        return this.device;
    }

    public boolean isDeviceConnected()
    {
        return this.device != null;
    }

    public boolean isRunning()
    {
        return isRunning;
    }
}
