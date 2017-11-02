package n.customselectview.customSelectView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by nam on 2017. 8. 15..
 */

public class CustomSelectSubViewButton extends AppCompatButton {

    private boolean isSelected = false;
    private int bgColor = Color.rgb(200,200,0);
    private int txtColor = Color.rgb(255,255,255);
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public void setTxtColor(int txtColor) {
        this.txtColor = txtColor;
    }

    public void initSelected(){
        setSelected(false);
        setBackgroundColor(bgColor);
        setTextColor(txtColor);
    }

    public void setDeselect(){
            setSelected(false);
            setBackgroundColor(bgColor);
            setTextColor(txtColor);
            //deselect
    }

    public void setSelect(){
        setSelected(true);
        setBackgroundColor(txtColor);
        setTextColor(bgColor);
        //select
    }

    public void setColorSelected(){
        setBackgroundColor(txtColor);
        setTextColor(bgColor);
    }

    public CustomSelectSubViewButton(Context context) {
        super(context);
    }

    public CustomSelectSubViewButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSelectSubViewButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
