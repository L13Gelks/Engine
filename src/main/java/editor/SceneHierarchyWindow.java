package editor;

import Engine.GameObject;
import Engine.Window;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

import java.util.List;

public class SceneHierarchyWindow {
    private static String payLoadDragDropType = "Scene Hierarchy";
    public void imgui(){
        ImGui.begin("Scene Hierarchy");

        List<GameObject> gameObjects = Window.getScene().getGameObjects();
        int index = 0;
        for(GameObject go : gameObjects){
            if(!go.doSerialization()){
                continue;
            }

            boolean treeNodeOpen = doTreeNode(go, index);

            if(treeNodeOpen){
                ImGui.treePop();
            }
            index++;
        }
        ImGui.end();
    }

    private boolean doTreeNode(GameObject go, int index){
        ImGui.pushID(index);
        boolean treeNodeOpen = ImGui.treeNodeEx(
                go.name,
                ImGuiTreeNodeFlags.DefaultOpen |
                        ImGuiTreeNodeFlags.FramePadding |
                        ImGuiTreeNodeFlags.OpenOnArrow |
                        ImGuiTreeNodeFlags.SpanAvailWidth,
                go.name
        );
        ImGui.popID();

        if(ImGui.beginDragDropSource()){
            ImGui.setDragDropPayload(payLoadDragDropType, go);
            ImGui.text(go.name);
            ImGui.endDragDropSource();
        }

        if(ImGui.beginDragDropTarget()){
            Object payLoadObj = ImGui.acceptDragDropPayload(payLoadDragDropType);
            if(payLoadObj != null){
                if(payLoadObj.getClass().isAssignableFrom(GameObject.class)){
                    GameObject gameObject = (GameObject) payLoadObj;
                    System.out.println("Pauload: " + gameObject.name);
                }
            }
            ImGui.endDragDropTarget();
        }

        return treeNodeOpen;
    }
}
