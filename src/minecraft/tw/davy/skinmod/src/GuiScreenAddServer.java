package tw.davy.skinmod.src;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;

import org.lwjgl.input.Keyboard;

public class GuiScreenAddServer extends GuiScreen
{
    /** This GUI's parent GUI. */
    private GuiScreen parentGui;
    private GuiTextField serverName;
    private GuiTextField serverSkinURI;
    private GuiTextField serverCloakURI;

    /** ServerData to be modified by this GUI */
    private ServerData newServerData;

    public GuiScreenAddServer(GuiScreen par1GuiScreen, ServerData par2ServerData)
    {
        this.parentGui = par1GuiScreen;
        this.newServerData = par2ServerData;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.serverName.updateCursorCounter();
        this.serverSkinURI.updateCursorCounter();
        this.serverCloakURI.updateCursorCounter();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        net.minecraft.src.StringTranslate var1 = net.minecraft.src.StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, var1.translateKey("addServer.add")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
        this.serverName = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 66, 200, 20);
        this.serverName.setFocused(true);
        this.serverName.setText(this.newServerData.serverName);
        this.serverSkinURI = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 106, 200, 20);
        this.serverSkinURI.setMaxStringLength(128);
        this.serverSkinURI.setText(this.newServerData.serverSkinURI);
        this.serverCloakURI = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 146, 200, 20);
        this.serverCloakURI.setMaxStringLength(128);
        this.serverCloakURI.setText(this.newServerData.serverCloakURI);
        ((GuiButton)this.buttonList.get(0)).enabled = this.serverSkinURI.getText().length() > 0 && 
        		this.serverSkinURI.getText().split(":").length > 0 && this.serverName.getText().length() > 0;
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id == 1)
            {
                this.parentGui.confirmClicked(false, 0);
            }
            else if (par1GuiButton.id == 0)
            {
                this.newServerData.serverName = this.serverName.getText();
                this.newServerData.serverSkinURI = this.serverSkinURI.getText();
                this.newServerData.serverCloakURI = this.serverCloakURI.getText();
                this.parentGui.confirmClicked(true, 0);
            }
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        this.serverName.textboxKeyTyped(par1, par2);
        this.serverSkinURI.textboxKeyTyped(par1, par2);
        this.serverCloakURI.textboxKeyTyped(par1, par2);

        if (par1 == 9)
        {
            if (this.serverName.isFocused())
            {
                this.serverName.setFocused(false);
                this.serverSkinURI.setFocused(true);
            }
            else if (this.serverSkinURI.isFocused())
            {
                this.serverSkinURI.setFocused(false);
                this.serverCloakURI.setFocused(true);
            }
            else
            {
                this.serverCloakURI.setFocused(false);
                this.serverName.setFocused(true);
            }
        }

        if (par1 == 13)
        {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }

        ((GuiButton)this.buttonList.get(0)).enabled = this.serverSkinURI.getText().length() > 0 && 
        		this.serverSkinURI.getText().split(":").length > 0 && this.serverName.getText().length() > 0 &&
        		(this.serverCloakURI.getText().length() == 0 || this.serverCloakURI.getText().split(":").length > 0);
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        this.serverName.mouseClicked(par1, par2, par3);
        this.serverSkinURI.mouseClicked(par1, par2, par3);
        this.serverCloakURI.mouseClicked(par1, par2, par3);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        StringTranslate var4 = StringTranslate.getInstance();
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, var4.translateKey("addServer.title"), this.width / 2, 17, 16777215);
        this.drawString(this.fontRenderer, var4.translateKey("addServer.serverName"), this.width / 2 - 100, 53, 10526880);
        this.drawString(this.fontRenderer, var4.translateKey("addServer.serverSkin"), this.width / 2 - 100, 94, 10526880);
        this.drawString(this.fontRenderer, var4.translateKey("addServer.serverCloak"), this.width / 2 - 100, 135, 10526880);
        this.serverName.drawTextBox();
        this.serverSkinURI.drawTextBox();
        this.serverCloakURI.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }
}
