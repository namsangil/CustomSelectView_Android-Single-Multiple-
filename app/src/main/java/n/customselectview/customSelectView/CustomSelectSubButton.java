package n.customselectview.customSelectView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import n.customselectview.R;


/**
 * Created by nam on 2017. 8. 14..
 */

public class CustomSelectSubButton extends LinearLayout {
    private Context mContext;
    private Path path;
    private Button button;
    private int primaryColor = 0;
    private int height = 0;
    private boolean isOpen = false;
    private String title = null;
    private ImageView arrowDown;

    public interface CustomSelectSubButtonListener{
        void open();
        void close();
    }

    public boolean isOpen(){
        return this.isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    private CustomSelectSubButtonListener customSelectSubButtonListener;
    public void setCustomSelectSubButtonListener(CustomSelectSubButtonListener l){
        this.customSelectSubButtonListener = l;
    }

    public CustomSelectSubButton(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public void setButtonHeight(int _height){
        if(this.height == 0){
            this.height = _height;
        }
        button.getLayoutParams().height = height;
        arrowDown.getLayoutParams().height = height;
        button.requestLayout();
        arrowDown.requestLayout();
    }

    public void setColor(int bgColor, int txtColor){
        if(primaryColor == 0){
            primaryColor = bgColor;
        }
        if(button != null){
            button.setBackgroundColor(bgColor);
            button.setTextColor(txtColor);
        }
    }

    public CustomSelectSubButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public CustomSelectSubButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }


    private void init(){
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_select_subbutton, null, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button = (Button)v.findViewById(R.id.button);
        arrowDown = (ImageView)v.findViewById(R.id.arrow_down);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isOpen){
                    customSelectSubButtonListener.open();
                    isOpen = true;
                }
                else{
                    customSelectSubButtonListener.close();
                    isOpen = false;
                }
            }
        });
        addView(v,params);
    }

    public void setTitle(String _title, String item){
        title = _title;
        button.setText(title+ " : "+item);
    }

    public void setTitle(String item){
        setTitle(title,item);
    }



    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (path == null) {
            path = new Path();
            path.addRoundRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), 15, 15, Path.Direction.CW);
        }
        canvas.clipPath(path);

        return super.drawChild(canvas, child, drawingTime);

    }
}
