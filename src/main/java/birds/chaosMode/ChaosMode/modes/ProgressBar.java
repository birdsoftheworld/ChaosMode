package birds.chaosMode.ChaosMode.modes;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.List;

public class ProgressBar {
    public BossBar getBar() {
        BossBar cooldown = new BossBar() {
            @Override
            public String getTitle() {
                return null;
            }

            @Override
            public void setTitle(String s) {

            }

            @Override
            public BarColor getColor() {
                return null;
            }

            @Override
            public void setColor(BarColor barColor) {

            }

            @Override
            public BarStyle getStyle() {
                return null;
            }

            @Override
            public void setStyle(BarStyle barStyle) {

            }

            @Override
            public void removeFlag(BarFlag barFlag) {

            }

            @Override
            public void addFlag(BarFlag barFlag) {

            }

            @Override
            public boolean hasFlag(BarFlag barFlag) {
                return false;
            }

            @Override
            public void setProgress(double v) {

            }

            @Override
            public double getProgress() {
                return 0;
            }

            @Override
            public void addPlayer(Player player) {

            }

            @Override
            public void removePlayer(Player player) {

            }

            @Override
            public void removeAll() {

            }

            @Override
            public List<Player> getPlayers() {
                return null;
            }

            @Override
            public void setVisible(boolean b) {

            }

            @Override
            public boolean isVisible() {
                return false;
            }

            @Override
            public void show() {

            }

            @Override
            public void hide() {

            }
        };
        return cooldown;
    }
}
