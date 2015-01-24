package com.mcjtf.Prefix;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	HashMap<String, List<String>> prefixmap;
	HashMap<String, List<String>> suffixmap;
	HashMap<String, List<String>> gprefix;
	HashMap<String, List<String>> gsuffix;
	public static Chat chat = null;
	public Permission permission = null;
	private String displaynamne;
	  public void onEnable()
	  {
	    saveDefaultConfig();
	    setupChat();
	    setupPermissions();
	    getCommand("prm").setExecutor(new Commands(this));
	    getServer().getPluginManager().registerEvents(this, this);
	    LoadConfig();
	    this.getLogger().info("插件加载完毕");
	  }
	  //插件加载
	  public boolean setupChat()
	  {
	    RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(Chat.class);
	    if (chatProvider != null) {
	      chat = ((Chat)chatProvider.getProvider());
	    }
	    return chat != null;
	  }
	  public boolean setupPermissions()
	  {
	    RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
	    if (permissionProvider != null) {
	      permission = (Permission)permissionProvider.getProvider();
	    }
	    return permission != null;
	  }
	  public void LoadConfig(){
		 File file=new File("./plugins/PrefixManager/config.yml");
		 FileConfiguration config=YamlConfiguration.loadConfiguration(file);
		 int size = config.getConfigurationSection("player").getKeys(false).size()*2;
		 prefixmap = new HashMap<String, List<String>>(size);
		 suffixmap = new HashMap<String, List<String>>(size);
		 gprefix = new HashMap<String, List<String>>(size);
		 gsuffix = new HashMap<String, List<String>>(size);
		 for(String name : config.getConfigurationSection("player").getKeys(false)){
			  prefixmap.put(name, this.getConfig().getStringList("player."+name+".prefix"));
			  suffixmap.put(name, this.getConfig().getStringList("player."+name+".suffix"));
		 }
		 for(String name : config.getConfigurationSection("group").getKeys(false)){
			 gprefix.put(name, this.getConfig().getStringList("group."+name+".prefix"));
			 gsuffix.put(name, this.getConfig().getStringList("group."+name+".suffix"));
		 }
		 if(this.getConfig().getString("format")==null){
			 this.getConfig().set("format", "<[DISPLAYNAME]> [MESSAGE]");
			 this.saveConfig();
		 }
		 this.getLogger().info("玩家数据读取完毕");
	  }
	  //读取配置文件
		@EventHandler(ignoreCancelled=true)
		public void onPlayerJoinEvent (PlayerJoinEvent event) {
			String name = event.getPlayer().getName();
			if(!(this.getConfig().contains("player."+name+".In-use.prefix"))){
				this.getConfig().set("player."+name+".In-use.prefix", 0);
			}
			if(!(this.getConfig().contains("player."+name+"In-use.suffix"))){
				this.getConfig().set("player."+name+".In-use.suffix", 0);
				this.saveConfig();
			}
			if(prefixmap.containsKey(name)){
				return;
			}else{
				List<String> p = new ArrayList<String>();
				List<String> s = new ArrayList<String>();
				p.add(Main.chat.getGroupPrefix(event.getPlayer().getWorld(), this.permission.getPrimaryGroup(event.getPlayer())));
				s.add(Main.chat.getGroupSuffix(event.getPlayer().getWorld(), this.permission.getPrimaryGroup(event.getPlayer())));
				this.prefixmap.put(name, p);
				this.suffixmap.put(name, s);
			}		
		}
		//玩家进入判断数据是否存在
		@EventHandler(ignoreCancelled=true)
		public void onPlayerQuitEvent (PlayerQuitEvent event) {
			String name = event.getPlayer().getName();
			this.getConfig().set("player."+name+".prefix",this.prefixmap.get(name));
			this.getConfig().set("player."+name+".suffix",this.suffixmap.get(name));
			saveConfig();
		}
		//玩家退出保存数据
		@EventHandler
		public void onPluginDisableEvent (PluginDisableEvent event) {
			if(event.getPlugin()==this.getServer().getPluginManager().getPlugin("PrefixManager")){
				this.save();
			}	  
		}
		public void save(){
		      this.getLogger().info("保存玩家数据……");
			  Iterator<String> lt = this.prefixmap.keySet().iterator();
			  while(lt.hasNext()){
				  String name = lt.next();
				  this.getConfig().set("player."+name+".prefix",this.prefixmap.get(name));
				  this.getConfig().set("player."+name+".suffix",this.suffixmap.get(name));
				}
			  Iterator<String> gt = this.gprefix.keySet().iterator();
			  while(gt.hasNext()){
				  String name = gt.next();
				  this.getConfig().set("group."+name+".prefix",this.gprefix.get(name));
				  this.getConfig().set("group."+name+".suffix",this.gsuffix.get(name));
			  }
			  saveConfig();
			  this.getLogger().info("数据存储完毕");	
		}

		public void check(Player player){
			String p = Main.chat.getPlayerPrefix(player);
			String s = Main.chat.getPlayerSuffix(player);
			String name = player.getName();
			if(p!=null){
				if(!(this.prefixmap.get(name).contains(p))){
					this.prefixmap.get(name).add(p);
				}
			}
			if(s!=null){
				if(!(this.suffixmap.get(name).contains(s))){
					this.suffixmap.get(name).add(p);
				}
			}
		}
		//检查玩家当前称号
		public void del(Player player,String type,String chat){
			if(type=="prefix"){
				if(Main.chat.getPlayerPrefix(player).equalsIgnoreCase(chat)){
					Main.chat.setPlayerPrefix(player, Main.chat.getGroupPrefix(player.getWorld(), this.permission.getPrimaryGroup(player)));
				}
			}else{
				if(Main.chat.getPlayerSuffix(player).equalsIgnoreCase(chat)){
					Main.chat.setPlayerSuffix(player, Main.chat.getGroupSuffix(player.getWorld(), this.permission.getPrimaryGroup(player)));
				}
			}
		}
		@EventHandler(priority=EventPriority.HIGHEST)
		public void onPlayerChat(AsyncPlayerChatEvent event)
		{
			String format = this.getConfig().getString("format");
			String name = event.getPlayer().getName();
			if(!prefixmap.get(name).isEmpty()){
				this.displaynamne = prefixmap.get(name).get(getConfig().getInt("player."+name+".In-use.prefix"))+name;
			}
			if(!suffixmap.get(name).isEmpty()){
				this.displaynamne = this.displaynamne+suffixmap.get(name).get(getConfig().getInt("player."+name+".In-use.suffix"));
			}
			format= format.replaceAll("\\[DISPLAYNAME\\]", displaynamne).replaceAll("\\[WORLDNAME\\]",event.getPlayer().getWorld().getName()).replaceAll("\\[GROUP\\]", this.permission.getPrimaryGroup(event.getPlayer())).replaceAll("\\[MESSAGE\\]", event.getMessage());
			format= format.replace('&', '§');
			event.setFormat(format);
	    }

}