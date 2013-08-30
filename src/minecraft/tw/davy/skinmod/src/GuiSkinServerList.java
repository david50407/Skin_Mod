package tw.davy.skinmod.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSmallButton;
import net.minecraft.src.GuiYesNo;
import net.minecraft.src.StatCollector;
import net.minecraft.src.StringTranslate;

import org.lwjgl.input.Keyboard;

public class GuiSkinServerList extends GuiScreen
{
    /** Number of outstanding ThreadPollServers threads */
    private static int threadsPending = 0;

    /** Lock object for use with synchronized() */
    private static Object lock = new Object();

    /**
     * A reference to the screen object that created this. Used for navigating between screens.
     */
    private GuiScreen parentScreen;

    /** Slot container for the server list */
    private GuiSlotServer serverSlotContainer;
    private ServerList internetServerList;

    /** Index of the currently selected server */
    private int selectedServer = -1;
    private GuiButton field_96289_p;

    /** The 'Delete' button */
    private GuiButton buttonDelete;

    /** The 'Delete' button was clicked */
    private boolean deleteClicked = false;

    /** The 'Add server' button was clicked */
    private boolean addClicked = false;

    /** The 'Edit' button was clicked */
    private boolean editClicked = false;

    /** The 'Direct Connect' button was clicked */
    private boolean directClicked = false;

    /** This GUI's lag tooltip text or null if no lag icon is being hovered. */
    private String lagTooltip = null;

    /** Instance of ServerData. */
    private ServerData theServerData = null;
    
    /** How many ticks this Gui is already opened */
    private int ticksOpened;
    private boolean field_74024_A;
    private List listofLanServers = Collections.emptyList();

    public GuiSkinServerList(GuiScreen par1GuiScreen)
    {
        this.parentScreen = par1GuiScreen;
    }
    
    public Minecraft getMinecraft()
    {
    	return this.mc;
    }
    
    public FontRenderer getFontRenderer()
    {
    	return this.fontRenderer;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();

        if (!this.field_74024_A)
        {
            this.field_74024_A = true;
            this.internetServerList = new ServerList();
            this.internetServerList.loadServerList();

            this.serverSlotContainer = new GuiSlotServer(this);
        }
        else
        {
            this.serverSlotContainer.func_77207_a(this.width, this.height, 32, this.height - 64);
        }

        this.initGuiControls();
    }

