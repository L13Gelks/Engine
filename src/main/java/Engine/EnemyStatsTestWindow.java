package Engine;

import Game.entity.Enemy;
import Game.entity.Player;
import Game.entity.TestDummy;
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

public class EnemyStatsTestWindow {
    private Enemy enemy;

    public EnemyStatsTestWindow() {
        this.enemy = null;
    }

    public void imgui() {
        if(enemy != null){
            ImGui.begin("Enemy");
            enemy.imgui("Enemy");
            ImGui.end();
        }
    }

    public void  setEnemy(Enemy enemy){
        this.enemy = enemy;
    }
    public Enemy  getEnemy(){
        return this.enemy;
    }
}
