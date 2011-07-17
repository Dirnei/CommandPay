package me.dirnei.commandpay;

import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.util.config.Configuration;

import com.iConomy.system.Holdings;

public class CommandPayPlayerListener extends PlayerListener {
public final CommandPay plugin;
	
	public CommandPayPlayerListener(CommandPay instance){
		this.plugin = instance;
	}
	
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
		
		if( plugin.permissionHandler.has(event.getPlayer(), "commandpay.free")){
			event.getPlayer().sendMessage(ChatColor.AQUA + "[CommandPay]" + ChatColor.WHITE + plugin.getConfiguration().getProperty("messages.free").toString());
			return;
		}
		
		String befehl = "";
		Configuration conf = plugin.getConfiguration();
		conf.load();
		befehl = conf.getProperty("commands.list").toString();
		if (conf.getProperty("enabled").toString() == "true"){
			if(befehl.contains(event.getMessage())){
				
				befehl = befehl.substring(befehl.indexOf(event.getMessage()));
				try{
					befehl = befehl.substring(0, befehl.indexOf(','));
				}
				catch(Exception e){
					befehl = befehl.substring(0, befehl.indexOf(']'));
				}
				
				befehl = befehl.substring(befehl.indexOf('|')+1).trim();
				
				Holdings balance = com.iConomy.iConomy.getAccount(event.getPlayer().getName()).getHoldings();
				balance.subtract(Integer.valueOf(befehl));
				if(conf.getProperty("message").toString() == "true"){
					Double geld = Double.valueOf(befehl);
					
					String out = com.iConomy.iConomy.format(geld);
					event.getPlayer().sendMessage(ChatColor.AQUA+ "[CommandPay]" + ChatColor.WHITE + conf.getProperty("messages.pay").toString().replace("%money", out));
				}
			}
		}	
	}
}
