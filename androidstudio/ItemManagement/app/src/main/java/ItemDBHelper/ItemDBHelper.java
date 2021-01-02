package ItemDBHelper;

import TableContanst.TableContanst;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class ItemDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "ItemDBHelper";
    public static final String DB_NAME = "itm_manager.db";
    public static final int VERSION = 1;    //构造方法
    public ItemDBHelper(Context context, String name, CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }
    public ItemDBHelper(Context context) {
        this(context, DB_NAME, null, VERSION);     }

    //创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, "onCreate");
        db.execSQL("create table "
                + TableContanst.ITEM_TABLE                 + "(_id Integer primary key AUTOINCREMENT,"
                + "name char,money float, type char, usetimer char, limit_time char,buy_date date, "
                + "modify_time DATETIME)");     }
    //更新数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(TAG, "onUpgrade");
    }
}
