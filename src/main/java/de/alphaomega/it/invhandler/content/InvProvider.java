package de.alphaomega.it.invhandler.content;

import org.bukkit.entity.Player;

public interface InvProvider {
	
	void init(Player p, InvContents c);
	void update(Player p, InvContents c);
	
}
