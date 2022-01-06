package Engine;

public class LevelScene extends Scene{
    public LevelScene(){
        System.out.println("Level Scene");
        Window.get().r = 0.0f;
        Window.get().g = 0.0f;
        Window.get().b = 0.0f;
        Window.get().a = 0.0f;
    }

    @Override
    public void update(float dt){

    }
}
