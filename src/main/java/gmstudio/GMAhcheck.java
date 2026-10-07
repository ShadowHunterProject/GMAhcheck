package gmstudio;

import org.bukkit.plugin.java.JavaPlugin;

public class GMAhcheck extends JavaPlugin {

    @Override
    public void onEnable() {
        // Lưu config mặc định nếu chưa có
        saveDefaultConfig();

        // Đăng ký Listener
        getServer().getPluginManager().registerEvents(new AuctionListener(this), this);

        getLogger().info("GMAhcheck (Author: Gemix) da duoc bat thanh cong!");
    }

    @Override
    public void onDisable() {
        getLogger().info("GMAhcheck da duoc tat.");
    }
}
