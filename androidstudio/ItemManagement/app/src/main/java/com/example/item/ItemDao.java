package com.example.item;

import ItemDBHelper.ItemDBHelper;
import TableContanst.TableContanst;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.TextView;
import Item.Item;
public class ItemDao {
    private ItemDBHelper dbHelper;
    private Cursor cursor;
    public ItemDao(ItemDBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }
    // 添加一个Item对象数据到数据库表
    public long addItem(Item s) {
        ContentValues values = new ContentValues();
        values.put(TableContanst.ItemColumns.NAME, s.getName());
        values.put(TableContanst.ItemColumns.MONEY, s.getAge());
        values.put(TableContanst.ItemColumns.TYPE, s.getSex());
        values.put(TableContanst.ItemColumns.USETIMER, s.getLike());
        values.put(TableContanst.ItemColumns.PHONE_NUMBER, s.getLimitTime());
        values.put(TableContanst.ItemColumns.TRAIN_DATE, s.getTrainDate());
        values.put(TableContanst.ItemColumns.MODIFY_TIME, s.getModifyDateTime());
        return dbHelper.getWritableDatabase().insert(TableContanst.ITEM_TABLE, null, values);
    }

    // 删除一个id所对应的数据库表itm的记录
    public int deleteItemById(long id) {
        return dbHelper.getWritableDatabase().delete(TableContanst.ITEM_TABLE,
                TableContanst.ItemColumns.ID + "=?", new String[] { id + "" });
    }

    //模糊查询一条记录
    public Cursor findItem(String name){
        Cursor cursor = dbHelper.getWritableDatabase().query(TableContanst.ITEM_TABLE,  null, "name like ?",
                new String[] { "%" + name + "%" }, null, null, null,null);
        return cursor;      }

    public void closeDB() {
        dbHelper.close();     }   //自定义的方法通过View和Id得到一个itm对象

    public Item getItemFromView(View view, long id) {
        TextView nameView = (TextView) view.findViewById(R.id.tv_stu_name);
        TextView ageView = (TextView) view.findViewById(R.id.tv_stu_age);
        TextView typeView = (TextView) view.findViewById(R.id.tv_stu_sex);
        TextView usetimeView = (TextView) view.findViewById(R.id.tv_itm_usetime);
        TextView limitView = (TextView) view.findViewById(R.id.tv_itm_limit);
        TextView dataView = (TextView) view.findViewById(R.id.tv_stu_buydate);
        String name = nameView.getText().toString();
        float money = Integer.parseInt(ageView.getText().toString());
        String type = typeView.getText().toString();
        String usetime = usetimeView.getText().toString();
        String limit = limitView.getText().toString();
        String data = dataView.getText().toString();
        Item itm = new Item(id, name, money, type, usetime, limit, data,null);
        return itm;
    }

    public long initDataInfo(String id, String name, String money, String type, String usetime, String limit , String data) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("name", name);
        cv.put("money", money);
        cv.put("type", type);
        cv.put("usetime", usetime);
        cv.put("limit", limit);
        cv.put("data", data);
        //db.insert(TableContanst.ITEM_TABLE, null, cv);
        ContentValues values = new ContentValues();
        //values.put(TableContanst.ItemColumns.ID, id);
        values.put(TableContanst.ItemColumns.NAME, name);
        values.put(TableContanst.ItemColumns.MONEY, money);
        values.put(TableContanst.ItemColumns.TYPE, type);
        values.put(TableContanst.ItemColumns.USETIMER, usetime);
        values.put(TableContanst.ItemColumns.PHONE_NUMBER, limit);
        values.put(TableContanst.ItemColumns.TRAIN_DATE, data);
        return dbHelper.getWritableDatabase().insert(TableContanst.ITEM_TABLE, null, values);
    }
}