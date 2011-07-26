package me.dirnei.commandpay;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
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
		//get the command-list
				String commandList = "";
				Configuration conf = plugin.getConfiguration();
				conf.load();
				commandList = conf.getProperty("commands.list").toString();
				
				
		//command for free if you have the permission
		if( plugin.permissionHandler.has(event.getPlayer(), "commandpay.free") && conf.getProperty("message.free").toString() == "true" ){
			event.getPlayer().sendMessage(ChatColor.AQUA + "[CommandPay] " + ChatColor.WHITE + plugin.getConfiguration().getProperty("messages.free").toString());
			return;
		}else if(plugin.permissionHandler.has(event.getPlayer(), "commandpay.free") && conf.getProperty("message.free").toString() == "false"){
			return;
		}
		
		//if plugin enabled
		if (conf.getProperty("enabled").toString() == "true"){
			Player player = event.getPlayer();
			
			World world = player.getWorld();
			
			player.sendMessage("Du bist in: " + world.getName());
			//get argument from command
			String after = event.getMessage();
			int argsCount = event.getMessage().length() - after.length();
			String commandArgs[] = new String[argsCount+1];
			
			commandArgs = after.split(" ");
			
			argsCount = commandArgs.length;
			
			if(commandList.contains(commandArgs[0])){
				
				//delete []
				commandList = commandList.replace("[", "");
				commandList = commandList.replace("]", "");
				
				//get comment args
				String command = commandList.substring(commandList.indexOf(commandArgs[0]));
				
				command = command.substring(0, command.indexOf('|'));
				command = command.substring(command.indexOf("(")+1,command.indexOf(")"));
				String samount = "";
				
				 samount = commandList.substring(commandList.indexOf(commandArgs[0]));
				samount = samount.substring(samount.indexOf('|'));
				try{
				samount = samount.substring(0, samount.indexOf(','));
				}catch(Exception e){
					
				}
				samount = samount.replace("|", "").trim();
				double amount = Double.valueOf(samount);
				
				
				
				if(commandArgs.length > 1 ){
					if(command.contains(commandArgs[1]) || command.contains("*")){
						if(payCommand(player, amount) == false){
							event.setCancelled(true);
						}
					}else{
					}
				}else if(command.contains("*")){
					if(payCommand(player, amount) == false){
						event.setCancelled(true);
					}
				}else{
					if(conf.getProperty("message.free").toString() == "true"){
						player.sendMessage(ChatColor.AQUA + "[CommandPay] " + ChatColor.WHITE + conf.getProperty("messages.free").toString());
					}
				}
				
			}
		}	
	}
	
	public boolean payCommand(Player player, double amount){
		Configuration conf = plugin.getConfiguration();
		conf.load();
		//get holdings
		Holdings balance = com.iConomy.iConomy.getAccount(player.getName()).getHoldings();
		//if enough money
		if(balance.balance() > amount){
			
			//pay
			balance.subtract(amount);
			if(conf.getProperty("message.pay").toString() == "true"){
				
				String out = com.iConomy.iConomy.format(amount);
				player.sendMessage(ChatColor.AQUA+ "[CommandPay] " + ChatColor.WHITE + conf.getProperty("messages.pay").toString().replace("%money", out));
			}
		}else{
			//not enough / cancel
			if(conf.getProperty("message.notenough").toString() == "true"){
				player.sendMessage(ChatColor.AQUA + "[CommandPay] " + ChatColor.WHITE + conf.getProperty("messages.notenough").toString());
				return false;
			}
		}
		
		return true;
	}
}
