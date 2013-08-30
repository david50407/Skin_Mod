package tw.davy.skinmod.src;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.src.StringUtils;

public class HelperSkinServer {
	private static HashMap skinMaps = new HashMap();
	private static HashMap cloakMaps = new HashMap();
	
	public static String getSkinURI(String username)
	{
		if (skinMaps.containsKey(username))
			return (String)skinMaps.get(username);
		String rtn = "";
		ServerList sl = new ServerList();
		for (int i = 0; i < sl.countServers(); ++i)
		{
			ServerData sd = sl.getServerData(i);
			HttpURLConnection var1 = null;

	        try
	        {
	            URL var2 = new URL(sd.serverSkinURI + "/" + username + ".png");
	            var1 = (HttpURLConnection)var2.openConnection();
	            var1.setDoInput(true);
	            var1.setDoOutput(false);
	            var1.connect();

	            if (var1.getResponseCode() == HttpURLConnection.HTTP_OK)
	            {
	            	rtn = sd.serverSkinURI + "/" + username + ".png";
	                break;
	            }
	        }
	        catch (MalformedURLException var6)
	        {
	        	// do nothing
	        }
	        catch (Exception var6)
	        {
	            var6.printStackTrace();
	        }
	        finally
	        {
	            var1.disconnect();
	        }
		}
		if (rtn == "")
			rtn = "http://skins.minecraft.net/MinecraftSkins/" + username + ".png";
		skinMaps.put(username, rtn);
		return rtn;
	}
	public static String getCloakURI(String username)
	{
		if (cloakMaps.containsKey(username))
			return (String)cloakMaps.get(username);
		String rtn = "";
		ServerList sl = new ServerList();
		for (int i = 0; i < sl.countServers(); ++i)
		{
			ServerData sd = sl.getServerData(i);
			if (sd.serverCloakURI == null || sd.serverCloakURI.equals(""))
				continue;
			HttpURLConnection var1 = null;

	        try
	        {
	            URL var2 = new URL(sd.serverCloakURI + "/" + username + ".png");
	            var1 = (HttpURLConnection)var2.openConnection();
	            var1.setDoInput(true);
	            var1.setDoOutput(false);
	            var1.connect();

	            if (var1.getResponseCode() == HttpURLConnection.HTTP_OK)
	            {
	            	rtn = sd.serverCloakURI + "/" + username + ".png";
	                break;
	            }
	        }
	        catch (MalformedURLException var6)
	        {
	        	// do nothing
	        }
	        catch (Exception var6)
	        {
	            var6.printStackTrace();
	        }
	        finally
	        {
	            var1.disconnect();
	        }
		}
		if (rtn == "")
			rtn = "http://skins.minecraft.net/MinecraftCloaks/" + username + ".png";
		cloakMaps.put(username, rtn);
		return rtn;
	}
}
