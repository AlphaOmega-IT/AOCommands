package de.alphaomega.it.utils;

import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.IOException;

public class ItemStackSerialization {
	
	private static final Yaml yaml;
	
	static {
		yaml = new Yaml(new YamlBukkitConstructor(), new YamlRepresenter(), new DumperOptions());
	}
	
	public synchronized static String getBase64FromItemStack(ItemStack itemStack) {
		if (itemStack == null) return null;
		try {
			String dumped = yaml.dump(new ItemStack(itemStack));
			ItemStack loadedItem = yaml.loadAs(dumped, ItemStack.class);
			if (! loadedItem.isSimilar(itemStack)) dumped = yaml.dump(loadedItem);
			return Base64.encodeObject(dumped);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public synchronized static ItemStack getItemStackFromBase64(String base64) {
		if (base64 == null) return null;
		try {
			return yaml.loadAs((String) Base64.decodeToObject(base64), ItemStack.class);
		} catch (IOException | ClassNotFoundException | YAMLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static class YamlBukkitConstructor extends YamlConstructor {
		
		public YamlBukkitConstructor() {
			this.yamlConstructors.put(new Tag(Tag.PREFIX + "org.bukkit.inventory.ItemStack"), this.yamlConstructors.get(Tag.MAP));
		}
		
	}
	
}
