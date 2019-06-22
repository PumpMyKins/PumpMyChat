package fr.pumpmybchat.command;

import fr.pumpmybchat.Main;
import fr.pumpmybchat.chat.ChatManager;
import fr.pumpmybchat.utils.ChatColorUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class NicknameCommand extends Command {

	private ChatManager chatManager;

	public NicknameCommand(String name, ChatManager chatManager) {
		super(name);
		this.chatManager = chatManager;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {

		if(!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(new TextComponent(Main.PLUGIN_PREFIX + "§cVous devez etre un joueur pour faire cela !"));
			return;
		}

		if(sender.hasPermission("pumpmybchat.command.nick")) {

			ProxiedPlayer player = (ProxiedPlayer) sender;

			if(args.length == 1) {
				
				String stringNickname = args[0];
				if(stringNickname.trim().isEmpty() || stringNickname.length() > 16) {

					this.sendMessage_CmdSynthaxError(player);

				}else {

					for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
						
						if(!p.getName().equalsIgnoreCase(player.getName()) && p.getName().equalsIgnoreCase(ChatColorUtils.getWithoutChatColorCodesString(stringNickname)) &&  !player.hasPermission("pumpmybchat.nick.usurp")) {
							
							TextComponent txt = new TextComponent(Main.PLUGIN_PREFIX);
							TextComponent txt1 = new TextComponent("Un joueur possède déjà ce nom.");
							txt1.setColor(ChatColor.RED);
							txt.addExtra(txt1);			
							sender.sendMessage(txt);							
							return;
							
						}else {
							
							break;
							
						}
						
					}
					
					if(ChatColorUtils.containsChatColorCodes(stringNickname) && !player.hasPermission("pumpmybchat.nick.colored")) {

						TextComponent txt = new TextComponent(Main.PLUGIN_PREFIX);
						TextComponent txt1 = new TextComponent("Impossible de colorer votre surnom");
						txt1.setColor(ChatColor.RED);
						txt.addExtra(txt1);			
						sender.sendMessage(txt);
						
						TextComponent txt2 = new TextComponent("Fonctionnalité réservé aux tiers supérieurs");
						txt2.setColor(ChatColor.RED);	
						sender.sendMessage(txt2);

						TextComponent link = new TextComponent("http://store.pumpmykins.eu/");
						link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://store.pumpmykins.eu/"));
						link.setBold(true);
						link.setColor(ChatColor.DARK_BLUE);
						TextComponent txt3 = new TextComponent("Voir : ");
						txt3.setColor(ChatColor.RED);
						txt3.addExtra(link);
						sender.sendMessage(txt3);

					}else {

						stringNickname = ChatColorUtils.getChatColorCodesTranslatedString(stringNickname);
						
						try {
							this.chatManager.setPlayerNickname(player,stringNickname);
							sender.sendMessage(new TextComponent(Main.PLUGIN_PREFIX + "§bSurnom (\"§r" + stringNickname + "§r§b\") appliqué !"));
							TextComponent txt = new TextComponent("§bUtilisez la commande : \"");
							TextComponent cmd = new TextComponent("/nick");
							cmd.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/nick"));
							cmd.setBold(true);
							cmd.setColor(ChatColor.DARK_BLUE);

							txt.addExtra(cmd);
							txt.addExtra("§b\" pour supprimer votre surnom.");
							sender.sendMessage(txt);
						} catch (Exception e) {
							e.printStackTrace();
							sender.sendMessage(new TextComponent(e.getMessage()));
						}

					}					

				}	


			} else if(args.length == 0){

				if(this.chatManager.playerHasNickname(player)) {

					this.chatManager.unsetPlayerNickname(player);					
					sender.sendMessage(new TextComponent(Main.PLUGIN_PREFIX + "§bSurnom supprimé !"));

				}else {

					sender.sendMessage(new TextComponent(Main.PLUGIN_PREFIX + "§cVous ne possedez pas de surnom !"));

				}

			}else {

				this.sendMessage_CmdSynthaxError(player);

			}

		} else {

			TextComponent txt = new TextComponent(Main.PLUGIN_PREFIX);
			TextComponent txt1 = new TextComponent("Fonctionnalité achetable en boutique");
			txt1.setColor(ChatColor.RED);
			txt.addExtra(txt1);			
			sender.sendMessage(txt);

			TextComponent link = new TextComponent("http://store.pumpmykins.eu/");
			link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://store.pumpmykins.eu/"));
			link.setBold(true);
			link.setColor(ChatColor.DARK_BLUE);
			TextComponent txt2 = new TextComponent("Voir : ");
			txt2.setColor(ChatColor.RED);
			txt2.addExtra(link);
			sender.sendMessage(txt2);

		}

	}

	private void sendMessage_CmdSynthaxError(ProxiedPlayer player) {

		player.sendMessage(new TextComponent(Main.PLUGIN_PREFIX + "§cErreur de synthaxe dans votre commande !"));
		TextComponent txt = new TextComponent("§cDéfinir un surnom : ");
		TextComponent cmd = new TextComponent("§1/nick <surnom>");
		cmd.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/nick PumpMyKins"));
		txt.addExtra(cmd);
		player.sendMessage(new TextComponent(txt));

		TextComponent txt1 = new TextComponent("§cSupprimer un surnom : ");
		TextComponent cmd1 = new TextComponent("§1/nick");
		cmd1.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/nick"));
		txt1.addExtra(cmd1);
		player.sendMessage(new TextComponent(txt1));

	}

}