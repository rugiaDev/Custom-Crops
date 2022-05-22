package net.momirealms.customcrops.DataManager;

import net.momirealms.customcrops.CustomCrops;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CropManager {

    public static HashMap<Location, String> instances;

    public CropManager(FileConfiguration data) {
        FileConfiguration config = CustomCrops.instance.getConfig();
        File file = new File(CustomCrops.instance.getDataFolder(), "crop-data.yml");
        data = YamlConfiguration.loadConfiguration(file);
        try {
            for (String world : config.getStringList("config.whitelist-worlds")) {
                CropManager.instances = new HashMap<Location, String>();
                if(data.getConfigurationSection(world) != null){
                    for (String coordinate : data.getConfigurationSection(world).getKeys(false)) {
                        Location tempLocation = new Location(Bukkit.getWorld(world), (double)Integer.parseInt(coordinate.split(",")[0]), (double)Integer.parseInt(coordinate.split(",")[1]), (double)Integer.parseInt(coordinate.split(",")[2]));
                        String season = data.getString(world + "." + coordinate);
                        CropManager.instances.put(tempLocation, season);
                    }
                }
            }
        }
        catch (Exception e) {
            CropManager.instances = new HashMap<Location, String>();
            e.printStackTrace();
        }
        saveData();
    }

    public static List<Location> getCrops(World world){
        FileConfiguration config = CustomCrops.instance.getConfig();
        File file = new File(CustomCrops.instance.getDataFolder(), "crop-data.yml");
        FileConfiguration data;
        data = YamlConfiguration.loadConfiguration(file);
        List<Location> locations = new ArrayList<Location>();
        if (config.getStringList("config.whitelist-worlds").contains(world.getName())){
            if(data.contains(world.getName())){
                data.getConfigurationSection(world.getName()).getKeys(false).forEach(key ->{
                    String[] string_list = key.split(",");
                    if (config.getBoolean("config.only-grow-in-loaded-chunks")){
                        if (world.isChunkLoaded(Integer.parseInt(string_list[0])/16, Integer.parseInt(string_list[2])/16)){
                            locations.add(new Location(world, Double.parseDouble(string_list[0]),Double.parseDouble(string_list[1]),Double.parseDouble(string_list[2])));
                        }
                    }else {
                        locations.add(new Location(world, Double.parseDouble(string_list[0]),Double.parseDouble(string_list[1]),Double.parseDouble(string_list[2])));
                    }
                });
            }
        }
        return locations;
    }

    public static void saveData(){
        File file = new File(CustomCrops.instance.getDataFolder(), "crop-data.yml");
        FileConfiguration data;
        data = YamlConfiguration.loadConfiguration(file);
        if (CropManager.instances != null) {
            Set<Map.Entry<Location, String>> en = instances.entrySet();
            for(Map.Entry<Location, String> entry : en){
                data.set(entry.getKey().getWorld().getName() + "." + entry.getKey().getBlockX() + "," + entry.getKey().getBlockY()+ ","+entry.getKey().getBlockZ(), entry.getValue());
            }
        }
        else {
            CropManager.instances = new HashMap<Location, String>();
            Bukkit.getConsoleSender().sendMessage("错误");
        }
        try {
            data.save(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    //test
    public static void putInstance(Location location, String season) {
        CropManager.instances.put(location, season);
    }
    public HashMap<Location, String> getMap() {
        return CropManager.instances;
    }
    public static void cleanLoadedCache() {
        FileConfiguration config = CustomCrops.instance.getConfig();
        File file = new File(CustomCrops.instance.getDataFolder(), "crop-data.yml");
        FileConfiguration data;
        data = YamlConfiguration.loadConfiguration(file);
        config.getStringList("config.whitelist-worlds").forEach(worldName ->{
            if(data.contains(worldName)){
                World world = Bukkit.getWorld(worldName);
                data.getConfigurationSection(worldName).getKeys(false).forEach(key ->{
                    String[] string_list = key.split(",");
                    if (world.isChunkLoaded(Integer.parseInt(string_list[0])/16, Integer.parseInt(string_list[2])/16)){
                        Location tempLoc = new Location(world,Double.parseDouble(string_list[0]),Double.parseDouble(string_list[1]),Double.parseDouble(string_list[2]));
                        if(world.getBlockAt(tempLoc).getType() != Material.TRIPWIRE){
                            CropManager.instances.remove(tempLoc);
                            data.set(worldName+"."+string_list[0]+","+string_list[1]+","+string_list[2], null);
                        }
                    }
                });
            }
        });
        try{
            data.save(file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}