    /**
     * Populate the GuiScreen controlList
     */
    public void initGuiControls()
    {
        net.minecraft.src.StringTranslate var1 = StringTranslate.getInstance();
        this.buttonList.add(this.field_96289_p = new GuiSmallButton(7, this.width / 2 - 154, this.height - 52, var1.translateKey("selectServer.edit")));
        this.buttonList.add(this.buttonDelete = new GuiSmallButton(2, this.width / 2 + 4, this.height - 52, var1.translateKey("selectServer.delete")));
        this.buttonList.add(new GuiSmallButton(3, this.width / 2 - 154, this.height - 28, var1.translateKey("selectServer.add")));
        this.buttonList.add(new GuiSmallButton(0, this.width / 2 + 4, this.height - 28, var1.translateKey("gui.done")));
        boolean var2 = this.selectedServer >= 0 && this.selectedServer < this.serverSlotContainer.getSize();
        this.field_96289_p.enabled = var2;
        this.buttonDelete.enabled = var2;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ++this.ticksOpened;
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
    	tw.davy.skinmod.src.StringTranslate st = tw.davy.skinmod.src.StringTranslate.getInstance();
        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id == 2)
            {
                String var2 = this.internetServerList.getServerData(this.selectedServer).serverName;

                if (var2 != null)
                {
                    this.deleteClicked = true;
                    StringTranslate var3 = StringTranslate.getInstance();
                    String var4 = var3.translateKey("selectServer.deleteQuestion");
                    String var5 = "\'" + var2 + "\' " + var3.translateKey("selectServer.deleteWarning");
                    String var6 = var3.translateKey("selectServer.deleteButton");
                    String var7 = var3.translateKey("gui.cancel");
                    GuiYesNo var8 = new GuiYesNo(this, var4, var5, var6, var7, this.selectedServer);
                    this.mc.displayGuiScreen(var8);
                }
            }
            else if (par1GuiButton.id == 3)
            {
                this.addClicked = true;
                this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.theServerData = new ServerData(st.translateKey("addServer.defaultName"), "", "")));
            }
            else if (par1GuiButton.id == 7)
            {
                this.editClicked = true;
                ServerData var9 = this.internetServerList.getServerData(this.selectedServer);
                this.theServerData = new ServerData(var9.serverName, var9.serverSkinURI, var9.serverCloakURI);
                this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.theServerData));
            }
            else if (par1GuiButton.id == 0)
            {
            	this.internetServerList.saveServerList();
                this.mc.displayGuiScreen(this.parentScreen);
            }
            else
            {
                this.serverSlotContainer.actionPerformed(par1GuiButton);
            }
        }
    }

    public void confirmClicked(boolean par1, int par2)
    {
        if (this.deleteClicked)
        {
            this.deleteClicked = false;

            if (par1)
            {
                this.internetServerList.removeServerData(par2);
                this.internetServerList.saveServerList();
                this.selectedServer = -1;
            }

            this.mc.displayGuiScreen(this);
        }
        else if (this.addClicked)
        {
            this.addClicked = false;

            if (par1)
            {
                this.internetServerList.addServerData(this.theServerData);
                this.internetServerList.saveServerList();
                this.selectedServer = -1;
            }

            this.mc.displayGuiScreen(this);
        }
        else if (this.editClicked)
        {
            this.editClicked = false;

            if (par1)
            {
                ServerData var3 = this.internetServerList.getServerData(this.selectedServer);
                var3.serverName = this.theServerData.serverName;
                var3.serverSkinURI = this.theServerData.serverSkinURI;
                var3.serverCloakURI = this.theServerData.serverCloakURI;
                this.internetServerList.saveServerList();
            }

            this.mc.displayGuiScreen(this);
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        int var3 = this.selectedServer;
        if (isShiftKeyDown() && par2 == 200)
        {
            if (var3 > 0 && var3 < this.internetServerList.countServers())
            {
                this.internetServerList.swapServers(var3, var3 - 1);
                --this.selectedServer;

                if (var3 < this.internetServerList.countServers() - 1)
                {
                    this.serverSlotContainer.func_77208_b(-this.serverSlotContainer.getSlotHeight());
                }
            }
        }
        else if (isShiftKeyDown() && par2 == 208)
        {
            if (var3 < this.internetServerList.countServers() - 1)
            {
                this.internetServerList.swapServers(var3, var3 + 1);
                ++this.selectedServer;

                if (var3 > 0)
                {
                    this.serverSlotContainer.func_77208_b(this.serverSlotContainer.getSlotHeight());
                }
            }
        }
        else if (par1 == 13)
        {
            this.actionPerformed((GuiButton)this.buttonList.get(2));
        }
        else
        {
            super.keyTyped(par1, par2);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.lagTooltip = null;
        tw.davy.skinmod.src.StringTranslate var4 = tw.davy.skinmod.src.StringTranslate.getInstance();
        this.drawDefaultBackground();
        this.serverSlotContainer.drawScreen(par1, par2, par3);
        this.drawCenteredString(this.fontRenderer, var4.translateKey("serverlist.title"), this.width / 2, 20, 16777215);
        super.drawScreen(par1, par2, par3);

        if (this.lagTooltip != null)
        {
            this.func_74007_a(this.lagTooltip, par1, par2);
        }
    }

    /**
     * Join server by slot index
     */
    protected void func_74007_a(String par1Str, int par2, int par3)
    {
        if (par1Str != null)
        {
            int var4 = par2 + 12;
            int var5 = par3 - 12;
            int var6 = this.fontRenderer.getStringWidth(par1Str);
            this.drawGradientRect(var4 - 3, var5 - 3, var4 + var6 + 3, var5 + 8 + 3, -1073741824, -1073741824);
            this.fontRenderer.drawStringWithShadow(par1Str, var4, var5, -1);
        }
    }

    static ServerList getInternetServerList(GuiSkinServerList par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.internetServerList;
    }

    static List getListOfLanServers(GuiSkinServerList par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.listofLanServers;
    }

    static int getSelectedServer(GuiSkinServerList par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.selectedServer;
    }

    static int getAndSetSelectedServer(GuiSkinServerList par0GuiMultiplayer, int par1)
    {
        return par0GuiMultiplayer.selectedServer = par1;
    }

    /**
     * Return buttonEdit GuiButton
     */
    static GuiButton getButtonEdit(GuiSkinServerList par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.field_96289_p;
    }

    /**
     * Return buttonDelete GuiButton
     */
    static GuiButton getButtonDelete(GuiSkinServerList par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.buttonDelete;
    }

    static int getTicksOpened(GuiSkinServerList par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.ticksOpened;
    }

    /**
     * Returns the lock object for use with synchronized()
     */
    static Object getLock()
    {
        return lock;
    }

    static int getThreadsPending()
    {
        return threadsPending;
    }

    static int increaseThreadsPending()
    {
        return threadsPending++;
    }

    static int decreaseThreadsPending()
    {
        return threadsPending--;
    }
}
