package tw.davy.skinmod.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EnumChatFormatting;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSlot;
import net.minecraft.src.Tessellator;

import org.lwjgl.opengl.GL11;

class GuiSlotServer extends GuiSlot
{
    /** Instance to the GUI this list is on. */
    final GuiSkinServerList parentGui;

    public GuiSlotServer(GuiSkinServerList par1GuiServerList)
    {
        super(par1GuiServerList.getMinecraft(), par1GuiServerList.width, par1GuiServerList.height, 32, par1GuiServerList.height - 64, 36);
        this.parentGui = par1GuiServerList;
    }
    
    protected int getSlotHeight()
    {
    	return this.slotHeight;
    }

    /**
     * Gets the size of the current slot list.
     */
    protected int getSize()
    {
        return GuiSkinServerList.getInternetServerList(this.parentGui).countServers() + 
        		GuiSkinServerList.getListOfLanServers(this.parentGui).size() + 1;
    }

    /**
     * the element in the slot that was clicked, boolean for wether it was double clicked or not
     */
    protected void elementClicked(int par1, boolean par2)
    {
        if (par1 < GuiSkinServerList.getInternetServerList(this.parentGui).countServers() + 
        		GuiSkinServerList.getListOfLanServers(this.parentGui).size())
        {
            int var3 = GuiSkinServerList.getSelectedServer(this.parentGui);
            GuiSkinServerList.getAndSetSelectedServer(this.parentGui, par1);
            ServerData var4 = GuiSkinServerList.getInternetServerList(this.parentGui).countServers() > par1 ? 
            		GuiSkinServerList.getInternetServerList(this.parentGui).getServerData(par1) : null;
            boolean var5 = GuiSkinServerList.getSelectedServer(this.parentGui) >= 0 && 
            		GuiSkinServerList.getSelectedServer(this.parentGui) < this.getSize() && var4 == null;
            boolean var6 = GuiSkinServerList.getSelectedServer(this.parentGui) < 
            		GuiSkinServerList.getInternetServerList(this.parentGui).countServers();
            GuiSkinServerList.getButtonEdit(this.parentGui).enabled = var6;
            GuiSkinServerList.getButtonDelete(this.parentGui).enabled = var6;

            if (var6 && GuiScreen.isShiftKeyDown() && var3 >= 0 && 
            		var3 < GuiSkinServerList.getInternetServerList(this.parentGui).countServers())
            {
            	GuiSkinServerList.getInternetServerList(this.parentGui).swapServers(var3, GuiSkinServerList.getSelectedServer(this.parentGui));
            }
        }
    }

    /**
     * returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int par1)
    {
        return par1 == GuiSkinServerList.getSelectedServer(this.parentGui);
    }

    /**
     * return the height of the content being scrolled
     */
    protected int getContentHeight()
    {
        return this.getSize() * 36;
    }

    protected void drawBackground()
    {
        this.parentGui.drawDefaultBackground();
    }

    protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        if (par1 < GuiSkinServerList.getInternetServerList(this.parentGui).countServers())
        {
            this.func_77247_d(par1, par2, par3, par4, par5Tessellator);
        }
    }

    private void func_77247_d(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        ServerData var6 = GuiSkinServerList.getInternetServerList(this.parentGui).getServerData(par1);

        this.parentGui.drawString(this.parentGui.getFontRenderer(), var6.serverName, par2 + 2, par3 + 1, 16777215);
        this.parentGui.drawString(this.parentGui.getFontRenderer(), var6.serverSkinURI, par2 + 2, par3 + 12, 8421504);
        this.parentGui.drawString(this.parentGui.getFontRenderer(), var6.serverCloakURI, par2 + 2, par3 + 12 + 11, 8421504);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
