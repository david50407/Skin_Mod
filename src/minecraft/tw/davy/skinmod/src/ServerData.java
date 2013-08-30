package tw.davy.skinmod.src;

import net.minecraft.src.NBTTagCompound;

public class ServerData
{
    public String serverName;
    public String serverSkinURI;
    public String serverCloakURI;

    public ServerData(String par1Str, String par2Str, String par3Str)
    {
        this.serverName = par1Str;
        this.serverSkinURI = par2Str;
        this.serverCloakURI = par3Str;
    }

    /**
     * Returns an NBTTagCompound with the server's name, IP and maybe acceptTextures.
     */
    public NBTTagCompound getNBTCompound()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        var1.setString("name", this.serverName);
        var1.setString("skinUri", this.serverSkinURI);
        var1.setString("cloakUri", this.serverCloakURI);

        return var1;
    }
    
    /**
     * Takes an NBTTagCompound with 'name', 'skinUri' and 'cloakUri' keys, returns a ServerData instance.
     */
    public static ServerData getServerDataFromNBTCompound(NBTTagCompound par0NBTTagCompound)
    {
        ServerData var1 = new ServerData(par0NBTTagCompound.getString("name"), 
        		par0NBTTagCompound.getString("skinUri"), par0NBTTagCompound.getString("cloakUri"));

        return var1;
    }
}
