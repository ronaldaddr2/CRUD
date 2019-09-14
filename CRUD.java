package ans.ans;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import ans.ans.Function_RowsItemBarang;
import android.text.Html;

public class CRUD extends DBHelper{
    public CRUD(Context context) {
        super(context);
    }
    public boolean createData(String data) {

        //ContentValues values = new ContentValues();
        //values.put("INPUT", data);

        //SQLiteDatabase db = this.getWritableDatabase();
        //boolean create =  db.insert(TABLE_NAME, null, values) > 0;
        //db.close();
        boolean create=true;
        return create;
    }
    public boolean updateData(String data, String whereData) {
        ContentValues values = new ContentValues();
        values.put("INPUT", data);

        SQLiteDatabase db = this.getWritableDatabase();
        boolean update = db.update(TABLE_NAME, values, " INPUT = ? ", new String[]{whereData}) > 0;
        db.close();
        return update;
    }

    public boolean UpdateConfig1(Context context,ArrayList<HashMap<String, String>> hdrow) {
    try{
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "update " + TABLE_NAME4 + " set " +
                COLUMN_HOSTNAME.toString() + "='" + hdrow.get(0).get("hostname").toString() + "'," +
                COLUMN_PORTID.toString() + "='" + hdrow.get(0).get("portid").toString() + "'," +
                COLUMN_USERID.toString() + "='" + hdrow.get(0).get("userid").toString() + "'," +
                COLUMN_PWDID.toString() + "='" + hdrow.get(0).get("pwdid").toString() + "'";
        db.beginTransaction();
        db.execSQL(sql);
        db.setTransactionSuccessful();
        db.endTransaction();
        //Toast.makeText(context,sql , Toast.LENGTH_SHORT).show();
        return true;
    }
    catch (SQLException e){
        e.printStackTrace();
        return false;
    }
    }



