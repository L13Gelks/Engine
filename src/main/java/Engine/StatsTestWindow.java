package Engine;

import Game.entity.Player;
import imgui.ImGui;

public class StatsTestWindow {
    private Player player;

    public StatsTestWindow() {
        this.player = null;
    }

    public void imgui() {
        ImGui.begin("Player");
        player.imgui("Player");
        ImGui.end();

        ImGui.begin("Level Stats");
        player.imgui("Level Stats");
        ImGui.end();

        ImGui.begin("Stats");
        player.imgui("Stats");
        ImGui.end();

        ImGui.begin("Resistances");
        player.imgui("Resistances");
        ImGui.end();
    }

    public void  setPlayer(Player player){
        this.player = player;
    }
    public Player  getPlayer(){
        return this.player;
    }
}
