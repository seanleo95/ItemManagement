package com.example.item;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import ItemDBHelper.ItemDBHelper;
import Item.Item;
//import AddItemActivity;
import TableContanst.TableContanst;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ItemListActivity extends ListActivity implements
        OnClickListener, OnItemClickListener, OnItemLongClickListener {

    private static final String TAG = "TestSQLite";
    private Button addItem;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;
    private ListView listView;
    private List<Long> list;
    private RelativeLayout relativeLayout;
    private Button searchButton;
    private Button selectButton;
    private Button deleteButton;
    private Button selectAllButton;
    private Button cancelButton;
    private LinearLayout layout;
    private ItemDao dao;
    private Item itm;
    private Boolean isDeleteList = false;
    private TextView numberTextView;
    private TextView nameTextView;
    private TextView typeTextView;
    private int themeType;
    private ImageView imageView;
    private int isChanged = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //设置主题
        themeType = getSharedPreferences("theme", MODE_PRIVATE).getInt("themeType", 0);
        if (themeType == 0) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.AppTheme2);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.e(TAG, "onCreate");
        list = new ArrayList<Long>();
        itm = new Item();
        dao = new ItemDao(new ItemDBHelper(this));
        addItem = (Button) findViewById(R.id.btn_add_itm);
        searchButton = (Button) findViewById(R.id.bn_search_id);
        selectButton = (Button) findViewById(R.id.bn_select);
        deleteButton = (Button) findViewById(R.id.bn_delete);
        selectAllButton = (Button) findViewById(R.id.bn_selectall);
        cancelButton = (Button) findViewById(R.id.bn_canel);
        layout = (LinearLayout) findViewById(R.id.showLiner);
        relativeLayout=(RelativeLayout) findViewById(R.id.RelativeLayout);
        listView = getListView();
        numberTextView=findViewById(R.id.number);
        nameTextView=findViewById(R.id.name);
        typeTextView=findViewById(R.id.type);
        imageView=findViewById(R.id.image);

        // 为按键设置监听
        addItem.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        selectButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        selectAllButton.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        listView.setOnCreateContextMenuListener(this);
        numberTextView.setOnClickListener(this);
        nameTextView.setOnClickListener(this);
        typeTextView.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    // 调用load()方法将数据库中的所有记录显示在当前页面
    @Override
    protected void onStart() {
        super.onStart();
        load();
    }

    public  void onClick(View v) {
        // 跳转到添加信息的界面
        if (v == addItem) {
            startActivity(new Intent(ItemListActivity.this, AddItemActivity.class));
        } else if (v == searchButton) {
            // 跳转到查询界面
            startActivity(new Intent(this, ItemSearch.class));
        } else if (v == selectButton) {
            // 跳转到选择界面
            isDeleteList = !isDeleteList;
            if (isDeleteList) {
                checkOrClearAllCheckboxs(true);
            } else {
                showOrHiddenCheckBoxs(false);
            }
        } else if (v == deleteButton) {
            // 删除数据
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    long id = list.get(i);
                    Log.e(TAG, "delete id=" + id);
                    int count = dao.deleteItemById(id);
                }
                dao.closeDB();
                load();
            }
        } else if (v == cancelButton) {
            // 点击取消，回到初始界面
            load();
            layout.setVisibility(View.GONE);
            isDeleteList = !isDeleteList;
        } else if (v == selectAllButton) {
            // 全选，如果当前全选按钮显示是全选，则在点击后变为取消全选，如果当前为取消全选，则在点击后变为全选
            selectAllMethods();
        }else if (v==numberTextView) {
            //按序号排序
            idload();
            layout.setVisibility(View.GONE);
            isDeleteList = !isDeleteList;
        }else if (v==nameTextView) {
            //按名称排序
            nameload();
            layout.setVisibility(View.GONE);
            isDeleteList = !isDeleteList;
        }else if (v==typeTextView) {
            //按类别排序
            typeload();
            layout.setVisibility(View.GONE);
            isDeleteList = !isDeleteList;
        }else if (v==imageView) {
            if(isChanged==1){
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.d));
                isChanged=2;
            }else
            {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.c));
                isChanged=1;
            }
        }
    }

    // 创建菜单
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        MenuInflater inflater = new MenuInflater(this); //getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    }

    // 对菜单中的按钮添加响应事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        itm = (Item) listView.getTag();
        Log.v(TAG, "TestSQLite++++itm+" + listView.getTag() + "");
        final long itm_id = itm.getId();
        Intent intent = new Intent();
        Log.v(TAG, "TestSQLite+++++++id"+itm_id);
        switch (item_id) {
            /* 添加
            case R.id.add:
                startActivity(new Intent(this, AddItemActivity.class));
                break;*/
            // 删除
            case R.id.delete:
                deleteItemInformation(itm_id);
                break;
            case R.id.look:
                // 查看物品信息
                Log.v(TAG, "TestSQLite+++++++look"+itm+"");
                intent.putExtra("itm", itm);
                intent.setClass(this, ShowItemActivity.class);
                this.startActivity(intent);
                break;
            case R.id.write:
                // 修改物品信息
                intent.putExtra("itm", itm);
                intent.setClass(this, AddItemActivity.class);
                this.startActivity(intent);
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        Item itm = (Item) dao.getItemFromView(view, id);
        listView.setTag(itm);
        registerForContextMenu(listView);
        return false;
    }

    // 点击一条记录是触发的事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (!isDeleteList) {
            itm = dao.getItemFromView(view, id);
            Log.e(TAG, "itm*****" + dao.getItemFromView(view, id));
            Intent intent = new Intent();
            intent.putExtra("itm", itm);
            intent.setClass(this, ShowItemActivity.class);
            this.startActivity(intent);
        } else {
            CheckBox box = (CheckBox) view.findViewById(R.id.cb_box);
            box.setChecked(!box.isChecked());
            list.add(id);
            deleteButton.setEnabled(box.isChecked());
        }
    }

    // 自定义一个加载数据库中的全部记录到当前页面的无参方法
    public void load() {
        ItemDBHelper itmDBHelper = new ItemDBHelper(
                ItemListActivity.this);
        SQLiteDatabase database = itmDBHelper.getWritableDatabase();

        cursor = database.query(TableContanst.ITEM_TABLE, null, null, null,
                null, null, TableContanst.ItemColumns.MODIFY_TIME + " asc");
        startManagingCursor(cursor);
        adapter = new SimpleCursorAdapter(this, R.layout.item_list_item,
                cursor, new String[] { TableContanst.ItemColumns.ID,
                TableContanst.ItemColumns.NAME,
                TableContanst.ItemColumns.MONEY,
                TableContanst.ItemColumns.TYPE,
                TableContanst.ItemColumns.USETIMER,
                TableContanst.ItemColumns.PHONE_NUMBER,
                TableContanst.ItemColumns.TRAIN_DATE }, new int[] {
                R.id.tv_stu_id, R.id.tv_stu_name, R.id.tv_stu_age,
                R.id.tv_stu_sex, R.id.tv_itm_usetime, R.id.tv_itm_limit,
                R.id.tv_stu_buydate });
        listView.setAdapter(adapter);
    }
    //按序号显示数据库
    public void idload() {
        ItemDBHelper itmDBHelper = new ItemDBHelper(
                ItemListActivity.this);
        SQLiteDatabase database = itmDBHelper.getWritableDatabase();

        cursor = database.query(TableContanst.ITEM_TABLE, null, null, null,
                null, null, TableContanst.ItemColumns.ID + " asc");
        startManagingCursor(cursor);
        adapter = new SimpleCursorAdapter(this, R.layout.item_list_item,
                cursor, new String[] { TableContanst.ItemColumns.ID,
                TableContanst.ItemColumns.NAME,
                TableContanst.ItemColumns.MONEY,
                TableContanst.ItemColumns.TYPE,
                TableContanst.ItemColumns.USETIMER,
                TableContanst.ItemColumns.PHONE_NUMBER,
                TableContanst.ItemColumns.TRAIN_DATE }, new int[] {
                R.id.tv_stu_id, R.id.tv_stu_name, R.id.tv_stu_age,
                R.id.tv_stu_sex, R.id.tv_itm_usetime, R.id.tv_itm_limit,
                R.id.tv_stu_buydate });
        listView.setAdapter(adapter);
    }
    //按名称显示数据库
    public void nameload() {
        ItemDBHelper itmDBHelper = new ItemDBHelper(
                ItemListActivity.this);
        SQLiteDatabase database = itmDBHelper.getWritableDatabase();

        cursor = database.query(TableContanst.ITEM_TABLE, null, null, null,
                null, null, TableContanst.ItemColumns.NAME + " asc");
        startManagingCursor(cursor);
        adapter = new SimpleCursorAdapter(this, R.layout.item_list_item,
                cursor, new String[] { TableContanst.ItemColumns.ID,
                TableContanst.ItemColumns.NAME,
                TableContanst.ItemColumns.MONEY,
                TableContanst.ItemColumns.TYPE,
                TableContanst.ItemColumns.USETIMER,
                TableContanst.ItemColumns.PHONE_NUMBER,
                TableContanst.ItemColumns.TRAIN_DATE }, new int[] {
                R.id.tv_stu_id, R.id.tv_stu_name, R.id.tv_stu_age,
                R.id.tv_stu_sex, R.id.tv_itm_usetime, R.id.tv_itm_limit,
                R.id.tv_stu_buydate });
        listView.setAdapter(adapter);
    }
    //按类别显示数据库
    public void typeload() {
        ItemDBHelper itmDBHelper = new ItemDBHelper(
                ItemListActivity.this);
        SQLiteDatabase database = itmDBHelper.getWritableDatabase();

        cursor = database.query(TableContanst.ITEM_TABLE, null, null, null,
                null, null, TableContanst.ItemColumns.TYPE + " asc");
        startManagingCursor(cursor);
        adapter = new SimpleCursorAdapter(this, R.layout.item_list_item,
                cursor, new String[] { TableContanst.ItemColumns.ID,
                TableContanst.ItemColumns.NAME,
                TableContanst.ItemColumns.MONEY,
                TableContanst.ItemColumns.TYPE,
                TableContanst.ItemColumns.USETIMER,
                TableContanst.ItemColumns.PHONE_NUMBER,
                TableContanst.ItemColumns.TRAIN_DATE }, new int[] {
                R.id.tv_stu_id, R.id.tv_stu_name, R.id.tv_stu_age,
                R.id.tv_stu_sex, R.id.tv_itm_usetime, R.id.tv_itm_limit,
                R.id.tv_stu_buydate });
        listView.setAdapter(adapter);
    }

    // 全选或者取消全选
    private void checkOrClearAllCheckboxs(boolean b) {
        int childCount = listView.getChildCount();
        Log.e(TAG, "list child size=" + childCount);
        for (int i = 0; i < childCount; i++) {
            View view = listView.getChildAt(i);
            if (view != null) {
                CheckBox box = (CheckBox) view.findViewById(R.id.cb_box);
                box.setChecked(!b);
            }
        }
        showOrHiddenCheckBoxs(true);
    }

    // 显示或者隐藏自定义菜单
    private void showOrHiddenCheckBoxs(boolean b) {
        int childCount = listView.getChildCount();
        Log.e(TAG, "list child size=" + childCount);
        for (int i = 0; i < childCount; i++) {
            View view = listView.getChildAt(i);
            if (view != null) {
                CheckBox box = (CheckBox) view.findViewById(R.id.cb_box);
                int visible = b ? View.VISIBLE : View.GONE;
                box.setVisibility(visible);
                layout.setVisibility(visible);
                deleteButton.setEnabled(false);
            }
        }
    }

    // 自定义一个利用对话框形式进行数据的删除

    private void deleteItemInformation(final long delete_id) {
        // 利用对话框的形式删除数据
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("物品信息删除")
                .setMessage("确定删除所选记录?")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int raws = dao.deleteItemById(delete_id);
                        layout.setVisibility(View.GONE);
                        isDeleteList = !isDeleteList;
                        load();
                        if (raws > 0) {
                            Toast.makeText(ItemListActivity.this, "删除成功!",
                                    Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(ItemListActivity.this, "删除失败!",
                                    Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // 点击全选事件时所触发的响应
    private void selectAllMethods() {
        // 全选，如果当前全选按钮显示是全选，则在点击后变为取消全选，如果当前为取消全选，则在点击后变为全选
        if (selectAllButton.getText().toString().equals("全选")) {
            int childCount = listView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = listView.getChildAt(i);
                if (view != null) {
                    CheckBox box = (CheckBox) view.findViewById(R.id.cb_box);
                    box.setChecked(true);
                    deleteButton.setEnabled(true);
                    selectAllButton.setText("取消全选");
                }
            }
        } else if (selectAllButton.getText().toString().equals("取消全选")) {
            checkOrClearAllCheckboxs(true);
            deleteButton.setEnabled(false);
            selectAllButton.setText("全选");
        }
    }

    //setting
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.setting,menu);
        return true;
    }
    public int item1;
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.leading:
                //使用引导
                startActivity(new Intent(this,LeadActivity.class));
                return true;
            case R.id.localdata:
                //导入本地数据
                importSheet();
                load();
                break;
            case R.id.developer:
                //回馈开发人员
                startActivity(new Intent(this,DeveloperActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //读取Excel数据
    private void importSheet() {
        try {
            InputStream is = getResources().getAssets().open("excel.xls");
            Workbook book = Workbook.getWorkbook(is);
            Sheet sheet = book.getSheet(0);
            for (int j = 0; j < sheet.getRows(); ++j) {
                dao.initDataInfo(sheet.getCell(0, j).getContents(), sheet.getCell(1, j).getContents(), sheet.getCell(2, j).getContents(), sheet.getCell(3, j).getContents(), sheet.getCell(4, j).getContents(), sheet.getCell(5, j).getContents(), sheet.getCell(6, j).getContents());
            }
            book.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
    }

    //改变主题
    public void changeTheme(MenuItem item) {
        themeType = themeType == 0 ? 1 : 0;
        getSharedPreferences("theme", MODE_PRIVATE).edit().putInt("themeType", themeType).commit();
        recreate();
        //会丢失当前页面的状态，需要保持的数据做持久化保持
    }
}