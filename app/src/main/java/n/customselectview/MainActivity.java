package n.customselectview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import n.customselectview.customSelectView.CustomSelectView;

public class MainActivity extends AppCompatActivity {

    private CustomSelectView singleSelectView;
    private CustomSelectView multiSelectView;
    private TextView singleResult;
    private TextView multiResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        singleSelectView = (CustomSelectView)findViewById(R.id.single_select_view);
        multiSelectView =  (CustomSelectView)findViewById(R.id.multi_select_view);
        singleResult = (TextView)findViewById(R.id.single_result);
        multiResult =  (TextView) findViewById(R.id.multi_result);

        String[][] str = {{"1", "One"}, {"2", "Two"}, {"3", "Three"}, {"4", "Four"}};
        ArrayList<HashMap<String,String>> map = new ArrayList<>();
        ArrayList<String> selectKeys = new ArrayList<>();
        for(String[] item : str){
            final String[] tmp = item;
            map.add(new HashMap<String, String>(){{
                put(tmp[0], tmp[1]);
            }});
            selectKeys.add(tmp[0]);
        }

        singleSelectView.setSingleData("single", map, "1", Color.rgb(150,30,30), Color.argb(240,250,250,250), CustomSelectView.SelectMode.SINGLE);
        multiSelectView.setMultiData("single", map, selectKeys, Color.rgb(150,30,30), Color.argb(240,250,250,250), CustomSelectView.SelectMode.MULTIPLE);

        singleSelectView.setCustomSingleSelectViewListener(new CustomSelectView.CustomSingleSelectViewListener() {
            @Override
            public void selectedItem(String key) {
                singleResult.setText(key);
            }

            @Override
            public void onOpenView() {

            }

            @Override
            public void onCloseView() {

            }
        });
        multiSelectView.setCustomMultiSelectViewListener(new CustomSelectView.CustomMultiSelectViewListener() {
            @Override
            public void selectedItems(String[] keys) {
                multiResult.setText(Arrays.toString(keys));
            }

            @Override
            public void noneSelectedItem() {

            }

            @Override
            public void onOpenView() {

            }

            @Override
            public void onCloseView() {

            }
        });
    }
}
