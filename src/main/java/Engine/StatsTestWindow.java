package Engine;

import Game.entity.Player;
import components.SpriteRenderer;
import imgui.ImGui;
import org.joml.Vector4f;
import physics2d.components.Box2DCollider;
import physics2d.components.CircleCollider;
import physics2d.components.RigidBody2D;
import renderer.PickingTexture;

import java.util.ArrayList;
import java.util.List;




        import components.NonPickable;
        import components.SpriteRenderer;
        import imgui.ImGui;
        import Engine.GameObject;
        import Engine.MouseListener;
        import org.joml.Vector4f;
        import physics2d.components.Box2DCollider;
        import physics2d.components.CircleCollider;
        import physics2d.components.RigidBody2D;
        import renderer.PickingTexture;
        import scenes.Scene;

        import java.util.ArrayList;
        import java.util.List;

        import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

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
