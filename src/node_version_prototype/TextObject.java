package node_version_prototype;

import javafx.scene.control.Label;

public class TextObject extends Label {

    public TextObject(int x, int y, String text) {
        setTranslateX(x);
        setTranslateY(y);
        setText(text);
    }


}
