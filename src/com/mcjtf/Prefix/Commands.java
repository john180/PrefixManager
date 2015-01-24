package com.mcjtf.Prefix;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor{
		  private Main plugin;
		private String display;
		  public Commands(Main plugin)
		  {
		    this.plugin = plugin;
		  }
		  @SuppressWarnings("deprecation")
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
		  {
			if (cmd.getName().equalsIgnoreCase("prm"))
			{
			String name =sender.getName();
			if(args.length==0){
				sender.sendMessage(ChatColor.RED+"----------["+ChatColor.YELLOW+"PrefixManager指令说明"+ChatColor.RED+"]----------");
		    	sender.sendMessage(ChatColor.GREEN + "/prm add <玩家ID> <prefix/suffix> <称号>");
		    	sender.sendMessage(ChatColor.BLUE+"  -给指定玩家添加前缀/后缀");
		    	sender.sendMessage(ChatColor.GREEN + "/prm list <prefix/suffix>");
		    	sender.sendMessage(ChatColor.BLUE+"  -显示你拥有的前缀/后缀");
		    	sender.sendMessage(ChatColor.GREEN + "/prm set <prefix/suffix> <称号序号>");
		    	sender.sendMessage(ChatColor.BLUE+"  -设置你的前缀/后缀");
		    	sender.sendMessage(ChatColor.RED+"-------------------------------------------");
		        return true;
			}
			  if(args[0].equalsIgnoreCase("list")){
				  if(!(sender instanceof Player)){
					  sender.sendMessage("控制台无法使用此指令");
					  return true;
				  }
				//禁用控制台
				  Player p = (Player) sender;
					  if((args.length==2)&&((args[1].equalsIgnoreCase("prefix"))||(args[1].equalsIgnoreCase("p"))||(args[1].equalsIgnoreCase("s"))||(args[1].equalsIgnoreCase("suffix")))){
						  if(!p.hasPermission("prefixmanager.list")){
							  sender.sendMessage(ChatColor.RED+"你没有权限执行此指令");
							  return true;
						  }
						  //检测权限
						  if((args[1].equalsIgnoreCase("prefix"))||(args[1].equalsIgnoreCase("p"))){
							  if(this.plugin.prefixmap.containsKey(sender.getName())){
								  sender.sendMessage(ChatColor.DARK_GREEN+"你拥有以下前缀称号:");
								  sender.sendMessage(ChatColor.RED+"----------["+ChatColor.YELLOW+"你拥有的前缀称号"+ChatColor.RED+"]----------");
								  this.getprefix((Player) sender);
								  sender.sendMessage(ChatColor.RED+"---------------------------------------");
								  return true;
								  //输出前缀
							  }else{
								  sender.sendMessage(ChatColor.RED+"你没有任何前缀称号");
								  return true;
							  }
							  //判断玩家数据是否存在
						  }else{
							  if(this.plugin.suffixmap.containsKey(sender.getName())){
								  sender.sendMessage(ChatColor.RED+"----------["+ChatColor.YELLOW+"你拥有的后缀称号"+ChatColor.RED+"]----------");
								  this.getsuffix((Player) sender);
								  sender.sendMessage(ChatColor.RED+"--------------------------------------");
								  return true;
									   //输出后缀
							  }else{
								  sender.sendMessage(ChatColor.RED+"你没有任何后缀称号");
								  return true;
							  }
							//判断玩家数据是否存在
						  }
					  }
					  //查看自己前缀/后缀权限
					  if(args.length==3){
						  if(sender.hasPermission("prefixmanager.list.other")){
							  if((args[1].equalsIgnoreCase("prefix"))||(args[1].equalsIgnoreCase("p"))||(args[1].equalsIgnoreCase("s"))||(args[1].equalsIgnoreCase("suffix"))){
								  if((args[1].equalsIgnoreCase("prefix"))||(args[1].equalsIgnoreCase("p"))){
									  if(this.plugin.prefixmap.containsKey(args[2])){
										  sender.sendMessage(ChatColor.DARK_BLUE+args[2]+ChatColor.DARK_GREEN+"拥有以下前缀");
										  sender.sendMessage(ChatColor.YELLOW+"--------------------");
										  Iterator<String> i = this.plugin.prefixmap.get(args[2]).iterator();
								          int n = 0;
										  while(i.hasNext())
										  {
										     sender.sendMessage(ChatColor.YELLOW+String.valueOf(n)+":"+changeformat(i.next()));
											 n++;
										  }
										  sender.sendMessage(ChatColor.YELLOW+"--------------------");
										  return true;
										  //获取前缀
									  }else{
										  sender.sendMessage(ChatColor.RED+"玩家前缀数据不存在");
										  return true;
									  }
								  }else{
									  if(this.plugin.suffixmap.containsKey(args[2])){
										  sender.sendMessage(ChatColor.DARK_BLUE+args[2]+ChatColor.DARK_GREEN+"拥有以下后缀");
										  sender.sendMessage(ChatColor.YELLOW+"--------------------");
										  Iterator<String> i = this.plugin.suffixmap.get(args[2]).iterator();
								          int n = 0;
										  while(i.hasNext())
										  {
										     sender.sendMessage(ChatColor.YELLOW+String.valueOf(n)+":"+changeformat(i.next()));
											 n++;
										  }
										  sender.sendMessage(ChatColor.YELLOW+"--------------------");
										  return true;
									  }else{
										  sender.sendMessage(ChatColor.RED+"玩家后缀数据不存在");
										  return true;
									  }
									  //获取后缀
								  }
							  }else{
								  sender.sendMessage(ChatColor.RED+"参数错误");
								  sender.sendMessage(ChatColor.DARK_GREEN+"/prm list <prefix/suffix> <玩家ID> —— 查看指定玩家的前缀/后缀");
								  return true;
							  }
							  }else{
								  sender.sendMessage(ChatColor.RED+"你没有权限执行此指令");
								  return true;
							 }
						  }else{
							  sender.sendMessage(ChatColor.RED+"参数错误");
							  sender.sendMessage(ChatColor.DARK_GREEN+"/prm list <prefix/suffix> —— 查看自己的前缀/后缀");
							  return true;
						  }
					//查看他人前缀/后缀权限
					  }
			//list指令处理
			  if(args[0].equalsIgnoreCase("add")){
				  if((args.length==4)&&((args[2].equalsIgnoreCase("prefix"))||(args[2].equalsIgnoreCase("p"))||(args[2].equalsIgnoreCase("s"))||(args[2].equalsIgnoreCase("suffix")))){
					  if(sender.hasPermission("prefixmanager.add")){
						  if((this.plugin.prefixmap.containsKey(args[1]))||(this.plugin.suffixmap.containsKey(args[1]))){
							  if((args[2].equalsIgnoreCase("prefix"))||(args[2].equalsIgnoreCase("p"))){
								  if(this.plugin.prefixmap.get(args[1]).contains(args[3])){
									  sender.sendMessage(ChatColor.RED+"请勿重复添加称号");
									  return true;
								  }
								  this.plugin.prefixmap.get(args[1]).add(args[3]);
								  sender.sendMessage(ChatColor.DARK_GREEN+"已成功为玩家"+ChatColor.YELLOW+args[1]+ChatColor.DARK_GREEN+"添加前缀"+this.changeformat(args[3]));
								  return true;
							  }else{
								  if(this.plugin.suffixmap.get(args[1]).contains(args[3])){
									  sender.sendMessage(ChatColor.RED+"请勿重复添加称号");
									  return true;
								  }
								  this.plugin.suffixmap.get(args[1]).add(args[3]);
								  sender.sendMessage(ChatColor.DARK_GREEN+"已成功为玩家"+ChatColor.YELLOW+args[1]+ChatColor.DARK_GREEN+"添加后缀"+this.changeformat(args[3]));
								  return true;
							  }
						  }else{
							  sender.sendMessage(ChatColor.RED+"玩家不存在");
							  return true;
						  }
					  }else{
						  sender.sendMessage(ChatColor.RED+"你没有权限执行此指令");
						  return true;
					  }
				  }else{
					  sender.sendMessage(ChatColor.RED+"参数错误");
					  sender.sendMessage(ChatColor.DARK_GREEN+"/prm add <玩家ID> <prefix/suffix> <称号> —— 设置玩家前缀/后缀");
					  return true;
				  }
			  }
			  //add指令处理
			  if(args[0].equalsIgnoreCase("del")){
				  if((args.length==4)&&(args[3].matches("^[0-9]*$"))&&((args[2].equalsIgnoreCase("prefix"))||(args[2].equalsIgnoreCase("p"))||(args[2].equalsIgnoreCase("s"))||(args[2].equalsIgnoreCase("suffix")))){
					  if(sender.hasPermission("prefixmanager.del")){
						  if((args[2].equalsIgnoreCase("prefix"))||(args[2].equalsIgnoreCase("p"))){
							  if(this.plugin.prefixmap.containsKey(args[1])){
								  this.plugin.del(this.plugin.getServer().getPlayer(args[1]), "prefix",  this.plugin.prefixmap.get(args[1]).get(Integer.parseInt(args[3])));
								  this.plugin.prefixmap.get(args[1]).remove(Integer.parseInt(args[3]));
								  sender.sendMessage(ChatColor.DARK_GREEN+"前缀已删除");
								  return true;
							  }else{
								  sender.sendMessage(ChatColor.RED+"玩家不存在前缀");
								  return true;
							  }
						  }else{
							  if(this.plugin.suffixmap.containsKey(args[1])){
								  this.plugin.del(this.plugin.getServer().getPlayer(args[1]), "suffix",  this.plugin.suffixmap.get(args[1]).get(Integer.parseInt(args[3])));
								  this.plugin.suffixmap.get(args[1]).remove(Integer.parseInt(args[3]));
								  sender.sendMessage(ChatColor.DARK_GREEN+"后缀已删除");
								  return true;
							  }else{
								  sender.sendMessage(ChatColor.RED+"玩家不存在后缀");
								  return true;
							  }	  
						  }
					  }else{
						  sender.sendMessage(ChatColor.RED+"你没有权限执行此指令");
						  return true;
					  }
				  }else{
					  sender.sendMessage(ChatColor.RED+"参数错误");
					  sender.sendMessage(ChatColor.DARK_GREEN+"/prm del <玩家ID> <prefix/suffix> <称号序号> —— 删除玩家前缀/后缀");
					  return true;
				  }
			  }
			  //del指令处理
			  if(args[0].equalsIgnoreCase("set")){
				  if(!(sender instanceof Player)){
					  sender.sendMessage("控制台无法使用此指令");
					  return true;
				  }
				  if((args.length==3)&&(args[2].matches("^[0-9]*$"))&&((args[1].equalsIgnoreCase("prefix"))||(args[1].equalsIgnoreCase("p"))||(args[1].equalsIgnoreCase("s"))||(args[1].equalsIgnoreCase("suffix")))){
					  if(sender.hasPermission("prefixmanager.set")){
						  if((args[1].equalsIgnoreCase("prefix"))||(args[1].equalsIgnoreCase("p"))){
							  if((Integer.parseInt(args[2])+1)>this.plugin.prefixmap.get(name).size()){
								  sender.sendMessage(ChatColor.RED+"无效的序号");
								  return true;
							  }
							  this.plugin.getConfig().set("player."+name+".In-use.prefix", Integer.parseInt(args[2]));
							  this.plugin.saveConfig();
							  Player p =this.plugin.getServer().getPlayer(name);
							  display= plugin.prefixmap.get(name).get(plugin.getConfig().getInt("player."+name+".In-use.prefix"))+name;
							  if(!plugin.suffixmap.get(name).isEmpty()){
								  display= display+plugin.suffixmap.get(name).get(plugin.getConfig().getInt("player."+name+".In-use.suffix"));
							  }
							  display=display.replace('&', '§');
							  if(display.length()<=16){
								  p.setPlayerListName(display);
							  }
							  p.setDisplayName(display);
							  sender.sendMessage(ChatColor.DARK_GREEN + "前缀已设置");
							  return true;
						  }else{
							  if((Integer.parseInt(args[2])+1)>this.plugin.suffixmap.get(name).size()){
								  sender.sendMessage(ChatColor.RED+"无效的序号");
								  return true;
							  }
							  this.plugin.getConfig().set("player."+name+".In-use.suffix", Integer.parseInt(args[2]));
							  this.plugin.saveConfig();
							  Player p =this.plugin.getServer().getPlayer(name);
							  display= name+plugin.suffixmap.get(name).get(plugin.getConfig().getInt("player."+name+".In-use.suffix"));
							  if(!plugin.prefixmap.get(name).isEmpty()){
								  display= plugin.prefixmap.get(name).get(plugin.getConfig().getInt("player."+name+".In-use.prefix"))+display;
							  }
							  display=display.replace('&', '§');
							  p.setDisplayName(display);
							  if(display.length()<=16){
								  p.setPlayerListName(display);
							  }
							  sender.sendMessage(ChatColor.DARK_GREEN+"后缀已设置");
							  return true;
						  }
					  }else{
						  sender.sendMessage(ChatColor.RED+"你没有权限执行此指令");
						  return true;
					  }
				  }else{
					  sender.sendMessage(ChatColor.RED+"参数错误");
					  sender.sendMessage(ChatColor.DARK_GREEN+"/prm set <prefix/suffix> <称号序号> —— 设置前缀/后缀");
					  return true;
				  }
			  }
			 //set指令处理
			  if(args[0].equalsIgnoreCase("check")){
				  if(!(sender instanceof Player)){
					  sender.sendMessage("控制台无法使用此指令");
					  return true;
				  }
				  List<String> list = new ArrayList<String>();
				  if(sender.hasPermission("prefixmanager.check")){
					  this.plugin.check((Player) sender);
					  list = this.plugin.prefixmap.get(sender.getName());
					  if(this.plugin.gprefix.containsKey(this.plugin.permission.getPrimaryGroup((Player)sender))){
						  for(String n : this.plugin.gprefix.get(this.plugin.permission.getPrimaryGroup((Player)sender))){
							  if(n!=null){
								  if(!list.contains(n)){
									  list.add(n);
								  } 
							  }
						  }
					  }
					  list = this.plugin.suffixmap.get(sender.getName());
					  if(this.plugin.gsuffix.containsKey(this.plugin.permission.getPrimaryGroup((Player)sender))){
						  for(String n : this.plugin.gsuffix.get(this.plugin.permission.getPrimaryGroup((Player)sender))){
							  if(n!=null){
								  if(!list.contains(n)){
									  list.add(n);
								  }
							  }
						  }
					  }
					  sender.sendMessage(ChatColor.DARK_GREEN+"已将现有前缀/后缀加入列表");
					  return true;
				  }else{
					  sender.sendMessage(ChatColor.RED+"你没有权限执行此指令");
					  return true;
				  }
			  }
			  //check指令处理
			  if(args[0].equalsIgnoreCase("load")){
				  if(sender.hasPermission("prefixmanager.load")){
					  this.plugin.reloadConfig();
					  this.plugin.LoadConfig();
					  sender.sendMessage(ChatColor.DARK_GREEN+"插件重载完毕");
					  return true;
				  }
			  }
			  //load指令处理
			  if(args[0].equalsIgnoreCase("gadd")){
				  if(sender.hasPermission("prefixmanager.gadd")){
					  if(!((args[2].equalsIgnoreCase("prefix"))||(args[2].equalsIgnoreCase("p"))||(args[2].equalsIgnoreCase("s"))||(args[2].equalsIgnoreCase("suffix")))){
						  sender.sendMessage(ChatColor.RED+"参数错误");
						  sender.sendMessage(ChatColor.DARK_GREEN+"/prm gadd <用户组名> <prefix/suffix> <称号> —— 设置用户组前缀/后缀");
						  return true;
					  }
					  if(!(sender instanceof Player)){
						  sender.sendMessage("控制台无法使用此指令");
						  return true;
					  }
					  Player player=(Player)sender;
					  Iterator<String> gn = Arrays.asList(this.plugin.permission.getGroups()).iterator();
					  String grn = null;
					  while(gn.hasNext()){
						  grn=gn.next();
						  if(grn.equalsIgnoreCase(args[1])){
							  break;
						  }
						  grn=null;
					  }
					  if(!(grn==null)){
						 if((args[2].equalsIgnoreCase("prefix"))||(args[2].equalsIgnoreCase("p"))){
							 if(!this.plugin.gprefix.containsKey(grn)){
								 List<String> list = new ArrayList<String>();
								 list.add(args[3]);
								 this.plugin.gprefix.put(grn, list);
							 }else{
								 if(this.plugin.gprefix.get(grn).contains(args[3])){
									 sender.sendMessage(ChatColor.RED+"请勿重复添加称号");
									 return true;
								 }
								 this.plugin.gprefix.get(grn).add(args[3]);
							 }
							 sender.sendMessage(ChatColor.DARK_GREEN+"称号添加中……");
							 Iterator<String> i = this.plugin.prefixmap.keySet().iterator();
                             while(i.hasNext()){
                            	 String key = i.next();
                            	 if(grn.equalsIgnoreCase(this.plugin.permission.getPrimaryGroup(player.getWorld(),key))){
                            		 this.plugin.prefixmap.get(key).add(args[3]);
                            	 }
                             }
                             sender.sendMessage(ChatColor.DARK_GREEN+"称号添加完毕");
                             return true;
						 }else{
							 if(!this.plugin.gsuffix.containsKey(grn)){
								 List<String> list = new ArrayList<String>();
								 list.add(args[3]);
								 this.plugin.gsuffix.put(grn, list);
							 }else{
								 if(this.plugin.gsuffix.get(grn).contains(args[3])){
									 sender.sendMessage(ChatColor.RED+"请勿重复添加称号");
									 return true;
								 }
								 this.plugin.gsuffix.get(grn).add(args[3]);
							 }
							 sender.sendMessage(ChatColor.DARK_GREEN+"称号添加中……");
							 Iterator<String> i = this.plugin.suffixmap.keySet().iterator();
                             while(i.hasNext()){
                            	 String key = i.next();
                            	 if(grn.equalsIgnoreCase(this.plugin.permission.getPrimaryGroup(player.getWorld(),key))){
                            		 this.plugin.suffixmap.get(key).add(args[3]);
                            	 }
                             }
                             sender.sendMessage(ChatColor.DARK_GREEN+"称号添加完毕");
                             return true;
						 }
					  }else{
						  sender.sendMessage(ChatColor.RED+"用户组不存在");
						  return true;
					  }
				  }else{
					  sender.sendMessage(ChatColor.RED+"你没有权限执行此指令");
					  return true;
				  }
			  }
			  //gadd指令处理	
			  if(args[0].equalsIgnoreCase("glist")){
				  if(sender.hasPermission("prefixmanager.glist")){
					  if((args.length==3)&&((args[2].equalsIgnoreCase("prefix"))||(args[2].equalsIgnoreCase("p"))||(args[2].equalsIgnoreCase("s"))||(args[2].equalsIgnoreCase("suffix")))){
						  Iterator<String> gn = Arrays.asList(this.plugin.permission.getGroups()).iterator();
						  String grn = null;
						  while(gn.hasNext()){
							  grn=gn.next();
							  if(grn.equalsIgnoreCase(args[1])){
								  break;
							  }
							  grn=null;
						  }
						  if(grn==null){
							  sender.sendMessage(ChatColor.RED+"用户组不存在");
							  return true;
						  }
						  if((args[2].equalsIgnoreCase("prefix"))||(args[2].equalsIgnoreCase("p"))){
							  if(this.plugin.gprefix.containsKey(grn)){
								  sender.sendMessage(ChatColor.DARK_GREEN+grn+"用户组拥有以下前缀称号:");
								  sender.sendMessage(ChatColor.YELLOW+"--------------------");
								  Iterator<String> i = this.plugin.gprefix.get(grn).iterator();
						          int n = 0;
								  while(i.hasNext())
								  {
									sender.sendMessage(ChatColor.YELLOW+String.valueOf(n)+":"+changeformat(i.next()));
									n++;
								  }  
								  sender.sendMessage(ChatColor.YELLOW+"--------------------");
								  return true;
							  }else{
								  sender.sendMessage(ChatColor.RED+"此用户组尚未拥有任何前缀");
								  return true;
							  }
						  }else{
							  if(this.plugin.gsuffix.containsKey(grn)){
								  sender.sendMessage(ChatColor.DARK_GREEN+grn+"用户组拥有以下后缀称号:");
								  sender.sendMessage(ChatColor.YELLOW+"--------------------");
								  Iterator<String> i = this.plugin.gsuffix.get(grn).iterator();
						          int n = 0;
								  while(i.hasNext())
								  {
									sender.sendMessage(ChatColor.YELLOW+String.valueOf(n)+":"+changeformat(i.next()));
									n++;
								  }  
								  sender.sendMessage(ChatColor.YELLOW+"--------------------");
								  return true;
							  }else{
								  sender.sendMessage(ChatColor.RED+"此用户组尚未拥有任何后缀");
								  return true;
							  }
						  }
					  }else{
						  sender.sendMessage(ChatColor.RED+"参数错误");
						  sender.sendMessage(ChatColor.DARK_GREEN+"/prm glist <用户组名> <prefix/suffix> —— 显示用户组前缀/后缀");
						  return true;
					  }
				  }else{
					  sender.sendMessage(ChatColor.RED+"你没有权限执行此指令");
					  return true;
				  }
			  }
			  //glist指令处理
			  if(args[0].equalsIgnoreCase("gdel")){
				  if(sender.hasPermission("prefixmanager.gdel")){
					  if((args[3].matches("^[0-9]*$"))&&(args.length==4)&&((args[2].equalsIgnoreCase("prefix"))||(args[2].equalsIgnoreCase("p"))||(args[2].equalsIgnoreCase("s"))||(args[2].equalsIgnoreCase("suffix")))){
						  Iterator<String> gn = Arrays.asList(this.plugin.permission.getGroups()).iterator();
						  String grn = null;
						  while(gn.hasNext()){
							  grn=gn.next();
							  if(grn.equalsIgnoreCase(args[1])){
								  break;
							  }
							  grn=null;
						  }
						  if(grn==null){
							  sender.sendMessage(ChatColor.RED+"用户组不存在");
							  return true;
						  }
						  if((args[2].equalsIgnoreCase("prefix"))||(args[2].equalsIgnoreCase("p"))){
							 if(this.plugin.gprefix.containsKey(grn)){
								 if((Integer.parseInt(args[3])+1)<=this.plugin.gprefix.get(grn).size()){									 
									 String nn = this.plugin.gprefix.get(grn).get(Integer.parseInt(args[3]));									 
									 gn = this.plugin.prefixmap.keySet().iterator();
									 sender.sendMessage(ChatColor.DARK_GREEN+"称号删除中……");
									 while(gn.hasNext()){
										 String key = gn.next();
										 if(this.plugin.prefixmap.get(key).contains(nn)){
											 this.plugin.prefixmap.get(key).remove(nn);
										 }
									 }
									 this.plugin.gprefix.get(grn).remove(Integer.parseInt(args[3]));
									 sender.sendMessage(ChatColor.DARK_GREEN+"称号删除完毕……");
									 return true;
								 }else{
									 sender.sendMessage(ChatColor.RED+"序号无效");
									 return true;  
								 }
							 }else{
								 sender.sendMessage(ChatColor.RED+"此用户组尚未拥有任何前缀");
								 return true;  
							 }
						  }else{
							  if(this.plugin.gsuffix.containsKey(grn)){
									 if((Integer.parseInt(args[3])+1)<=this.plugin.gsuffix.get(grn).size()){									 
										 String nn = this.plugin.gsuffix.get(grn).get(Integer.parseInt(args[3]));									 
										 gn = this.plugin.gsuffix.keySet().iterator();
										 sender.sendMessage(ChatColor.DARK_GREEN+"称号删除中……");
										 while(gn.hasNext()){
											 String key = gn.next();
											 if(this.plugin.suffixmap.get(key).contains(nn)){
												 this.plugin.suffixmap.get(key).remove(nn);
											 }
										 }
										 this.plugin.gsuffix.get(grn).remove(Integer.parseInt(args[3]));
										 sender.sendMessage(ChatColor.DARK_GREEN+"称号删除完毕……");
										 return true;
									 }else{
										 sender.sendMessage(ChatColor.RED+"序号无效");
										 return true;  
									 }
								 }else{
									 sender.sendMessage(ChatColor.RED+"此用户组尚未拥有任何后缀");
									 return true;  
								 }
						  }
					  }else{
						  sender.sendMessage(ChatColor.RED+"参数错误");
						  sender.sendMessage(ChatColor.DARK_GREEN+"/prm gdel <用户组名> <prefix/suffix> <称号序号> —— 删除用户组前缀/后缀");
						  return true;
					  }
				  }else{
					  sender.sendMessage(ChatColor.RED+"你没有权限执行此指令");
					  return true;  
				  }
			  }
				//gdel指令处理
				if(args[0].equalsIgnoreCase("shop")){
				return true;
				}

			  if(args[0].equalsIgnoreCase("save")){
				  if(sender.hasPermission("prefixmanager.save")){
					  sender.sendMessage(ChatColor.DARK_GREEN+"数据存储……");
					  this.plugin.save();
					  sender.sendMessage(ChatColor.DARK_GREEN+"数据存储完毕");
					  return true;
				  }else{
				  sender.sendMessage(ChatColor.RED+"你没有权限执行此指令");
				  return true;
				  }	  
			  }else{
					sender.sendMessage(ChatColor.RED+"----------["+ChatColor.YELLOW+"PrefixManager指令说明"+ChatColor.RED+"]----------");
			    	sender.sendMessage(ChatColor.GREEN + "/prm add <玩家ID> <prefix/suffix> <称号>");
			    	sender.sendMessage(ChatColor.BLUE+"  -给指定玩家添加前缀/后缀");
			    	sender.sendMessage(ChatColor.GREEN + "/prm list <prefix/suffix>");
			    	sender.sendMessage(ChatColor.BLUE+"  -显示你拥有的前缀/后缀");
			    	sender.sendMessage(ChatColor.GREEN + "/prm set <prefix/suffix> <称号序号>");
			    	sender.sendMessage(ChatColor.BLUE+"  -设置你的前缀/后缀");
			    	sender.sendMessage(ChatColor.RED+"------------------------------------------");
			        return true;
			  }
			  //save指令处理
			}
			return false;
		  }
//------------------------------辅助---------------------------------------------------------
		  public String changeformat(String msg)
		  {
		    return ChatColor.translateAlternateColorCodes("&".charAt(0), msg);
		  }
		  //彩色字体
		  public void getprefix(Player player)
		  {
			 Iterator<String> i = this.plugin.prefixmap.get(player.getName()).iterator();
             int n = 0;
			   while(i.hasNext())
			   {
				player.sendMessage(ChatColor.DARK_GREEN+"序号: "+ChatColor.YELLOW+String.valueOf(n)+ChatColor.DARK_GREEN+" —— "+changeformat(i.next()));
			    n++;
			   }
		  }
		  //前缀统一输出
		  public void getsuffix(Player player)
		  {
	          int n = 0;
			  Iterator<String> i = this.plugin.suffixmap.get(player.getName()).iterator();
			  while(i.hasNext())
			   {
				player.sendMessage(ChatColor.DARK_GREEN+"序号: "+ChatColor.YELLOW+String.valueOf(n)+ChatColor.DARK_GREEN+" —— "+changeformat(i.next()));
				n++;
			   }
		  }
		  //后缀统一输出
}