    public boolean AddData(Context context, ArrayList<HashMap<String, String>> hdrowtrans,ArrayList<HashMap<String, String>> dtrowtrans) {
        Function_RowsItemBarang func_dec=new Function_RowsItemBarang();
        try {
            Cursor cursor = null;
            SQLiteDatabase db = this.getWritableDatabase();
            int noinv=Integer.valueOf(getAutoNoInvoice().toString());
            String sql = "insert into " + TABLE_NAME2 + "(" +
                    COLUMN_NO_TRANS.toString() + "," +
                    COLUMN_TGL_TRANS.toString() + "," +
                    COLUMN_KODE_REK.toString() + "," +
                    COLUMN_NAMA_REK.toString() + "," +
                    COLUMN_JENIS_BAYAR.toString() + "," +
                    COLUMN_JML_QTY_HD.toString() + "," +
                    COLUMN_JML_BERAT_HD.toString() + "," +
                    COLUMN_JML_HD.toString() + "," +
                    COLUMN_JML_DIBAYAR.toString() + ")" +
                    " VALUES (" +
                    "'" + hdrowtrans.get(0).get("notrans").toString() + "'," +
                    "'" + hdrowtrans.get(0).get("tgl").toString() + "'," +
                    "'" + hdrowtrans.get(0).get("kode_rek").toString() + "'," +
                    "'" + hdrowtrans.get(0).get("nama_rek").toString() + "'," +
                    "'" + hdrowtrans.get(0).get("jenis_bayar").toString() + "'," +
                    "'" + hdrowtrans.get(0).get("jml_qty").toString() + "'," +
                    "'" + hdrowtrans.get(0).get("jml_berat").toString() + "'," +
                    "'" + func_dec.JmlField(hdrowtrans,"jml") + "'," +
                    "'" + func_dec.JmlField(hdrowtrans,"jml_dibayar") + "')";
            db.beginTransaction();
            db.execSQL(sql);

            //detil transaksi
            String SqlDetilTrans=new String();;
            for (int cc=0; cc < dtrowtrans.size(); cc++)
            {

                SqlDetilTrans="insert into " + TABLE_NAME3 + "(" +
                        COLUMN_NO_TRANS.toString() + "," +
                        COLUMN_TGL_TRANS.toString() + "," +
                        COLUMN_IDBARANG.toString() + "," +
                        COLUMN_NAMABARANG.toString() + "," +
                        COLUMN_UNIT.toString() + "," +
                        COLUMN_QTY.toString() + "," +
                        COLUMN_BERAT_DT.toString() + "," +
                        COLUMN_HARGA_DT.toString() + "," +
                        COLUMN_DISKON_DT.toString() + "," +
                        COLUMN_JMLBERAT_DT.toString() + ")" +
                        " VALUES (" +
                        "'" + hdrowtrans.get(0).get("notrans").toString() + "'," +
                        "'" + hdrowtrans.get(0).get("tgl").toString() + "'," +
                        "'" + dtrowtrans.get(cc).get("kode").toString() + "'," +
                        "'" + dtrowtrans.get(cc).get("description").toString() + "'," +
                        "'" + dtrowtrans.get(cc).get("unit").toString() + "'," +
                        "'" + func_dec.JmlFieldCustom(dtrowtrans.get(cc).get("qty").toString()) + "'," +
                        "'" + func_dec.JmlFieldCustom(dtrowtrans.get(cc).get("berat").toString()) + "'," +
                        "'" + func_dec.JmlFieldCustom(dtrowtrans.get(cc).get("harga").toString()) + "'," +
                        "'" + func_dec.JmlFieldCustom(dtrowtrans.get(cc).get("disc").toString()) + "'," +
                        "'" + func_dec.JmlFieldCustom(dtrowtrans.get(cc).get("jmlberat").toString()) + "');";
                db.execSQL(SqlDetilTrans);
            }
            //detil transaksi


            //UPDATE NEXT NO INVOICE
            String sqlINV ="update configdb2 set nobukti=" + (noinv+1) ;
            db.execSQL(sqlINV);
            //UPDATE NEXT NO INVOICE

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            Log.d("Data", "onAddNewRow : " + sql);
            return true;
        }catch (SQLException ex){
            this.close();
            Toast.makeText(context,"NOMOR TRANSAKSI SUDAH ADA" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public boolean AddInboxStok(Context context,ArrayList<HashMap<String, String>> dtrowtrans) {
        Function_RowsItemBarang func_dec = new Function_RowsItemBarang();
        try {
            Cursor cursor = null;
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();

            //detil transaksi
            String SqlDetilTrans = new String();
            ;
            for (int cc = 0; cc < dtrowtrans.size(); cc++) {

                SqlDetilTrans = "insert into " + TABLE_NAME1 + "(" +
                        COLUMN_NO_TRANS.toString() + "," +
                        COLUMN_TGL_TRANS.toString() + "," +
                        COLUMN_IDBARANG.toString() + "," +
                        COLUMN_NAMABARANG.toString() + "," +
                        COLUMN_UNIT.toString() + "," +
                        COLUMN_QTY.toString() + "," +
                        COLUMN_BERAT_DT.toString() + "," +
                        COLUMN_HARGA_DT.toString() + "," +
                        COLUMN_DISKON_DT.toString() + "," +
                        COLUMN_JMLBERAT_DT.toString() + ")" +
                        " VALUES (" +
                        "'" + dtrowtrans.get(0).get("notrans").toString() + "'," +
                        "'" + dtrowtrans.get(0).get("tgl").toString() + "'," +
                        "'" + dtrowtrans.get(cc).get("kode").toString() + "'," +
                        "'" + dtrowtrans.get(cc).get("description").toString() + "'," +
                        "'" + dtrowtrans.get(cc).get("unit").toString() + "'," +
                        "'" + func_dec.JmlFieldCustom(dtrowtrans.get(cc).get("qty").toString()) + "'," +
                        "'" + func_dec.JmlFieldCustom(dtrowtrans.get(cc).get("berat").toString()) + "'," +
                        "'" + func_dec.JmlFieldCustom(dtrowtrans.get(cc).get("harga").toString()) + "'," +
                        "'" + func_dec.JmlFieldCustom(dtrowtrans.get(cc).get("disc").toString()) + "'," +
                        "'" + func_dec.JmlFieldCustom(dtrowtrans.get(cc).get("jmlberat").toString()) + "');";
                db.execSQL(SqlDetilTrans);
            }
            //detil transaksi
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            Log.d("Data", "onAddNewRow : " + SqlDetilTrans);
            return true;
        } catch (SQLException ex) {
            this.close();
            //Toast.makeText(context,"NOMOR TRANSAKSI SUDAH ADA" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public String getAutoNoInvoice() {
        Cursor cursor = null;
        String StrKode = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT NOBUKTI FROM " + TABLE_NAME5, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                StrKode = cursor.getString(cursor.getColumnIndex("nobukti"));
            }
     /*'*/       return StrKode;
        } finally {
            cursor.close();
        }
    }

    public Cursor getTableCust() {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        } catch (Exception e){
            cursor.close();
        }
        return cursor;
    }

    public Cursor getTableTransaksi() {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME2, null);
        } catch (Exception e){
            cursor.close();
        }
        return cursor;
    }

    public Cursor getTableConfig1() {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME4, null);
        } catch (Exception e){
            cursor.close();
        }
        return cursor;
    }

    public Cursor getCustomTableTransaksi(String strsql) {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME2 + " WHERE NOTRANS='" + strsql + "'" , null);
        } catch (Exception e){
            cursor.close();
        }
        return cursor;
    }

    public Cursor getTableDetTransaksi(String strsql) {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME3 + " WHERE NOTRANS='" + strsql + "'" , null);
        } catch (Exception e){
            cursor.close();
        }
        return cursor;
    }

    public Cursor getTableItemBarang(String IdBarang) {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME1 + " WHERE idbarang='" + IdBarang.toString() + "' LIMIT 1", null);
        } catch (Exception e){
            cursor.close();
        }
        return cursor;
    }

    public String getNoInvoice() {
        Cursor cursor = null;
        String NOBUKTI="";

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + this.TABLE_NAME5 + " limit 1", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                NOBUKTI=cursor.getString(1).toString() + String.format("%1$04d",cursor.getInt(0)) ;
                //String.format("%1$04d",notemp)
            }
     /*'*/       return NOBUKTI;
        } finally {
            cursor.close();
        }
    }

    public String sendData_ori(String IDNostruk){
        Function_RowsItemBarang func_dec=new Function_RowsItemBarang();
        final StringBuilder textSb = new StringBuilder();
        final Cursor cursor;
        cursor= getCustomTableTransaksi(IDNostruk);
        cursor.moveToFirst();
        byte[] cmd=new byte[3];
        cmd[0]=0x1b;
        cmd[1]=0x21;
        cmd[2]=0x10;
        byte[]INIT={0x1B, '@' };
        byte[]RIGHT_ALIGN={0x1B, 'a', 0x00 };


        for (int cc=0; cc < cursor.getCount(); cc++)


        {
            cursor.moveToPosition(cc);
            try {
                //textSb.append("-------------------------------" + "\n" );
                textSb.append("===============================" + "\n" );
                //textSb.append(String.format("%15s",String.valueOf("-------------------------------").length()) + "\n" );
                //textSb.append(Space);
                textSb.append("STRUK PEMBAYARAN ANSILVER" + "\n" );



                textSb.append("===============================" + "\n" );
                textSb.append(String.format("%1s","Nomor") + String.format("%4s",":") + String.format("%15s",cursor.getString(cursor.getColumnIndex("notrans"))) + "\n" );
                //textSb.append(String.format("%2s","Nomor") +   ":" + String.format("%1s",cursor.getString(cursor.getColumnIndex("notrans"))) + "\n" );
                //length=9
                textSb.append(String.format("%1s","Tanggal") + String.format("%2s",":") + String.format("%15s",cursor.getString(cursor.getColumnIndex("tgl"))) + "\n" );
                //textSb.append("Tanggal" +   ":" + String.format("%1s",cursor.getString(cursor.getColumnIndex("tgl"))) + "\n" );

                //textSb.append(String.format("%1s","Nama") + String.format("%5s",":") + String.format("%15s",cursor.getString(cursor.getColumnIndex("kode_rek"))+ "-" + cursor.getString(cursor.getColumnIndex("nama_rek"))) + "\n" );
                textSb.append(String.format("%1s","Nama") + String.format("%5s",":") + String.format("%15s", cursor.getString(cursor.getColumnIndex("nama_rek"))) + "\n" );
                textSb.append(String.format("%1s","Jenis") + String.format("%4s",":") + String.format("%15s",cursor.getString(cursor.getColumnIndex("jenis_bayar"))) + "\n" );
                textSb.append("===============================" + "\n" );
                textSb.append("\n" );
                final Cursor cursorDet;
                cursorDet= getTableDetTransaksi(IDNostruk);
                cursorDet.moveToFirst();
                for (int cc1=0; cc1 < cursorDet.getCount(); cc1++)
                {
                    cursorDet.moveToPosition(cc1);
                    textSb.append(cursorDet.getString(cursorDet.getColumnIndex("idbarang")) + " " + cursorDet.getString(cursorDet.getColumnIndex("namabarang")) + "  " + cursorDet.getString(cursorDet.getColumnIndex("berat")) + "  GR" + "\n" );
                    textSb.append(cursorDet.getString(cursorDet.getColumnIndex("jmlberat")) + " " + cursorDet.getString(cursorDet.getColumnIndex("unit"))  + " x " + func_dec.FormatDecimalItems(Double.valueOf(cursorDet.getString(cursorDet.getColumnIndex("harga")))) + "\n" );
                    func_dec.setM_QTY(Double.valueOf(cursorDet.getString(cursorDet.getColumnIndex("qty"))));
                    func_dec.setM_BERAT(Double.valueOf(cursorDet.getString(cursorDet.getColumnIndex("berat"))));
                    func_dec.setM_JMLBERAT(Double.valueOf(cursorDet.getString(cursorDet.getColumnIndex("jmlberat"))));
                    func_dec.setM_HS(Double.valueOf(cursorDet.getString(cursorDet.getColumnIndex("harga"))));
                    func_dec.setM_DISC(Double.valueOf(cursorDet.getString(cursorDet.getColumnIndex("disk"))));
                    if(cursorDet.getString(cursorDet.getColumnIndex("idbarang")).matches("GROSIR")){
                        textSb.append( "                   " + String.format("%10s",func_dec.FormatDecimalItems(func_dec.jumlah()).toString()) + "\n" );
                    }else{
                        textSb.append( "                   " + String.format("%10s",func_dec.FormatDecimalItems(func_dec.jumlah_eceran()).toString()) + "\n" );
                    }
                    textSb.append("\n" );
                }
                textSb.append("\n" );
                textSb.append("-------------------------------" + "\n" );
                textSb.append("Jumlah : " + String.format("%20s",func_dec.FormatDecimalItems(Double.valueOf(cursor.getString(cursor.getColumnIndex("jml"))).doubleValue()).toString())  + "\n" );
                textSb.append("Tunai  : " + String.format("%20s",func_dec.FormatDecimalItems(Double.valueOf(cursor.getString(cursor.getColumnIndex("jml_dibayar"))).doubleValue()).toString()) + "\n" );
                textSb.append("===============================" + "\n" );
                textSb.append("\n" );
                textSb.append("\n" );
                textSb.append("\n" );
                textSb.append("(SALES)" + "           " + "(TOKO)" + "\n" );
                textSb.append("\n" );
                textSb.append("\n" );
                textSb.append("\n" );

                //tambahan
                cursorDet.close();
                return textSb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
          }
        }return textSb.toString();
    }




    public String sendData(String IDNostruk){
        Function_RowsItemBarang func_dec=new Function_RowsItemBarang();
        final StringBuilder textSb = new StringBuilder();
        final Cursor cursor;

        StringBuilder MyHeader = new StringBuilder();
        StringBuilder MyFooter = new StringBuilder();
        StringBuilder MyContainsStruk = new StringBuilder();
        String InvoiceNumber=new String();
        cursor= getCustomTableTransaksi(IDNostruk);
        cursor.moveToFirst();
        for (int cc=0; cc < cursor.getCount(); cc++)
        {
            cursor.moveToPosition(cc);
            try {

                MyHeader.append("<html>\n");
                MyHeader.append("<body>\n");
                MyHeader.append("<DIV><TABLE>\n");
                MyHeader.append("<TR><TH></TH></TR>\n");
                MyHeader.append("<TR><TD WIDTH=100% FONT-SIZE=5PX>STRUK PEMBAYARAN</TD></TR>");
                MyHeader.append("<DIV></TABLE>\n");
                MyHeader.append("<DIV><TABLE>\n");
                MyHeader.append("<TR><TH></TH><TH></TH><TH></TH></TR>\n");
                MyHeader.append("<TR><TD FONT-SIZE=5PX>NO.BUKTI</TD><TD WIDTH=5%>:</TD><TD WIDTH=70% ALIGN=RIGHT>" + cursor.getString(cursor.getColumnIndex("notrans"))  + "</TD></TR>\n");
                MyHeader.append("<TR><TD FONT-SIZE=5PX>TANGGAL</TD><TD WIDTH=5%>:</TD><TD WIDTH=70% ALIGN=RIGHT>" + cursor.getString(cursor.getColumnIndex("tgl")) + "</TD></TR>\n");
                MyHeader.append("<TR><TD FONT-SIZE=5PX>TOKO</TD><TD WIDTH=5%>:</TD><TD WIDTH=70% ALIGN=RIGHT>"  + cursor.getString(cursor.getColumnIndex("kode_rek"))  + "-" + cursor.getString(cursor.getColumnIndex("nama_rek")) + "</TD></TR>\n");
                MyHeader.append("<TR><TD FONT-SIZE=5PX>JENIS</TD><TD WIDTH=5%>:</TD><TD WIDTH=70% ALIGN=RIGHT>"  + cursor.getString(cursor.getColumnIndex("jenis_bayar")) + "</TD></TR>\n");
                MyHeader.append("</TABLE></DIV>\n");

                MyContainsStruk.append("<TABLE>\n");
                MyContainsStruk.append("<TR><TH>KODE</TH><TH>DESKRIPSI</TH><TH>QTY</TH><TH>H.S</TH><TH>SUB TOTAL</TH></TR>\n");



                final Cursor cursorDet;
                cursorDet= getTableDetTransaksi(IDNostruk);
                cursorDet.moveToFirst();
                for (int cc1=0; cc1 < cursorDet.getCount(); cc1++)
                {
                    cursorDet.moveToPosition(cc1);
                    func_dec.setM_QTY(Double.valueOf(cursorDet.getString(cursorDet.getColumnIndex("qty"))));
                    func_dec.setM_BERAT(Double.valueOf(cursorDet.getString(cursorDet.getColumnIndex("berat"))));
                    func_dec.setM_JMLBERAT(Double.valueOf(cursorDet.getString(cursorDet.getColumnIndex("jmlberat"))));
                    func_dec.setM_HS(Double.valueOf(cursorDet.getString(cursorDet.getColumnIndex("harga"))));
                    func_dec.setM_DISC(Double.valueOf(cursorDet.getString(cursorDet.getColumnIndex("disk"))));
                    if(cursorDet.getString(cursorDet.getColumnIndex("idbarang")).matches("GROSIR")){
                        MyContainsStruk.append("<TR><TD>" + cursorDet.getString(cursorDet.getColumnIndex("idbarang")) + "</TD><TD>" + cursorDet.getString(cursorDet.getColumnIndex("namabarang")) + "</TD><TD ALIGN=RIGHT>" + func_dec.FormatDecimalItems(Double.valueOf(cursorDet.getString(cursorDet.getColumnIndex("qty")))) + "</TD><TD>" + func_dec.FormatDecimalItems(Double.valueOf(cursorDet.getString(cursorDet.getColumnIndex("harga"))))  + "</TD><TD align=right>" + func_dec.FormatDecimalItems(func_dec.jumlah()) +"</TD></TR>");
                    }else{
                        MyContainsStruk.append("<TR><TD>" + cursorDet.getString(cursorDet.getColumnIndex("idbarang")) + "</TD><TD>" + cursorDet.getString(cursorDet.getColumnIndex("namabarang")) + "</TD><TD ALIGN=RIGHT>" + func_dec.FormatDecimalItems(Double.valueOf(cursorDet.getString(cursorDet.getColumnIndex("qty")))) + "</TD><TD>" + func_dec.FormatDecimalItems(Double.valueOf(cursorDet.getString(cursorDet.getColumnIndex("harga")))) + "</TD><TD align=right>" + func_dec.FormatDecimalItems(func_dec.jumlah_eceran()) +"</TD></TR>");
                    }
                }

                //tambahan
                MyContainsStruk.append("<TR></TR><TR></TR>\n");
                MyContainsStruk.append("<TR><TD>JUMLAH</TD><TD>:</TD><TD></TD><TD></TD><TD align=\"right\">" + func_dec.FormatDecimalItems(Double.valueOf(cursor.getString(cursor.getColumnIndex("jml"))).doubleValue()).toString() + "</TD>");
                MyContainsStruk.append("<TR><TD>TUNAI</TD><TD>:</TD><TD></TD><TD></TD><TD align=\"right\">" + func_dec.FormatDecimalItems(Double.valueOf(cursor.getString(cursor.getColumnIndex("jml_dibayar"))).doubleValue()).toString() + "</TD>");
                MyContainsStruk.append("</TABLE>\n");
                MyContainsStruk.append("(SALES)" + "           " + "(TOKO)" + "\n" );

                MyFooter.append("</body>\n");
                MyFooter.append("</html>\n");

                cursorDet.close();
                InvoiceNumber=MyHeader.toString() + MyContainsStruk.toString() + MyFooter.toString();
                return InvoiceNumber.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }return InvoiceNumber.toString();
    }


}
