package com.example.item;

import ItemDBHelper.ItemDBHelper;
import TableContanst.TableContanst;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
public class ItemSearch extends Activity implements OnClickListener {
    private EditText nameText;
    private Button button;
    private Button reButton;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;
    private ListView listView;
    private ItemDao dao;
    private Button returnButton;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        nameText = (EditText) findViewById(R.id.et_srarch);
        layout=(LinearLayout) findViewById(R.id.linersearch);
        button = (Button) findViewById(R.id.bn_sure_search);
        reButton = (Button) findViewById(R.id.bn_return);
        listView = (ListView) findViewById(R.id.searchListView);
        returnButton = (Button) findViewById(R.id.return_id);
        dao = new ItemDao(new ItemDBHelper(this));


        reButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            reButton.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            nameText.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
            String name = nameText.getText().toString();
            cursor = dao.findItem(name);
            if (!cursor.moveToFirst()) {
                Toast.makeText(this, "没有所查物品信息！", Toast.LENGTH_SHORT).show();
            } else
                //如果有所查询的信息，则将查询结果显示出来
                adapter = new SimpleCursorAdapter(this, R.layout.find_item_list_item,
                        cursor, new String[] { TableContanst.ItemColumns.ID,
                        TableContanst.ItemColumns.NAME,
                        TableContanst.ItemColumns.MONEY,
                        TableContanst.ItemColumns.TYPE,
                        TableContanst.ItemColumns.USETIMER,
                        TableContanst.ItemColumns.PHONE_NUMBER,
                        TableContanst.ItemColumns.TRAIN_DATE },
                        new int[] {
                                R.id.tv_stu_id,
                                R.id.tv_stu_name,
                                R.id.tv_stu_age,
                                R.id.tv_stu_sex,
                                R.id.tv_itm_usetime,
                                R.id.tv_itm_limit,
                                R.id.tv_stu_buydate });
            listView.setAdapter(adapter);
        }else if(v==reButton|v==returnButton){
            finish();
        }
    }
}
