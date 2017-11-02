package n.customselectview.customSelectView;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import n.customselectview.R;

/**
 * Created by nam on 2017. 8. 14..
 */

public class CustomSelectView extends LinearLayout {
    private Context mContext;
    private ArrayList<HashMap<String, String>> list = new ArrayList<>();
    private View v;
    private ViewGroup thisView = this;

    public CustomSelectView(Context context) {
        super(context);
        mContext = context;
        init();
    }


    private CustomSingleSelectViewListener customSingleSelectViewListener;
    private CustomMultiSelectViewListener customMultiSelectViewListener;

    public interface CustomSingleSelectViewListener {
        void selectedItem(String key);

        void onOpenView();

        void onCloseView();
    }

    public interface CustomMultiSelectViewListener {
        void selectedItems(String[] keys);

        void noneSelectedItem();

        void onOpenView();

        void onCloseView();

    }

    public void setCustomSingleSelectViewListener(CustomSingleSelectViewListener l) {
        this.customSingleSelectViewListener = l;
    }

    public void setCustomMultiSelectViewListener(CustomMultiSelectViewListener l) {
        this.customMultiSelectViewListener = l;
    }

    public enum SelectMode {
        SINGLE, MULTIPLE
    }

    private SelectMode mode = SelectMode.SINGLE;

    public CustomSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public CustomSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    CustomSelectSubButton button;
    CustomSelectSubView view;

    private void init() {
        v = LayoutInflater.from(mContext).inflate(R.layout.custom_select_view, null, false);
        button = (CustomSelectSubButton) v.findViewById(R.id.button);
        view = (CustomSelectSubView) v.findViewById(R.id.view);

        view.setCustomSelectSubViewListener(new CustomSelectSubView.CustomSelectSubViewListener() {
            @Override
            public void selectItem(String key, String value) {
                if (customSingleSelectViewListener != null) {
                    customSingleSelectViewListener.selectedItem(key);
                }
                button.setTitle(value);
                if(customSingleSelectViewListener != null){
                    customSingleSelectViewListener.onCloseView();
                }
                if(customMultiSelectViewListener != null){
                    customMultiSelectViewListener.onCloseView();
                }
                closeViewWithAnimation();

            }

            @Override
            public void selectItems(String[] keys, String[] values) {
                if (customMultiSelectViewListener != null) {
                    customMultiSelectViewListener.selectedItems(keys);
                }

                ArrayList<String> keyList = new ArrayList<>(Arrays.asList(keys));
                ArrayList<String> valueList = new ArrayList<>(Arrays.asList(values));

                setMultiButtonTitle(keyList, valueList);

            }

            @Override
            public void noneSelected() {
                if (customMultiSelectViewListener != null) {
                    customMultiSelectViewListener.noneSelectedItem();
                }
            }
        });

        button.setCustomSelectSubButtonListener(new CustomSelectSubButton.CustomSelectSubButtonListener() {
            @Override
            public void open() {
                if (mode == SelectMode.SINGLE) {
                    view.initSelected();
                }
                if(customSingleSelectViewListener != null){
                    customSingleSelectViewListener.onOpenView();
                }
                if(customMultiSelectViewListener != null){
                    customMultiSelectViewListener.onOpenView();
                }
                view.openSelected();
                int viewHeight = view.getViewHeight();
                final int originHeight = thisView.getLayoutParams().height;
                final int openHeight = thisView.getLayoutParams().height + ((LayoutParams) view.getLayoutParams()).topMargin + viewHeight;
                Animation a = new Animation() {

                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        ViewGroup.LayoutParams params = thisView.getLayoutParams();
                        params.height = (int) (originHeight + (openHeight * interpolatedTime));
                        thisView.setLayoutParams(params);
                    }
                };
                a.setDuration(300); // in ms
                thisView.startAnimation(a);
                v.getLayoutParams().height = openHeight;
                v.requestLayout();

            }

            @Override
            public void close() {
                if(customSingleSelectViewListener != null){
                    customSingleSelectViewListener.onCloseView();
                }
                if(customMultiSelectViewListener != null){
                    customMultiSelectViewListener.onCloseView();
                }
                closeViewWithAnimation();

            }
        });


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        addView(v, params);
    }

    public void setMultiData(String title, ArrayList<HashMap<String, String>> keyValueList, ArrayList<String> beforeKeys, int _bgColor, int _txtColor, SelectMode _mode) {
        this.list = keyValueList;
        this.mode = _mode;
        if (keyValueList.size() > 0) {
            button.setTitle(title, "전체");
        }

        ArrayList<String> beforeValues = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Set keySet = list.get(i).keySet();
            for (Iterator iterator = keySet.iterator(); iterator.hasNext(); ) {
                String _key = (String) iterator.next();
                for (int j = 0; j < beforeKeys.size(); j++) {
                    if (_key.equals(beforeKeys.get(j))) {
                        beforeValues.add(list.get(i).get(_key));
                    }
                }
            }
        }

        setMultiButtonTitle(beforeKeys, beforeValues);
        button.setColor(_bgColor, _txtColor);
        view.setSelectedKeyValueList(beforeKeys, beforeValues);
        view.addButton(list, _txtColor, _bgColor, _mode);
    }

    public void setSingleData(String title, ArrayList<HashMap<String, String>> keyValueList, String beforeKey, int _bgColor, int _txtColor, SelectMode _mode) {
        this.list = keyValueList;
        this.mode = _mode;
        if (keyValueList.size() > 0) {
            button.setTitle(title, "전체");
        }
        button.setColor(_bgColor, _txtColor);
        button.setTitle(findValue(beforeKey));
        view.setBeforeKey(beforeKey);
        view.addButton(list, _txtColor, _bgColor, _mode);
    }

    public String findValue(String key) {
        for (int i = 0; i < list.size(); i++) {
            Set keySet = list.get(i).keySet();

            for (Iterator iterator = keySet.iterator(); iterator.hasNext(); ) {
                iterator.next();
                String _value = list.get(i).get(key);
                if (_value != null) {
                    return _value;
                }
            }
        }
        return "전체";
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        view.setHeight(canvas.getHeight());
        button.setButtonHeight(canvas.getHeight());
        super.dispatchDraw(canvas);
    }

    public void closeView() {
        if (button.isOpen()) {
            ViewGroup.LayoutParams params = thisView.getLayoutParams();
            params.height = button.getHeight();
            thisView.setLayoutParams(params);

            button.setOpen(false);
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }

    private void setMultiButtonTitle(ArrayList<String> keys, ArrayList<String> values) {
        if (keys.size() == list.size()) {
            button.setTitle("전체");
        } else if (keys.size() == 0) {
            button.setTitle("전체");
        } else if (keys.size() == 1) {
            button.setTitle(values.get(0));
        } else {
            button.setTitle(values.size() + "개");
        }
    }

    public void closeViewWithAnimation() {
        if (button.isOpen()) {

            final int originHeight = button.getHeight();
            final int closeHeight = thisView.getLayoutParams().height;
            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    ViewGroup.LayoutParams params = thisView.getLayoutParams();
                    params.height = (int) (originHeight + (closeHeight * (1.0 - interpolatedTime)));
                    thisView.setLayoutParams(params);
                }
            };
            a.setDuration(300); // in ms
            thisView.startAnimation(a);
            button.setOpen(false);
        }

    }
}
