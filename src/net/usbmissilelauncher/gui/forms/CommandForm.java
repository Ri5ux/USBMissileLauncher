package net.usbmissilelauncher.gui.forms;

import java.awt.Font;

import javax.swing.JLabel;

import org.asx.glx.gui.GuiPanel;
import org.asx.glx.gui.elements.GuiElement;
import org.asx.glx.gui.elements.GuiRectangle;
import org.asx.glx.gui.elements.GuiText;
import org.asx.glx.gui.elements.GuiTextfield;
import org.asx.glx.gui.forms.GuiForm;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import net.usbmissilelauncher.Interface;
import net.usbmissilelauncher.Packet;
import net.usbmissilelauncher.USBMissileLauncher;

public class CommandForm extends GuiForm
{
//    private GuiRectangle background;
    private GuiTextfield commandField;
    private GuiText      commandLabel, commandInput;

    @SuppressWarnings("all")
    public CommandForm(GuiPanel panel, GuiForm parentForm)
    {
        super(panel, parentForm);

//        this.background = new GuiRectangle(this, 0, 160, Display.getWidth(), 254);
//        this.background.setHasBorder(true);
//        this.background.setBorderColor(this.panel.theme().borderColor);
//        this.background.setColor(this.panel.theme().backgroundColor, this.panel.theme().backgroundColor);
//        this.add(this.background);

        Font font = new Font("System", Font.PLAIN, 14);
        Font secondaryFont = font;

        this.commandLabel = new GuiText(this, font, "Command>");
        this.commandLabel.setColor(this.panel.theme().alternativeTextColor, this.panel.theme().alternativeTextColor);
        this.commandInput = new GuiText(this, secondaryFont, "");
        this.commandField = new GuiTextfield(this, (Display.getWidth() - 210) / 2, 60, 210, 30, this.commandInput, false);
        this.commandField.setColor(this.panel.theme().textFieldColor, this.panel.theme().textFieldHoveredColor);
        this.commandField.setBorderColor(this.panel.theme().borderColor);
        this.add(this.commandField);

        GuiTextfield.activeTextfield = this.commandField;
    }

    @Override
    public void render()
    {
        super.render();
        int padding = 10;

//        this.background.setSize(500, 250);
//        this.background.setPosition(Display.getWidth() / 2 - this.background.getWidth() / 2, Display.getHeight() / 2 - this.background.getHeight() / 2);

        this.commandField.setSize(Display.getWidth() - 100, 25);
        this.commandField.setColor(Color.black, Color.darkGray);
        this.commandField.setPosition(85, Display.getHeight() - 40);
        this.commandLabel.render(this.commandField.getX() - this.commandLabel.getWidth() - padding, this.commandField.getY() + this.commandField.getHeight() / 8);
        this.commandField.setBorderColor(Color.gray);
    }

    @Override
    public void onElementClick(GuiElement element)
    {
        super.onElementClick(element);
        System.out.println(element);
    }

    @Override
    public void onKey(int key, char character)
    {
        if (key == Keyboard.KEY_RETURN)
        {
            if (GuiTextfield.activeTextfield == this.commandField)
            {
                for (Packet packet : Packet.values())
                {
                    if (packet.toString().equalsIgnoreCase(this.commandField.getText()))
                    {
                        packet.send(USBMissileLauncher.INSTANCE.getDevice());
                        this.commandField.clearText();
                    }
                }
            }
            
            if (this.onScreen)
            {
                this.onScreen = false;
            }
        }
        else if (key == Keyboard.KEY_TAB)
        {
            GuiTextfield.activeTextfield = this.commandField;
        }
    }
}
