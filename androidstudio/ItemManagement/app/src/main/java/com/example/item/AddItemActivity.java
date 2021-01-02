package com.example.item;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import ItemDBHelper.ItemDBHelper;
import Item.Item;
import TableContanst.TableContanst;
public class AddItemActivity extends Activity implements OnClickListener {
    private static final String TAG = "AddItemActivity";
    private final static int DATE_DIALOG = 1;
    private static final int DATE_PICKER_ID = 1;
    private TextView idText;
    private EditText nameText;
    private EditText ageText;
    private EditText limitText;
    private EditText dataText;
    private RadioGroup group;
    private RadioButton button1;
    private RadioButton button2;
    private CheckBox box1;
    private CheckBox box2;
    private CheckBox box3;
    private Button restoreButton;
    private String type;
    private Button resetButton;
    private Long itm_id;
    private ItemDao dao;
    private boolean isAdd = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        idText = (TextView) findViewById(R.id.tv_stu_id);
        nameText = (EditText) findViewById(R.id.et_name);
        ageText = (EditText) findViewById(R.id.et_age);
        button1 = (RadioButton) findViewById(R.id.rb_sex_female);
        button2 = (RadioButton) findViewById(R.id.rb_sex_male);
        limitText = (EditText) findViewById(R.id.et_limit);
        dataText = (EditText) findViewById(R.id.et_buydate);
        group = (RadioGroup) findViewById(R.id.rg_sex);
        box1 = (CheckBox) findViewById(R.id.box1);
        box2 = (CheckBox) findViewById(R.id.box2);
        box3 = (CheckBox) findViewById(R.id.box3);
        restoreButton = (Button) findViewById(R.id.btn_save);
        resetButton = (Button) findViewById(R.id.btn_clear);
        dao = new ItemDao(new ItemDBHelper(this)); // 设置监听 78
        restoreButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        dataText.setOnClickListener(this);
        checkIsAddItem();
    }
    // 检查此时Activity是否用于添加物品信息
    private void checkIsAddItem() {
        Intent intent = getIntent();
        Serializable serial = intent.getSerializableExtra(TableContanst.ITEM_TABLE);
        if (serial == null) {
            isAdd = true;
            dataText.setText(getCurrentDate());
        } else {
            isAdd = false;
            Item s = (Item) serial;
            showEditUI(s);
        }
    }
    //显示物品信息更新的UI104
    private void showEditUI(Item itm) {
        // 先将Item携带的数据还原到itm的每一个属性中去
        itm_id = itm.getId();
        String name = itm.getName();
        float money = itm.getAge();
        String limit = itm.getLimitTime();
        String data = itm.getTrainDate();
        String usetime = itm.getLike();
        String type = itm.getSex();
        if (type.toString().equals("电子产品")) {
            button2.setChecked(true);
        } else if (type.toString().equals("日用品")) {
            button1.setChecked(true);
        }
        if (usetime != null && !"".equals(usetime)) {
            if (box1.getText().toString().indexOf(usetime) >= 0) {
                box1.setChecked(true);
            }
            if (box2.getText().toString().indexOf(usetime) >= 0) {
                box2.setChecked(true);
            }
            if (box3.getText().toString().indexOf(usetime) >= 0) {
                box3.setChecked(true);
            }
        }
        // 还原数据
        idText.setText(itm_id + "");
        nameText.setText(name + "");
        ageText.setText(money + "");
        limitText.setText(limit + "");
        dataText.setText(data + "");
        setTitle("物品信息更新");
        restoreButton.setText("更新");
    }
    public void onClick(View v) {
        // 收集数据
        if (v == restoreButton) {
            if (!checkUIInput()) {// 界面输入验证
                return;
            }
            Item itm = getItemFromUI();
            if (isAdd) {
                long id = dao.addItem(itm);
                dao.closeDB();
                if (id > 0) {
                    Toast.makeText(this, "保存成功， ID=" + id,Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "保存失败，请重新输入！", Toast.LENGTH_SHORT).show();
                }
            } else if (!isAdd) {
                long id = dao.addItem(itm);
                dao.closeDB();
                if (id > 0) {
                    Toast.makeText(this, "更新成功",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "更新失败，请重新输入！",Toast.LENGTH_SHORT).show();
                }
            }
        } else if (v == resetButton) {
            clearUIData();
        } else if (v == dataText) {
            showDialog(DATE_PICKER_ID);
        }
    }
    //       清空界面的数据176
    private void clearUIData() {
        nameText.setText("");
        ageText.setText("");
        limitText.setText("");
        dataText.setText("");
        box1.setChecked(false);
        box2.setChecked(false);
        group.clearCheck();
    }
    //      收集界面输入的数据，并将封装成Item对象
    private Item getItemFromUI() {
        String name = nameText.getText().toString();
        float money = Integer.parseInt(ageText.getText().toString());
        String type = ((RadioButton) findViewById(group
                .getCheckedRadioButtonId())).getText().toString();
        String usetimer = "";
        if (box1.isChecked()) { // basketball, football football
            usetimer += box1.getText();
        }
        if (box2.isChecked()) {
            if (usetimer.equals("")) {
                usetimer += box2.getText();
            } else {
                usetimer += "," + box2.getText();
            }
            if (usetimer.equals("")) {
                usetimer += box3.getText();
            } else {
                usetimer += "," + box3.getText();
            }
        }
        String buyDate = dataText.getText().toString();
        String limitTime = limitText.getText().toString();
        String modifyDateTime = getCurrentDateTime();
        Item s=new Item(name, money, type, usetimer, limitTime, buyDate,
                modifyDateTime);
        if (!isAdd) {
            s.setId(Integer.parseInt(idText.getText().toString()));
            dao.deleteItemById(itm_id);
        }
        return s;
    }
    //      * 得到当前的日期时间
    private String getCurrentDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(new Date());
    }
    //      * 得到当前的日期
    private String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }
    //验证用户是否按要求输入了数据
    private boolean checkUIInput() { // name, money, type
        String name = nameText.getText().toString();
        String money = ageText.getText().toString();
        int id = group.getCheckedRadioButtonId();
        String message = null;
        View invadView = null;
        if (name.trim().length() == 0) {
            message = "请输入物品名称！";
            invadView = nameText;
        } else if (money.trim().length() == 0) {
            message = "请输入购买金额！";
            invadView = ageText;
        } else if (id == -1) {
            message = "请选择物品类别！";
        }
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            if (invadView != null)
                invadView.requestFocus();
            return false;
        }         return true;     }
    //时间的监听与事件
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dataText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        }
    };
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                return new DatePickerDialog(this, onDateSetListener, 2011, 8, 14);
        }
        return null;
    }
}

