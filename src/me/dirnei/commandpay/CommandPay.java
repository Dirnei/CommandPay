package me.dirnei.commandpay;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.iConomy.iConomy;

public class CommandPay extends JavaPlugin {
	public iConomy iConomy = null;
	Logger log = Logger.getLogger("Minecraft");
	public final CommandPayPlayerListener playerListener = new CommandPayPlayerListener(this);
	
    public void onEnable() {
    	PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Type.PLUGIN_ENABLE, new iConomyServer(this), Priority.Monitor, this);
        pm.registerEvent(Type.PLUGIN_DISABLE, new iConomyServer(this), Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_COMMAND_PREPROCESS, playerListener, Event.Priority.Normal , this);
        log.info("[CommandPay] enabled.");
    }
    
    public void onDisable(){
    	log.info("[CommandPay] disabled.");
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
    	
    	if(cmd.getName().equalsIgnoreCase("cp") && args[0] == "set" && args.length == 3){
			sender.sendMessage(cmd.getName());
			return false;
		}
    	return false;
    }
}
