package ans.ans;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import android.os.Environment;
import java.io.File;

/**
 * Created by imam-pc on 23/09/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static String DB_PATH = "/data/data/ans.ans/databases/";

    protected static final String DATABASE_NAME = "ans.db";
    protected static final String TABLE_NAME = "customer";
    protected static final String TABLE_NAME1 = "stok";
    protected static final String TABLE_NAME2 = "pos_hd";
    protected static final String TABLE_NAME3 = "pos_dt";
    protected static final String TABLE_NAME4 = "configdb1";
    protected static final String TABLE_NAME5 = "configdb2";
    public static final String COLUMN_NO_TRANS = "notrans";
    public static final String COLUMN_TGL_TRANS = "tgl";
    public static final String COLUMN_KODE_REK = "kode_rek";
    public static final String COLUMN_NAMA_REK = "nama_rek";
    public static final String COLUMN_JENIS_BAYAR = "jenis_bayar";
    public static final String COLUMN_JML_QTY_HD = "jml_qty";
    public static final String COLUMN_JML_BERAT_HD = "jml_berat";
    public static final String COLUMN_JML_HD = "jml";
    public static final String COLUMN_JML_DIBAYAR = "jml_dibayar";
    public static final String COLUMN_IDBARANG = "idbarang";
    public static final String COLUMN_NAMABARANG = "namabarang";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_JENIS_STOK = "jenis_stok";
    public static final String COLUMN_QTY = "qty";
    public static final String COLUMN_BERAT_DT = "berat";
    public static final String COLUMN_HARGA_DT = "harga";
    public static final String COLUMN_DISKON_DT = "disk";
    public static final String COLUMN_JMLBERAT_DT = "jmlberat";
    public static final String COLUMN_HOSTNAME = "hostname";
    public static final String COLUMN_PORTID = "portid";
    public static final String COLUMN_DBNAME = "dbname";
    public static final String COLUMN_USERID = "userid";
    public static final String COLUMN_PWDID = "pwdid";


    public DBHelper(Context context) {
        super(context, Environment.getExternalStorageDirectory().toString() + File.separator + "ANSILVERPOS" + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (kode text not null ,nama text ,alamat text null,telp text null,kontak text null,email text null,pic blob, primary key(kode));";
        //String sql = "CREATE TABLE " + TABLE_NAME + " (id integer primary key,kode text null);";
        Log.d("Data","onCreate : " + sql);
        db.execSQL(sql);
        String sql1 = "CREATE TABLE " + TABLE_NAME1 + " (idbarang text ,namabarang text ,unit text null, notrans text, tgl text, kode_rek text, nama_rek text,jenis_stok text null,qty text null,berat text null,harga text null,disk text null,jmlberat text null);";
        Log.d("Data","onCreate : " + sql1);
        db.execSQL(sql1);

        sql1 = "insert into stok (idbarang,namabarang,unit,notrans,tgl,kode_rek,nama_rek,jenis_stok,qty,berat,harga,jmlberat) VALUES ('1000','CC FASHION RD','GR','DNSAL001','1/1/2017','1','SINAR JAYA','GROSIR','10','1.5','25000','15')";
        db.execSQL(sql1);

        String sql2 = "CREATE TABLE " + TABLE_NAME2 + " (notrans text not null ,tgl text ,kode_rek text, nama_rek text, jenis_bayar text, jml_qty text, jml_berat text, jml text, jml_dibayar text, waktu text, primary key(notrans));";
        Log.d("Data","onCreate : " + sql2);
        db.execSQL(sql2);
        String sql3 = "CREATE TABLE " + TABLE_NAME3 + " (notrans text,tgl text, idbarang text , namabarang text ,unit text null,jenis_stok text null,qty text null,berat text null,harga text null,disk text null,jmlberat text null);";
        Log.d("Data","onCreate : " + sql3);
        db.execSQL(sql3);
        String sql4 = "CREATE TABLE " + TABLE_NAME4 + " (hostname text not null,portid text, dbname text , userid text ,pwdid text, primary key(hostname));";
        Log.d("Data","onCreate : " + sql4);
        db.execSQL(sql4);
        String sql5 = "CREATE TABLE " + TABLE_NAME5 + " (nobukti int ,judul text,primary key(nobukti));";
        Log.d("Data","onCreate : " + sql5);
        db.execSQL(sql5);
        sql5 = "insert into " + TABLE_NAME5 + " (nobukti,judul) VALUES (1,'INV')";
        db.execSQL(sql5);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + "";
        db.execSQL(sql);

        sql= "DROP TABLE IF EXISTS " + TABLE_NAME1 + "";
        db.execSQL(sql);

        sql= "DROP TABLE IF EXISTS " + TABLE_NAME2 + "";
        db.execSQL(sql);

        sql= "DROP TABLE IF EXISTS " + TABLE_NAME3 + "";
        db.execSQL(sql);

        sql= "DROP TABLE IF EXISTS " + TABLE_NAME4 + "";
        db.execSQL(sql);

        sql= "DROP TABLE IF EXISTS " + TABLE_NAME5 + "";
        db.execSQL(sql);

        onCreate(db);
    }
}
/**
 * Created by RONALD on 5/15/2017.
 */

