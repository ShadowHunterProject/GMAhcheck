package gmstudio;

import com.artillexstudios.axauctions.api.events.AxAuctionsPreSellEvent;
import io.lumine.mythic.lib.api.item.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class AuctionListener implements Listener {

    private final GMAhcheck plugin;

    public AuctionListener(GMAhcheck plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPreSell(AxAuctionsPreSellEvent event) {
        // Tuỳ theo API của AxAuctions mà có thể gọi là getItem() hoặc getItemStack()
        // Nếu trình biên dịch báo lỗi ở đây, hãy dùng gợi ý của IDE để sửa lại.
        ItemStack item = event.getItem(); 
        Player player = event.getPlayer();

        if (item == null || item.getType().isAir()) return;

        // Bọc item lại thành NBTItem của MythicLib để đọc dữ liệu
        NBTItem nbtItem = NBTItem.get(item);

        boolean isMMOItem = nbtItem.hasType();
        boolean allowVanilla = plugin.getConfig().getBoolean("messages.allow-vanilla", true);

        if (isMMOItem) {
            String tier = nbtItem.getString("MMOITEMS_TIER");
            
            // Lấy danh sách tier từ config và chuyển đổi tất cả sang chữ hoa để dễ so sánh
            List<String> allowedTiers = plugin.getConfig().getStringList("allowed-tiers")
                    .stream().map(String::toUpperCase).collect(Collectors.toList());

            if (tier == null || !allowedTiers.contains(tier.toUpperCase())) {
                event.setCancelled(true);
                String msg = plugin.getConfig().getString("messages.not-allowed-tier", "&cBạn chỉ được phép bán các vật phẩm có Tier được cấu hình trên chợ!");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
        } else {
            // Nếu không phải là MMOItem và config KHÔNG cho phép bán đồ vanilla
            if (!allowVanilla) {
                event.setCancelled(true);
                String msg = plugin.getConfig().getString("messages.not-mmoitem", "&cBạn không thể bán vật phẩm thường trên chợ!");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
        }
    }
}
