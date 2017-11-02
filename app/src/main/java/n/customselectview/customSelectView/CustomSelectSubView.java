package n.customselectview.customSelectView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import n.customselectview.R;

/**
 * Created by nam on 2017. 8. 14..
 */

public class CustomSelectSubView extends LinearLayout {
    private Path path;
    private int primaryColor;
    private int textColor;
    private Context mContext;
    private ArrayList<HashMap<String, String>> list = new ArrayList<>();
    private ArrayList<CustomSelectSubViewButton> btnList = new ArrayList<>();
    private ArrayList<String> selectedKeyList = new ArrayList<>();
    private ArrayList<String> selectedValueList = new ArrayList<>();
    private CustomSelectView.SelectMode mode = CustomSelectView.SelectMode.SINGLE;
    private int height = 0;
    private String beforeKey = "";

    public void setBeforeKey(String key){
        beforeKey = key;
    }

    public CustomSelectSubView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public void initSelected() {
        for (int i = 0; i < btnList.size(); i++) {
            btnList.get(i).initSelected();
        }
    }

    public void openSelected(){
        int idx = findKey(beforeKey);
        if(idx >= 0) {
            btnList.get(idx).setColorSelected();
        }
    }

    public int findKey(String key){
        for(int i=0; i<list.size();i++){
            Set keySet = list.get(i).keySet();

            for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
                String _key = (String)iterator.next();
                if(_key.equals(key)){
                    return i;
                }
            }
        }
        return -1;
    }

    public interface CustomSelectSubViewListener {
        void selectItem(String key, String value);

        void selectItems(String[] keys, String[] values);

        void noneSelected();
    }

    public void setSelectedKeyValueList(ArrayList<String> _selectedKeyList, ArrayList<String> _selectedValueList) {
        this.selectedKeyList = _selectedKeyList;
        this.selectedValueList = _selectedValueList;
    }

    private CustomSelectSubViewListener customSelectSubViewListener;

    public void setCustomSelectSubViewListener(CustomSelectSubViewListener l) {
        this.customSelectSubViewListener = l;
    }

    public CustomSelectSubView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private boolean isNoneSelected() {
        int count = 0;
        for (CustomSelectSubViewButton btn : btnList) {
            if (btn.isSelected()) {
                count += 1;
            }
        }
        if (count > 1) {
            return false;
        }
        return true;

    }

    public CustomSelectSubView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    public void setHeight(int _height) {
        if (height == 0) {
            height = _height;
        }

        for (int i = 0; i < btnList.size(); i++) {
            btnList.get(i).getLayoutParams().height = height;
            btnList.get(i).requestLayout();
        }
    }

    LinearLayout view;

    public int getViewHeight() {
        return height * list.size();
    }
    private void init() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_select_subview, null, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view = (LinearLayout) v.findViewById(R.id.view);
        addView(v, params);
//        setBackgroundColor(Color.BLACK);
    }

    public void addButton(ArrayList<HashMap<String, String>> _keyValuelist, int bgColor, int txtColor, CustomSelectView.SelectMode _mode) {
        mode = _mode;
        list = _keyValuelist;
        primaryColor = bgColor;
        textColor = txtColor;



        for (int i = 0; i < list.size(); i++) {
            for (String key : list.get(i).keySet()) {

                if (i == 0) {
                    addButton(key, list.get(i).get(key), this.height, true);
                } else {
                    addButton(key, list.get(i).get(key), this.height, false);
                }
            }
        }
    }

    private void addButton(String key, String value, int height, boolean isFirst) {

        if (!isFirst) {
            View line = new View(mContext);
            line.setBackgroundColor(Color.BLACK);
            LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            view.addView(line, lineParams);
        }

        CustomSelectSubViewButton btn = new CustomSelectSubViewButton(mContext);
        btn.setTextSize(15);
        btn.setKey(key);
        btn.setValue(value);
        btn.setText(value);
        btn.setPadding(0, 0, 0, 0);
        btn.setBgColor(primaryColor);
        btn.setBackgroundColor(primaryColor);
        btn.setTxtColor(textColor);
        btn.setTextColor(textColor);

        if(selectedKeyList.contains(key) && mode == CustomSelectView.SelectMode.MULTIPLE){
            btn.setSelect();
        }
        if( key.equals(beforeKey) && mode == CustomSelectView.SelectMode.SINGLE){
            btn.setColorSelected();
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        params.gravity = Gravity.CENTER;
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isPass = true;
                CustomSelectSubViewButton btn = (CustomSelectSubViewButton) view;
                String key = btn.getKey();
                String value = btn.getValue();
                switch (mode) {
                    case SINGLE:
                        customSelectSubViewListener.selectItem(key, value);
                        beforeKey = key;

                        initSelected();
                        btn.setSelect();
                        break;
                    case MULTIPLE:
                        if (btn.isSelected()) {
                            if (isNoneSelected()) {
                                //아무것도 선택되지 않은 상태면 안 됨.
                                customSelectSubViewListener.noneSelected();
                                isPass = false;
                            } else {
                                int idx = selectedKeyList.indexOf(key);
                                if (idx >= 0) {
                                    selectedKeyList.remove(idx);
                                }
                                idx = selectedValueList.indexOf(value);
                                if (idx >= 0) {
                                    selectedValueList.remove(idx);
                                }
                                btn.setDeselect();
                            }
                        } else {
                            if (!selectedKeyList.contains(key)) {
                                selectedKeyList.add(key);
                            }
                            if (!selectedValueList.contains(value)) {
                                selectedValueList.add(value);
                            }
                            btn.setSelect();
                        }
                        if (isPass) {
                            onSelectItems();

                        }
                        break;
                }
            }
        });

        btnList.add(btn);
        view.addView(btn, params);

    }

    private void onSelectItems() {
        String[] keys = new String[selectedKeyList.size()];
        String[] values = new String[selectedValueList.size()];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = selectedKeyList.get(i);
            values[i] = selectedValueList.get(i);
        }
        customSelectSubViewListener.selectItems(keys, values);

    }
    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (path == null) {
            path = new Path();
            path.addRoundRect(new RectF(1, 1, canvas.getWidth()-1, canvas.getHeight()-1), 15, 15, Path.Direction.CW);

        }
        canvas.clipPath(path);
        return super.drawChild(canvas, child, drawingTime);

    }
}
